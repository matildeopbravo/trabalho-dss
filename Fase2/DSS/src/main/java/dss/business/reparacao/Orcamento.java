package dss.business.reparacao;

import dss.business.auxiliar.Pair;
import dss.business.equipamento.Componente;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Orcamento implements Serializable {
    // vai ser dado pela soma dos custos
    private float custoTotal;
    private final List<Componente> componentesNecessarios;
    private final LocalDateTime prazoMaximo;
    private PlanoReparacao planoReparacao;

    public Orcamento(PlanoReparacao p) {
        this.planoReparacao = p;
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
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Custo total: %.2f€\n", custoTotal));
        if (this.componentesNecessarios.size() > 0) {
            sb.append(String.format("Serão necessários os seguintes componentes:\n"));
            for (Componente componente : this.componentesNecessarios) {
                sb.append(componente.toString()).append("\n");
            }
        }

        return sb.toString();
    }
}
