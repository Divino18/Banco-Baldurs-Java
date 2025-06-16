package model;

import java.time.LocalDateTime;

public class Relatorio {
    private int idRelatorio;
    private int idFuncionario;
    private String tipoRelatorio;
    private LocalDateTime dataGeracao;
    private String conteudo;

    public Relatorio(int idRelatorio, int idFuncionario, String tipoRelatorio, LocalDateTime dataGeracao, String conteudo) {
        this.idRelatorio = idRelatorio;
        this.idFuncionario = idFuncionario;
        this.tipoRelatorio = tipoRelatorio;
        this.dataGeracao = dataGeracao;
        this.conteudo = conteudo;
    }

    public Relatorio(int idFuncionario, String tipoRelatorio, String conteudo) {
        this.idFuncionario = idFuncionario;
        this.tipoRelatorio = tipoRelatorio;
        this.dataGeracao = LocalDateTime.now();
        this.conteudo = conteudo;
    }

    public int getIdRelatorio() { return idRelatorio; }
    public void setIdRelatorio(int idRelatorio) { this.idRelatorio = idRelatorio; }
    public int getIdFuncionario() { return idFuncionario; }
    public void setIdFuncionario(int idFuncionario) { this.idFuncionario = idFuncionario; }
    public String getTipoRelatorio() { return tipoRelatorio; }
    public void setTipoRelatorio(String tipoRelatorio) { this.tipoRelatorio = tipoRelatorio; }
    public LocalDateTime getDataGeracao() { return dataGeracao; }
    public void setDataGeracao(LocalDateTime dataGeracao) { this.dataGeracao = dataGeracao; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    @Override
    public String toString() {
        return "Relatorio{" +
                "idRelatorio=" + idRelatorio +
                ", idFuncionario=" + idFuncionario +
                ", tipoRelatorio='" + tipoRelatorio + '\'' +
                ", dataGeracao=" + dataGeracao +
                ", conteudo='" + conteudo + '\'' +
                '}';
    }
}