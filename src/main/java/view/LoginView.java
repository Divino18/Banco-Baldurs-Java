package view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField cpfField;
    private JPasswordField senhaField;
    private JTextField otpField;
    private JButton gerarOtpButton; // NOVO BOTÃO
    private JButton loginButton;
    private JButton sairButton;
    private JRadioButton funcionarioRadioButton;
    private JRadioButton clienteRadioButton;
    private ButtonGroup tipoUsuarioGroup;

    public LoginView() {
        setTitle("Banco Baldur's - Autenticação");
        setSize(450, 350); // Ajustei o tamanho para o novo botão
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        funcionarioRadioButton = new JRadioButton("Funcionário");
        clienteRadioButton = new JRadioButton("Cliente", true);
        tipoUsuarioGroup = new ButtonGroup();
        tipoUsuarioGroup.add(funcionarioRadioButton);
        tipoUsuarioGroup.add(clienteRadioButton);
        radioPanel.add(funcionarioRadioButton);
        radioPanel.add(clienteRadioButton);
        formPanel.add(radioPanel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("CPF:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        cpfField = new JTextField(15);
        formPanel.add(cpfField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        senhaField = new JPasswordField(15);
        formPanel.add(senhaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("OTP:"), gbc);

        // Painel para OTP e botão Gerar OTP
        JPanel otpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Sem espaçamento horizontal
        otpField = new JTextField(8); // Campo OTP menor
        otpPanel.add(otpField);
        gerarOtpButton = new JButton("Gerar OTP"); // Botão ao lado do campo OTP
        otpPanel.add(gerarOtpButton);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE; // Para não esticar o painel OTP
        gbc.anchor = GridBagConstraints.WEST; // Alinha à esquerda
        formPanel.add(otpPanel, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Restaura para os próximos
        gbc.anchor = GridBagConstraints.CENTER; // Restaura para os próximos

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        loginButton = new JButton("Login");
        sairButton = new JButton("Sair");
        buttonPanel.add(loginButton);
        buttonPanel.add(sairButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // Getters
    public JTextField getCpfField() { return cpfField; }
    public JPasswordField getSenhaField() { return senhaField; }
    public JTextField getOtpField() { return otpField; }
    public JButton getGerarOtpButton() { return gerarOtpButton; } // NOVO GETTER
    public JButton getLoginButton() { return loginButton; }
    public JButton getSairButton() { return sairButton; }
    public JRadioButton getFuncionarioRadioButton() { return funcionarioRadioButton; }
    public JRadioButton getClienteRadioButton() { return clienteRadioButton; }
}