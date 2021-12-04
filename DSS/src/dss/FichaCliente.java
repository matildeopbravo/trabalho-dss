package dss;

import java.util.ArrayList;
import java.util.List;

public class FichaCliente {
    private String NIF;
    private String nome;
    private String email;
    private String numeroTelemovel;
    private String funcionarioCriador;
    private final List<String> idReparacoesAtuais = new ArrayList<>();
    private final List<String> idReparacoesConcluidas = new ArrayList<>();

    public FichaCliente(String NIF, String nome, String email, String numeroTelemovel, String funcionarioCriador) {
        this.NIF = NIF;
        this.nome = nome;
        this.email = email;
        this.numeroTelemovel = numeroTelemovel;
        this.funcionarioCriador = funcionarioCriador;
    }

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroTelemovel() {
        return numeroTelemovel;
    }

    public void setNumeroTelemovel(String numeroTelemovel) {
        this.numeroTelemovel = numeroTelemovel;
    }

    public String getFuncionarioCriador() {
        return funcionarioCriador;
    }

    public void setFuncionarioCriador(String funcionarioCriador) {
        this.funcionarioCriador = funcionarioCriador;
    }

    public List<String> getIdReparacoesAtuais() {
        return new ArrayList<>(idReparacoesAtuais);
    }

    public List<String> getIdReparacoesConcluidas() {
        return new ArrayList<>(idReparacoesConcluidas);
    }
}
