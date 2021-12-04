package dss;

import java.util.List;

public class FichaReparacaoProgramada extends FichaReparacao {
    Equipamento equipamentoAReparar;
    PlanoReparacao planoReparacao;
    Orcamento orcamento;
    boolean pausado ;
    Fase fase;

    public FichaReparacaoProgramada(String idCliente, String funcionarioCriador) {
        super(idCliente, funcionarioCriador);
        this.equipamentoAReparar = new Equipamento(idCliente);
        pausado = true;
        this.orcamento = null;
        fase = Fase.AEsperaOrcamento;
    }

    @Override
    public void efetuaReparacao(String id) {
        if(!tecnicosQueRepararam.contains(id)) {
            tecnicosQueRepararam.add(id);
        }
        // TODO
    }

    public void realizaOrcamento(String id) {
        this.tecnicosQueRepararam.add(id);
        this.orcamento = new Orcamento(planoReparacao);
    }
}
