package model;
public class Funcionario {
    private int idFuncionario;
    private int idUsuario;
    private String codigoFuncionario;
    private String cargo;
    private Integer idSupervisor;
    public Funcionario() {} // Construtor vazio
    // Getters e Setters...
    public int getIdFuncionario() { return idFuncionario; }
    public void setIdFuncionario(int idFuncionario) { this.idFuncionario = idFuncionario; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getCodigoFuncionario() { return codigoFuncionario; }
    public void setCodigoFuncionario(String codigoFuncionario) { this.codigoFuncionario = codigoFuncionario; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public Integer getIdSupervisor() { return idSupervisor; }
    public void setIdSupervisor(Integer idSupervisor) { this.idSupervisor = idSupervisor; }
}