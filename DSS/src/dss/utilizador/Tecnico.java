package dss.utilizador;

import dss.*;

public class Tecnico extends Utilizador {
    private boolean ocupado;

    public void realizaOrcamento(FichaReparacaoProgramada ficha) {
        ficha.realizaOrcamento(this.getId());
    }

    // executa Passo ou subpasso se programada ; executa a reparacao toda se for expresso
    public boolean efetuaReparacao(FichaReparacao ficha, int custo, int tempo) {
        return ficha.efetuaReparacao(this.getId(), custo, tempo);
    }
    public void pausaReparacao(FichaReparacaoProgramada ficha) {
        //ficha.pausarReparacao();
        ficha.togglePausarReparacao();
    }
}
