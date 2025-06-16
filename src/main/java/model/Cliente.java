package model;
public class Cliente {
    private int idCliente;
    private int idUsuario;
    private double scoreCredito;
    public Cliente() {} // Construtor vazio
    // Getters e Setters...
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public double getScoreCredito() { return scoreCredito; }
    public void setScoreCredito(double scoreCredito) { this.scoreCredito = scoreCredito; }
}