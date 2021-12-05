package dss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlanoReparacao {
  List<PassoReparacao> passosReparacaoConcluidos = new ArrayList<>();
  List<PassoReparacao> passosReparacaoAExecutar = new ArrayList<>();

  // devolve para o caso de se quereradicionar subpassos
  public PassoReparacao addPasso(String descricao, float duracao, float custo)  {
    PassoReparacao p = new PassoReparacao(descricao,duracao,custo);
    passosReparacaoAExecutar.add(p);
    return p;
  }

  public boolean repara() {
    // nunca pode dar erro porque se nao tivesse passos nao estaria na lista a reparar
    PassoReparacao p = this.passosReparacaoAExecutar.get(0);
    boolean executouPassoCompleto = p.executaPassoOuSubpasso();;
    if (executouPassoCompleto) {
      this.passosReparacaoAExecutar.remove(p);
      passosReparacaoConcluidos.add(p);
    }
    return this.passosReparacaoAExecutar.size() == 0;
  }

  public Pair<Float,Float> getCustoEDuracaoPrevista(){
    float custo = 0;
    float duracao = 0;

    for (PassoReparacao elem: passosReparacaoConcluidos) {
      custo += elem.custo;
      duracao += elem.duracao;
    }

    for (PassoReparacao elem: passosReparacaoAExecutar) {
      custo += elem.custo;
      duracao += elem.duracao;
    }
    return new Pair<>(custo,duracao);
  }

  public Pair<Float,Float> getCustoEDuracaoReal(){
    float custo = 0;
    float duracao = 0;

    for (PassoReparacao elem: passosReparacaoConcluidos) {
      custo += elem.custoReal;
      duracao += elem.duracaoReal;
    }

    for (PassoReparacao elem: passosReparacaoAExecutar) {
      custo += elem.custoReal;
      duracao += elem.duracaoReal;
    }
    return new Pair<>(custo,duracao);
  }

  public HashMap<String, Pair<Componente,Integer>> getComponentes(){
    HashMap<String, Pair<Componente,Integer>> map = new HashMap<>();
    for (PassoReparacao elem: passosReparacaoConcluidos){
      for (Componente c : elem.getComponentesNecessarios()){
        Pair<Componente,Integer> p = map.get(c.getDescricao());
        if(p==null)  {
          p = new Pair<>(c.clone(),1);
          map.put(c.getDescricao(),p);
        }
        else p.setY( p.getSecond() + 1);
      }
    }
    for (PassoReparacao elem: passosReparacaoAExecutar)
      for (Componente c : elem.getComponentesNecessarios()){
        Pair<Componente,Integer> p = map.get(c.getDescricao());
        if(p==null)  {
          p = new Pair<>(c.clone(),1);
          map.put(c.getDescricao(),p);
        }
        else p.setY( p.getSecond() + 1);
      }
    return map;
  }
}



