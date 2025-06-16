package model;

public class ClienteConta {
    private String agencia;
    private String numeroConta;
    private String nomeCliente;
    private String cpf;
    private String dataNascimento; // String para facilitar a entrada da UI
    private String telefone;
    private String endereco; // String para facilitar a entrada da UI (será o logradouro)
    private String senha;
    private String tipoConta;
    private double limite;
    private String dataVencimento; // String para facilitar a entrada da UI

    // Novos campos para endereço detalhado no cadastro
    private String cep;
    private int numeroCasa;
    private String bairro;
    private String cidade;
    private String estado;
    private String complemento; // Pode ser nulo

    // Construtor para Conta Poupança e Investimento (simplificado)
    public ClienteConta(String agencia, String numeroConta, String nomeCliente, String cpf,
                        String dataNascimento, String telefone, String senha, String tipoConta,
                        String cep, String logradouro, int numeroCasa, String bairro, String cidade, String estado, String complemento) {
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.nomeCliente = nomeCliente;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.senha = senha;
        this.tipoConta = tipoConta;
        this.cep = cep;
        this.endereco = logradouro; // O campo 'endereco' da ClienteConta será o logradouro
        this.numeroCasa = numeroCasa;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.complemento = complemento;
    }


    // Construtor para Conta Corrente (com limite e data de vencimento)
    public ClienteConta(String agencia, String numeroConta, String nomeCliente, String cpf,
                        String dataNascimento, String telefone, String senha,
                        String tipoConta, double limite, String dataVencimento,
                        String cep, String logradouro, int numeroCasa, String bairro, String cidade, String estado, String complemento) {
        this(agencia, numeroConta, nomeCliente, cpf, dataNascimento, telefone, senha, tipoConta,
                cep, logradouro, numeroCasa, bairro, cidade, estado, complemento); // Chama o construtor acima
        this.limite = limite;
        this.dataVencimento = dataVencimento;
    }

    // Getters e Setters
    public String getAgencia() { return agencia; }
    public void setAgencia(String agencia) { this.agencia = agencia; }
    public String getNumeroConta() { return numeroConta; }
    public void setNumeroConta(String numeroConta) { this.numeroConta = numeroConta; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEndereco() { return endereco; } // Retorna o logradouro
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getTipoConta() { return tipoConta; }
    public void setTipoConta(String tipoConta) { this.tipoConta = tipoConta; }
    public double getLimite() { return limite; }
    public void setLimite(double limite) { this.limite = limite; }
    public String getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(String dataVencimento) { this.dataVencimento = dataVencimento; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public int getNumeroCasa() { return numeroCasa; }
    public void setNumeroCasa(int numeroCasa) { this.numeroCasa = numeroCasa; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
}