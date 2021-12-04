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
}
