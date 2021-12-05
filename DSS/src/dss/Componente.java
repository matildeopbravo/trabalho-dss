package dss;

import java.util.ArrayList;
import java.util.List;

public class Componente {
    private static int lastId = -1;

    private int id;
    private String descricao;
    private int quantidade;
    private float preco;
    private List<String> categorias;

    public Componente(int quantidade, String descricao, List<String> categorias) {
        this.id = ++lastId;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.categorias = new ArrayList<>(categorias);
    }

    public Componente(Componente componente) {
        this.id = componente.id;
        this.descricao = componente.descricao;
        this.quantidade = componente.quantidade;
        this.preco = componente.preco;
        this.categorias = new ArrayList<>(componente.categorias);
    }

    public Componente clone () {
        return new Componente(this);
    }
}
