package dao;

import model.Endereco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EnderecoDAO {

    /**
     * Busca um endereço pelo ID do usuário.
     */
    public Endereco findByUsuarioId(int idUsuario) {
        System.out.println("--- EXECUTANDO findByUsuarioId no EnderecoDAO para idUsuario: " + idUsuario + " ---");
        String sql = "SELECT * FROM endereco WHERE id_usuario = ?";
        Endereco endereco = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                endereco = new Endereco();
                endereco.setIdEndereco(rs.getInt("id_endereco"));
                endereco.setIdUsuario(rs.getInt("id_usuario"));
                endereco.setCep(rs.getString("cep"));
                endereco.setLocal(rs.getString("local"));
                endereco.setNumeroCasa(rs.getInt("numero_casa"));
                endereco.setBairro(rs.getString("bairro"));
                endereco.setCidade(rs.getString("cidade"));
                endereco.setEstado(rs.getString("estado"));
                endereco.setComplemento(rs.getString("complemento"));
                System.out.println("Endereço encontrado para idUsuario: " + idUsuario);
            } else {
                System.out.println("Nenhum endereço encontrado para idUsuario: " + idUsuario);
            }
        } catch (SQLException e) {
            System.err.println("!!! ERRO DE SQL ao buscar endereço por idUsuario: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("--- FINALIZANDO findByUsuarioId no EnderecoDAO ---");
        return endereco;
    }

    /**
     * Salva um novo endereço ou atualiza um existente.
     * Se o endereço já tem um ID, atualiza. Caso contrário, insere.
     */
    public Endereco salvarOuAtualizar(Endereco endereco) throws SQLException {
        System.out.println("--- EXECUTANDO salvarOuAtualizar no EnderecoDAO ---");
        if (endereco.getIdEndereco() > 0) {
            System.out.println("Tentando ATUALIZAR endereço ID: " + endereco.getIdEndereco() + " para idUsuario: " + endereco.getIdUsuario());
            String sql = "UPDATE endereco SET cep = ?, local = ?, numero_casa = ?, bairro = ?, cidade = ?, estado = ?, complemento = ? WHERE id_endereco = ? AND id_usuario = ?";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, endereco.getCep());
                stmt.setString(2, endereco.getLocal());
                stmt.setInt(3, endereco.getNumeroCasa());
                stmt.setString(4, endereco.getBairro());
                stmt.setString(5, endereco.getCidade());
                stmt.setString(6, endereco.getEstado());
                stmt.setString(7, endereco.getComplemento());
                stmt.setInt(8, endereco.getIdEndereco());
                stmt.setInt(9, endereco.getIdUsuario());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Endereço ID " + endereco.getIdEndereco() + " atualizado com sucesso.");
                } else {
                    System.err.println("!!! Nenhuma linha afetada ao ATUALIZAR endereço ID: " + endereco.getIdEndereco() + ".");
                }
            }
        } else {
            System.out.println("Tentando INSERIR novo endereço para idUsuario: " + endereco.getIdUsuario());
            String sql = "INSERT INTO endereco (id_usuario, cep, local, numero_casa, bairro, cidade, estado, complemento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setInt(1, endereco.getIdUsuario());
                stmt.setString(2, endereco.getCep());
                stmt.setString(3, endereco.getLocal());
                stmt.setInt(4, endereco.getNumeroCasa());
                stmt.setString(5, endereco.getBairro());
                stmt.setString(6, endereco.getCidade());
                stmt.setString(7, endereco.getEstado());
                stmt.setString(8, endereco.getComplemento());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Falha ao inserir endereço, nenhuma linha afetada.");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        endereco.setIdEndereco(generatedKeys.getInt(1));
                        System.out.println("Novo endereço INSERIDO com ID: " + endereco.getIdEndereco());
                    } else {
                        throw new SQLException("Falha ao inserir endereço, não obteve ID gerado.");
                    }
                }
            }
        }
        System.out.println("--- FINALIZANDO salvarOuAtualizar no EnderecoDAO ---");
        return endereco;
    }
}