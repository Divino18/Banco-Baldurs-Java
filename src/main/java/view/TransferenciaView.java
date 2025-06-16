package view;

import javax.swing.*;
import java.awt.*;

public class TransferenciaView extends JDialog {

    private JTextField numeroContaDestinoField;
    private JTextField valorField;
    private JButton confirmarButton;
    private JButton cancelarButton;

    public TransferenciaView(Frame owner) {
        super(owner, "Realizar Transferência", true);
        setSize(400, 200);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos do formulário
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Conta de Destino:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        numeroContaDestinoField = new JTextField(15);
        formPanel.add(numeroContaDestinoField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Valor (R$):"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        valorField = new JTextField(15);
        formPanel.add(valorField, gbc);

        // Painel dos botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        confirmarButton = new JButton("Confirmar");
        cancelarButton = new JButton("Cancelar");
        buttonPanel.add(confirmarButton);
        buttonPanel.add(cancelarButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Getters para os componentes
    public JTextField getNumeroContaDestinoField() { return numeroContaDestinoField; }
    public JTextField getValorField() { return valorField; }
    public JButton getConfirmarButton() { return confirmarButton; }
    public JButton getCancelarButton() { return cancelarButton; }
}