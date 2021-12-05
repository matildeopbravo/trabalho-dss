package dss;

import java.util.ArrayList;
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

  public void repara() {
    PassoReparacao p = this.passosReparacaoAExecutar.get(0);
    if (p != null) {
      boolean executouPassoCompleto = p.executaSubPasso();;
      if (executouPassoCompleto) {
        this.passosReparacaoAExecutar.remove(p);
        passosReparacaoConcluidos.add(p);
      }
    }
    else {
      // TODO marcar Reparacao como completa
    }
  }
}



