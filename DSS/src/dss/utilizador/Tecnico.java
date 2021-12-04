package dss.utilizador;

import dss.*;

public class Tecnico extends Utilizador {
    private boolean ocupado;

    public void realizaOrcamento(FichaReparacaoProgramada ficha) {
        ficha.realizaOrcamento(this.getId());
    }

    public void executaReparacao(FichaReparacao ficha) {
        // TODO
        // EXpresso ou programada
    }
}
