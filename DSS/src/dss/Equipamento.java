package dss;

public class Equipamento {
    private static int lastId = -1;

    private final int idEquipamento;
    private final String idCliente;
    private boolean abandonado;

    public Equipamento(String idCliente) {
        this.idEquipamento = ++lastId;
        this.idCliente = idCliente;
        this.abandonado = false;
    }

    public int getIdEquipamento() {
        return idEquipamento;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public boolean isAbandonado() {
        return abandonado;
    }
}
