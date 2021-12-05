package dss;

import java.util.ArrayList;
import java.util.List;

public class PassoReparacao {
  List<PassoReparacao> subPassosPorExecutar;
  List<PassoReparacao> subpassosExecutados;
  String descricao;
  float duracao;
  float custo;
  boolean pausado = false;
  private List<Componente> componentesNecessarios = null;

  public PassoReparacao(String descricao, float duracao, float custo) {
    this.descricao = descricao;
    this.duracao = duracao;
    this.custo = custo;
  }

  public void togglePause(){
    this.pausado = !pausado ;
  }

  public void addComponente(Componente e){
    if(componentesNecessarios == null)  {
      this.componentesNecessarios = new ArrayList<>();
    }
    this.componentesNecessarios.add(e.clone());
  }

  // clone ??
  public void addSubpaco(PassoReparacao p)  {
    this.subPassosPorExecutar.add(p);
  }

  public boolean executaSubPasso() {
    if (subPassosPorExecutar.size() > 0){
      this.subpassosExecutados.add(this.subPassosPorExecutar.remove(0));
      return false;
    }
      // executou o proprio passo e ele pode ser removido
      return true;
  }


}
