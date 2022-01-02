
package dss.business.reparacao;

import dss.business.equipamento.Fase;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Reparacao implements Serializable {
    protected static int lastId = -1;
    protected int id;
    protected String idCliente;
    protected String funcionarioCriador;
    protected String funcionarioEntregou = null;
    protected List<String> tecnicosQueRepararam = new ArrayList<>();
    protected Fase fase = Fase.NaoIniciada;
    protected String descricao;
    protected boolean notificado = false;
    protected LocalDateTime dataCriacao = LocalDateTime.now();

    public Reparacao(String idCliente, String funcionarioCriador, String descricao) {
        this.id = ++lastId;
        this.idCliente = idCliente;
        this.funcionarioCriador = funcionarioCriador;
        this.descricao = descricao;
        this.fase = Fase.AEsperaOrcamento;
    }

    public Reparacao(String idCliente, String funcionarioCriador, String idTecnico, String descricao) {
        this(idCliente,funcionarioCriador, descricao);
        this.tecnicosQueRepararam.add(idTecnico);
    }

    public static void updateLastID(int max) {
        lastId = max;
    }


    public abstract Duration getDuracaoReal();

    public abstract Duration getDuracaoPrevista() ;

    public abstract float getCustoTotalPrevisto() ;

    public abstract float getCustoTotalReal() ;

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

    public Fase getFase() {
        return this.fase;
    }

    public void setFuncionarioEntregou(String id) {
        this.funcionarioEntregou = id;
    }

    public String getFuncionarioEntregou() {
        return  funcionarioEntregou;
    }

    public abstract  List<Intervencao>  getIntervencoesRealizadas();

    public String getDescricao() {
        return this.descricao;
    }

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
        this.fase = Fase.AEsperaResposta;
    }
}