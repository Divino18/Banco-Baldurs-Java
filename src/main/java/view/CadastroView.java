package view;

import javax.swing.*;
import java.awt.*;

public class CadastroView extends JDialog {

    private JComboBox<String> tipoContaBox;
    private JTextField nomeField, cpfField, telefoneField, dataNascimentoField;
    private JPasswordField senhaField;
    private JButton salvarButton, cancelarButton;

    // Campos específicos
    private JTextField taxaRendimentoField; // Poupança e Investimento
    private JTextField limiteField, taxaManutencaoField, dataVencimentoField; // Corrente
    private JComboBox<String> perfilRiscoBox; // Investimento
    private JTextField valorMinimoField; // Investimento

    private JPanel specificFieldsPanel;

    public CadastroView(Frame owner) {
        super(owner, "Abertura de Nova Conta", true);
        setSize(500, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Campos Comuns ---
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Tipo de Conta:"), gbc);
        gbc.gridx = 1; tipoContaBox = new JComboBox<>(new String[]{"Conta Poupança", "Conta Corrente", "Conta Investimento"});
        formPanel.add(tipoContaBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Nome Completo:"), gbc);
        gbc.gridx = 1; nomeField = new JTextField(20); formPanel.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1; cpfField = new JTextField(20); formPanel.add(cpfField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Data Nascimento (AAAA-MM-DD):"), gbc);
        gbc.gridx = 1; dataNascimentoField = new JTextField(20); formPanel.add(dataNascimentoField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; telefoneField = new JTextField(20); formPanel.add(telefoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Senha de Acesso:"), gbc);
        gbc.gridx = 1; senhaField = new JPasswordField(20); formPanel.add(senhaField, gbc);

        // --- Painel para Campos Específicos ---
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        specificFieldsPanel = new JPanel(new GridBagLayout());
        specificFieldsPanel.setBorder(BorderFactory.createTitledBorder("Detalhes da Conta"));
        formPanel.add(specificFieldsPanel, gbc);

        // --- Botões ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        salvarButton = new JButton("Salvar");
        cancelarButton = new JButton("Cancelar");
        buttonPanel.add(salvarButton);
        buttonPanel.add(cancelarButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Ação para mudar os campos específicos
        tipoContaBox.addActionListener(e -> updateSpecificFields());
        updateSpecificFields(); // Chamada inicial
    }

    public void updateSpecificFields() {
        specificFieldsPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String selected = (String) tipoContaBox.getSelectedItem();
        if (selected.equals("Conta Poupança")) {
            gbc.gridx = 0; gbc.gridy = 0; specificFieldsPanel.add(new JLabel("Taxa de Rendimento (%):"), gbc);
            gbc.gridx = 1; taxaRendimentoField = new JTextField(10); specificFieldsPanel.add(taxaRendimentoField, gbc);
        } else if (selected.equals("Conta Corrente")) {
            gbc.gridx = 0; gbc.gridy = 0; specificFieldsPanel.add(new JLabel("Limite de Crédito:"), gbc);
            gbc.gridx = 1; limiteField = new JTextField(10); specificFieldsPanel.add(limiteField, gbc);
            gbc.gridx = 0; gbc.gridy = 1; specificFieldsPanel.add(new JLabel("Taxa de Manutenção:"), gbc);
            gbc.gridx = 1; taxaManutencaoField = new JTextField(10); specificFieldsPanel.add(taxaManutencaoField, gbc);
            gbc.gridx = 0; gbc.gridy = 2; specificFieldsPanel.add(new JLabel("Data Vencimento (AAAA-MM-DD):"), gbc);
            gbc.gridx = 1; dataVencimentoField = new JTextField(10); specificFieldsPanel.add(dataVencimentoField, gbc);
        } else if (selected.equals("Conta Investimento")) {
            gbc.gridx = 0; gbc.gridy = 0; specificFieldsPanel.add(new JLabel("Taxa de Rendimento Base (%):"), gbc);
            gbc.gridx = 1; taxaRendimentoField = new JTextField(10); specificFieldsPanel.add(taxaRendimentoField, gbc);
            gbc.gridx = 0; gbc.gridy = 1; specificFieldsPanel.add(new JLabel("Perfil de Risco:"), gbc);
            gbc.gridx = 1; perfilRiscoBox = new JComboBox<>(new String[]{"BAIXO", "MEDIO", "ALTO"}); specificFieldsPanel.add(perfilRiscoBox, gbc);
            gbc.gridx = 0; gbc.gridy = 2; specificFieldsPanel.add(new JLabel("Valor Mínimo de Invest.:"), gbc);
            gbc.gridx = 1; valorMinimoField = new JTextField(10); specificFieldsPanel.add(valorMinimoField, gbc);
        }
        specificFieldsPanel.revalidate();
        specificFieldsPanel.repaint();
    }

    // Getters para todos os campos e botões
    public JComboBox<String> getTipoContaBox() { return tipoContaBox; }
    public JTextField getNomeField() { return nomeField; }
    public JTextField getCpfField() { return cpfField; }
    public JTextField getTelefoneField() { return telefoneField; }
    public JTextField getDataNascimentoField() { return dataNascimentoField; }
    public JPasswordField getSenhaField() { return senhaField; }
    public JButton getSalvarButton() { return salvarButton; }
    public JButton getCancelarButton() { return cancelarButton; }
    public JTextField getTaxaRendimentoField() { return taxaRendimentoField; }
    public JTextField getLimiteField() { return limiteField; }
    public JTextField getTaxaManutencaoField() { return taxaManutencaoField; }
    public JTextField getDataVencimentoField() { return dataVencimentoField; }
    public JComboBox<String> getPerfilRiscoBox() { return perfilRiscoBox; }
    public JTextField getValorMinimoField() { return valorMinimoField; }
}