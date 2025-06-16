package view;

import javax.swing.*;
import java.awt.*;

public class FuncionarioView extends JFrame {

    private JButton abrirContaButton;
    private JButton encerrarContaButton;
    private JButton alterarDadosButton;
    private JButton cadastrarFuncionarioButton;
    private JButton gerarRelatoriosButton;
    private JButton consultarResumoContasButton;
    private JButton consultarMovimentacoesButton;
    private JButton sairButton;

    public FuncionarioView() {
        setTitle("Painel do Funcionário - Banco Baldur's");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        abrirContaButton = new JButton("Abrir Nova Conta Cliente");
        encerrarContaButton = new JButton("Encerrar Conta Cliente");
        alterarDadosButton = new JButton("Alterar Dados (Geral)");
        cadastrarFuncionarioButton = new JButton("Cadastrar Novo Funcionário");
        gerarRelatoriosButton = new JButton("Gerar Relatórios Exportáveis");
        consultarResumoContasButton = new JButton("Consultar Resumo de Contas");
        consultarMovimentacoesButton = new JButton("Consultar Movimentações Recentes");
        sairButton = new JButton("Sair (Logout)");

        mainPanel.add(abrirContaButton);
        mainPanel.add(encerrarContaButton);
        mainPanel.add(cadastrarFuncionarioButton);
        mainPanel.add(alterarDadosButton);
        mainPanel.add(consultarResumoContasButton);
        mainPanel.add(consultarMovimentacoesButton);
        mainPanel.add(gerarRelatoriosButton);
        mainPanel.add(sairButton);

        add(mainPanel);
    }

    // Getters
    public JButton getAbrirContaButton() { return abrirContaButton; }
    public JButton getEncerrarContaButton() { return encerrarContaButton; }
    public JButton getAlterarDadosButton() { return alterarDadosButton; }
    public JButton getCadastrarFuncionarioButton() { return cadastrarFuncionarioButton; }
    public JButton getGerarRelatoriosButton() { return gerarRelatoriosButton; }
    public JButton getConsultarResumoContasButton() { return consultarResumoContasButton; }
    public JButton getConsultarMovimentacoesButton() { return consultarMovimentacoesButton; }
    public JButton getSairButton() { return sairButton; }
}