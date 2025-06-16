package controller;

import dao.AuditoriaDAO;
import dao.FuncionarioDAO;
import model.Auditoria;
import model.Funcionario;
import model.Usuario;
import util.PasswordUtil;
import view.CadastroFuncionarioView;
import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class CadastroFuncionarioController {

    private CadastroFuncionarioView view;
    private FuncionarioDAO dao;
    private AuditoriaDAO auditoriaDAO;
    private Usuario funcionarioQueCadastra;

    public CadastroFuncionarioController(CadastroFuncionarioView view, Usuario funcionarioQueCadastra) {
        this.view = view;
        this.funcionarioQueCadastra = funcionarioQueCadastra;
        this.dao = new FuncionarioDAO();
        this.auditoriaDAO = new AuditoriaDAO();
        initController();
    }

    private void initController() {
        view.getSalvarButton().addActionListener(e -> salvarFuncionario());
        view.getCancelarButton().addActionListener(e -> view.dispose());
    }

    private void salvarFuncionario() {
        System.out.println("--- CadastroFuncionarioController: Método salvarFuncionario INICIADO ---");
        try {
            if (view.getNomeField().getText().trim().isEmpty() ||
                    view.getCpfField().getText().trim().isEmpty() ||
                    view.getDataNascimentoField().getText().trim().isEmpty() ||
                    view.getTelefoneField().getText().trim().isEmpty() ||
                    new String(view.getSenhaField().getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(view, "Todos os campos são obrigatórios!", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String senhaDigitada = new String(view.getSenhaField().getPassword());
            // --- VALIDAÇÃO DE SENHA FORTE ---
            if (!PasswordUtil.isPasswordStrong(senhaDigitada)) {
                JOptionPane.showMessageDialog(view,
                        "A senha não é forte o suficiente.\n" +
                                "Requisitos: Mínimo 8 caracteres, 1 maiúscula, 1 minúscula, 1 número, 1 especial (!@#$%).",
                        "Senha Fraca", JOptionPane.ERROR_MESSAGE);
                System.out.println("Validação falhou: Senha fraca para novo funcionário.");
                return;
            }
            // --- FIM DA VALIDAÇÃO DE SENHA FORTE ---

            Usuario usuario = new Usuario();
            usuario.setNome(view.getNomeField().getText().trim());
            usuario.setCpf(view.getCpfField().getText().trim().replaceAll("[^0-9]", ""));
            usuario.setTelefone(view.getTelefoneField().getText().trim());
            usuario.setDataNascimento(LocalDate.parse(view.getDataNascimentoField().getText().trim()));
            usuario.setSenhaHash(senhaDigitada);

            System.out.println("Objeto Usuario para novo funcionário preenchido e senha validada.");

            Funcionario funcionario = new Funcionario();
            funcionario.setCargo((String) view.getCargoBox().getSelectedItem());
            System.out.println("Objeto Funcionario preenchido: Cargo " + funcionario.getCargo());


            System.out.println("Chamando dao.salvarNovoFuncionario...");
            dao.salvarNovoFuncionario(usuario, funcionario);

            System.out.println(">>> FuncionarioDAO.salvarNovoFuncionario executado.");

            // Registrar Auditoria
            Auditoria log = new Auditoria();
            log.setIdUsuario(funcionarioQueCadastra.getIdUsuario());
            log.setAcao("CADASTRO_FUNCIONARIO");
            log.setDetalhes("Novo funcionário CPF: " + usuario.getCpf() + " (Usuário ID: " + usuario.getIdUsuario() + ") cadastrado com cargo: " + funcionario.getCargo());
            auditoriaDAO.registrarAcao(log);

            JOptionPane.showMessageDialog(view, "Funcionário cadastrado com sucesso!");
            view.dispose();

        } catch (DateTimeParseException ex) {
            System.err.println("!!! ERRO de Formato de Data no CadastroFuncionarioController !!!");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Formato de data inválido. Use AAAA-MM-DD.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            System.err.println("!!! ERRO de SQL no CadastroFuncionarioController !!!");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Erro de banco de dados: " + ex.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            if(ex.getMessage() != null && ex.getMessage().contains("Duplicate entry")){
                JOptionPane.showMessageDialog(view, "O CPF informado já está cadastrado.", "Erro de Duplicidade", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            System.err.println("!!! ERRO Inesperado no CadastroFuncionarioController !!!");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("--- CadastroFuncionarioController: Método salvarFuncionario FINALIZADO ---");
    }
}