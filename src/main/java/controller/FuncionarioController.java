package controller;

import dao.ContaDAO;
import dao.EnderecoDAO;
import dao.RelatorioDAO;
import dao.UsuarioDAO;
import dao.AuditoriaDAO;
import model.Conta;
import model.ContaCorrente;
import model.Endereco;
import model.Funcionario;
import model.Usuario;
import model.Auditoria;
import view.AlterarClienteView;
import view.AlterarContaCorrenteView;
import view.BuscarContaView;
import view.CadastroFuncionarioView;
import view.CadastroView;
import view.ConsultaDadosView;
import view.EncerrarContaView;
import view.FuncionarioView;
import view.RelatorioView;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FuncionarioController {

    private FuncionarioView view;
    private ContaDAO contaDAO;
    private RelatorioDAO relatorioDAO;
    private UsuarioDAO usuarioDAO;
    private EnderecoDAO enderecoDAO;
    private AuditoriaDAO auditoriaDAO;
    private Conta contaParaEncerrar;
    private Usuario funcionarioLogado;

    public FuncionarioController(FuncionarioView view, Usuario funcionarioLogado) {
        this.view = view;
        this.funcionarioLogado = funcionarioLogado;
        this.contaDAO = new ContaDAO();
        this.relatorioDAO = new RelatorioDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.enderecoDAO = new EnderecoDAO();
        this.auditoriaDAO = new AuditoriaDAO();
        System.out.println("--- FuncionarioController: Construtor chamado para funcionário: " + (funcionarioLogado != null ? funcionarioLogado.getNome() : "N/A") + " ---");
        initController();
    }

    private void initController() {
        System.out.println("--- FuncionarioController: initController chamado ---");
        view.getAbrirContaButton().addActionListener(e -> abrirTelaCadastroConta());
        view.getEncerrarContaButton().addActionListener(e -> abrirTelaEncerrarConta());
        view.getCadastrarFuncionarioButton().addActionListener(e -> abrirTelaCadastroFuncionario());
        view.getAlterarDadosButton().addActionListener(e -> escolherTipoAlteracao());
        view.getGerarRelatoriosButton().addActionListener(e -> abrirTelaRelatorios());
        view.getConsultarResumoContasButton().addActionListener(e -> consultarResumoDeContas());
        view.getConsultarMovimentacoesButton().addActionListener(e -> consultarMovimentacoesRecentes());
        view.getSairButton().addActionListener(e -> logout());
        System.out.println("--- FuncionarioController: ActionListeners configurados ---");
    }

    private void abrirTelaCadastroConta() {
        System.out.println("--- FuncionarioController: Método abrirTelaCadastroConta chamado ---");
        CadastroView cadastroView = new CadastroView(view);
        new CadastroContaController(cadastroView, funcionarioLogado); // Passa o funcionário logado
        cadastroView.setVisible(true);
    }

    private void abrirTelaCadastroFuncionario() {
        System.out.println("--- FuncionarioController: Método abrirTelaCadastroFuncionario chamado ---");
        CadastroFuncionarioView cadFuncView = new CadastroFuncionarioView(view);
        new CadastroFuncionarioController(cadFuncView, funcionarioLogado); // Passa o funcionário logado
        cadFuncView.setVisible(true);
    }

    private void abrirTelaRelatorios() {
        System.out.println("--- FuncionarioController: Método abrirTelaRelatorios chamado ---");
        RelatorioView relatorioView = new RelatorioView(view);
        new RelatorioController(relatorioView /*, funcionarioLogado */); // Passar se RelatorioController precisar
        relatorioView.setVisible(true);
    }

    private void abrirTelaEncerrarConta() {
        System.out.println("--- FuncionarioController: Método abrirTelaEncerrarConta chamado ---");
        EncerrarContaView encerrarView = new EncerrarContaView(view);
        contaParaEncerrar = null;
        encerrarView.getConfirmarEncerramentoButton().setEnabled(false);
        encerrarView.getDadosContaLabel().setText("<html><i>Busque uma conta para ver os detalhes...</i></html>");

        encerrarView.getBuscarContaButton().addActionListener(e -> {
            String numeroConta = encerrarView.getNumeroContaField().getText().trim();
            if (numeroConta.isEmpty()) {
                JOptionPane.showMessageDialog(encerrarView, "Por favor, digite o número da conta.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            contaParaEncerrar = contaDAO.findByNumeroConta(numeroConta);
            if (contaParaEncerrar != null) {
                String infoText = "<html><b>Conta:</b> " + contaParaEncerrar.getNumeroConta() +
                        "<br><b>Saldo:</b> R$ " + (contaParaEncerrar.getSaldo() != null ? contaParaEncerrar.getSaldo().toPlainString() : "N/A") +
                        "<br><b>Status:</b> " + contaParaEncerrar.getStatus();
                if ("ENCERRADA".equals(contaParaEncerrar.getStatus())) {
                    infoText += "<br><i>Esta conta já está encerrada.</i></html>";
                    encerrarView.getConfirmarEncerramentoButton().setEnabled(false);
                } else if (!"ATIVA".equals(contaParaEncerrar.getStatus())) {
                    infoText += "<br><i>Esta conta não pode ser encerrada (status: " + contaParaEncerrar.getStatus() + ").</i></html>";
                    encerrarView.getConfirmarEncerramentoButton().setEnabled(false);
                } else if (contaParaEncerrar.getSaldo() != null && contaParaEncerrar.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
                    infoText += "<br><i>Conta com saldo diferente de zero. Não pode ser encerrada.</i></html>";
                    encerrarView.getConfirmarEncerramentoButton().setEnabled(false);
                } else {
                    infoText += "</html>";
                    encerrarView.getConfirmarEncerramentoButton().setEnabled(true);
                }
                encerrarView.getDadosContaLabel().setText(infoText);
            } else {
                encerrarView.getDadosContaLabel().setText("<html><i>Conta não encontrada.</i></html>");
                encerrarView.getConfirmarEncerramentoButton().setEnabled(false);
            }
        });
        encerrarView.getConfirmarEncerramentoButton().addActionListener(e -> {
            if (contaParaEncerrar != null) {
                if (contaParaEncerrar.getSaldo() != null && contaParaEncerrar.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
                    JOptionPane.showMessageDialog(encerrarView, "A conta precisa ter saldo zero para ser encerrada.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!"ATIVA".equals(contaParaEncerrar.getStatus())){
                    JOptionPane.showMessageDialog(encerrarView, "Apenas contas ativas podem ser encerradas.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String adminPassword = JOptionPane.showInputDialog(encerrarView, "Digite SUA senha de funcionário para confirmar:", "Autenticação Necessária", JOptionPane.WARNING_MESSAGE);
                if (adminPassword == null || !validaSenhaFuncionario(adminPassword)) {
                    JOptionPane.showMessageDialog(encerrarView, "Senha de funcionário incorreta ou cancelada.", "Falha na Autenticação", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(encerrarView,
                        "Tem certeza que deseja encerrar a conta " + contaParaEncerrar.getNumeroConta() + "?\nEsta ação não pode ser desfeita.",
                        "Confirmar Encerramento", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean sucesso = contaDAO.encerrarConta(contaParaEncerrar.getIdConta());
                    if (sucesso) {
                        Auditoria log = new Auditoria();
                        log.setIdUsuario(funcionarioLogado.getIdUsuario());
                        log.setAcao("ENCERRAMENTO_CONTA");
                        log.setDetalhes("Conta ID: " + contaParaEncerrar.getIdConta() + ", Numero: " + contaParaEncerrar.getNumeroConta() + ". Motivo: " + encerrarView.getMotivoArea().getText());
                        auditoriaDAO.registrarAcao(log);

                        JOptionPane.showMessageDialog(encerrarView, "Conta encerrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        encerrarView.dispose();
                    } else {
                        JOptionPane.showMessageDialog(encerrarView, "Não foi possível encerrar a conta.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        encerrarView.getCancelarButton().addActionListener(e -> encerrarView.dispose());
        encerrarView.setVisible(true);
    }

    private void consultarResumoDeContas() {
        System.out.println("--- FuncionarioController: Método consultarResumoDeContas chamado ---");
        try {
            TableModel model = relatorioDAO.gerarRelatorioFromView("vw_resumo_contas");
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(view, "Não há dados para exibir no resumo de contas.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            ConsultaDadosView consultaView = new ConsultaDadosView(view, "Resumo de Contas por Cliente");
            consultaView.setTableModel(model);
            consultaView.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Erro ao buscar resumo de contas: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void consultarMovimentacoesRecentes() {
        System.out.println("--- FuncionarioController: Método consultarMovimentacoesRecentes chamado ---");
        try {
            TableModel model = relatorioDAO.gerarRelatorioFromView("vw_movimentacoes_recentes");
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(view, "Não há movimentações recentes para exibir.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            ConsultaDadosView consultaView = new ConsultaDadosView(view, "Movimentações Recentes (Últimos 90 dias)");
            consultaView.setTableModel(model);
            consultaView.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Erro ao buscar movimentações recentes: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void escolherTipoAlteracao() {
        System.out.println("--- FuncionarioController: Método escolherTipoAlteracao chamado ---");
        String[] options = {"Alterar Dados do Cliente", "Alterar Dados da Conta Corrente", "Cancelar"};
        int escolha = JOptionPane.showOptionDialog(view, "Qual tipo de dado você deseja alterar?",
                "Selecionar Tipo de Alteração",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (escolha == 0) {
            abrirTelaAlterarDadosCliente();
        } else if (escolha == 1) {
            abrirTelaAlterarDadosContaCorrente();
        }
    }

    private void abrirTelaAlterarDadosCliente() {
        System.out.println("--- FuncionarioController: Método abrirTelaAlterarDadosCliente chamado ---");
        String cpfCliente = JOptionPane.showInputDialog(view, "Digite o CPF do cliente que deseja alterar:", "Buscar Cliente", JOptionPane.PLAIN_MESSAGE);

        if (cpfCliente == null || cpfCliente.trim().isEmpty()) {
            System.out.println("Busca por CPF cancelada ou CPF vazio.");
            return;
        }
        cpfCliente = cpfCliente.trim().replaceAll("[^0-9]", "");

        Usuario clienteParaAlterar = usuarioDAO.findByCpf(cpfCliente);

        if (clienteParaAlterar == null) {
            JOptionPane.showMessageDialog(view, "Cliente com CPF " + cpfCliente + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!"CLIENTE".equalsIgnoreCase(clienteParaAlterar.getTipoUsuario())) {
            JOptionPane.showMessageDialog(view, "O CPF informado não pertence a um cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Endereco enderecoDoCliente = enderecoDAO.findByUsuarioId(clienteParaAlterar.getIdUsuario());
        AlterarClienteView alterarView = new AlterarClienteView(view, clienteParaAlterar, enderecoDoCliente);
        new AlterarClienteController(alterarView, clienteParaAlterar, enderecoDoCliente, funcionarioLogado); // Passa funcionarioLogado
        alterarView.setVisible(true);
    }

    private void abrirTelaAlterarDadosContaCorrente() {
        System.out.println("--- FuncionarioController: Método abrirTelaAlterarDadosContaCorrente chamado ---");
        BuscarContaView buscarView = new BuscarContaView(view);
        buscarView.setVisible(true);

        String numeroConta = buscarView.getNumeroContaInput();

        if (numeroConta == null || numeroConta.isEmpty()) {
            System.out.println("Busca por número de conta cancelada ou vazia.");
            return;
        }

        Conta conta = contaDAO.findByNumeroConta(numeroConta);
        if (conta == null) {
            JOptionPane.showMessageDialog(view, "Conta com número " + numeroConta + " não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!"CORRENTE".equalsIgnoreCase(conta.getTipoConta())) {
            JOptionPane.showMessageDialog(view, "A conta " + numeroConta + " não é uma Conta Corrente.", "Tipo de Conta Inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ContaCorrente contaCorrente = contaDAO.findContaCorrenteByContaId(conta.getIdConta());
        if (contaCorrente == null) {
            JOptionPane.showMessageDialog(view, "Detalhes da conta corrente não encontrados para a conta " + numeroConta + ".", "Erro Interno", JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("Conta Corrente encontrada: " + conta.getNumeroConta());
        AlterarContaCorrenteView alterarView = new AlterarContaCorrenteView(view, conta, contaCorrente);

        new AlterarContaCorrenteController(alterarView, conta, contaCorrente /*, funcionarioLogado */); // Adicionar se for usar
        alterarView.setVisible(true);
    }

    private boolean validaSenhaFuncionario(String senhaDigitada) {
        if (funcionarioLogado == null || funcionarioLogado.getSenhaHash() == null) {
            return false;
        }
        return util.PasswordUtil.md5(senhaDigitada).equals(funcionarioLogado.getSenhaHash());
    }

    private void logout() {
        System.out.println("--- FuncionarioController: Método logout chamado ---");
        view.dispose();
    }
}