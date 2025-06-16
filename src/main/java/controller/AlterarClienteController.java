package controller;

import dao.AuditoriaDAO;
import dao.EnderecoDAO;
import dao.UsuarioDAO;
import model.Auditoria;
import model.Endereco;
import model.Usuario;
import view.AlterarClienteView;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AlterarClienteController {
    private AlterarClienteView view;
    private UsuarioDAO usuarioDAO;
    private EnderecoDAO enderecoDAO;
    private AuditoriaDAO auditoriaDAO;
    private Usuario usuarioOriginalCliente;
    private Endereco enderecoOriginalCliente;
    private Usuario funcionarioLogado;

    public AlterarClienteController(AlterarClienteView view, Usuario usuarioCliente, Endereco enderecoCliente, Usuario funcionarioLogado) {
        this.view = view;
        this.usuarioDAO = new UsuarioDAO();
        this.enderecoDAO = new EnderecoDAO();
        this.auditoriaDAO = new AuditoriaDAO();
        this.usuarioOriginalCliente = usuarioCliente;
        this.enderecoOriginalCliente = enderecoCliente;
        this.funcionarioLogado = funcionarioLogado;
        initController();
    }

    private void initController() {
        view.getSalvarButton().addActionListener(e -> salvarAlteracoes());
        view.getCancelarButton().addActionListener(e -> view.dispose());
    }

    private void salvarAlteracoes() {
        System.out.println("--- INICIANDO salvarAlteracoes no AlterarClienteController ---");

        String adminPassword = JOptionPane.showInputDialog(view, "Digite SUA senha de funcionário para confirmar:", "Autenticação Necessária", JOptionPane.WARNING_MESSAGE);
        if (adminPassword == null || !util.PasswordUtil.md5(adminPassword).equals(funcionarioLogado.getSenhaHash())) {
            JOptionPane.showMessageDialog(view, "Senha de funcionário incorreta ou cancelada.", "Falha na Autenticação", JOptionPane.ERROR_MESSAGE);
            System.out.println("Falha na autenticação do funcionário para alterar dados do cliente.");
            // Registrar tentativa falha de alteração na auditoria
            Auditoria logFalhaAuth = new Auditoria();
            logFalhaAuth.setIdUsuario(funcionarioLogado.getIdUsuario());
            logFalhaAuth.setAcao("TENTATIVA_ALTERACAO_CLIENTE_FALHA_AUTH");
            logFalhaAuth.setDetalhes("Tentativa de alterar dados do Cliente ID: " + usuarioOriginalCliente.getIdUsuario() + " (CPF: " + usuarioOriginalCliente.getCpf() + ") falhou na autenticação do funcionário.");
            // logFalhaAuth.setDataHora(LocalDateTime.now()); // Já é setado no construtor de Auditoria
            auditoriaDAO.registrarAcao(logFalhaAuth);
            return;
        }
        System.out.println("Autenticação do funcionário para alterar dados do cliente bem-sucedida.");

        boolean algumaAlteracaoRealizada = false;
        StringBuilder detalhesAuditoria = new StringBuilder("Alteração de dados do Cliente ID: " + usuarioOriginalCliente.getIdUsuario() + " (CPF: " + usuarioOriginalCliente.getCpf() + ").");

        // --- Atualiza telefone ---
        String novoTelefone = view.getTelefoneField().getText().trim();
        String telefoneAntigo = usuarioOriginalCliente.getTelefone() != null ? usuarioOriginalCliente.getTelefone() : "";

        if (!novoTelefone.equals(telefoneAntigo)) {
            System.out.println("Tentando atualizar telefone de: [" + telefoneAntigo + "] para: [" + novoTelefone + "]");
            if (usuarioDAO.atualizarTelefone(usuarioOriginalCliente.getIdUsuario(), novoTelefone)) {
                System.out.println("Telefone atualizado com sucesso para: " + novoTelefone);
                detalhesAuditoria.append(" Telefone alterado de '").append(telefoneAntigo).append("' para '").append(novoTelefone).append("'.");
                usuarioOriginalCliente.setTelefone(novoTelefone);
                algumaAlteracaoRealizada = true;
            } else {
                JOptionPane.showMessageDialog(view, "Falha ao atualizar telefone.", "Erro de Atualização", JOptionPane.ERROR_MESSAGE);
                System.err.println("Falha ao atualizar telefone no DAO.");
                return;
            }
        }

        // --- Prepara o objeto Endereco (novo ou existente) ---
        Endereco enderecoParaSalvar;
        boolean isNewEndereco = false;
        if (enderecoOriginalCliente != null) {
            enderecoParaSalvar = enderecoOriginalCliente; // Atualiza o existente
            System.out.println("Endereço original encontrado, ID: " + enderecoOriginalCliente.getIdEndereco());
        } else {
            enderecoParaSalvar = new Endereco(); // Cria um novo se não existia
            enderecoParaSalvar.setIdUsuario(usuarioOriginalCliente.getIdUsuario());
            isNewEndereco = true;
            System.out.println("Nenhum endereço original, preparando um novo para idUsuario: " + usuarioOriginalCliente.getIdUsuario());
        }

        // Coleta valores antigos do endereço para auditoria (mesmo que seja novo, os antigos serão vazios)
        String cepAntigo = isNewEndereco ? "" : (enderecoOriginalCliente.getCep() != null ? enderecoOriginalCliente.getCep() : "");
        String localAntigo = isNewEndereco ? "" : (enderecoOriginalCliente.getLocal() != null ? enderecoOriginalCliente.getLocal() : "");
        String numeroCasaAntigo = isNewEndereco ? "" : (enderecoOriginalCliente.getNumeroCasa() > 0 ? String.valueOf(enderecoOriginalCliente.getNumeroCasa()) : "");
        String bairroAntigo = isNewEndereco ? "" : (enderecoOriginalCliente.getBairro() != null ? enderecoOriginalCliente.getBairro() : "");
        String cidadeAntiga = isNewEndereco ? "" : (enderecoOriginalCliente.getCidade() != null ? enderecoOriginalCliente.getCidade() : "");
        String estadoAntigo = isNewEndereco ? "" : (enderecoOriginalCliente.getEstado() != null ? enderecoOriginalCliente.getEstado() : "");
        String complementoAntigo = isNewEndereco ? "" : (enderecoOriginalCliente.getComplemento() != null ? enderecoOriginalCliente.getComplemento() : "");

        // Coleta novos valores dos campos
        String cepNovo = view.getCepField().getText().trim();
        String localNovo = view.getLocalField().getText().trim();
        String numeroCasaNovoStr = view.getNumeroCasaField().getText().trim();
        String bairroNovo = view.getBairroField().getText().trim();
        String cidadeNova = view.getCidadeField().getText().trim();
        String estadoNovo = view.getEstadoField().getText().trim().toUpperCase();
        String complementoNovo = view.getComplementoField().getText().trim();

        boolean mudancaNoEndereco =
                !cepNovo.equals(cepAntigo) ||
                        !localNovo.equals(localAntigo) ||
                        !numeroCasaNovoStr.equals(numeroCasaAntigo) ||
                        !bairroNovo.equals(bairroAntigo) ||
                        !cidadeNova.equals(cidadeAntiga) ||
                        !estadoNovo.equals(estadoAntigo) ||
                        !complementoNovo.equals(complementoAntigo);

        // Se houve alguma alteração OU se é um endereço novo e algum campo foi preenchido
        if (mudancaNoEndereco &&
                !(cepNovo.isEmpty() && localNovo.isEmpty() && numeroCasaNovoStr.isEmpty() && bairroNovo.isEmpty() && cidadeNova.isEmpty() && estadoNovo.isEmpty() && complementoNovo.isEmpty() && isNewEndereco) ) {

            enderecoParaSalvar.setCep(cepNovo);
            enderecoParaSalvar.setLocal(localNovo);
            try {
                enderecoParaSalvar.setNumeroCasa(numeroCasaNovoStr.isEmpty() ? 0 : Integer.parseInt(numeroCasaNovoStr));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Número da casa inválido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                System.err.println("Número da casa inválido: " + numeroCasaNovoStr);
                return;
            }
            enderecoParaSalvar.setBairro(bairroNovo);
            enderecoParaSalvar.setCidade(cidadeNova);
            enderecoParaSalvar.setEstado(estadoNovo);
            enderecoParaSalvar.setComplemento(complementoNovo);

            try {
                System.out.println("Tentando salvar/atualizar endereço...");
                enderecoDAO.salvarOuAtualizar(enderecoParaSalvar);
                // Constrói detalhes da auditoria para o endereço
                detalhesAuditoria.append(" Endereço: ");
                if (!cepNovo.equals(cepAntigo)) detalhesAuditoria.append("CEP('").append(cepAntigo).append("'->'").append(cepNovo).append("') ");
                if (!localNovo.equals(localAntigo)) detalhesAuditoria.append("Local('").append(localAntigo).append("'->'").append(localNovo).append("') ");
                if (!numeroCasaNovoStr.equals(numeroCasaAntigo)) detalhesAuditoria.append("Num('").append(numeroCasaAntigo).append("'->'").append(numeroCasaNovoStr).append("') ");
                if (!bairroNovo.equals(bairroAntigo)) detalhesAuditoria.append("Bairro('").append(bairroAntigo).append("'->'").append(bairroNovo).append("') ");
                if (!cidadeNova.equals(cidadeAntiga)) detalhesAuditoria.append("Cidade('").append(cidadeAntiga).append("'->'").append(cidadeNova).append("') ");
                if (!estadoNovo.equals(estadoAntigo)) detalhesAuditoria.append("UF('").append(estadoAntigo).append("'->'").append(estadoNovo).append("') ");
                if (!complementoNovo.equals(complementoAntigo)) detalhesAuditoria.append("Compl.('").append(complementoAntigo).append("'->'").append(complementoNovo).append("').");

                System.out.println("Endereço salvo/atualizado com sucesso.");
                algumaAlteracaoRealizada = true;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Falha ao salvar/atualizar endereço: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
                System.err.println("Falha ao salvar/atualizar endereço no DAO: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        } else {
            System.out.println("Nenhuma alteração detectada nos dados do endereço ou campos de endereço vazios para novo endereço.");
        }


        if (algumaAlteracaoRealizada) {
            Auditoria log = new Auditoria();
            log.setIdUsuario(funcionarioLogado.getIdUsuario());
            log.setAcao("ALTERACAO_DADOS_CLIENTE");
            log.setDetalhes(detalhesAuditoria.toString().trim());
            // log.setDataHora(LocalDateTime.now()); // Já é setado no construtor de Auditoria
            auditoriaDAO.registrarAcao(log);

            JOptionPane.showMessageDialog(view, "Dados do cliente atualizados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            view.dispose();
        } else {
            JOptionPane.showMessageDialog(view, "Nenhuma alteração foi realizada.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
        System.out.println("--- FINALIZANDO salvarAlteracoes no AlterarClienteController ---");
    }
}