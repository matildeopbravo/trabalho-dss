package dss.business.reparacao;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<Integer, ServicoExpressoTabelado> populate() {
        HashMap<Integer,ServicoExpressoTabelado> l = new HashMap<>();
        l.put(0, new ServicoExpressoTabelado(0,"Substituir Ecra Iphone XS", 150,Duration.ofMinutes(30)));
        l.put(1, new ServicoExpressoTabelado(0,"Formatar PC ", 50,Duration.ofMinutes(20)));
        l.put(2, new ServicoExpressoTabelado(0,"Substituir Ecra Samsung Galaxy S10", 150,Duration.ofMinutes(30)));
        return l;
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
