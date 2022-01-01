package dss.business.equipamento;

import java.io.Serializable;

public enum Fase implements Serializable {
    NaoIniciada("Não Iniciada"),
    AEsperaOrcamento("À Espera de Orçamento"),
    AEsperaResposta("À Espera de Resposta"),
    Recusada("Recusada"),
    Reparado("Reparado"),
    EmReparacao("Em Reparação"),
    NaoPodeSerReparado("Irreparável"),
    EntregueConcluida("Concluída e Entregue"),
    EntregueRecusada("Recusada e Entregue");

    private final String fase;

    Fase(String s) {
        fase = s;
    }
    public String toString() {
        return fase;
    }
}