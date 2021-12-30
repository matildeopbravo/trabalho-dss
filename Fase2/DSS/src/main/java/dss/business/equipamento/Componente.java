package dss.business.equipamento;

import java.util.ArrayList;
import java.util.List;

public class Componente {
    private static int lastId = -1;

    private int id;
    private String descricao;
    private float preco;
    private List<String> categorias;

    public Componente(String descricao, float preco, List<String> categorias) {
        this.id = ++lastId;
        this.descricao = descricao;
        this.preco = preco;
        this.categorias = new ArrayList<>(categorias);
    }

    public Componente(Componente componente) {
        this.id = componente.id;
        this.descricao = componente.descricao;
        this.preco = componente.preco;
        this.categorias = new ArrayList<>(componente.categorias);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public Componente clone () {
        return new Componente(this);
    }

    @Override
    public String toString() {
        return descricao + " - " + String.format("%.2f€",preco);
    }
}
