package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.reparacao.ReparacaoProgramada;
import dss.exceptions.NaoExisteException;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.util.LinkedHashMap;
import java.util.List;

public class NovaReparacaoProgramada extends Form implements Navigatable {

    SGRInterface sgr;
    Navigator navigator;
    private TextField idCliente;
    private TextField descricao;

    public NovaReparacaoProgramada(SGRInterface sgr, Navigator frame, String idCliente) {
        this(sgr, frame);
        this.idCliente.setText(idCliente);
    }

    public NovaReparacaoProgramada(SGRInterface sgr, Navigator frame) {
        this.sgr = sgr;
        this.navigator = frame;

        this.idCliente = new TextField();
        this.descricao = new TextField();
        LinkedHashMap<String, Node> inputs = new LinkedHashMap<>();

        this.idCliente = new javafx.scene.control.TextField();
        inputs.put("NIF Cliente", this.idCliente);
        inputs.put("Descrição ", this.descricao);

        init("Nova Reparação Programada", inputs, "Criar Reparação Programada");
    }

    @Override
    protected boolean validateSubmit() {
        return !this.idCliente.getText().isEmpty() && !this.descricao.getText().isBlank();
    }

    @Override
    protected List<String> submit() {
        if (validateSubmit()) {
            try {
                ReparacaoProgramada rp = sgr.criaReparacaoProgramada(idCliente.getText(), descricao.getText());
                navigator.navigateBack("Código do equipamento é #" + rp.getEquipamentoAReparar().getIdEquipamento() + ".");
                return List.of();
            } catch (NaoExisteException e) {
                return List.of("Cliente não existe");
            }
        } else {
            // Isto não deve acontecer!
            return null;
        }
    }
}
