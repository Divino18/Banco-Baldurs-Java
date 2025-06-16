package view;

import model.Conta;
import model.ContaCorrente;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class AlterarContaCorrenteView extends JDialog {
    private JTextField numeroContaField;
    private JTextField saldoAtualField;
    private JTextField limiteAtualField;
    private JTextField novoLimiteField;
    private JTextField taxaManutencaoAtualField;
    private JTextField novaTaxaManutencaoField;
    private JButton salvarButton;
    private JButton cancelarButton;

    public AlterarContaCorrenteView(Frame owner, Conta conta, ContaCorrente contaCorrente) {
        super(owner, "Alterar Dados da Conta Corrente", true);
        setSize(450, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10,10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Número da Conta:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; numeroContaField = new JTextField(15); numeroContaField.setEditable(false); formPanel.add(numeroContaField, gbc);

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Saldo Atual (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; saldoAtualField = new JTextField(15); saldoAtualField.setEditable(false); formPanel.add(saldoAtualField, gbc);

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Limite Atual (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; limiteAtualField = new JTextField(15); limiteAtualField.setEditable(false); formPanel.add(limiteAtualField, gbc);

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Novo Limite (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; novoLimiteField = new JTextField(15); formPanel.add(novoLimiteField, gbc);

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Taxa Manutenção Atual (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; taxaManutencaoAtualField = new JTextField(15); taxaManutencaoAtualField.setEditable(false); formPanel.add(taxaManutencaoAtualField, gbc);

        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Nova Taxa Manutenção (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; novaTaxaManutencaoField = new JTextField(15); formPanel.add(novaTaxaManutencaoField, gbc);

        // Popular campos
        if (conta != null) {
            numeroContaField.setText(conta.getNumeroConta());
            saldoAtualField.setText(conta.getSaldo() != null ? conta.getSaldo().toPlainString() : "0.00");
        }
        if (contaCorrente != null) {
            limiteAtualField.setText(contaCorrente.getLimite() != null ? contaCorrente.getLimite().toPlainString() : "0.00");
            novoLimiteField.setText(contaCorrente.getLimite() != null ? contaCorrente.getLimite().toPlainString() : "0.00"); // Inicia com o valor atual
            taxaManutencaoAtualField.setText(contaCorrente.getTaxaManutencao() != null ? contaCorrente.getTaxaManutencao().toPlainString() : "0.00");
            novaTaxaManutencaoField.setText(contaCorrente.getTaxaManutencao() != null ? contaCorrente.getTaxaManutencao().toPlainString() : "0.00"); // Inicia com o valor atual
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        salvarButton = new JButton("Salvar Alterações");
        cancelarButton = new JButton("Cancelar");
        buttonPanel.add(salvarButton);
        buttonPanel.add(cancelarButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Getters para os campos e botões
    public JTextField getNovoLimiteField() { return novoLimiteField; }
    public JTextField getNovaTaxaManutencaoField() { return novaTaxaManutencaoField; }
    public JButton getSalvarButton() { return salvarButton; }
    public JButton getCancelarButton() { return cancelarButton; }
}