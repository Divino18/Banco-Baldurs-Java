package view;

import controller.ClienteController;
import model.Usuario;
import javax.swing.*;
import java.awt.*;

public class ClienteView extends JFrame {

    private JLabel welcomeLabel;
    private JButton saldoButton, depositoButton, saqueButton, transferenciaButton, extratoButton, limiteButton;
    private JButton alterarSenhaButton; // NOVO BOTÃO
    private JButton sairButton;
    private Usuario usuarioLogado;

    public ClienteView(Usuario usuario) {
        this.usuarioLogado = usuario;

        setTitle("Painel do Cliente - Banco Baldur's");
        setSize(800, 650); // Aumentei um pouco a altura para o novo botão
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        welcomeLabel = new JLabel("Bem-vindo(a), " + usuario.getNome() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel buttonPanel = new JPanel(new GridLayout(5, 2, 15, 15)); // Ajustado para 5 linhas
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        saldoButton = new JButton("Consultar Saldo");
        depositoButton = new JButton("Realizar Depósito");
        saqueButton = new JButton("Realizar Saque");
        transferenciaButton = new JButton("Realizar Transferência");
        extratoButton = new JButton("Consultar Extrato");
        limiteButton = new JButton("Consultar Limite");
        alterarSenhaButton = new JButton("Alterar Minha Senha"); // NOVO
        sairButton = new JButton("Encerrar Sessão (Logout)");

        buttonPanel.add(saldoButton);
        buttonPanel.add(depositoButton);
        buttonPanel.add(saqueButton);
        buttonPanel.add(transferenciaButton);
        buttonPanel.add(extratoButton);
        buttonPanel.add(limiteButton);
        buttonPanel.add(alterarSenhaButton); // Adicionado
        buttonPanel.add(new JLabel()); // Espaço em branco
        buttonPanel.add(sairButton);

        add(welcomeLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        new ClienteController(this, this.usuarioLogado);
    }

    // Getters
    public JButton getSaldoButton() { return saldoButton; }
    public JButton getDepositoButton() { return depositoButton; }
    public JButton getSaqueButton() { return saqueButton; }
    public JButton getTransferenciaButton() { return transferenciaButton; }
    public JButton getExtratoButton() { return extratoButton; }
    public JButton getLimiteButton() { return limiteButton; }
    public JButton getAlterarSenhaButton() { return alterarSenhaButton; } // NOVO GETTER
    public JButton getSairButton() { return sairButton; }
}