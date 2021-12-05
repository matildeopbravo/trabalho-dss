package dss;

import java.util.ArrayList;
import java.util.List;

public class PassoReparacao {
  // Pode ser null
  List<PassoReparacao> subPassosPorExecutar;
  List<PassoReparacao> subpassosExecutados;

  String descricao;
  float duracao;  // duracao prevista
  float custo;    // custo previsto

  float duracaoReal;  // duracao real
  float custoReal;    // custo real

  private List<Componente> componentesNecessarios = null;

  public PassoReparacao(String descricao, float duracao, float custo) {
    this.subpassosExecutados = null;
    this.subPassosPorExecutar = null;
    this.descricao = descricao;
    this.duracao = duracao; // prevista
    this.custo = custo;     // prevista
    float duracaoReal = -1;  // duracao real
    float custoReal = -1;    // custo real
  }

  public List<Componente> getComponentesNecessarios(){
    return new ArrayList<>(componentesNecessarios);
  }

  /**
   * Método que associa um dado componente a um passo de reparação.
   * @param e Componente a adicionar
   */
  public void addComponente(Componente e){
    if(componentesNecessarios == null)  {
      this.componentesNecessarios = new ArrayList<>();
    }
    this.componentesNecessarios.add(e.clone());
    this.custo += e.getPreco();
  }

  // clone ??

  /**
   * Método que adiciona um sub-passo ao passo de reparação
   * @param p Sub-passo a ser adicionado
   */
  public void addSubpaco(PassoReparacao p)  {
    if(this.subPassosPorExecutar == null) {
      this.subPassosPorExecutar = new ArrayList<>();
      this.subpassosExecutados = new ArrayList<>();
    }
    this.subPassosPorExecutar.add(p);

    this.custo += p.custo;
    this.duracao += p.duracao;
  }

  /**
   * Método que realiza um subpasso (caso exista) ou o passo.
   * @return True se o passo (incluindo os seus subpassos) se encontrarem completos.
   */
  // TODO Adicionar o tempo real que foi necxessario e o custo real
  public boolean executaPassoOuSubpasso() {
    if (subPassosPorExecutar.size() > 0){
      this.subpassosExecutados.add(this.subPassosPorExecutar.remove(0));
      return subPassosPorExecutar.size() == 0;
    }
      // nao tinha subpassos e executou o passo em si
      return true;
  }

}
