package model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Conta {
    private int idConta;
    private String numeroConta;
    private int idAgencia;
    private BigDecimal saldo;
    private String tipoConta;
    private int idCliente;
    private LocalDateTime dataAbertura;
    private String status;
    public Conta() {} // Construtor vazio
    // Getters e Setters...
    public int getIdConta() { return idConta; }
    public void setIdConta(int idConta) { this.idConta = idConta; }
    public String getNumeroConta() { return numeroConta; }
    public void setNumeroConta(String numeroConta) { this.numeroConta = numeroConta; }
    public int getIdAgencia() { return idAgencia; }
    public void setIdAgencia(int idAgencia) { this.idAgencia = idAgencia; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    public String getTipoConta() { return tipoConta; }
    public void setTipoConta(String tipoConta) { this.tipoConta = tipoConta; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}