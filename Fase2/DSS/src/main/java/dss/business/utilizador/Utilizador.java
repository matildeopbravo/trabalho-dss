package dss.business.utilizador;

import dss.business.BCrypt.BCrypt;
import dss.data.UtilizadoresDAO;
import jdk.jshell.execution.Util;

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

    public void changePassword(String password) {
        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
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
