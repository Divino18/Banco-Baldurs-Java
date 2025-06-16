package model;

import java.time.LocalDate; // Importa LocalDate
import java.time.format.DateTimeFormatter; // Importa para parsear a data
import java.util.ArrayList;
import java.util.List;

public class UsuarioConta {
    private String nome;
    private String email;
    private String cpf;
    private LocalDate dataNascimento; // Tipo de dado atualizado para LocalDate
    private String telefone;
    private String tipoUsuario;
    private List<Conta> contas;

    // Construtor principal
    public UsuarioConta(String nome, String email, String cpf, String dataNascimentoStr, String telefone, String tipoUsuario) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        // Converter String para LocalDate, assumindo o formato do banco
        this.dataNascimento = LocalDate.parse(dataNascimentoStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Ajustado para formato DB
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
        this.contas = new ArrayList<>();
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public LocalDate getDataNascimento() { return dataNascimento; } // Retorna LocalDate
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public List<Conta> getContas() { return contas; }
    public void setContas(List<Conta> contas) { this.contas = contas; }

    public void adicionarConta(int idConta, String numeroConta, String agencia, double saldo, String tipoConta) {
        Conta conta = new Conta(idConta, numeroConta, agencia, saldo, tipoConta);
        this.contas.add(conta);
    }

    public static class Conta {
        private int idConta; // Adicionado para uso em relatórios/DAOs
        private final String numeroConta;
        private final String agencia;
        private final double saldo;
        private final String tipoConta;

        public Conta(int idConta, String numeroConta, String agencia, double saldo, String tipoConta) {
            this.idConta = idConta;
            this.numeroConta = numeroConta;
            this.agencia = agencia;
            this.saldo = saldo;
            this.tipoConta = tipoConta;
        }

        public int getIdConta() { return idConta; }
        public void setIdConta(int idConta) { this.idConta = idConta; }
        public String getNumeroConta() { return numeroConta; }
        public String getAgencia() { return agencia; }
        public double getSaldo() { return saldo; }
        public String getTipoConta() { return tipoConta; }
    }
}