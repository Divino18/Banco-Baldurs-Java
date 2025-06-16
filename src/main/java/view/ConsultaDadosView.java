package view;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

public class ConsultaDadosView extends JDialog {

    private JTable tabelaDados;
    private JScrollPane scrollPane;

    public ConsultaDadosView(Frame owner, String titulo) {
        super(owner, titulo, true); // Agora aceita um título dinâmico
        setSize(800, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        tabelaDados = new JTable();
        scrollPane = new JScrollPane(tabelaDados);
        tabelaDados.setFillsViewportHeight(true);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void setTableModel(TableModel tableModel) {
        tabelaDados.setModel(tableModel);
    }
}