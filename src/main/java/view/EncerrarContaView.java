package view;

import javax.swing.*;
import java.awt.*;

public class EncerrarContaView extends JDialog {

    private JTextField numeroContaField;
    private JTextArea motivoArea;
    private JButton buscarContaButton;
    private JButton confirmarEncerramentoButton;
    private JButton cancelarButton;
    private JLabel dadosContaLabel;

    public EncerrarContaView(Frame owner) {
        super(owner, "Encerrar Conta de Cliente", true);
        setSize(500, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de busca
        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscaPanel.add(new JLabel("Número da Conta:"));
        numeroContaField = new JTextField(15);
        buscaPanel.add(numeroContaField);
        buscarContaButton = new JButton("Buscar Conta");
        buscaPanel.add(buscarContaButton);

        // Painel de dados da conta e motivo
        JPanel infoPanel = new JPanel(new BorderLayout(5,5));
        dadosContaLabel = new JLabel("<html><i>Busque uma conta para ver os detalhes...</i></html>");
        dadosContaLabel.setPreferredSize(new Dimension(450, 60)); // Aumentar altura para caber mais texto
        dadosContaLabel.setVerticalAlignment(SwingConstants.TOP);
        infoPanel.add(dadosContaLabel, BorderLayout.NORTH);

        JPanel motivoPanel = new JPanel(new BorderLayout());
        motivoPanel.setBorder(BorderFactory.createTitledBorder("Motivo do Encerramento (Opcional)"));
        motivoArea = new JTextArea(5, 30);
        motivoArea.setLineWrap(true);
        motivoArea.setWrapStyleWord(true);
        JScrollPane scrollMotivo = new JScrollPane(motivoArea);
        motivoPanel.add(scrollMotivo, BorderLayout.CENTER);
        infoPanel.add(motivoPanel, BorderLayout.CENTER);


        // Painel de botões de ação
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        confirmarEncerramentoButton = new JButton("Confirmar Encerramento");
        confirmarEncerramentoButton.setEnabled(false);
        cancelarButton = new JButton("Cancelar");
        actionButtonPanel.add(confirmarEncerramentoButton);
        actionButtonPanel.add(cancelarButton);

        add(buscaPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(actionButtonPanel, BorderLayout.SOUTH);
    }

    // Getters
    public JTextField getNumeroContaField() { return numeroContaField; }
    public JTextArea getMotivoArea() { return motivoArea; }
    public JButton getBuscarContaButton() { return buscarContaButton; }
    public JButton getConfirmarEncerramentoButton() { return confirmarEncerramentoButton; }
    public JButton getCancelarButton() { return cancelarButton; }
    public JLabel getDadosContaLabel() { return dadosContaLabel; }
}