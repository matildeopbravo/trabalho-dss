package dss;

import java.time.LocalDate;
import java.util.List;

public class Orcamento {
    // vai ser dado pela soma dos custos
    private float total;
    private List<Componente> componentesDisponiveis;
    // lista de descricoes porque o Componente nao existe e nao tem id
    private List<String> componentesAEncomendar;
    LocalDate prazoMaximo;

    public Orcamento(PlanoReparacao p, LocalDate prazoMaximo) {
        // TODO
        this.prazoMaximo = prazoMaximo;
    }
}
