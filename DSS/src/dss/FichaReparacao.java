package dss;

import java.util.ArrayList;
import java.util.List;

public abstract class FichaReparacao {
    private static int lastId = -1;
    int id;
    String idCliente;
    String funcionarioCriador;
    List<String> tecnicosQueRepararam = new ArrayList<>();
    // descricao deles
    List<String> componentesAEncomendar;

    public FichaReparacao(String idCliente, String funcionarioCriador) {
        this.id = ++lastId;
        this.idCliente = idCliente;
        this.funcionarioCriador = funcionarioCriador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getFuncionarioCriador() {
        return funcionarioCriador;
    }

    public void setFuncionarioCriador(String funcionarioCriador) {
        this.funcionarioCriador = funcionarioCriador;
    }

    public List<String> getTecnicosQueRepararam() {
        return new ArrayList<>(tecnicosQueRepararam);
    }

    public abstract void efetuaReparacao(String id);

}
