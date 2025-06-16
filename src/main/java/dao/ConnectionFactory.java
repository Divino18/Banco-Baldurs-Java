package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    // --- INFORMAÇÕES DE CONEXÃO COM O BANCO DE DADOS ---
    private static final String URL = "jdbc:mysql://localhost:3306/BancoBaldurs";
    private static final String USER = "root";
    private static final String PASSWORD = "Di394639@"; // senha

    /**
     * Obtém uma conexão com o banco de dados.
     * @return um objeto Connection com a conexão estabelecida.
     * @throws SQLException se ocorrer um erro ao tentar conectar.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Tenta carregar o driver JDBC. Essencial para a conexão.
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Retorna a conexão estabelecida com o banco de dados
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            // Lança uma exceção de SQL se o driver não for encontrado.
            throw new SQLException("Driver do MySQL não encontrado.", e);
        }
    }
}