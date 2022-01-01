package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.equipamento.Fase;
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
            try {
                sgr.marcaComoEntregue(idCliente.getText(), Integer.parseInt(idEquipamento.getText()));
            } catch (NaoExisteException e) {
                e.printStackTrace();
            }
            navigator.navigateBack("Serviço Concluída!");
                return List.of();
        } else {
            // Isto não deve acontecer!
            return null;
        }
    }



}
