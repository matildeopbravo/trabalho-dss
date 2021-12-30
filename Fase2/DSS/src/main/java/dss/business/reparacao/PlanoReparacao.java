package dss.business.reparacao;

import dss.business.auxiliar.Pair;
import dss.business.equipamento.Componente;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PlanoReparacao {
  // se for demasiado lente calcular o custo on demand, volta-se a meter isto
  //private float custoAteAgora = 0;
  private List<PassoReparacao> passosReparacaoConcluidos = new ArrayList<>();
  private List<PassoReparacao> passosReparacaoAExecutar = new ArrayList<>();

  public List<PassoReparacao> getPassosReparacaoConcluidos() {
    return passosReparacaoConcluidos;
  }

  public boolean estaConcluido() {
    return passosReparacaoAExecutar.isEmpty();
  }
  /**
   * Método que adiciona um passo ao plano
   * @return O objeto de passo criado
   */


  public PassoReparacao addPasso(String descricao, Duration duracao, float custoMaoDeObra)  {
    PassoReparacao p = new PassoReparacao(descricao,duracao,custoMaoDeObra);
    passosReparacaoAExecutar.add(p);
    return p;
  }
  /**
    * Método que executa o próximo passo/subpasso de reparação neste plano
    */
  public boolean repara(int custoMaoDeObraReal, Duration duracaoReal, Collection<Componente> componentesReais) {
    // nunca pode dar erro porque se nao tivesse passos nao estaria na lista a reparar
    PassoReparacao p = this.passosReparacaoAExecutar.get(passosReparacaoAExecutar.size() -  1);
    boolean executouPassoCompleto = p.executaPassoOuSubpasso(custoMaoDeObraReal, duracaoReal, componentesReais);
    //this.custoAteAgora += custoReal;
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
  public Pair<Float,Duration> getCustoTotalEDuracaoPrevista(){
    // se for pedida uma previsao durante o periodo de reparacao, vai ser dado quanto falta e nao ter
    // em conta o que ja passou

    float custo = 0;
    Duration duracao = Duration.ZERO;

    for (PassoReparacao elem: passosReparacaoAExecutar) {
      custo += elem.getCustoTotalPrevisto();
      duracao = duracao.plus(elem.getDuracaoPrevista());
    }
    return new Pair<>(custo,duracao);
  }

  public Pair<Float,Duration> getCustoTotalEDuracaoReal(){
    float custo = 0;
    Duration duracao = Duration.ZERO;

    for (PassoReparacao elem: passosReparacaoConcluidos) {
      custo += elem.getCustoTotalReal();
      duracao = duracao.plus(elem.getDuracaoReal());
    }
    return new Pair<>(custo,duracao);
  }

  public HashMap<String, Pair<Componente,Integer>> getComponentesPrevistos(){
    HashMap<String, Pair<Componente,Integer>> map = new HashMap<>();
    for (PassoReparacao elem: passosReparacaoConcluidos){
      for (Componente c : elem.getComponentesPrevistos()){
        Pair<Componente,Integer> p = map.get(c.getDescricao());
        if(p==null)  {
          p = new Pair<>(c.clone(),1);
          map.put(c.getDescricao(),p);
        }
        else p.setY( p.getSecond() + 1);
      }
    }
    for (PassoReparacao elem: passosReparacaoAExecutar)
      for (Componente c : elem.getComponentesPrevistos()){
        Pair<Componente,Integer> p = map.get(c.getDescricao());
        if(p==null)  {
          p = new Pair<>(c.clone(),1);
          map.put(c.getDescricao(),p);
        }
        else p.setY( p.getSecond() + 1);
      }
    return map;
  }

  public HashMap<String, Pair<Componente,Integer>> getComponentesReais(){
    HashMap<String, Pair<Componente,Integer>> map = new HashMap<>();
    for (PassoReparacao elem: passosReparacaoConcluidos){
      for (Componente c : elem.getComponentesReais()){
        Pair<Componente,Integer> p = map.get(c.getDescricao());
        if(p==null)  {
          p = new Pair<>(c.clone(),1);
          map.put(c.getDescricao(),p);
        }
        else p.setY( p.getSecond() + 1);
      }
    }
    for (PassoReparacao elem: passosReparacaoAExecutar)
      for (Componente c : elem.getComponentesReais()){
        Pair<Componente,Integer> p = map.get(c.getDescricao());
        if(p==null)  {
          p = new Pair<>(c.clone(),1);
          map.put(c.getDescricao(),p);
        }
        else p.setY( p.getSecond() + 1);
      }
    return map;
  }

  public void addSubPasso(PassoReparacao passo, String descricao, Duration duracao, float custo) {
      passo.addSubpasso(new PassoReparacao(descricao,duracao,custo));
  }
}



