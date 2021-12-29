package dss;

import dss.auxiliar.Pair;
import dss.equipamentos.Componente;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Orcamento {
    // vai ser dado pela soma dos custos
    private final float custoTotal;
    private final List<Componente> componentesNecessarios;
    private final LocalDateTime prazoMaximo;

    public Orcamento(PlanoReparacao p) {
        this.componentesNecessarios = p.getComponentesPrevistos().values().stream().map(Pair::getFirst).toList();

        Pair<Float, Duration> custoEDuracao = p.getCustoTotalEDuracaoPrevista();
        this.custoTotal = custoEDuracao.getFirst();
        Duration duracao = custoEDuracao.getSecond();
        this.prazoMaximo = LocalDateTime.now().plusSeconds(duracao.getSeconds());
    }

    public float getPreco() {
        return custoTotal;
    }

    @Override
    public String toString() {
        return "Orcamento{" +
                "custoTotal=" + custoTotal +
                ", componentesNecessarios=" + componentesNecessarios +
                ", prazoMaximo=" + prazoMaximo +
                '}';
    }
}
