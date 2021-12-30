package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.utilizador.TipoUtilizador;
import dss.exceptions.JaExisteException;
import dss.exceptions.ReparacaoNaoExisteException;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.ArrayList;
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
                sgr.marcaComoEntregueConluida(idCliente.getText());
            } catch (ReparacaoNaoExisteException e) {
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
