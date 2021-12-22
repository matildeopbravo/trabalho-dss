package dss;

import java.time.Duration;

public class ServicoExpressoTabelado {
    private int id;
    private String descricao;
    private float custoPrevisto ;
    private Duration duracaoPrevista;

    public ServicoExpressoTabelado(int id, String descricao, float custoPrevisto, Duration duracaoPrevista) {
        this.id = id;
        this.descricao = descricao;
        this.custoPrevisto = custoPrevisto;
        this.duracaoPrevista = duracaoPrevista;
    }

    public ServicoExpressoTabelado(ServicoExpressoTabelado servicoExpressoTabelado) {
        this.id = servicoExpressoTabelado.id;
        this.descricao = servicoExpressoTabelado.descricao;
        this.custoPrevisto = servicoExpressoTabelado.custoPrevisto;
        this.duracaoPrevista = servicoExpressoTabelado.duracaoPrevista;
    }

    public int getId() {
        return id;
    }

    public float getCustoPrevisto() {
        return custoPrevisto;
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
