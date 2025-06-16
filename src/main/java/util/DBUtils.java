package util;

import dao.ConnectionFactory;
import model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class DBUtils {

    private static String sql;
    private static String sqlEndereco;

    public static String GerarCodigoFunc() {
        return UUID.randomUUID().toString();
    }

    public static void insertNewUser(int idGerado, String tipoUser, String cargo, Endereco enderecoUsuario) {
        System.out.println("ID gerado: " + idGerado);

        String sqlFuncionario = "INSERT INTO funcionario (codigo_funcionario, cargo, id_usuario) VALUES (?, ?, ?)";
        String sqlCliente = "INSERT INTO cliente (id_usuario) VALUES (?)";
        String sqlInsertEndereco = "INSERT INTO endereco (cep, local, numero_casa, bairro, cidade, estado, complemento, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            if (tipoUser.equals("FUNCIONARIO")) {
                setSql(sqlFuncionario); // Mantendo seus setters se eles têm um propósito específico
                setSqlEndereco(sqlInsertEndereco);

                try (PreparedStatement psFunc = conn.prepareStatement(getSql()); // Usando getSql()
                     PreparedStatement psEnd = conn.prepareStatement(getSqlEndereco())) { // Usando getSqlEndereco()

                    psFunc.setString(1, GerarCodigoFunc()); // Gerar código aqui, não pegar de uma variável estática
                    psFunc.setString(2, cargo);
                    psFunc.setInt(3, idGerado);
                    psFunc.executeUpdate();

                    psEnd.setString(1, enderecoUsuario.getCep());
                    psEnd.setString(2, enderecoUsuario.getLocal()); // <--- CORRIGIDO AQUI
                    psEnd.setInt(3, enderecoUsuario.getNumeroCasa());
                    psEnd.setString(4, enderecoUsuario.getBairro());
                    psEnd.setString(5, enderecoUsuario.getCidade());
                    psEnd.setString(6, enderecoUsuario.getEstado());
                    psEnd.setString(7, enderecoUsuario.getComplemento());
                    psEnd.setInt(8, idGerado);
                    psEnd.executeUpdate();
                }
            } else { // CLIENTE
                setSql(sqlCliente);
                setSqlEndereco(sqlInsertEndereco);

                try (PreparedStatement psCli = conn.prepareStatement(getSql());
                     PreparedStatement psEnd = conn.prepareStatement(getSqlEndereco())) {

                    psCli.setInt(1, idGerado);
                    psCli.executeUpdate();

                    psEnd.setString(1, enderecoUsuario.getCep());
                    psEnd.setString(2, enderecoUsuario.getLocal()); // <--- CORRIGIDO AQUI
                    psEnd.setInt(3, enderecoUsuario.getNumeroCasa());
                    psEnd.setString(4, enderecoUsuario.getBairro());
                    psEnd.setString(5, enderecoUsuario.getCidade());
                    psEnd.setString(6, enderecoUsuario.getEstado());
                    psEnd.setString(7, enderecoUsuario.getComplemento());
                    psEnd.setInt(8, idGerado);
                    psEnd.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao reverter transação: " + ex.getMessage());
                }
            }
            e.printStackTrace();

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao restaurar auto-commit ou fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    public static String getSqlEndereco() { return sqlEndereco; }
    public static void setSqlEndereco(String sqlEnderecoParam) { DBUtils.sqlEndereco = sqlEnderecoParam; }
    public static String getSql() { return sql; }
    public static void setSql(String sqlParam) { DBUtils.sql = sqlParam; }
}