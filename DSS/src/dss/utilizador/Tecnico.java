package dss.utilizador;

import dss.Equipamento;
import dss.Orcamento;
import dss.PlanoReparacao;

import java.time.LocalDate;

public class Tecnico extends Utilizador {
    private boolean ocupado;

    public Orcamento realizarOrcamento(PlanoReparacao p, LocalDate prazoReparacao) {
        return new Orcamento(p,prazoReparacao);
        
    }

}
