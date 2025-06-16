package controller;

import dao.AuditoriaDAO;
import dao.UsuarioDAO;
import model.Auditoria;
import model.Usuario;
import util.PasswordUtil;
import view.ClienteView;
import view.FuncionarioView;
import view.LoginView;

import javax.swing.JOptionPane;
import java.time.LocalDateTime;

public class UsuarioController {
    private LoginView loginView;
    private UsuarioDAO usuarioDAO;
    private AuditoriaDAO auditoriaDAO;

    public UsuarioController(LoginView loginView) {
        this.loginView = loginView;
        this.usuarioDAO = new UsuarioDAO();
        this.auditoriaDAO = new AuditoriaDAO();
        this.initController();
    }

    private void initController() {
        loginView.getGerarOtpButton().addActionListener(e -> gerarOtpParaUsuario());
        loginView.getLoginButton().addActionListener(e -> realizarLoginComOtp());
        loginView.getSairButton().addActionListener(e -> sairDoSistema());
    }

    private void gerarOtpParaUsuario() {
        System.out.println("--- UsuarioController: Ação Gerar OTP Iniciada ---");
        String cpf = loginView.getCpfField().getText().trim();
        if (cpf.isEmpty()) {
            JOptionPane.showMessageDialog(loginView, "Por favor, digite o CPF para gerar o OTP.", "CPF Necessário", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Busca o usuário APENAS para obter o ID e verificar existência
        Usuario usuarioExistente = usuarioDAO.findByCpf(cpf);

        if (usuarioExistente == null) {
            JOptionPane.showMessageDialog(loginView, "Usuário com o CPF '" + cpf + "' não encontrado. Verifique o CPF.", "Usuário Não Encontrado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verifica o tipo de usuário selecionado na tela ANTES de gerar OTP
        String tipoSelecionadoTela = loginView.getFuncionarioRadioButton().isSelected() ? "FUNCIONARIO" : "CLIENTE";
        if (!usuarioExistente.getTipoUsuario().equalsIgnoreCase(tipoSelecionadoTela)) {
            JOptionPane.showMessageDialog(loginView, "O tipo de usuário selecionado (" + tipoSelecionadoTela + ") não corresponde ao tipo do usuário cadastrado ("+ usuarioExistente.getTipoUsuario() +") para este CPF.", "Tipo de Usuário Incorreto", JOptionPane.ERROR_MESSAGE);
            return;
        }


        System.out.println("Solicitando novo OTP para o usuário ID: " + usuarioExistente.getIdUsuario());
        String otpGerado = usuarioDAO.solicitarNovoOtp(usuarioExistente.getIdUsuario());

        if (otpGerado != null) {
            loginView.getOtpField().setText(otpGerado); // Preenche o campo para facilitar o teste
            JOptionPane.showMessageDialog(loginView, "OTP Gerado: " + otpGerado + "\nEste OTP é válido por 5 minutos.", "OTP Gerado com Sucesso", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("OTP gerado e exibido para usuário ID " + usuarioExistente.getIdUsuario() + ": " + otpGerado);
        } else {
            JOptionPane.showMessageDialog(loginView, "Não foi possível gerar o OTP no momento. Tente novamente.", "Falha ao Gerar OTP", JOptionPane.ERROR_MESSAGE);
            System.err.println("Falha ao gerar OTP para usuário ID " + usuarioExistente.getIdUsuario());
        }
    }

    private void realizarLoginComOtp() {
        System.out.println("--- UsuarioController: Ação de Login Iniciada ---");
        String cpf = loginView.getCpfField().getText().trim();
        String senhaDigitada = new String(loginView.getSenhaField().getPassword());
        String otpDigitado = loginView.getOtpField().getText().trim();

        if (cpf.isEmpty() || senhaDigitada.isEmpty() || otpDigitado.isEmpty()) {
            JOptionPane.showMessageDialog(loginView, "Todos os campos (CPF, Senha e OTP) são obrigatórios.", "Campos Vazios", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Busca os dados mais recentes do usuário, incluindo OTP ativo e expiração
        Usuario usuarioDoBanco = usuarioDAO.findByCpf(cpf);
        Integer idUsuarioParaAuditoria = (usuarioDoBanco != null) ? usuarioDoBanco.getIdUsuario() : null;

        if (usuarioDoBanco == null) {
            JOptionPane.showMessageDialog(loginView, "Usuário não encontrado.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            registrarAuditoriaLogin(idUsuarioParaAuditoria, "LOGIN_FALHA", "Tentativa de login para CPF: " + cpf + ". Motivo: Usuário não encontrado.");
            return;
        }

        // Valida o tipo de usuário selecionado na tela COM o tipo do usuário no banco
        String tipoSelecionadoTela = loginView.getFuncionarioRadioButton().isSelected() ? "FUNCIONARIO" : "CLIENTE";
        if (!usuarioDoBanco.getTipoUsuario().equalsIgnoreCase(tipoSelecionadoTela)) {
            JOptionPane.showMessageDialog(loginView, "O tipo de usuário selecionado não corresponde ao cadastro para este CPF.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            registrarAuditoriaLogin(idUsuarioParaAuditoria, "LOGIN_FALHA", "Tentativa de login para CPF: " + cpf + ". Motivo: Tipo de usuário incorreto (Selecionado: "+tipoSelecionadoTela+", Cadastrado: "+usuarioDoBanco.getTipoUsuario()+").");
            return;
        }

        // Valida a senha
        String senhaDigitadaHash = PasswordUtil.md5(senhaDigitada);
        if (!senhaDigitadaHash.equals(usuarioDoBanco.getSenhaHash())) {
            JOptionPane.showMessageDialog(loginView, "Senha incorreta.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            registrarAuditoriaLogin(idUsuarioParaAuditoria, "LOGIN_FALHA", "Tentativa de login para CPF: " + cpf + ". Motivo: Senha incorreta.");
            // Implementar lógica de contagem de tentativas e bloqueio aqui futuramente
            return;
        }

        // Valida o OTP
        if (usuarioDoBanco.getOtpAtivo() == null || usuarioDoBanco.getOtpExpiracao() == null) {
            JOptionPane.showMessageDialog(loginView, "OTP não foi gerado para este usuário ou os dados do OTP no banco são inválidos. Por favor, clique em 'Gerar OTP'.", "Erro de OTP", JOptionPane.ERROR_MESSAGE);
            registrarAuditoriaLogin(idUsuarioParaAuditoria, "LOGIN_FALHA", "Tentativa de login para CPF: " + cpf + ". Motivo: OTP não encontrado no banco / dados inválidos.");
            return;
        }

        if (!otpDigitado.equals(usuarioDoBanco.getOtpAtivo())) {
            JOptionPane.showMessageDialog(loginView, "OTP incorreto.", "Erro de OTP", JOptionPane.ERROR_MESSAGE);
            registrarAuditoriaLogin(idUsuarioParaAuditoria, "LOGIN_FALHA", "Tentativa de login para CPF: " + cpf + ". Motivo: OTP digitado incorreto.");
            // Implementar lógica de contagem de tentativas e bloqueio aqui futuramente
            return;
        }

        if (LocalDateTime.now().isAfter(usuarioDoBanco.getOtpExpiracao())) {
            JOptionPane.showMessageDialog(loginView, "OTP expirado. Por favor, clique em 'Gerar OTP' para um novo código.", "Erro de OTP", JOptionPane.ERROR_MESSAGE);
            registrarAuditoriaLogin(idUsuarioParaAuditoria, "LOGIN_FALHA", "Tentativa de login para CPF: " + cpf + ". Motivo: OTP expirado.");
            return;
        }

        // Se chegou até aqui, todas as validações passaram
        registrarAuditoriaLogin(idUsuarioParaAuditoria, "LOGIN_SUCESSO", "Login realizado com sucesso para CPF: " + cpf + " como " + tipoSelecionadoTela);
        JOptionPane.showMessageDialog(loginView, "Login bem-sucedido! Bem-vindo(a), " + usuarioDoBanco.getNome() + "!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        loginView.dispose(); // Fecha a tela de login

        if (tipoSelecionadoTela.equals("FUNCIONARIO")) {
            FuncionarioView funcView = new FuncionarioView();
            new FuncionarioController(funcView, usuarioDoBanco);
            funcView.setVisible(true);
        } else { // CLIENTE
            new ClienteView(usuarioDoBanco).setVisible(true);
        }
    }

    private void registrarAuditoriaLogin(Integer idUsuario, String acao, String detalhes) {
        Auditoria log = new Auditoria();
        // idUsuario pode ser nulo se o CPF digitado não corresponder a nenhum usuário
        if (idUsuario != null) {
            log.setIdUsuario(idUsuario);
        }
        log.setAcao(acao);
        log.setDetalhes(detalhes);
        // dataHora é definida automaticamente no construtor de Auditoria
        auditoriaDAO.registrarAcao(log);
        System.out.println("Auditoria registrada: " + acao + " - " + detalhes);
    }

    private void sairDoSistema() {
        System.out.println("--- UsuarioController: Ação Sair do Sistema ---");
        // Poderia registrar um log de SAIDA_SISTEMA aqui se desejado
        System.exit(0);
    }
}