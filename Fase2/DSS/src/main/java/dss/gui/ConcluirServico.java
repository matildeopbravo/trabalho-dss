package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.exceptions.NaoExisteException;
import dss.exceptions.ReparacaoNaoExisteException;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;

public class ConcluirServico extends  Form implements Navigatable {
    private SGRInterface sgr;
    private Navigator navigator;

    TextField idCliente;
    TextField idEquipamento;


    public ConcluirServico(SGRInterface sgr, Navigator frame) {
        this.sgr = sgr;
        this.navigator = frame;

        LinkedHashMap<String, Node> inputs = new LinkedHashMap<>();

        this.idCliente = new TextField();
        this.idEquipamento = new TextField();

        inputs.put("Id Cliente", this.idCliente);
        inputs.put("Id Equipamento", this.idEquipamento);
        init("Concluir Serviço", inputs, "Concluir Serviço");
    }

    @Override
    protected boolean validateSubmit() {
        return !this.idCliente.getText().isEmpty() && !this.idEquipamento.getText().isEmpty();
    }

    @Override
    protected List<String> submit() {
        if (validateSubmit()) {
                // TODO popup a pedir a actual Duracao
            try {
                sgr.marcaComoEntregueConcluida(idCliente.getText(), Duration.ZERO);
            } catch (NaoExisteException e) {
                // TODO
                e.printStackTrace();
            }
            navigator.navigateBack("Reparação Concluída!");
                return List.of();
        } else {
            // Isto não deve acontecer!
            return null;
        }
    }



}
