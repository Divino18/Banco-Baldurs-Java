package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BuscarContaView extends JDialog {
    private JTextField numeroContaField;
    private JButton buscarButton;
    private JButton cancelarButton;
    private String numeroContaInput;

    public BuscarContaView(Frame owner) {
        super(owner, "Buscar Conta para Alteração", true);
        setSize(350, 150);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Número da Conta:"));
        numeroContaField = new JTextField(15);
        inputPanel.add(numeroContaField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buscarButton = new JButton("Buscar");
        cancelarButton = new JButton("Cancelar");

        buscarButton.addActionListener((ActionEvent e) -> {
            numeroContaInput = numeroContaField.getText().trim();
            if (numeroContaInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite o número da conta.", "Atenção", JOptionPane.WARNING_MESSAGE);
                numeroContaInput = null; // Garante que não retorne vazio
            } else {
                dispose();
            }
        });

        cancelarButton.addActionListener((ActionEvent e) -> {
            numeroContaInput = null;
            dispose();
        });

        buttonPanel.add(buscarButton);
        buttonPanel.add(cancelarButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getNumeroContaInput() {
        // Este método é chamado após o JDialog ser fechado
        return numeroContaInput;
    }
}