package dss;

import java.util.List;

public class PassoReparacao {
  List<PassoReparacao> sub_passos;
  String descricao;
  float duraçao;
  float custo;
  boolean pausado = false;
  private List<Componente> componentesDisponiveis;
  // lista de descricoes porque o Componente nao existe e nao tem id
  private List<String> componentesAEncomendar;
}
