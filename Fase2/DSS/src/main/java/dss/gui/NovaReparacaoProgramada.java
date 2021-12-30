package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.exceptions.NaoExisteException;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.util.LinkedHashMap;
import java.util.List;

public class NovaReparacaoProgramada extends  Form implements Navigatable {

    SGRInterface sgr;
    Navigator navigator;
    private TextField idCliente;
    private TextField descricao;

    public NovaReparacaoProgramada(SGRInterface sgr, Navigator frame) {
        this.sgr = sgr;
        this.navigator = frame;

        this.idCliente = new TextField();
        this.descricao = new TextField();
        LinkedHashMap<String, Node> inputs = new LinkedHashMap<>();

        this.idCliente = new javafx.scene.control.TextField();
        inputs.put("Id Cliente", this.idCliente);
        inputs.put("Descricao ", this.descricao);

        init("Nova Reparação Programada", inputs, "Criar Reparação Programada");
    }

    @Override
    protected boolean validateSubmit() {
        return !this.idCliente.getText().isEmpty();
    }

    @Override
    protected List<String> submit() {
        if (validateSubmit()) {
            try {
                sgr.criaReparacaoProgramada(idCliente.getText(),descricao.getText() );
                navigator.navigateBack("Ficha de Reparação do Cliente " + idCliente.getText() + " criada!");
                return List.of();
            } catch (NaoExisteException e) {
                // TODO
                e.printStackTrace();
                return null;
            }
        } else {
            // Isto não deve acontecer!
            return null;
        }
    }
}
