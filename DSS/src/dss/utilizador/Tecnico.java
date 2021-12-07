package dss.utilizador;

import dss.fichas.FichaReparacao;
import dss.fichas.FichaReparacaoProgramada;

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

    public boolean estaOcupado(){
        return ocupado;
    }

    public void ocupaTecnico() {
        ocupado = true;
    }

    public  void libertaTecnico() {
        ocupado = false;
    }
}
