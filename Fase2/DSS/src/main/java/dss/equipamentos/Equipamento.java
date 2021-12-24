package dss.equipamentos;

import java.time.LocalDateTime;

public class Equipamento {
    private static int lastId = -1;

    private final int idEquipamento;
    private final String idCliente;
    private final LocalDateTime dataEntrega;

    public Equipamento(String idCliente, LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
        this.idEquipamento = ++lastId;
        this.idCliente = idCliente;
    }

    public int getIdEquipamento() {
        return idEquipamento;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public LocalDateTime getDataEntrega() {k
        return dataEntrega;
    }
}
