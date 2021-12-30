package dss.business.reparacao;

import dss.business.equipamento.Componente;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PassoReparacao implements Intervencao {
    String descricao;
    private DuracaoCusto duracaoCusto;

    private boolean executado = false;

    private List<Componente> componentesPrevistos;
    private List<Componente> componentesReais;

    private List<PassoReparacao> subpassos;

    public PassoReparacao(String descricao, Duration duracao, float custo, List<Componente> componentesPrevistos) {
        this.descricao = descricao;
        this.duracaoCusto = new DuracaoCusto(duracao, custo);
        this.componentesPrevistos = componentesPrevistos;
        this.subpassos = new ArrayList<>();

        componentesReais = new ArrayList<>();
    }

    public List<Componente> getComponentesPrevistos() {
        List<Componente> previstos = new ArrayList<>(componentesPrevistos);
        for (PassoReparacao subpasso : subpassos) {
            previstos.addAll(subpasso.getComponentesPrevistos());
        }

        return previstos;
    }

    public List<Componente> getComponentesReais() {
        List<Componente> reais = new ArrayList<>(componentesReais);
        for (PassoReparacao subpasso : subpassos) {
            reais.addAll(subpasso.getComponentesReais());
        }

        return reais;
    }

    public List<PassoReparacao> getSubpassos() {
        return this.subpassos;
    }

    /**
     * Método que associa um dado componente a um passo de reparação.
     *
     * @param e Componente a adicionar
     */
    public void addComponenteReal(Componente e) {
        if (componentesReais == null) {
            this.componentesReais = new ArrayList<>();
        }
        // TODO clones averiguar
        this.componentesReais.add(e.clone());
    }

    public void addComponentePrevisto(Componente e) {
        if (componentesPrevistos == null) {
            this.componentesPrevistos = new ArrayList<>();
        }
        // TODO clones averiguar
        this.componentesPrevistos.add(e.clone());
    }

    // clone ??

    /**
     * Método que adiciona um sub-passo ao passo de reparação
     *
     * @param p Sub-passo a ser adicionado
     */
    public void addSubpasso(PassoReparacao p) {
        this.subpassos.add(p);
    }

    /**
     * Método que realiza o passo atual
     *
     * @return True se o passo se encontrar completo. Se tiver subpassos, devolve falso.
     */
    public boolean executa(float custoRealMaoDeObra, Duration duracaoReal
            , Collection<Componente> componentesReais) {
        if (this.subpassos.size() > 0)
            return false;

        this.duracaoCusto.setDuracaoReal(duracaoReal);
        this.duracaoCusto.setCustoMaoDeObraReal(custoRealMaoDeObra);
        this.componentesReais = new ArrayList<>(componentesReais);
        this.executado = true;
        return true;
    }

    public Duration getDuracaoReal() {
        Duration d = duracaoCusto.getDuracaoReal();
        for (PassoReparacao subpasso : subpassos) {
            Duration tmp = subpasso.getDuracaoReal();
            if (tmp != null) {
                if (d == null)
                    d = tmp;
                else
                    d = tmp.plus(d);
            }
        }
        return d;
    }

    @Override
    public Duration getDuracaoPrevista() {
        Duration d = duracaoCusto.getDuracaoPrevista();
        for (PassoReparacao subpasso : subpassos) {
            d = subpasso.getDuracaoPrevista().plus(d);
        }
        return d;
    }

    public float getCustoMaoDeObraReal() {
        return duracaoCusto.getCustoMaoDeObraReal() + subpassos.stream()
                .map(PassoReparacao::getCustoMaoDeObraReal).reduce(0f, Float::sum);
    }

    public float getCustoMaoDeObraPrevisto() {
        return duracaoCusto.getCustoMaoDeObraPrevisto() + subpassos.stream()
                .map(PassoReparacao::getCustoMaoDeObraPrevisto).reduce(0f, Float::sum);
    }

    public float getCustoComponentesReal() {
        return componentesReais.stream().map(Componente::getPreco).reduce(0f, Float::sum)
                + subpassos.stream().map(PassoReparacao::getCustoComponentesReal).reduce(0f, Float::sum);
    }

    public float getCustoComponentesPrevisto() {
        return componentesPrevistos.stream().map(Componente::getPreco).reduce(0f, Float::sum)
                + subpassos.stream().map(PassoReparacao::getCustoComponentesPrevisto).reduce(0f, Float::sum);
    }

    public boolean getExecutado() {
        if (subpassos.size() > 0) {
            return subpassos.stream().allMatch(PassoReparacao::getExecutado);
        } else {
            return executado;
        }
    }

    @Override
    public String getDescricao() {
        return descricao;
    }

    @Override
    public float getCustoTotalReal() {
        return getCustoComponentesReal() + getCustoMaoDeObraReal();
    }

    @Override
    public float getCustoTotalPrevisto() {
        return getCustoComponentesPrevisto() + getCustoMaoDeObraPrevisto();
    }

    public List<PassoReparacao> getSubPassosPorExecutar() {
        return subpassos.stream().filter(p -> !p.getExecutado()).toList();
    }

    public PassoReparacao getNextPasso() {
        for (PassoReparacao passo : subpassos) {
            if (!passo.getExecutado()) {
                return passo.getNextPasso();
            }
        }
        if (getExecutado())
            return null;
        return this;
    }
}