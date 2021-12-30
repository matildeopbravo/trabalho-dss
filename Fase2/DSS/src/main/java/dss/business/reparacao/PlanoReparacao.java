package dss.business.reparacao;

import dss.business.auxiliar.Pair;
import dss.business.equipamento.Componente;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PlanoReparacao implements Serializable {
    // se for demasiado lente calcular o custo on demand, volta-se a meter isto
    //private float custoAteAgora = 0;
    private List<PassoReparacao> subpassos = new ArrayList<>();

    public boolean reparado() {
        return subpassos.stream().allMatch(PassoReparacao::getExecutado);
    }

    public List<PassoReparacao> getPassosReparacaoConcluidos() {
        return subpassos.stream().filter(PassoReparacao::getExecutado).toList();
    }

    public List<PassoReparacao> getPassosReparacaoPorRealizar() {
        return subpassos.stream().filter(p -> !p.getExecutado()).toList();
    }

    public List<PassoReparacao> getPassosReparacao() {
        return subpassos;
    }

    /**
     * Método que adiciona um passo ao plano
     *
     * @return O objeto de passo criado
     */
    public PassoReparacao addPasso(PassoReparacao p) {
        subpassos.add(p);
        return p;
    }

    public PassoReparacao addPasso(String descricao, Duration duracao, float custoMaoDeObra, List<Componente> componentesPrevistos) {
        PassoReparacao p = new PassoReparacao(descricao, duracao, custoMaoDeObra, componentesPrevistos);
        subpassos.add(p);
        return p;
    }

    /**
     * Método que calcula o somatório do custo e tempo previsto de todos os
     * passos, tanto os concluídos como os por realizar
     *
     * @return Par (custo, tempo).
     */
    public Pair<Float, Duration> getCustoTotalEDuracaoPrevista() {
        // se for pedida uma previsao durante o periodo de reparacao, vai ser dado quanto falta e nao ter
        // em conta o que ja passou

        float custo = 0;
        Duration duracao = Duration.ZERO;

        for (PassoReparacao elem : subpassos) {
            custo += elem.getCustoTotalPrevisto();
            duracao = duracao.plus(elem.getDuracaoPrevista());
        }
        return new Pair<>(custo, duracao);
    }

    public Pair<Float, Duration> getCustoTotalEDuracaoReal() {
        float custo = 0;
        Duration duracao = Duration.ZERO;

        for (PassoReparacao elem : subpassos) {
            custo += elem.getCustoTotalReal();
            if (elem.getDuracaoReal() != null)
                duracao = duracao.plus(elem.getDuracaoReal());
        }
        return new Pair<>(custo, duracao);
    }

    public HashMap<String, Pair<Componente, Integer>> getComponentesPrevistos() {
        HashMap<String, Pair<Componente, Integer>> map = new HashMap<>();

        for (PassoReparacao elem : subpassos) {
            for (Componente c : elem.getComponentesPrevistos()) {
                Pair<Componente, Integer> p = map.get(c.getDescricao());
                if (p == null) {
                    p = new Pair<>(c.clone(), 1);
                    map.put(c.getDescricao(), p);
                } else p.setY(p.getSecond() + 1);
            }
        }

        return map;
    }

    public HashMap<String, Pair<Componente, Integer>> getComponentesReais() {
        HashMap<String, Pair<Componente, Integer>> map = new HashMap<>();
        for (PassoReparacao elem : subpassos) {
            for (Componente c : elem.getComponentesReais()) {
                Pair<Componente, Integer> p = map.get(c.getDescricao());
                if (p == null) {
                    p = new Pair<>(c.clone(), 1);
                    map.put(c.getDescricao(), p);
                } else p.setY(p.getSecond() + 1);
            }
        }

        return map;
    }

    public PassoReparacao addSubPasso(PassoReparacao passo, String descricao, Duration duracao, float custo, List<Componente> componentes) {
        PassoReparacao p = new PassoReparacao(descricao, duracao, custo, componentes);
        passo.addSubpasso(p);
        return p;
    }

    public PassoReparacao getNextPasso() {
        for (PassoReparacao passo : subpassos) {
            if (!passo.getExecutado())
                return passo.getNextPasso();
        }
        return null;
    }
}



