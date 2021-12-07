package dss;

import dss.auxiliar.Pair;
import dss.equipamentos.Componente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlanoReparacao {
  List<PassoReparacao> passosReparacaoConcluidos = new ArrayList<>();
  List<PassoReparacao> passosReparacaoAExecutar = new ArrayList<>();

  /**
   * Método que adiciona um passo ao plano
   * @return O objeto de passo criado
   */
  public PassoReparacao addPasso(String descricao, float duracao, float custo)  {
    PassoReparacao p = new PassoReparacao(descricao,duracao,custo);
    passosReparacaoAExecutar.add(p);
    return p;
  }

  /**
    * Método que executa o próximo passo/subpasso de reparação neste plano
    * @return boleano que indica se o plano foi concluído na sua totalidade
    */
  public boolean repara(int custoReal, int duracaoReal) {
    // nunca pode dar erro porque se nao tivesse passos nao estaria na lista a reparar
    PassoReparacao p = this.passosReparacaoAExecutar.get(0);
    boolean executouPassoCompleto = p.executaPassoOuSubpasso(custoReal, duracaoReal);;
    if (executouPassoCompleto) {
      this.passosReparacaoAExecutar.remove(p);
      passosReparacaoConcluidos.add(p);
    }
    return this.passosReparacaoAExecutar.size() == 0;
  }

  /**
   * Método que calcula o somatório do custo e tempo previsto de todos os
   * passos, tanto os concluídos como os por realizar
   * @return Par (custo, tempo).
   */
  public Pair<Float,Float> getCustoEDuracaoPrevista(){
    float custo = 0;
    float duracao = 0;

    for (PassoReparacao elem: passosReparacaoConcluidos) {
      custo += elem.getCusto();
      duracao += elem.getDuracao();
    }

    for (PassoReparacao elem: passosReparacaoAExecutar) {
      custo += elem.getCusto();
      duracao += elem.getDuracao();
    }
    return new Pair<>(custo,duracao);
  }

  public Pair<Float,Float> getCustoEDuracaoReal(){
    float custo = 0;
    float duracao = 0;

    for (PassoReparacao elem: passosReparacaoConcluidos) {
      custo += elem.getCustoReal();
      duracao += elem.getDuracaoReal();
    }

    for (PassoReparacao elem: passosReparacaoAExecutar) {
      custo += elem.getCustoReal();
      duracao += elem.getDuracaoReal();
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



