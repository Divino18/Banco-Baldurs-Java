package model;
import java.math.BigDecimal;
public class ContaInvestimento {
    private int idContaInvestimento;
    private int idConta;
    private String perfilRisco;
    private BigDecimal valorMinimo;
    private BigDecimal taxaRendimentoBase;
    public ContaInvestimento() {} // Construtor vazio
    // Getters e Setters...
    public int getIdContaInvestimento() { return idContaInvestimento; }
    public void setIdContaInvestimento(int idContaInvestimento) { this.idContaInvestimento = idContaInvestimento; }
    public int getIdConta() { return idConta; }
    public void setIdConta(int idConta) { this.idConta = idConta; }
    public String getPerfilRisco() { return perfilRisco; }
    public void setPerfilRisco(String perfilRisco) { this.perfilRisco = perfilRisco; }
    public BigDecimal getValorMinimo() { return valorMinimo; }
    public void setValorMinimo(BigDecimal valorMinimo) { this.valorMinimo = valorMinimo; }
    public BigDecimal getTaxaRendimentoBase() { return taxaRendimentoBase; }
    public void setTaxaRendimentoBase(BigDecimal taxaRendimentoBase) { this.taxaRendimentoBase = taxaRendimentoBase; }
}