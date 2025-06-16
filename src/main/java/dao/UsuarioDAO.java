package dao;

import model.Usuario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class UsuarioDAO {

    public Usuario findByCpf(String cpf) {
        System.out.println("=========================================");
        System.out.println("--- EXECUTANDO findByCpf no UsuarioDAO ---");
        System.out.println("CPF recebido para busca: [" + cpf + "]");
        String sql = "SELECT id_usuario, nome, cpf, data_nascimento, telefone, tipo_usuario, senha_hash, otp_ativo, otp_expiracao FROM usuario WHERE cpf = ?";
        Usuario usuario = null;
        Connection conn = null;

        try {
            System.out.println("Tentando obter conexão com o banco...");
            conn = ConnectionFactory.getConnection();

            if (conn != null) {
                System.out.println("Conexão com o banco obtida com sucesso. Validade da conexão: " + conn.isValid(2));
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    System.out.println("Preparando a consulta SQL: " + sql);
                    stmt.setString(1, cpf);
                    System.out.println("Executando a consulta no banco para o CPF: " + cpf);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        System.out.println(">>> SUCESSO! Usuário foi encontrado no banco de dados pelo ResultSet.");
                        usuario = new Usuario();
                        usuario.setIdUsuario(rs.getInt("id_usuario"));
                        usuario.setNome(rs.getString("nome"));
                        usuario.setCpf(rs.getString("cpf"));
                        if (rs.getDate("data_nascimento") != null) {
                            usuario.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                        }
                        usuario.setTelefone(rs.getString("telefone"));
                        usuario.setTipoUsuario(rs.getString("tipo_usuario"));
                        usuario.setSenhaHash(rs.getString("senha_hash"));

                        usuario.setOtpAtivo(rs.getString("otp_ativo"));
                        Timestamp otpExpiracaoTimestamp = rs.getTimestamp("otp_expiracao");
                        if (otpExpiracaoTimestamp != null) {
                            usuario.setOtpExpiracao(otpExpiracaoTimestamp.toLocalDateTime());
                        }
                        System.out.println("Usuário mapeado: " + usuario.getNome());
                    } else {
                        System.out.println(">>> FALHA! Nenhum resultado foi retornado do banco para o CPF: " + cpf);
                    }
                }
            } else {
                System.err.println("!!! FALHA GRAVE: A conexão retornada pelo ConnectionFactory é nula!");
            }
        } catch (SQLException e) {
            System.err.println("!!! ERRO DE SQL ACONTECEU AO BUSCAR USUÁRIO no findByCpf !!!");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    System.out.println("Fechando a conexão com o banco...");
                    conn.close();
                    System.out.println("Conexão fechada.");
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar a conexão: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        System.out.println("--- FINALIZANDO findByCpf ---");
        if (usuario == null) {
            System.out.println("Retornando usuário: null");
        } else {
            System.out.println("Retornando usuário: " + usuario.getNome());
        }
        System.out.println("=========================================");
        return usuario;
    }

    public boolean atualizarTelefone(int idUsuario, String novoTelefone) {
        System.out.println("--- EXECUTANDO atualizarTelefone no UsuarioDAO para idUsuario: " + idUsuario + " ---");
        String sql = "UPDATE usuario SET telefone = ? WHERE id_usuario = ?";
        int affectedRows = 0;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoTelefone);
            stmt.setInt(2, idUsuario);

            affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Telefone do usuário ID " + idUsuario + " atualizado para: " + novoTelefone);
            } else {
                System.err.println("!!! Nenhuma linha afetada ao ATUALIZAR telefone para usuário ID: " + idUsuario + ". O usuário existe?");
            }
        } catch (SQLException e) {
            System.err.println("!!! ERRO DE SQL ao atualizar telefone: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            System.out.println("--- FINALIZANDO atualizarTelefone no UsuarioDAO ---");
        }
        return affectedRows > 0;
    }

    public String solicitarNovoOtp(int idUsuario) {
        System.out.println("--- EXECUTANDO solicitarNovoOtp no UsuarioDAO para idUsuario: " + idUsuario + " ---");
        String sqlProcedure = "{CALL gerar_otp(?)}";
        String otpGerado = null;

        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cstmt = conn.prepareCall(sqlProcedure)) {

            cstmt.setInt(1, idUsuario);
            System.out.println("Executando a procedure gerar_otp para o usuário ID: " + idUsuario);

            boolean hasResultSet = cstmt.execute();
            if (hasResultSet) {
                try (ResultSet rs = cstmt.getResultSet()) {
                    if (rs.next()) {
                        otpGerado = rs.getString(1);
                        System.out.println(">>> OTP gerado pelo banco: " + otpGerado);
                    } else {
                        System.err.println("!!! Procedure gerar_otp executada, mas não retornou um OTP.");
                    }
                }
            } else {
                System.err.println("!!! Procedure gerar_otp executada, mas não produziu um ResultSet.");
            }
        } catch (SQLException e) {
            System.err.println("!!! ERRO DE SQL ao solicitar novo OTP: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("--- FINALIZANDO solicitarNovoOtp no UsuarioDAO ---");
        return otpGerado; // Retorno do OTP gerado ou null se erro
    }

    public Usuario getOtpData(int idUsuario) {
        System.out.println("--- EXECUTANDO getOtpData no UsuarioDAO para idUsuario: " + idUsuario + " ---");
        String sql = "SELECT id_usuario, otp_ativo, otp_expiracao FROM usuario WHERE id_usuario = ?";
        Usuario otpData = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                otpData = new Usuario();
                otpData.setIdUsuario(rs.getInt("id_usuario"));
                otpData.setOtpAtivo(rs.getString("otp_ativo"));
                Timestamp otpExpiracaoTimestamp = rs.getTimestamp("otp_expiracao");
                if (otpExpiracaoTimestamp != null) {
                    otpData.setOtpExpiracao(otpExpiracaoTimestamp.toLocalDateTime());
                }
                System.out.println("Dados OTP encontrados para usuário ID " + idUsuario + ": OTP Ativo=" + otpData.getOtpAtivo() + ", Expira=" + otpData.getOtpExpiracao());
            } else {
                System.out.println("Nenhum dado OTP encontrado para usuário ID " + idUsuario);
            }
        } catch (SQLException e) {
            System.err.println("!!! ERRO DE SQL ao buscar dados OTP: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("--- FINALIZANDO getOtpData no UsuarioDAO ---");
        return otpData; // Retorno do objeto com dados do OTP ou null
    }

    public boolean atualizarSenha(int idUsuario, String novaSenhaHash) {
        System.out.println("--- EXECUTANDO atualizarSenha no UsuarioDAO para idUsuario: " + idUsuario + " ---");
        String sql = "UPDATE usuario SET senha_hash = ? WHERE id_usuario = ?";
        int affectedRows = 0;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novaSenhaHash);
            stmt.setInt(2, idUsuario);
            affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println(">>> Senha do usuário ID " + idUsuario + " atualizada com sucesso no banco.");
            } else {
                System.err.println("!!! Nenhuma linha afetada ao atualizar senha para usuário ID " + idUsuario + ". O usuário existe?");
            }
        } catch (SQLException e) {
            System.err.println("!!! ERRO DE SQL ao atualizar senha: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            System.out.println("--- FINALIZANDO atualizarSenha no UsuarioDAO ---");
        }
        return affectedRows > 0;
    }
}