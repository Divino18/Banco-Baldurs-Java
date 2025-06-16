package model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class Transacao {
    private int idTransacao;
    private Integer idContaOrigem;
    private Integer idContaDestino;
    private String tipoTransacao;
    private BigDecimal valor;
    private LocalDateTime dataHora;
    private String descricao;
    public Transacao() {} // Construtor vazio
    // Getters e Setters...
    public int getIdTransacao() { return idTransacao; }
    public void setIdTransacao(int idTransacao) { this.idTransacao = idTransacao; }
    public Integer getIdContaOrigem() { return idContaOrigem; }
    public void setIdContaOrigem(Integer idContaOrigem) { this.idContaOrigem = idContaOrigem; }
    public Integer getIdContaDestino() { return idContaDestino; }
    public void setIdContaDestino(Integer idContaDestino) { this.idContaDestino = idContaDestino; }
    public String getTipoTransacao() { return tipoTransacao; }
    public void setTipoTransacao(String tipoTransacao) { this.tipoTransacao = tipoTransacao; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}