package dss;

public class Equipamento {
    private final String idEquipamento;
    private final String idCliente;
    private boolean abandonado;

    public Equipamento(String idCliente) {
        // TODO gerar id unico
        this.idEquipamento = "";
        this.idCliente = idCliente;
        this.abandonado = false;
    }
}
