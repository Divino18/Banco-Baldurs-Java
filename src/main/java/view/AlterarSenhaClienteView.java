package view;

import javax.swing.*;
import java.awt.*;

public class AlterarSenhaClienteView extends JDialog {
    private JPasswordField senhaAtualField;
    private JPasswordField novaSenhaField;
    private JPasswordField confirmarNovaSenhaField;
    private JButton salvarButton;
    private JButton cancelarButton;

    public AlterarSenhaClienteView(Frame owner) {
        super(owner, "Alterar Minha Senha", true);
        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Senha Atual:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++;
        senhaAtualField = new JPasswordField(15);
        formPanel.add(senhaAtualField, gbc);

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Nova Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++;
        novaSenhaField = new JPasswordField(15);
        formPanel.add(novaSenhaField, gbc);

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Confirmar Nova Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++;
        confirmarNovaSenhaField = new JPasswordField(15);
        formPanel.add(confirmarNovaSenhaField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        salvarButton = new JButton("Salvar Nova Senha");
        cancelarButton = new JButton("Cancelar");
        buttonPanel.add(salvarButton);
        buttonPanel.add(cancelarButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Getters
    public JPasswordField getSenhaAtualField() { return senhaAtualField; }
    public JPasswordField getNovaSenhaField() { return novaSenhaField; }
    public JPasswordField getConfirmarNovaSenhaField() { return confirmarNovaSenhaField; }
    public JButton getSalvarButton() { return salvarButton; }
    public JButton getCancelarButton() { return cancelarButton; }
}