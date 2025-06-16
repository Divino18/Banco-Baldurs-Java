package controller;

import dao.AuditoriaDAO;
import dao.ContaDAO;
import dao.TransacaoDAO;
import dao.UsuarioDAO;
import model.Auditoria;
import model.Conta;
import model.ContaCorrente;
import model.Transacao;
import model.Usuario;
import util.PasswordUtil;
import view.AlterarSenhaClienteView;
import view.ClienteView;
import view.ExtratoView;
import view.TransferenciaView;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ClienteController {

    private ClienteView view;
    private Usuario usuarioLogado;
    private ContaDAO contaDAO;
    private TransacaoDAO transacaoDAO;
    private UsuarioDAO usuarioDAO;
    private AuditoriaDAO auditoriaDAO;

    public ClienteController(ClienteView view, Usuario usuario) {
        this.view = view;
        this.usuarioLogado = usuario;
        this.contaDAO = new ContaDAO();
        this.transacaoDAO = new TransacaoDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.auditoriaDAO = new AuditoriaDAO();
        initController();
    }

    private void initController() {
        view.getSaldoButton().addActionListener(e -> consultarSaldo());
        view.getDepositoButton().addActionListener(e -> realizarDeposito());
        view.getSaqueButton().addActionListener(e -> realizarSaque());
        view.getTransferenciaButton().addActionListener(e -> abrirTelaTransferencia());
        view.getExtratoButton().addActionListener(e -> consultarExtrato());
        view.getLimiteButton().addActionListener(e -> consultarLimite());
        view.getAlterarSenhaButton().addActionListener(e -> abrirTelaAlterarSenha());
        view.getSairButton().addActionListener(e -> {
            System.out.println("--- ClienteController: Botão Sair clicado, fechando ClienteView ---");
            view.dispose();
            // Para voltar à tela de login, a MainApp precisaria gerenciar isso
            // ou você poderia instanciar uma nova LoginView aqui, mas pode levar a múltiplas instâncias.
            // new LoginView().setVisible(true);
        });
    }

    private Conta getContaPrincipal() {
        System.out.println("--- ClienteController: Executando getContaPrincipal para usuario ID: " + usuarioLogado.getIdUsuario() + " ---");
        List<Conta> contas = contaDAO.findContasByClienteId(usuarioLogado.getIdUsuario());
        if (contas == null || contas.isEmpty()) {
            System.out.println("--- ClienteController: Nenhuma conta ativa encontrada para o cliente ID: " + usuarioLogado.getIdUsuario() + " ---");
            return null; // Retorna null explicitamente se nenhuma conta for encontrada
        }
        System.out.println("--- ClienteController: Conta principal encontrada: " + contas.get(0).getNumeroConta() + " ---");
        return contas.get(0); // Retorna a primeira conta da lista
    }

    private void consultarSaldo() {
        System.out.println("--- ClienteController: Ação consultarSaldo ---");
        Conta contaPrincipal = getContaPrincipal();
        if (contaPrincipal == null) {
            JOptionPane.showMessageDialog(view, "Você não possui contas ativas para consultar o saldo.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Para garantir que estamos vendo o saldo mais atualizado do banco
        List<Conta> contasAtualizadas = contaDAO.findContasByClienteId(usuarioLogado.getIdUsuario());
        if (contasAtualizadas != null && !contasAtualizadas.isEmpty()) {
            BigDecimal saldoAtualizado = contasAtualizadas.get(0).getSaldo(); // Assumindo que a ordem é a mesma
            JOptionPane.showMessageDialog(view, "Saldo da conta " + contaPrincipal.getNumeroConta() + ": R$ " + saldoAtualizado);
        } else {
            JOptionPane.showMessageDialog(view, "Não foi possível obter o saldo atualizado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarDeposito() {
        System.out.println("--- ClienteController: Ação realizarDeposito ---");
        Conta contaPrincipal = getContaPrincipal();
        if (contaPrincipal == null) {
            JOptionPane.showMessageDialog(view, "Você não possui conta ativa para realizar depósitos.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String valorStr = JOptionPane.showInputDialog(view, "Digite o valor a ser depositado:", "Depósito", JOptionPane.PLAIN_MESSAGE);
        if (valorStr == null || valorStr.trim().isEmpty()) return;

        try {
            BigDecimal valor = new BigDecimal(valorStr);
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(view, "O valor do depósito deve ser positivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Transacao deposito = new Transacao();
            deposito.setIdContaOrigem(contaPrincipal.getIdConta()); // No depósito, a conta de origem é a própria conta
            deposito.setTipoTransacao("DEPOSITO");
            deposito.setValor(valor);
            deposito.setDescricao("Depósito em conta");
            // dataHora é DEFAULT CURRENT_TIMESTAMP no banco

            transacaoDAO.salvar(deposito);

            // Registrar Auditoria
            Auditoria log = new Auditoria();
            log.setIdUsuario(usuarioLogado.getIdUsuario());
            log.setAcao("DEPOSITO");
            log.setDetalhes("Depósito de R$ " + valor.toPlainString() + " na conta ID: " + contaPrincipal.getIdConta());
            auditoriaDAO.registrarAcao(log);

            JOptionPane.showMessageDialog(view, "Depósito de R$ " + valor + " realizado com sucesso!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Valor inválido. Por favor, digite um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Erro ao realizar depósito: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void realizarSaque() {
        System.out.println("--- ClienteController: Ação realizarSaque ---");
        Conta contaPrincipal = getContaPrincipal();
        if (contaPrincipal == null) {
            JOptionPane.showMessageDialog(view, "Você não possui conta ativa para realizar saques.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // É importante buscar o saldo mais recente antes de validar
        List<Conta> contasAtualizadas = contaDAO.findContasByClienteId(usuarioLogado.getIdUsuario());
        if(contasAtualizadas == null || contasAtualizadas.isEmpty()){
            JOptionPane.showMessageDialog(view, "Não foi possível verificar o saldo atual da conta.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        BigDecimal saldoAtual = contasAtualizadas.get(0).getSaldo();


        String valorStr = JOptionPane.showInputDialog(view, "Digite o valor do saque:", "Saque", JOptionPane.PLAIN_MESSAGE);
        if (valorStr == null || valorStr.trim().isEmpty()) return;

        try {
            BigDecimal valor = new BigDecimal(valorStr);
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(view, "O valor do saque deve ser positivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(saldoAtual.compareTo(valor) < 0){
                JOptionPane.showMessageDialog(view, "Saldo insuficiente para realizar o saque.\nSaldo atual: R$ " + saldoAtual.toPlainString(), "Erro de Saldo", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Transacao saque = new Transacao();
            saque.setIdContaOrigem(contaPrincipal.getIdConta());
            saque.setTipoTransacao("SAQUE");
            saque.setValor(valor);
            saque.setDescricao("Saque em terminal");

            transacaoDAO.salvar(saque);

            // Registrar Auditoria
            Auditoria log = new Auditoria();
            log.setIdUsuario(usuarioLogado.getIdUsuario());
            log.setAcao("SAQUE");
            log.setDetalhes("Saque de R$ " + valor.toPlainString() + " da conta ID: " + contaPrincipal.getIdConta());
            auditoriaDAO.registrarAcao(log);

            JOptionPane.showMessageDialog(view, "Saque de R$ " + valor + " realizado com sucesso!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Valor inválido. Por favor, digite um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Erro ao realizar saque: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void abrirTelaTransferencia() {
        System.out.println("--- ClienteController: Ação abrirTelaTransferencia ---");
        Conta contaOrigem = getContaPrincipal();
        if (contaOrigem == null) {
            JOptionPane.showMessageDialog(view, "Você não possui uma conta para realizar transferências.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // É importante buscar o saldo mais recente antes de validar
        List<Conta> contasOrigemAtualizadas = contaDAO.findContasByClienteId(usuarioLogado.getIdUsuario());
        if(contasOrigemAtualizadas == null || contasOrigemAtualizadas.isEmpty()){
            JOptionPane.showMessageDialog(view, "Não foi possível verificar o saldo atual da sua conta.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        BigDecimal saldoAtualOrigem = contasOrigemAtualizadas.get(0).getSaldo();

        TransferenciaView transferenciaView = new TransferenciaView(view);

        transferenciaView.getCancelarButton().addActionListener(e -> transferenciaView.dispose());

        transferenciaView.getConfirmarButton().addActionListener(e -> {
            String numContaDestino = transferenciaView.getNumeroContaDestinoField().getText().trim();
            String valorStr = transferenciaView.getValorField().getText().trim();

            if (numContaDestino.isEmpty() || valorStr.isEmpty()) {
                JOptionPane.showMessageDialog(transferenciaView, "Todos os campos são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                BigDecimal valor = new BigDecimal(valorStr);
                if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(transferenciaView, "O valor da transferência deve ser positivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (saldoAtualOrigem.compareTo(valor) < 0) {
                    JOptionPane.showMessageDialog(transferenciaView, "Saldo insuficiente na conta de origem.\nSaldo atual: R$ " + saldoAtualOrigem.toPlainString(), "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Conta contaDestino = contaDAO.findByNumeroConta(numContaDestino);
                if(contaDestino == null) {
                    JOptionPane.showMessageDialog(transferenciaView, "A conta de destino não foi encontrada ou está inativa.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(contaDestino.getIdConta() == contaOrigem.getIdConta()){
                    JOptionPane.showMessageDialog(transferenciaView, "Não é possível transferir para a mesma conta.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Transacao transferencia = new Transacao();
                transferencia.setIdContaOrigem(contaOrigem.getIdConta());
                transferencia.setIdContaDestino(contaDestino.getIdConta());
                transferencia.setTipoTransacao("TRANSFERENCIA");
                transferencia.setValor(valor);
                transferencia.setDescricao("Transferência para conta " + numContaDestino);

                transacaoDAO.salvar(transferencia);

                // Registrar Auditoria
                Auditoria log = new Auditoria();
                log.setIdUsuario(usuarioLogado.getIdUsuario());
                log.setAcao("TRANSFERENCIA");
                log.setDetalhes("Transferência de R$ " + valor.toPlainString() + " da conta ID: " + contaOrigem.getIdConta() + " para conta ID: " + contaDestino.getIdConta());
                auditoriaDAO.registrarAcao(log);

                JOptionPane.showMessageDialog(transferenciaView, "Transferência realizada com sucesso!");
                transferenciaView.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(transferenciaView, "Valor inválido. Use apenas números e ponto para decimais.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(transferenciaView, "Erro de banco de dados ao realizar transferência: " + ex.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        transferenciaView.setVisible(true);
    }

    private void consultarExtrato() {
        System.out.println("--- ClienteController: Ação consultarExtrato ---");
        Conta contaPrincipal = getContaPrincipal();
        if (contaPrincipal == null) {
            JOptionPane.showMessageDialog(view, "Você não possui conta para ver o extrato.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Transacao> transacoes = transacaoDAO.getExtratoPorConta(contaPrincipal.getIdConta());

        if (transacoes.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nenhuma transação encontrada para esta conta.", "Extrato", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ExtratoView extratoView = new ExtratoView(view);
        extratoView.popularTabela(transacoes);
        extratoView.setVisible(true);
    }

    private void consultarLimite() {
        System.out.println("--- ClienteController: Ação consultarLimite ---");
        Conta contaPrincipal = getContaPrincipal();
        if (contaPrincipal == null) {
            JOptionPane.showMessageDialog(view, "Você não possui conta ativa.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!"CORRENTE".equalsIgnoreCase(contaPrincipal.getTipoConta())) {
            JOptionPane.showMessageDialog(view, "Esta funcionalidade está disponível apenas para Conta Corrente.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ContaCorrente contaCorrente = contaDAO.findContaCorrenteByContaId(contaPrincipal.getIdConta());
        if (contaCorrente != null) {
            JOptionPane.showMessageDialog(view, "Seu limite de crédito atual é: R$ " + contaCorrente.getLimite(), "Limite de Crédito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "Não foi possível encontrar os detalhes da sua conta corrente.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirTelaAlterarSenha() {
        System.out.println("--- ClienteController: Abrindo tela para alterar senha ---");
        AlterarSenhaClienteView alterarSenhaView = new AlterarSenhaClienteView(view);

        alterarSenhaView.getCancelarButton().addActionListener(e -> alterarSenhaView.dispose());
        alterarSenhaView.getSalvarButton().addActionListener(e -> {
            String senhaAtualDigitada = new String(alterarSenhaView.getSenhaAtualField().getPassword());
            String novaSenhaDigitada = new String(alterarSenhaView.getNovaSenhaField().getPassword());
            String confirmarNovaSenhaDigitada = new String(alterarSenhaView.getConfirmarNovaSenhaField().getPassword());

            if (senhaAtualDigitada.isEmpty() || novaSenhaDigitada.isEmpty() || confirmarNovaSenhaDigitada.isEmpty()) {
                JOptionPane.showMessageDialog(alterarSenhaView, "Todos os campos são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!PasswordUtil.md5(senhaAtualDigitada).equals(usuarioLogado.getSenhaHash())) {
                JOptionPane.showMessageDialog(alterarSenhaView, "Senha atual incorreta.", "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!novaSenhaDigitada.equals(confirmarNovaSenhaDigitada)) {
                JOptionPane.showMessageDialog(alterarSenhaView, "A nova senha e a confirmação não correspondem.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!PasswordUtil.isPasswordStrong(novaSenhaDigitada)) {
                JOptionPane.showMessageDialog(alterarSenhaView,
                        "A nova senha não é forte o suficiente.\n" +
                                "Requisitos: Mínimo 8 caracteres, 1 maiúscula, 1 minúscula, 1 número, 1 especial (!@#$%).",
                        "Senha Fraca", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (PasswordUtil.md5(novaSenhaDigitada).equals(usuarioLogado.getSenhaHash())) {
                JOptionPane.showMessageDialog(alterarSenhaView, "A nova senha não pode ser igual à senha atual.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String novoHash = PasswordUtil.md5(novaSenhaDigitada);
            boolean sucesso = usuarioDAO.atualizarSenha(usuarioLogado.getIdUsuario(), novoHash);

            if (sucesso) {
                usuarioLogado.setSenhaHash(novoHash);

                Auditoria log = new Auditoria();
                log.setIdUsuario(usuarioLogado.getIdUsuario());
                log.setAcao("ALTERACAO_PROPRIA_SENHA");
                log.setDetalhes("Cliente (ID: " + usuarioLogado.getIdUsuario() + ") alterou a própria senha.");
                auditoriaDAO.registrarAcao(log);

                JOptionPane.showMessageDialog(alterarSenhaView, "Senha alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                alterarSenhaView.dispose();
            } else {
                JOptionPane.showMessageDialog(alterarSenhaView, "Falha ao atualizar a senha no banco de dados.", "Erro de Atualização", JOptionPane.ERROR_MESSAGE);
            }
        });
        alterarSenhaView.setVisible(true);
    }
}