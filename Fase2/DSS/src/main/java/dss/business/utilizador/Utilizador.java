package dss.business.utilizador;

import dss.data.UtilizadoresDAO;

import java.io.Serializable;

public abstract class Utilizador implements Serializable  {
    private String nome;
    private String id;
    private String passwordHash;

    public Utilizador(String nome, String id, String password) {
        this.nome = nome;
        this.id = id;
        this.passwordHash = UtilizadoresDAO.encriptaPassword(password);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "Utilizador{" +
                "nome='" + nome + '\'' +
                ", id='" + id + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}
