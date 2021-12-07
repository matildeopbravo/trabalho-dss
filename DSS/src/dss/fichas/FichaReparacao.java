package dss.fichas;

import java.util.ArrayList;
import java.util.List;

public abstract class FichaReparacao {
    private static int lastId = -1;
    int id;
    String idCliente;
    String funcionarioCriador;
    List<String> tecnicosQueRepararam = new ArrayList<>();

    public FichaReparacao(String idCliente, String funcionarioCriador) {
        this.id = ++lastId;
        this.idCliente = idCliente;
        this.funcionarioCriador = funcionarioCriador;
    }

    public FichaReparacao(String idCliente, String funcionarioCriador, String idTecnico) {
        this(idCliente,funcionarioCriador);
        this.tecnicosQueRepararam.add(idTecnico);
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

    // marca como realizado um passo ou subpasso, indicando o custo e o tempo que gastou na realidade
    //public abstract boolean efetuaReparacao(String id, int custo, int tempo);

}
