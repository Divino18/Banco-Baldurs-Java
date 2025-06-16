package controller;

import dao.AuditoriaDAO;
import dao.ClienteDAO;
import dao.ContaDAO;
import model.*;
import util.PasswordUtil;
import view.CadastroView;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.sql.SQLException;
import java.util.Random;

public class CadastroContaController {

    private CadastroView view;
    private ClienteDAO clienteDAO;
    private ContaDAO contaDAO;
    private AuditoriaDAO auditoriaDAO;
    private Usuario funcionarioLogado;

    public CadastroContaController(CadastroView view, Usuario funcionarioLogado) {
        this.view = view;
        this.funcionarioLogado = funcionarioLogado;
        this.clienteDAO = new ClienteDAO();
        this.contaDAO = new ContaDAO();
        this.auditoriaDAO = new AuditoriaDAO();
        initController();
    }

    private void initController() {
        view.getSalvarButton().addActionListener(e -> salvarConta());
        view.getCancelarButton().addActionListener(e -> view.dispose());
    }

    private void salvarConta() {
        System.out.println("--- INICIANDO salvarConta no CadastroContaController ---");
        try {
            if (view.getNomeField().getText().trim().isEmpty() ||
                    view.getCpfField().getText().trim().isEmpty() ||
                    view.getDataNascimentoField().getText().trim().isEmpty() ||
                    view.getTelefoneField().getText().trim().isEmpty() ||
                    new String(view.getSenhaField().getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(view, "Todos os campos básicos de usuário são obrigatórios!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                System.out.println("Validação falhou: Campos básicos de usuário obrigatórios não preenchidos.");
                return;
            }

            String senhaDigitada = new String(view.getSenhaField().getPassword());
            // --- VALIDAÇÃO DE SENHA FORTE ---
            if (!PasswordUtil.isPasswordStrong(senhaDigitada)) {
                JOptionPane.showMessageDialog(view,
                        "A senha não é forte o suficiente.\n" +
                                "Requisitos: Mínimo 8 caracteres, 1 maiúscula, 1 minúscula, 1 número, 1 especial (!@#$%).",
                        "Senha Fraca", JOptionPane.ERROR_MESSAGE);
                System.out.println("Validação falhou: Senha fraca.");
                return;
            }
            // --- FIM DA VALIDAÇÃO DE SENHA FORTE ---

            Usuario novoUsuario = new Usuario();
            novoUsuario.setNome(view.getNomeField().getText().trim());
            novoUsuario.setCpf(view.getCpfField().getText().trim().replaceAll("[^0-9]", ""));
            novoUsuario.setTelefone(view.getTelefoneField().getText().trim());
            novoUsuario.setDataNascimento(LocalDate.parse(view.getDataNascimentoField().getText().trim()));
            novoUsuario.setSenhaHash(PasswordUtil.md5(senhaDigitada)); // Gera o hash APÓS a validação de força
            System.out.println("Dados do novo usuário preenchidos e senha validada.");


            System.out.println("Tentando salvar novo cliente (usuário)...");
            Cliente novoCliente = clienteDAO.salvarNovoCliente(novoUsuario);
            if (novoCliente != null && novoCliente.getIdCliente() > 0) {
                System.out.println(">>> Cliente salvo com sucesso! ID Cliente: " + novoCliente.getIdCliente() + ", ID Usuario: " + novoCliente.getIdUsuario());
                Auditoria logCliente = new Auditoria();
                logCliente.setIdUsuario(funcionarioLogado.getIdUsuario());
                logCliente.setAcao("CADASTRO_CLIENTE");
                logCliente.setDetalhes("Novo cliente ID: " + novoCliente.getIdCliente() + " (Usuário ID: " + novoCliente.getIdUsuario() + ", CPF: " + novoUsuario.getCpf() + ") cadastrado.");
                auditoriaDAO.registrarAcao(logCliente);
            } else {
                System.err.println("!!! FALHA ao salvar cliente ou ID retornado é inválido pelo clienteDAO.salvarNovoCliente.");
                JOptionPane.showMessageDialog(view, "Erro ao salvar dados do cliente. Verifique o console para detalhes.", "Erro Crítico", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Conta novaConta = new Conta();
            novaConta.setIdCliente(novoCliente.getIdCliente());
            String numeroContaGerado = gerarNumeroConta();
            novaConta.setNumeroConta(numeroContaGerado);
            novaConta.setIdAgencia(1);
            novaConta.setSaldo(BigDecimal.ZERO);
            novaConta.setStatus("ATIVA");
            System.out.println("Dados base da conta preparados. ID Cliente associado: " + novoCliente.getIdCliente());

            String tipoSelecionado = (String) view.getTipoContaBox().getSelectedItem();
            Object tipoContaEspecifica = null;
            System.out.println("Tipo de conta selecionado: " + tipoSelecionado);

            if (tipoSelecionado.equals("Conta Poupança")) {
                if (view.getTaxaRendimentoField().getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Taxa de rendimento é obrigatória para Conta Poupança.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Validação falhou: Taxa de rendimento obrigatória.");
                    return;
                }
                novaConta.setTipoConta("POUPANCA");
                ContaPoupanca cp = new ContaPoupanca();
                cp.setTaxaRendimento(new BigDecimal(view.getTaxaRendimentoField().getText().trim()));
                tipoContaEspecifica = cp;
            } else if (tipoSelecionado.equals("Conta Corrente")) {
                if (view.getLimiteField().getText().trim().isEmpty() ||
                        view.getTaxaManutencaoField().getText().trim().isEmpty() ||
                        view.getDataVencimentoField().getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Limite, Taxa de Manutenção e Data de Vencimento são obrigatórios para Conta Corrente.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Validação falhou: Campos da conta corrente obrigatórios.");
                    return;
                }
                novaConta.setTipoConta("CORRENTE");
                ContaCorrente cc = new ContaCorrente();
                cc.setLimite(new BigDecimal(view.getLimiteField().getText().trim()));
                cc.setTaxaManutencao(new BigDecimal(view.getTaxaManutencaoField().getText().trim()));
                cc.setDataVencimento(LocalDate.parse(view.getDataVencimentoField().getText().trim()));
                tipoContaEspecifica = cc;
            } else if (tipoSelecionado.equals("Conta Investimento")) {
                if (view.getTaxaRendimentoField().getText().trim().isEmpty() ||
                        view.getValorMinimoField().getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Taxa de Rendimento e Valor Mínimo são obrigatórios para Conta Investimento.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Validação falhou: Campos da conta investimento obrigatórios.");
                    return;
                }
                novaConta.setTipoConta("INVESTIMENTO");
                ContaInvestimento ci = new ContaInvestimento();
                ci.setTaxaRendimentoBase(new BigDecimal(view.getTaxaRendimentoField().getText().trim()));
                ci.setPerfilRisco((String) view.getPerfilRiscoBox().getSelectedItem());
                ci.setValorMinimo(new BigDecimal(view.getValorMinimoField().getText().trim()));
                tipoContaEspecifica = ci;
            }

            System.out.println("Tentando salvar conta e detalhes específicos via contaDAO.salvarConta...");
            Conta contaSalva = contaDAO.salvarConta(novaConta, tipoContaEspecifica);

            if (contaSalva != null && contaSalva.getIdConta() > 0) {
                System.out.println(">>> Conta salva com sucesso no banco! ID Conta: " + contaSalva.getIdConta());
                Auditoria logConta = new Auditoria();
                logConta.setIdUsuario(funcionarioLogado.getIdUsuario());
                logConta.setAcao("ABERTURA_CONTA");
                logConta.setDetalhes("Nova conta ID: " + contaSalva.getIdConta() + " (Número: " + contaSalva.getNumeroConta() + ") criada para Cliente ID: " + novoCliente.getIdCliente() + ". Tipo: " + contaSalva.getTipoConta());
                auditoriaDAO.registrarAcao(logConta);

                JOptionPane.showMessageDialog(view, "Conta criada com sucesso! Número da conta: " + novaConta.getNumeroConta());
                view.dispose();
            } else {
                System.err.println("!!! FALHA ao salvar a conta ou ID da conta retornado é inválido pelo contaDAO.salvarConta.");
                JOptionPane.showMessageDialog(view, "Erro ao salvar dados da conta. Verifique o console para detalhes.", "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            }

        } catch (DateTimeParseException ex) {
            System.err.println("!!! ERRO de Formato de Data no CadastroContaController !!!");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Formato de data inválido. Use AAAA-MM-DD.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            System.err.println("!!! ERRO de Formato de Número no CadastroContaController !!!");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Valor numérico inválido. Verifique os campos. Use ponto (.) para decimais.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            System.err.println("!!! ERRO de SQL no CadastroContaController !!!");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erro de banco de dados: " + ex.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            if(ex.getMessage() != null && ex.getMessage().contains("Duplicate entry")){
                JOptionPane.showMessageDialog(view, "O CPF informado já está cadastrado.", "Erro de Duplicidade", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            System.err.println("!!! ERRO Inesperado no CadastroContaController !!!");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("--- FINALIZANDO salvarConta no CadastroContaController ---");
    }

    private String gerarNumeroConta() {
        Random rand = new Random();
        int numero = 100000 + rand.nextInt(900000);
        return String.format("%d-%d", numero, (numero % 9 + 1));
    }
}