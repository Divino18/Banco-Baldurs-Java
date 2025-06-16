package controller;

import dao.RelatorioDAO;
import util.CSVExporter;
import view.RelatorioView;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class RelatorioController {
    private RelatorioView view;
    private RelatorioDAO dao;

    public RelatorioController(RelatorioView view) {
        this.view = view;
        this.dao = new RelatorioDAO();
        initController();
    }

    private void initController() {
        view.getTipoRelatorioBox().addActionListener(e -> carregarPreview());
        view.getExportarButton().addActionListener(e -> exportarRelatorio());
        carregarPreview();
    }

    private void carregarPreview() {
        String selected = (String) view.getTipoRelatorioBox().getSelectedItem();
        String viewName = "";
        if ("Movimentações Recentes".equals(selected)) {
            viewName = "vw_movimentacoes_recentes";
        } else if ("Resumo de Contas por Cliente".equals(selected)) {
            viewName = "vw_resumo_contas";
        }

        try {
            TableModel model = dao.gerarRelatorioFromView(viewName);
            view.getPreviewTable().setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Erro ao carregar dados do relatório: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void exportarRelatorio() {
        if (view.getPreviewTable().getRowCount() == 0) {
            JOptionPane.showMessageDialog(view, "Não há dados para exportar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));

        int userSelection = fileChooser.showSaveDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getPath().toLowerCase().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getPath() + ".csv");
            }

            try {
                CSVExporter.exportTable(view.getPreviewTable(), fileToSave);
                JOptionPane.showMessageDialog(view, "Relatório exportado com sucesso para:\n" + fileToSave.getAbsolutePath(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(view, "Erro ao salvar o arquivo: " + e.getMessage(), "Erro de Arquivo", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}