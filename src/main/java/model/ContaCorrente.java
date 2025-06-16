package model;
import java.math.BigDecimal;
import java.time.LocalDate;
public class ContaCorrente {
    private int idContaCorrente;
    private int idConta;
    private BigDecimal limite;
    private LocalDate dataVencimento;
    private BigDecimal taxaManutencao;
    public ContaCorrente() {} // Construtor vazio
    // Getters e Setters...
    public int getIdContaCorrente() { return idContaCorrente; }
    public void setIdContaCorrente(int idContaCorrente) { this.idContaCorrente = idContaCorrente; }
    public int getIdConta() { return idConta; }
    public void setIdConta(int idConta) { this.idConta = idConta; }
    public BigDecimal getLimite() { return limite; }
    public void setLimite(BigDecimal limite) { this.limite = limite; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public BigDecimal getTaxaManutencao() { return taxaManutencao; }
    public void setTaxaManutencao(BigDecimal taxaManutencao) { this.taxaManutencao = taxaManutencao; }
}