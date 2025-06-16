package dao;

import model.Cliente;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClienteDAO {

    public Cliente salvarNovoCliente(Usuario usuario) throws SQLException {
        System.out.println("--- EXECUTANDO salvarNovoCliente no ClienteDAO ---");
        System.out.println("Recebido para salvar (Usuário): " + usuario.getNome() + ", CPF: " + usuario.getCpf());
        Connection conn = null;
        Cliente novoCliente = null;
        try {
            conn = ConnectionFactory.getConnection();
            System.out.println("Conexão obtida no ClienteDAO. Válida: " + conn.isValid(2));
            conn.setAutoCommit(false);
            System.out.println("Autocommit desativado no ClienteDAO.");

            String sqlUsuario = "INSERT INTO usuario (nome, cpf, data_nascimento, telefone, tipo_usuario, senha_hash) VALUES (?, ?, ?, ?, 'CLIENTE', ?)";
            System.out.println("SQL Usuário (ClienteDAO): " + sqlUsuario);
            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, usuario.getNome());
                stmtUser.setString(2, usuario.getCpf());
                stmtUser.setDate(3, java.sql.Date.valueOf(usuario.getDataNascimento()));
                stmtUser.setString(4, usuario.getTelefone());
                stmtUser.setString(5, usuario.getSenhaHash()); // Senha JÁ VEM COM HASH do controller
                System.out.println("Executando INSERT para usuário (ClienteDAO)...");
                int affectedRowsUser = stmtUser.executeUpdate();
                System.out.println("Linhas afetadas (usuario): " + affectedRowsUser);

                if (affectedRowsUser == 0) {
                    System.err.println("!!! Falha crítica: INSERT na tabela usuário não afetou linhas.");
                    conn.rollback();
                    return null;
                }

                ResultSet rsUser = stmtUser.getGeneratedKeys();
                if (rsUser.next()) {
                    usuario.setIdUsuario(rsUser.getInt(1));
                    System.out.println("ID do usuário gerado (ClienteDAO): " + usuario.getIdUsuario());
                } else {
                    System.err.println("!!! Falha ao obter ID do novo usuário no ClienteDAO após INSERT bem-sucedido.");
                    conn.rollback();
                    return null;
                }
            }

            String sqlCliente = "INSERT INTO cliente (id_usuario, score_credito) VALUES (?, ?)";
            System.out.println("SQL Cliente (ClienteDAO): " + sqlCliente);
            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
                stmtCliente.setInt(1, usuario.getIdUsuario());
                stmtCliente.setDouble(2, 50.0); // Score inicial padrão
                System.out.println("Executando INSERT para cliente (ClienteDAO)...");
                int affectedRowsCliente = stmtCliente.executeUpdate();
                System.out.println("Linhas afetadas (cliente): " + affectedRowsCliente);

                if (affectedRowsCliente == 0) {
                    System.err.println("!!! Falha crítica: INSERT na tabela cliente não afetou linhas.");
                    conn.rollback();
                    return null;
                }

                ResultSet rsCliente = stmtCliente.getGeneratedKeys();
                if(rsCliente.next()){
                    novoCliente = new Cliente();
                    novoCliente.setIdCliente(rsCliente.getInt(1));
                    novoCliente.setIdUsuario(usuario.getIdUsuario());
                    novoCliente.setScoreCredito(50.0);
                    System.out.println("ID do cliente gerado (ClienteDAO): " + novoCliente.getIdCliente());
                } else {
                    System.err.println("!!! Falha ao obter ID do novo cliente no ClienteDAO após INSERT bem-sucedido.");
                    conn.rollback();
                    return null;
                }
            }

            conn.commit();
            System.out.println("Commit realizado no ClienteDAO. Cliente salvo com sucesso.");

        } catch (SQLException e) {
            System.err.println("!!! ERRO DE SQL no ClienteDAO: " + e.getMessage() + " SQLState: " + e.getSQLState());
            e.printStackTrace();
            if (conn != null) {
                try {
                    System.err.println("Tentando rollback no ClienteDAO...");
                    conn.rollback();
                    System.err.println("Rollback realizado no ClienteDAO devido a erro.");
                } catch (SQLException exRollback) {
                    System.err.println("!!! ERRO ao tentar fazer rollback no ClienteDAO: " + exRollback.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaura autocommit
                    conn.close();
                    System.out.println("Conexão fechada no ClienteDAO.");
                } catch (SQLException eClose) {
                    System.err.println("Erro ao fechar conexão no ClienteDAO: " + eClose.getMessage());
                }
            }
        }
        System.out.println("--- FINALIZANDO salvarNovoCliente no ClienteDAO --- Retornando: " + (novoCliente != null ? "ID Cliente " + novoCliente.getIdCliente() : "null"));
        return novoCliente;
    }
}