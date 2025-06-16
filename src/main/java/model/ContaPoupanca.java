package model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class ContaPoupanca {
    private int idContaPoupanca;
    private int idConta;
    private BigDecimal taxaRendimento;
    private LocalDateTime ultimoRendimento;
    public ContaPoupanca() {} // Construtor vazio
    // Getters e Setters...
    public int getIdContaPoupanca() { return idContaPoupanca; }
    public void setIdContaPoupanca(int idContaPoupanca) { this.idContaPoupanca = idContaPoupanca; }
    public int getIdConta() { return idConta; }
    public void setIdConta(int idConta) { this.idConta = idConta; }
    public BigDecimal getTaxaRendimento() { return taxaRendimento; }
    public void setTaxaRendimento(BigDecimal taxaRendimento) { this.taxaRendimento = taxaRendimento; }
    public LocalDateTime getUltimoRendimento() { return ultimoRendimento; }
    public void setUltimoRendimento(LocalDateTime ultimoRendimento) { this.ultimoRendimento = ultimoRendimento; }
}