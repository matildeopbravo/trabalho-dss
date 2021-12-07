package dss;

public class ServicoExpresso {
    private int id;
    private int custo;
    private String descricao;

    public ServicoExpresso(int id, int custo, String descricao) {
        this.id = id;
        this.custo = custo;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public int getCusto() {
        return custo;
    }

    public String getDescricao() {
        return descricao;
    }
}
