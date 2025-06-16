package dao;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class RelatorioDAO {

    /**
     * Executa uma consulta em uma VIEW do banco e retorna os dados como um TableModel.
     * Isso torna o método genérico para qualquer relatório baseado em uma view simples.
     * @param viewName O nome da VIEW a ser consultada (ex: "vw_movimentacoes_recentes").
     * @return um TableModel com os dados da consulta.
     */
    public TableModel gerarRelatorioFromView(String viewName) throws SQLException {
        // Validação simples para evitar SQL Injection no nome da view
        if (!viewName.matches("^[a-zA-Z0-9_]+$")) {
            throw new SQLException("Nome da view inválido.");
        }

        String sql = "SELECT * FROM " + viewName;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return buildTableModel(rs);
        }
    }

    /**
     * Constrói um DefaultTableModel a partir de um ResultSet.
     * Útil para popular JTables diretamente com dados do banco.
     */
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // Nomes das colunas
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Dados das linhas
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }
}