package model;

import java.util.ArrayList;
import java.util.List;

public class GerarRelatorio {

    private String cpfUsuario;
    private final List<UsuarioConta.Conta> contas;
    private final List<Transacao> transacoes;

    public GerarRelatorio(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
        this.contas = new ArrayList<>();
        this.transacoes = new ArrayList<>();
    }

    public String getCpfUsuario() { return cpfUsuario; }
    public void setCpfUsuario(String cpfUsuario) { this.cpfUsuario = cpfUsuario; }

    public void adicionarConta(UsuarioConta.Conta conta) {
        contas.add(conta);
    }

    public void adicionarTransacao(Transacao transacao) {
        transacoes.add(transacao);
    }

    public List<UsuarioConta.Conta> getContas() {
        return contas;
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }
}