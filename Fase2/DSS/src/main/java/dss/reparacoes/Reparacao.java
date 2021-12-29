
package dss.reparacoes;

import dss.DuracaoCusto;
import dss.Intervencao;
import dss.equipamentos.Fase;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public abstract class Reparacao implements Serializable {
    protected static int lastId = -1;
    protected int id;
    protected String idCliente;
    protected String funcionarioCriador;
    protected String funcionarioEntregou;
    protected List<String> tecnicosQueRepararam = new ArrayList<>();
    protected Fase fase;
    protected DuracaoCusto duracaoCusto;
    protected String descricao;
    protected boolean notificado;


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

    public Duration getDuracaoReal() {
        return duracaoCusto.getDuracaoReal();
    }

    public Duration getDuracaoPrevista() {
        return duracaoCusto.getDuracaoPrevista();
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


    public void marcaComoEntregueConcluida(String idFuncionario) {
        fase = Fase.EntregueConcluida;
        funcionarioEntregou = idFuncionario;
    }

    public void marcaComoEntregueRecusada(String idFuncionario) {
        fase = Fase.EntregueRecusada;
        funcionarioEntregou = idFuncionario;
    }

    public void marcaComoNotificado() {
        this.notificado = true;
    }
}