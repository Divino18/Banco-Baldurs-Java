package view;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

public class RelatorioView extends JDialog {
    private JComboBox<String> tipoRelatorioBox;
    private JButton exportarButton;
    private JTable previewTable;
    private JScrollPane scrollPane;

    public RelatorioView(Frame owner) {
        super(owner, "Geração de Relatórios", true);
        setSize(800, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Painel de controle no topo
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        controlPanel.add(new JLabel("Selecione o Relatório:"));

        // As opções correspondem às VIEWs que criamos no banco de dados
        String[] relatorios = {"Movimentações Recentes", "Resumo de Contas por Cliente"};
        tipoRelatorioBox = new JComboBox<>(relatorios);
        controlPanel.add(tipoRelatorioBox);

        exportarButton = new JButton("Exportar para CSV/Excel");
        controlPanel.add(exportarButton);

        // Tabela para pré-visualização dos dados
        previewTable = new JTable();
        previewTable.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(previewTable);

        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Getters para os componentes que o Controller vai precisar
    public JComboBox<String> getTipoRelatorioBox() {
        return tipoRelatorioBox;
    }

    public JButton getExportarButton() {
        return exportarButton;
    }

    public JTable getPreviewTable() {
        return previewTable;
    }
}