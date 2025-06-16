package dao;

import model.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContaDAO {

    public Conta salvarConta(Conta conta, Object tipoContaEspecifica) throws SQLException {
        System.out.println("--- EXECUTANDO salvarConta no ContaDAO ---");
        System.out.println("Recebido para salvar (Conta): " + conta.getNumeroConta() + ", Tipo: " + conta.getTipoConta() + ", ID Cliente: " + conta.getIdCliente());
        Connection conn = null;
        Conta contaSalva = null;

        try {
            conn = ConnectionFactory.getConnection();
            System.out.println("Conexão obtida no ContaDAO. Válida: " + conn.isValid(2));
            conn.setAutoCommit(false);
            System.out.println("Autocommit desativado no ContaDAO.");

            String sqlConta = "INSERT INTO conta (numero_conta, id_agencia, saldo, tipo_conta, id_cliente, status) VALUES (?, ?, ?, ?, ?, ?)";
            System.out.println("SQL Conta Base (ContaDAO): " + sqlConta);
            try (PreparedStatement stmtConta = conn.prepareStatement(sqlConta, Statement.RETURN_GENERATED_KEYS)) {
                stmtConta.setString(1, conta.getNumeroConta());
                stmtConta.setInt(2, conta.getIdAgencia());
                stmtConta.setBigDecimal(3, conta.getSaldo());
                stmtConta.setString(4, conta.getTipoConta());
                stmtConta.setInt(5, conta.getIdCliente());
                stmtConta.setString(6, conta.getStatus());
                System.out.println("Executando INSERT para conta base (ContaDAO)...");
                int affectedRowsConta = stmtConta.executeUpdate();
                System.out.println("Linhas afetadas (conta): " + affectedRowsConta);

                if (affectedRowsConta == 0) {
                    System.err.println("!!! Falha crítica: INSERT na tabela conta não afetou linhas.");
                    conn.rollback();
                    return null;
                }

                ResultSet rs = stmtConta.getGeneratedKeys();
                if (rs.next()) {
                    conta.setIdConta(rs.getInt(1));
                    System.out.println("ID da conta base gerado (ContaDAO): " + conta.getIdConta());
                } else {
                    System.err.println("!!! Falha ao obter ID da nova conta no ContaDAO após INSERT bem-sucedido.");
                    conn.rollback();
                    return null;
                }
            }

            if (tipoContaEspecifica instanceof ContaPoupanca) {
                System.out.println("Salvando detalhes de Conta Poupança (ContaDAO)...");
                salvarContaPoupanca(conn, conta.getIdConta(), (ContaPoupanca) tipoContaEspecifica);
            } else if (tipoContaEspecifica instanceof ContaCorrente) {
                System.out.println("Salvando detalhes de Conta Corrente (ContaDAO)...");
                salvarContaCorrente(conn, conta.getIdConta(), (ContaCorrente) tipoContaEspecifica);
            } else if (tipoContaEspecifica instanceof ContaInvestimento) {
                System.out.println("Salvando detalhes de Conta Investimento (ContaDAO)...");
                salvarContaInvestimento(conn, conta.getIdConta(), (ContaInvestimento) tipoContaEspecifica);
            }

            conn.commit();
            System.out.println("Commit realizado no ContaDAO. Conta salva com sucesso.");
            contaSalva = conta;

        } catch (SQLException e) {
            System.err.println("!!! ERRO DE SQL no ContaDAO: " + e.getMessage() + " SQLState: " + e.getSQLState());
            e.printStackTrace();
            if (conn != null) {
                try {
                    System.err.println("Tentando rollback no ContaDAO...");
                    conn.rollback();
                    System.err.println("Rollback realizado no ContaDAO devido a erro.");
                } catch (SQLException exRollback) {
                    System.err.println("!!! ERRO ao tentar fazer rollback no ContaDAO: " + exRollback.getMessage());
                }
            }
            throw e; // Relança a exceção para a camada de controller saber do erro
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("Conexão fechada no ContaDAO.");
                } catch (SQLException eClose) {
                    System.err.println("Erro ao fechar conexão no ContaDAO: " + eClose.getMessage());
                }
            }
        }
        System.out.println("--- FINALIZANDO salvarConta no ContaDAO --- Retornando: " + (contaSalva != null ? "ID Conta " + contaSalva.getIdConta() : "null"));
        return contaSalva; // Garante que sempre haja um retorno
    }

    private void salvarContaPoupanca(Connection conn, int idConta, ContaPoupanca cp) throws SQLException {
        System.out.println("Executando salvarContaPoupanca para idConta: " + idConta);
        String sql = "INSERT INTO conta_poupanca (id_conta, taxa_rendimento) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            stmt.setBigDecimal(2, cp.getTaxaRendimento());
            stmt.executeUpdate();
            System.out.println("Detalhes Conta Poupança salvos.");
        }
    }

    private void salvarContaCorrente(Connection conn, int idConta, ContaCorrente cc) throws SQLException {
        System.out.println("Executando salvarContaCorrente para idConta: " + idConta);
        String sql = "INSERT INTO conta_corrente (id_conta, limite, data_vencimento, taxa_manutencao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            stmt.setBigDecimal(2, cc.getLimite());
            stmt.setDate(3, java.sql.Date.valueOf(cc.getDataVencimento()));
            stmt.setBigDecimal(4, cc.getTaxaManutencao());
            stmt.executeUpdate();
            System.out.println("Detalhes Conta Corrente salvos.");
        }
    }

    private void salvarContaInvestimento(Connection conn, int idConta, ContaInvestimento ci) throws SQLException {
        System.out.println("Executando salvarContaInvestimento para idConta: " + idConta);
        String sql = "INSERT INTO conta_investimento (id_conta, perfil_risco, valor_minimo, taxa_rendimento_base) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            stmt.setString(2, ci.getPerfilRisco());
            stmt.setBigDecimal(3, ci.getValorMinimo());
            stmt.setBigDecimal(4, ci.getTaxaRendimentoBase());
            stmt.executeUpdate();
            System.out.println("Detalhes Conta Investimento salvos.");
        }
    }

    public List<Conta> findContasByClienteId(int idCliente) {
        String sql = "SELECT * FROM conta WHERE id_cliente = ? AND status = 'ATIVA'";
        List<Conta> contas = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Conta conta = new Conta();
                conta.setIdConta(rs.getInt("id_conta"));
                conta.setNumeroConta(rs.getString("numero_conta"));
                conta.setSaldo(rs.getBigDecimal("saldo"));
                conta.setTipoConta(rs.getString("tipo_conta"));
                conta.setIdCliente(rs.getInt("id_cliente"));
                conta.setStatus(rs.getString("status"));
                // Adicionar outros campos se necessário
                contas.add(conta);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Idealmente, tratar melhor ou relançar
        }
        return contas; // Retorno da lista (pode ser vazia)
    }

    public Conta findByNumeroConta(String numeroConta) {
        String sql = "SELECT * FROM conta WHERE numero_conta = ?";
        Conta conta = null;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroConta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                conta = new Conta();
                conta.setIdConta(rs.getInt("id_conta"));
                conta.setNumeroConta(rs.getString("numero_conta"));
                conta.setSaldo(rs.getBigDecimal("saldo"));
                conta.setIdCliente(rs.getInt("id_cliente"));
                conta.setTipoConta(rs.getString("tipo_conta"));
                conta.setStatus(rs.getString("status"));
                // Adicionar outros campos se necessário
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Idealmente, tratar melhor ou relançar
        }
        return conta; // Retorna a conta encontrada ou null
    }

    public ContaCorrente findContaCorrenteByContaId(int idConta) {
        String sql = "SELECT * FROM conta_corrente WHERE id_conta = ?";
        ContaCorrente contaCorrente = null;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                contaCorrente = new ContaCorrente();
                contaCorrente.setIdContaCorrente(rs.getInt("id_conta_corrente"));
                contaCorrente.setIdConta(rs.getInt("id_conta"));
                contaCorrente.setLimite(rs.getBigDecimal("limite"));
                contaCorrente.setDataVencimento(rs.getDate("data_vencimento").toLocalDate());
                contaCorrente.setTaxaManutencao(rs.getBigDecimal("taxa_manutencao"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Idealmente, tratar melhor ou relançar
        }
        return contaCorrente; // Retorna o objeto ou null
    }

    public boolean encerrarConta(int idConta) {
        System.out.println("--- EXECUTANDO encerrarConta no ContaDAO para ID: " + idConta + " ---");
        String sql = "UPDATE conta SET status = 'ENCERRADA' WHERE id_conta = ? AND status = 'ATIVA'";
        int affectedRows = 0;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConta);
            affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println(">>> Conta ID " + idConta + " encerrada com sucesso no banco.");
            } else {
                System.out.println(">>> Nenhuma conta encontrada com ID " + idConta + " e status 'ATIVA' para encerrar, ou a conta já estava encerrada/bloqueada.");
            }
        } catch (SQLException e) {
            System.err.println("!!! ERRO DE SQL ao tentar encerrar conta no ContaDAO !!!");
            e.printStackTrace();
            return false; // Indica falha
        } finally {
            System.out.println("--- FINALIZANDO encerrarConta no ContaDAO ---");
        }
        return affectedRows > 0; // Retorna true se alguma linha foi afetada
    }

    public boolean atualizarDadosContaCorrente(int idConta, BigDecimal novoLimite, BigDecimal novaTaxaManutencao) {
        System.out.println("--- EXECUTANDO atualizarDadosContaCorrente no ContaDAO para idConta: " + idConta + " ---");
        String sql = "UPDATE conta_corrente SET limite = ?, taxa_manutencao = ? WHERE id_conta = ?";
        int affectedRows = 0;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, novoLimite);
            stmt.setBigDecimal(2, novaTaxaManutencao);
            stmt.setInt(3, idConta);

            affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println(">>> Dados da conta corrente ID " + idConta + " atualizados com sucesso no banco.");
            } else {
                System.err.println("!!! Nenhuma linha afetada ao atualizar dados da conta corrente ID " + idConta + ". A conta corrente existe?");
            }
        } catch (SQLException e) {
            System.err.println("!!! ERRO DE SQL ao atualizar dados da conta corrente: " + e.getMessage());
            e.printStackTrace();
            return false; // Indica falha
        } finally {
            System.out.println("--- FINALIZANDO atualizarDadosContaCorrente no ContaDAO ---");
        }
        return affectedRows > 0; // Retorna true se alguma linha foi afetada
    }
}