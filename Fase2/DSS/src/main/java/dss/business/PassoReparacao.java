package dss.business;

import dss.business.equipamentos.Componente;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PassoReparacao implements Intervencao {
  String descricao;
  private DuracaoCusto duracaoCusto;

  private List<PassoReparacao> subPassosPorExecutar;
  private List<PassoReparacao> subpassosExecutados;

  private List<Componente> componentesPrevistos = null;
  private List<Componente> componentesReais = null;

  public PassoReparacao(String descricao, Duration duracao, float custo) {
    this.subpassosExecutados = null;
    this.subPassosPorExecutar = null;
    this.descricao = descricao;
    this.duracaoCusto = new DuracaoCusto(duracao,custo);
  }

  public List<Componente> getComponentesPrevistos(){
    return new ArrayList<>(componentesPrevistos);
  }

  public List<Componente> getComponentesReais() { return new ArrayList<>(componentesReais);}

  /**
   * Método que associa um dado componente a um passo de reparação.
   * @param e Componente a adicionar
   */
  public void addComponenteReal(Componente e){
    if(componentesReais == null)  {
      this.componentesReais = new ArrayList<>();
    }
    // TODO clones averiguar
    this.componentesReais.add(e.clone());
  }
  public void addComponentePrevisto(Componente e) {
    if(componentesPrevistos == null)  {
      this.componentesPrevistos = new ArrayList<>();
    }
    // TODO clones averiguar
    this.componentesPrevistos.add(e.clone());
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
    duracaoCusto.aumentaCustoPrevisto(p.getCustoMaoDeObraPrevisto());
    duracaoCusto.aumentaDuracaoPrevista(p.getDuracaoPrevista());
    this.subPassosPorExecutar.add(p);
    this.componentesPrevistos.addAll(p.getComponentesPrevistos());
    this.componentesReais.addAll(p.getComponentesReais());
  }


  /**
   * Método que realiza um subpasso (caso exista) ou o passo.
   * @return True se o passo (incluindo os seus subpassos) se encontrarem completos.
   */
  public boolean executaPassoOuSubpasso(float custoRealMaoDeObra, Duration duracaoReal
          , Collection<Componente> componentesReais) {
    // TODO Acrescentar os componentes usados nos subpassoss
    // quer seja passo ou subpasso, vamos aumentar o custo e duracao real
    this.duracaoCusto.aumentaCustoRealMaoDeObra(custoRealMaoDeObra);
    this.duracaoCusto.aumentaDuracaoReal(duracaoReal);
    for (Componente componente: componentesReais)
      addComponenteReal(componente);

    // Tem subpassos por executar
    if (subPassosPorExecutar.size() > 0){
      PassoReparacao subPasso = this.subPassosPorExecutar.remove(subPassosPorExecutar.size() - 1);
      subPasso.duracaoCusto.setCustoMaoDeObraReal(custoRealMaoDeObra);
      subPasso.duracaoCusto.setDuracaoReal(duracaoReal);
      for (Componente componente: componentesReais)
        subPasso.addComponenteReal(componente);
      this.subpassosExecutados.add(subPasso);

      // atualizar custo e duracao real com os reais dos subpassos
      // se foi o ultimo subpasso, entao o passo inteiro esta completo
      return subPassosPorExecutar.size() == 0 ;
    }
    // nao tinha subpassos e executou o passo em si
      return true;
    }
  public Duration getDuracaoReal() {
    return duracaoCusto.getDuracaoReal();
  }

  @Override
  public Duration getDuracaoPrevista() {
    return duracaoCusto.getDuracaoPrevista();
  }

  public float getCustoMaoDeObraReal() {
    return duracaoCusto.getCustoMaoDeObraReal();
  }

  public float getCustoMaoDeObraPrevisto() {
    return duracaoCusto.getCustoMaoDeObraPrevisto();
  }

  @Override
  public String getDescricao() {
    return descricao;
  }

  @Override
  public float getCustoTotalReal() {
    float total = duracaoCusto.getCustoMaoDeObraReal();
    for (Componente componente: componentesReais)
      total += componente.getPreco();
    return total;
  }

  @Override
  public float getCustoTotalPrevisto() {
    float total = duracaoCusto.getCustoMaoDeObraPrevisto();
    for (Componente componente: componentesPrevistos)
      total += componente.getPreco();
    return total;
  }
}