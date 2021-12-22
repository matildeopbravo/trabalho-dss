package dss;

import dss.auxiliar.Pair;
import dss.equipamentos.Componente;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlanoReparacao {
  List<PassoReparacao> passosReparacaoConcluidos = new ArrayList<>();
  List<PassoReparacao> passosReparacaoAExecutar = new ArrayList<>();

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


  public PassoReparacao addPasso(String descricao, Duration duracao, float custo)  {
    PassoReparacao p = new PassoReparacao(descricao,duracao,custo);
    passosReparacaoAExecutar.add(p);
    return p;
  }

  /**
    * Método que executa o próximo passo/subpasso de reparação neste plano
    * @return boleano que indica se o plano foi concluído na sua totalidade
    */
  public boolean repara(int custoReal, Duration duracaoReal) {
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
  public Pair<Float,Duration> getCustoEDuracaoPrevista(){
    // se for pedida uma previsao durante o periodo de reparacao, vai ser dado quanto falta e nao ter
    // em conta o que ja passou

    float custo = 0;
    Duration duracao = Duration.ZERO;

    for (PassoReparacao elem: passosReparacaoAExecutar) {
      custo += elem.getCustoPrevisto();
      duracao = duracao.plus(elem.getDuracaoPrevista());
    }
    return new Pair<>(custo,duracao);
  }

  public Pair<Float,Duration> getCustoEDuracaoReal(){
    float custo = 0;
    Duration duracao = Duration.ZERO;

    for (PassoReparacao elem: passosReparacaoConcluidos) {
      custo += elem.getCustoReal();
      duracao = duracao.plus(elem.getDuracaoReal());
    }
    return new Pair<>(custo,duracao);
  }

  // TODO vamos considerar que os componentes previstos sao os efetivamente utilizados ou noa?
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



