package dao;

import model.Transacao;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {

    public void salvar(Transacao transacao) throws SQLException {
        String sql = "INSERT INTO transacao (id_conta_origem, id_conta_destino, tipo_transacao, valor, descricao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, transacao.getIdContaOrigem());
            stmt.setObject(2, transacao.getIdContaDestino());
            stmt.setString(3, transacao.getTipoTransacao());
            stmt.setBigDecimal(4, transacao.getValor());
            stmt.setString(5, transacao.getDescricao());

            stmt.executeUpdate();
        }
    }

    /**
     * NOVO MÉTODO: Busca as últimas 50 transações de uma conta.
     */
    public List<Transacao> getExtratoPorConta(int idConta) {
        List<Transacao> transacoes = new ArrayList<>();
        // Busca transações onde a conta é tanto origem quanto destino
        String sql = "SELECT * FROM transacao WHERE id_conta_origem = ? OR id_conta_destino = ? ORDER BY data_hora DESC LIMIT 50";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idConta);
            stmt.setInt(2, idConta);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Transacao transacao = new Transacao();
                transacao.setIdTransacao(rs.getInt("id_transacao"));
                transacao.setIdContaOrigem(rs.getObject("id_conta_origem", Integer.class));
                transacao.setIdContaDestino(rs.getObject("id_conta_destino", Integer.class));
                transacao.setTipoTransacao(rs.getString("tipo_transacao"));
                transacao.setValor(rs.getBigDecimal("valor"));
                transacao.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                transacao.setDescricao(rs.getString("descricao"));
                transacoes.add(transacao);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transacoes;
    }
}