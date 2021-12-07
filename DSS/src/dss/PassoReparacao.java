package dss;

import dss.equipamentos.Componente;

import java.util.ArrayList;
import java.util.List;

public class PassoReparacao {
  // Pode ser null
  private List<PassoReparacao> subPassosPorExecutar;
  private List<PassoReparacao> subpassosExecutados;

  private String descricao;
  private float duracao;  // duracao prevista
  private float custo;    // custo previsto

  private float duracaoReal = -1;  // duracao real
  private float custoReal = -1;    // custo real

  private List<Componente> componentesNecessarios = null;

  public PassoReparacao(String descricao, float duracao, float custo) {
    this.subpassosExecutados = null;
    this.subPassosPorExecutar = null;
    this.descricao = descricao;
    this.duracao = duracao; // prevista
    this.custo = custo;     // prevista
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
  public void addSubpasso(PassoReparacao p)  {
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
  // TODO Adicionar o tempo real que foi necessario e o custo real
  public boolean executaPassoOuSubpasso(int custoReal, int duracaoReal) {
    if (subPassosPorExecutar.size() > 0){
      PassoReparacao subPasso = this.subPassosPorExecutar.remove(0);
      subPasso.setCustoReal(custoReal);
      subPasso.setDuracaoReal(duracaoReal);
      this.subpassosExecutados.add(subPasso);

      if (subPassosPorExecutar.size() == 0) {
        this.custoReal = custoReal;
        this.duracaoReal = duracaoReal;
        return true;
      }
      return false;
    }
    // nao tinha subpassos e executou o passo em si
    this.custoReal = custoReal;
    this.duracaoReal = duracaoReal;
    return true;
  }

  public float getDuracao() {
    return duracao;
  }

  public void setDuracao(float duracao) {
    this.duracao = duracao;
  }

  public float getCusto() {
    return custo;
  }

  public void setCusto(float custo) {
    this.custo = custo;
  }

  public float getDuracaoReal() {
    return duracaoReal;
  }

  public void setDuracaoReal(float duracaoReal) {
    this.duracaoReal = duracaoReal;
  }

  public float getCustoReal() {
    return custoReal;
  }

  public void setCustoReal(float custoReal) {
    this.custoReal = custoReal;
  }
}