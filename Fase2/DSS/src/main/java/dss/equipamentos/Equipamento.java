package dss.equipamentos;

public class Equipamento {
    private static int lastId = -1;

    private final int idEquipamento;
    private final String idCliente;

    public Equipamento(String idCliente) {
        this.idEquipamento = ++lastId;
        this.idCliente = idCliente;
    }

    public int getIdEquipamento() {
        return idEquipamento;
    }

    public String getIdCliente() {
        return idCliente;
    }
}
