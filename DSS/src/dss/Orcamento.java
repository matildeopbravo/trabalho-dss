package dss;

import java.time.LocalDate;
import java.util.List;

public class Orcamento {
    // vai ser dado pela soma dos custos
    private final float total;
    private final List<Componente> componentesNecessarios;
    private final LocalDate prazoMaximo;

    public Orcamento(PlanoReparacao p) {
        this.componentesNecessarios = p.getComponentes().values().stream().map(Pair::getFirst).toList();

        Pair<Float,Float> custoEDuracao = p.getCustoEDuracaoPrevista();
        this.total = custoEDuracao.getFirst();
        float duracao = custoEDuracao.getSecond();
        float days = duracao / 60;
        this.prazoMaximo = LocalDate.now();
        prazoMaximo.plusDays( (long) days);
    }

    public float getPreco() {
        return total;
    }
}
