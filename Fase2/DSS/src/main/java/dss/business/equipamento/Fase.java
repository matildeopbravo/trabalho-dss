package dss.business.equipamento;

import java.io.Serializable;

public enum Fase implements Serializable {
    NaoIniciada,
    AEsperaOrcamento,
    AEsperaResposta,
    Recusada,
    Reparado,
    EmReparacao,
    NaoPodeSerReparado,
    EntregueConcluida,
    EntregueRecusada
}