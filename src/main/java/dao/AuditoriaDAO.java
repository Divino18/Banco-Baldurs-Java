package dao;

import model.Auditoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AuditoriaDAO {

    public void registrarAcao(Auditoria auditoria) {
        System.out.println("--- EXECUTANDO registrarAcao no AuditoriaDAO ---");
        String sql = "INSERT INTO auditoria (id_usuario, acao, data_hora, detalhes) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // id_usuario pode ser nulo se a ação for do sistema, mas aqui será do funcionário
            if (auditoria.getIdUsuario() != null && auditoria.getIdUsuario() > 0) {
                stmt.setInt(1, auditoria.getIdUsuario());
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }

            stmt.setString(2, auditoria.getAcao());
            stmt.setTimestamp(3, Timestamp.valueOf(auditoria.getDataHora()));
            stmt.setString(4, auditoria.getDetalhes());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println(">>> Registro de auditoria salvo com sucesso: " + auditoria.getAcao());
            } else {
                System.err.println("!!! Falha ao salvar registro de auditoria. Nenhuma linha afetada.");
            }

        } catch (SQLException e) {
            System.err.println("!!! ERRO DE SQL ao registrar ação na auditoria: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("--- FINALIZANDO registrarAcao no AuditoriaDAO ---");
    }
}