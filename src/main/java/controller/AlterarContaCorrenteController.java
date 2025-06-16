package controller;

import dao.ContaDAO;
import model.Conta;
import model.ContaCorrente;
import view.AlterarContaCorrenteView;
import javax.swing.*;
import java.math.BigDecimal;

public class AlterarContaCorrenteController {
    private AlterarContaCorrenteView view;
    private ContaDAO contaDAO;
    private Conta contaGeral;
    private ContaCorrente contaCorrenteOriginal;

    public AlterarContaCorrenteController(AlterarContaCorrenteView view, Conta conta, ContaCorrente contaCorrente) {
        this.view = view;
        this.contaDAO = new ContaDAO();
        this.contaGeral = conta;
        this.contaCorrenteOriginal = contaCorrente;
        initController();
    }

    private void initController() {
        view.getSalvarButton().addActionListener(e -> salvarAlteracoesContaCorrente());
        view.getCancelarButton().addActionListener(e -> view.dispose());
    }

    private void salvarAlteracoesContaCorrente() {
        System.out.println("--- INICIANDO salvarAlteracoesContaCorrente ---");

        String adminPassword = JOptionPane.showInputDialog(view, "Digite a senha de administrador para confirmar:", "Autenticação Necessária", JOptionPane.WARNING_MESSAGE);
        if (adminPassword == null || !"admin".equals(adminPassword)) {
            JOptionPane.showMessageDialog(view, "Senha de administrador incorreta ou cancelada.", "Falha na Autenticação", JOptionPane.ERROR_MESSAGE);
            System.out.println("Falha na autenticação do administrador (Alterar Conta Corrente).");
            return;
        }
        System.out.println("Autenticação do administrador (Alterar CC) simulada com sucesso.");

        try {
            BigDecimal novoLimite = new BigDecimal(view.getNovoLimiteField().getText().trim());
            BigDecimal novaTaxa = new BigDecimal(view.getNovaTaxaManutencaoField().getText().trim());

            if (novoLimite.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(view, "O novo limite não pode ser negativo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (novaTaxa.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(view, "A nova taxa de manutenção não pode ser negativa.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean limiteMudou = novoLimite.compareTo(contaCorrenteOriginal.getLimite()) != 0;
            boolean taxaMudou = novaTaxa.compareTo(contaCorrenteOriginal.getTaxaManutencao()) != 0;

            if (!limiteMudou && !taxaMudou) {
                JOptionPane.showMessageDialog(view, "Nenhuma alteração detectada.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            System.out.println("Tentando atualizar dados da conta corrente ID: " + contaGeral.getIdConta());
            boolean sucesso = contaDAO.atualizarDadosContaCorrente(contaGeral.getIdConta(), novoLimite, novaTaxa);

            if (sucesso) {
                JOptionPane.showMessageDialog(view, "Dados da conta corrente atualizados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("AUDITORIA PENDENTE: Alteração de dados da conta corrente ID " + contaGeral.getIdConta());
                view.dispose();
            } else {
                JOptionPane.showMessageDialog(view, "Falha ao atualizar os dados da conta corrente.", "Erro de Atualização", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Valores de limite ou taxa inválidos. Use números e ponto (.) para decimais.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        System.out.println("--- FINALIZANDO salvarAlteracoesContaCorrente ---");
    }
}