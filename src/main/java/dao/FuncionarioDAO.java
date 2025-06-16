package dao;

import model.Funcionario;
import model.Usuario;
import util.PasswordUtil;
import java.sql.*;
import java.util.Random;

public class FuncionarioDAO {

    public void salvarNovoFuncionario(Usuario usuario, Funcionario funcionario) throws SQLException {
        System.out.println("--- EXECUTANDO salvarNovoFuncionario no FuncionarioDAO ---");
        System.out.println("Recebido para salvar (Usuário): " + usuario.getNome() + ", CPF: " + usuario.getCpf());
        System.out.println("Senha (texto plano recebido pelo DAO): " + usuario.getSenhaHash()); // Debug da senha
        System.out.println("Cargo do Funcionário: " + funcionario.getCargo());

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            System.out.println("Conexão obtida no FuncionarioDAO. Válida: " + conn.isValid(2));
            conn.setAutoCommit(false);
            System.out.println("Autocommit desativado no FuncionarioDAO.");

            // 1. Salva na tabela 'usuario'
            String sqlUsuario = "INSERT INTO usuario (nome, cpf, data_nascimento, telefone, tipo_usuario, senha_hash) VALUES (?, ?, ?, ?, 'FUNCIONARIO', ?)";
            System.out.println("SQL Usuário (FuncionarioDAO): " + sqlUsuario);
            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, usuario.getNome());
                stmtUser.setString(2, usuario.getCpf());
                stmtUser.setDate(3, java.sql.Date.valueOf(usuario.getDataNascimento()));
                stmtUser.setString(4, usuario.getTelefone());
                // A senha já veio como texto plano do controller, aqui fazemos o hash
                stmtUser.setString(5, PasswordUtil.md5(usuario.getSenhaHash()));
                System.out.println("Executando INSERT para usuário (FuncionarioDAO)... Hash da senha a ser salvo: " + PasswordUtil.md5(usuario.getSenhaHash()));

                int affectedRowsUser = stmtUser.executeUpdate();
                System.out.println("Linhas afetadas na tabela 'usuario': " + affectedRowsUser);

                if (affectedRowsUser == 0) {
                    System.err.println("!!! Falha crítica: INSERT na tabela usuário não afetou linhas (FuncionarioDAO).");
                    conn.rollback(); // Importante
                    throw new SQLException("Falha ao inserir usuário, nenhuma linha afetada.");
                }

                ResultSet rsUser = stmtUser.getGeneratedKeys();
                if (rsUser.next()) {
                    usuario.setIdUsuario(rsUser.getInt(1));
                    System.out.println("ID do usuário gerado (FuncionarioDAO): " + usuario.getIdUsuario());
                } else {
                    System.err.println("!!! Falha ao obter ID do novo usuário no FuncionarioDAO após INSERT bem-sucedido.");
                    conn.rollback(); // Importante
                    throw new SQLException("Falha ao obter ID gerado para usuário.");
                }
            }

            // 2. Salva na tabela 'funcionario'
            String sqlFuncionario = "INSERT INTO funcionario (id_usuario, codigo_funcionario, cargo, id_supervisor) VALUES (?, ?, ?, ?)";
            System.out.println("SQL Funcionário (FuncionarioDAO): " + sqlFuncionario);
            try (PreparedStatement stmtFunc = conn.prepareStatement(sqlFuncionario)) {
                stmtFunc.setInt(1, usuario.getIdUsuario());
                String codigoGerado = gerarCodigoFuncionario();
                stmtFunc.setString(2, codigoGerado);
                stmtFunc.setString(3, funcionario.getCargo());
                stmtFunc.setObject(4, funcionario.getIdSupervisor()); // Pode ser nulo
                System.out.println("Executando INSERT para funcionário (FuncionarioDAO)... Código: " + codigoGerado);

                int affectedRowsFunc = stmtFunc.executeUpdate();
                System.out.println("Linhas afetadas na tabela 'funcionario': " + affectedRowsFunc);

                if (affectedRowsFunc == 0) {
                    System.err.println("!!! Falha crítica: INSERT na tabela funcionario não afetou linhas (FuncionarioDAO).");
                    conn.rollback(); // Importante
                    throw new SQLException("Falha ao inserir funcionário, nenhuma linha afetada.");
                }
            }

            conn.commit();
            System.out.println("Commit realizado no FuncionarioDAO. Funcionário salvo com sucesso.");

        } catch (SQLException e) {
            System.err.println("!!! ERRO DE SQL no FuncionarioDAO: " + e.getMessage() + " SQLState: " + e.getSQLState());
            e.printStackTrace();
            if (conn != null) {
                try {
                    System.err.println("Tentando rollback no FuncionarioDAO...");
                    conn.rollback();
                    System.err.println("Rollback realizado no FuncionarioDAO devido a erro.");
                } catch (SQLException exRollback) {
                    System.err.println("!!! ERRO ao tentar fazer rollback no FuncionarioDAO: " + exRollback.getMessage());
                }
            }
            throw e; // Relança a exceção para o controller tratar
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("Conexão fechada no FuncionarioDAO.");
                } catch (SQLException eClose) {
                    System.err.println("Erro ao fechar conexão no FuncionarioDAO: " + eClose.getMessage());
                }
            }
        }
        System.out.println("--- FINALIZANDO salvarNovoFuncionario no FuncionarioDAO ---");
    }

    private String gerarCodigoFuncionario() {
        return "FUNC-" + (10000 + new Random().nextInt(90000));
    }
    // Outros métodos do FuncionarioDAO, se houver...
}