package model;

public class Agencia {
    private int idAgencia;
    private String nome;
    private String codigoAgencia;
    private Integer enderecoId;

    public Agencia() {}

    public Agencia(int idAgencia, String nome, String codigoAgencia, Integer enderecoId) {
        this.idAgencia = idAgencia;
        this.nome = nome;
        this.codigoAgencia = codigoAgencia;
        this.enderecoId = enderecoId;
    }

    public Agencia(String nome, String codigoAgencia, Integer enderecoId) {
        this.nome = nome;
        this.codigoAgencia = codigoAgencia;
        this.enderecoId = enderecoId;
    }

    public int getIdAgencia() { return idAgencia; }
    public void setIdAgencia(int idAgencia) { this.idAgencia = idAgencia; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCodigoAgencia() { return codigoAgencia; }
    public void setCodigoAgencia(String codigoAgencia) { this.codigoAgencia = codigoAgencia; }
    public Integer getEnderecoId() { return enderecoId; }
    public void setEnderecoId(Integer enderecoId) { this.enderecoId = enderecoId; }
}