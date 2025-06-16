package view;

import javax.swing.*;
import java.awt.*;

public class CadastroFuncionarioView extends JDialog {
    private JTextField nomeField, cpfField, telefoneField, dataNascimentoField;
    private JComboBox<String> cargoBox;
    private JPasswordField senhaField;
    private JButton salvarButton, cancelarButton;

    public CadastroFuncionarioView(Frame owner) {
        super(owner, "Cadastro de Novo Funcionário", true);
        setSize(450, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Nome Completo:"), gbc);
        gbc.gridx = 1; nomeField = new JTextField(20); formPanel.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1; cpfField = new JTextField(20); formPanel.add(cpfField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Data Nasc. (AAAA-MM-DD):"), gbc);
        gbc.gridx = 1; dataNascimentoField = new JTextField(20); formPanel.add(dataNascimentoField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; telefoneField = new JTextField(20); formPanel.add(telefoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Cargo:"), gbc);
        gbc.gridx = 1; cargoBox = new JComboBox<>(new String[]{"ESTAGIARIO", "ATENDENTE", "GERENTE"}); formPanel.add(cargoBox, gbc);

        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Senha de Acesso:"), gbc);
        gbc.gridx = 1; senhaField = new JPasswordField(20); formPanel.add(senhaField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        salvarButton = new JButton("Salvar");
        cancelarButton = new JButton("Cancelar");
        buttonPanel.add(salvarButton);
        buttonPanel.add(cancelarButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Getters
    public JTextField getNomeField() { return nomeField; }
    public JTextField getCpfField() { return cpfField; }
    public JTextField getTelefoneField() { return telefoneField; }
    public JTextField getDataNascimentoField() { return dataNascimentoField; }
    public JComboBox<String> getCargoBox() { return cargoBox; }
    public JPasswordField getSenhaField() { return senhaField; }
    public JButton getSalvarButton() { return salvarButton; }
    public JButton getCancelarButton() { return cancelarButton; }
}