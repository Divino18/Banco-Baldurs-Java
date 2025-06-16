package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.Transacao;

public class ExtratoView extends JDialog {

    private JTable tabelaExtrato;
    private DefaultTableModel tableModel;

    public ExtratoView(Frame owner) {
        super(owner, "Extrato da Conta", true);
        setSize(800, 400);
        setLocationRelativeTo(owner);

        // Define as colunas da tabela
        String[] colunas = {"Data/Hora", "Tipo", "Descrição", "Valor (R$)"};
        tableModel = new DefaultTableModel(colunas, 0);
        tabelaExtrato = new JTable(tableModel);

        // Adiciona a tabela a um painel com rolagem
        JScrollPane scrollPane = new JScrollPane(tabelaExtrato);
        tabelaExtrato.setFillsViewportHeight(true);

        add(scrollPane);
    }

    /**
     * Preenche a tabela com a lista de transações.
     */
    public void popularTabela(List<Transacao> transacoes) {
        tableModel.setRowCount(0); // Limpa a tabela antes de popular
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        for (Transacao transacao : transacoes) {
            Object[] rowData = {
                    transacao.getDataHora().format(formatter),
                    transacao.getTipoTransacao(),
                    transacao.getDescricao(),
                    transacao.getValor().toPlainString()
            };
            tableModel.addRow(rowData);
        }
    }
}