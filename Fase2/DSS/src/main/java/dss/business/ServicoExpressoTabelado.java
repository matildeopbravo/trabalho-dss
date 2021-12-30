package dss.business;

import java.time.Duration;

public class ServicoExpressoTabelado {
    private int id;
    private String descricao;
    private float custo ;
    private Duration duracaoPrevista;

    public ServicoExpressoTabelado(int id, String descricao, float custo, Duration duracaoPrevista) {
        this.id = id;
        this.descricao = descricao;
        this.custo = custo;
        this.duracaoPrevista = duracaoPrevista;
    }

    public ServicoExpressoTabelado(ServicoExpressoTabelado servicoExpressoTabelado) {
        this.id = servicoExpressoTabelado.id;
        this.descricao = servicoExpressoTabelado.descricao;
        this.custo = servicoExpressoTabelado.custo;
        this.duracaoPrevista = servicoExpressoTabelado.duracaoPrevista;
    }

    public int getId() {
        return id;
    }

    public float getCusto() {
        return custo;
    }

    public String getDescricao() {
        return descricao;
    }

    public ServicoExpressoTabelado clone()  {
        return new ServicoExpressoTabelado(this);
    }

    public Duration getDuracaoPrevista() {
        return this.duracaoPrevista;
    }
}
