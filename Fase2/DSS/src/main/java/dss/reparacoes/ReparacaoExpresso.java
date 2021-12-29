package dss.reparacoes;

import dss.Intervencao;
import dss.ServicoExpressoTabelado;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ReparacaoExpresso extends Reparacao implements Intervencao, Serializable {
    // TODO CLONES
    private final ServicoExpressoTabelado servicoTabelado;
    public ReparacaoExpresso(ServicoExpressoTabelado servicoTabelado, String idCliente, String utilizadorCriador, String idTecnico, String descricao ) {
        super(idCliente, utilizadorCriador,idTecnico, servicoTabelado.getCustoPrevisto(), servicoTabelado.getDuracaoPrevista(), descricao);
        this.servicoTabelado = servicoTabelado.clone();
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

    @Override
    public Duration getDuracaoReal() {
        return duracaoCusto.getDuracaoReal();
    }

    @Override
    public Duration getDuracaoPrevista() {
        return duracaoCusto.getDuracaoPrevista();
    }

    @Override
    public float getCustoMaoDeObraReal() {
        return duracaoCusto.getCustoMaoDeObraReal();
    }

    @Override
    public float getCustoMaoDeObraPrevisto() {
        return duracaoCusto.getCustoMaoDeObraPrevisto();
    }
}