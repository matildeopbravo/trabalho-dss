package dss.reparacoes;

import dss.DuracaoCusto;
import dss.Intervencao;
import dss.equipamentos.Fase;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public abstract class Reparacao {
    private static int lastId = -1;
    int id;
    String idCliente;
    String funcionarioCriador;
    String funcionarioEntregou;
    List<String> tecnicosQueRepararam = new ArrayList<>();
    Fase fase;
    DuracaoCusto duracaoCusto;
    String descricao;


    public Reparacao(String idCliente, String funcionarioCriador) {
        this.id = ++lastId;
        this.idCliente = idCliente;
        this.funcionarioCriador = funcionarioCriador;
    }

    public Reparacao(String idCliente, String funcionarioCriador, String idTecnico) {
        this(idCliente,funcionarioCriador);
        this.tecnicosQueRepararam.add(idTecnico);
    }

    public Reparacao(String idCliente, String utilizadorCriador, String idTecnico, float custoPrevisto, Duration duracaoPrevista) {
        this(idCliente,utilizadorCriador,idTecnico);
        this.duracaoCusto = new DuracaoCusto(duracaoPrevista, custoPrevisto);
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

    public void setFase(Fase fase) {
        this.fase = fase;
    }

    public void setFuncionarioEntregou(String id) {
        this.funcionarioEntregou = id;
    }

    public String getFuncionarioEntregou() {
        return  funcionarioEntregou;
    }

    public abstract  List<Intervencao>  getIntervencoesRealizadas();


    // marca como realizado um passo ou subpasso, indicando o custo e o tempo que gastou na realidade
    //public abstract boolean efetuaReparacao(String id, int custo, int tempo);

}
