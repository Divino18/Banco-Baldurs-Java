package util;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVExporter {

    public static void exportTable(JTable table, File file) throws IOException {
        TableModel model = table.getModel();
        try (FileWriter writer = new FileWriter(file)) {
            // Escreve o cabeçalho (nomes das colunas)
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.write(model.getColumnName(i) + (i < model.getColumnCount() - 1 ? "," : ""));
            }
            writer.write("\n");

            // Escreve os dados das linhas
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object cellObject = model.getValueAt(i, j);
                    String cellValue = (cellObject == null) ? "" : cellObject.toString();
                    // Escapa aspas dentro do valor, se houver
                    cellValue = cellValue.replace("\"", "\"\"");
                    writer.write("\"" + cellValue + "\"" + (j < model.getColumnCount() - 1 ? "," : ""));
                }
                writer.write("\n");
            }
        }
    }
}