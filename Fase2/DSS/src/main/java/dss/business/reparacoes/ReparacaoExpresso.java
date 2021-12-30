package dss.business.reparacoes;

import dss.business.Intervencao;
import dss.business.ServicoExpressoTabelado;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ReparacaoExpresso extends Reparacao implements Intervencao, Serializable {
    private final ServicoExpressoTabelado servicoTabelado;
    private Duration duracaoReal;
    // nao precisamos de guardar o custoReal porque vai ser sempre o tabelado

    public ReparacaoExpresso(ServicoExpressoTabelado servicoTabelado, String idCliente, String utilizadorCriador, String idTecnico, String descricao ) {
        super(idCliente, utilizadorCriador,idTecnico, descricao);
        this.servicoTabelado = servicoTabelado;
    }

    // no serviço expresso só sera um
    public String getIdTecnicoReparou() {
        return this.tecnicosQueRepararam.get(0);
    }

    @Override
    public List<Intervencao> getIntervencoesRealizadas() {
        return new ArrayList<>(List.of((Intervencao) this));
    }

    @Override
    public String getDescricao() {
        return this.descricao;
    }

    public void setDuracaoReal(Duration d) {
        duracaoReal = d;
    }

    @Override
    public Duration getDuracaoReal() {
        return duracaoReal;
    }

    @Override
    public Duration getDuracaoPrevista() {
        return servicoTabelado.getDuracaoPrevista();
    }

    @Override
    public float getCustoTotalPrevisto() {
        return servicoTabelado.getCusto();
    }

    @Override
    public float getCustoTotalReal() {
        return servicoTabelado.getCusto();
    }
}