package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.reparacao.Reparacao;
import dss.business.reparacao.ServicoExpressoTabelado;
import dss.business.utilizador.Tecnico;
import dss.exceptions.ReparacaoDesteClienteJaExisteException;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.LinkedHashMap;
import java.util.List;

public class NovaReparacaoExpresso extends Form implements Navigatable {
    private SGRInterface sgr;
    private Navigator navigator;

    private TextField idCliente;
    private ComboBox<ServicoExpressoTabelado> servicosExpresso;
    private TextField descricao;
    private ComboBox<Tecnico> tecnicos;

    public NovaReparacaoExpresso(SGRInterface sgr, Navigator navigator, String nif) {
        this.sgr = sgr;
        this.navigator = navigator;
        this.idCliente = new TextField();
        this.idCliente.setText(nif);
        this.descricao = new TextField();
        this.servicosExpresso = new ComboBox<>();
        this.tecnicos = new ComboBox<>();

        this.tecnicos = new ComboBox<>(FXCollections.observableArrayList(sgr.getTecnicosDisponveis()));
        this.servicosExpresso = new ComboBox<>(FXCollections.observableArrayList(sgr.getServicosTabelados()));

        LinkedHashMap<String, Node> map = new LinkedHashMap<>();

        map.put("NIF Cliente", this.idCliente);
        map.put("Descricao", this.descricao);
        map.put("Tipo de Reparação", this.servicosExpresso);
        map.put("Técnico", this.tecnicos);

        init("Nova Reparação Expresso", map, "Criar Reparação Expresso");
    }

    @Override
    protected boolean validateSubmit() {
        return !this.idCliente.getText().isEmpty() && this.servicosExpresso.getValue() != null && tecnicos.getValue() != null;
    }

    @Override
    protected List<String> submit() {
        if (validateSubmit()) {
            try {
                Reparacao r =  sgr.criaReparacaoExpresso(servicosExpresso.getValue().getId(),idCliente.getText(),tecnicos.getValue().getId(),descricao.getText());
                navigator.navigateBack("Código do equipamento é #" + sgr.getEquipamentoByIdCliente(r.getIdCliente()) + ".");
                return List.of();
            } catch (ReparacaoDesteClienteJaExisteException e) {
                return List.of("Cliente já tem Reparação Registada");
            }
        } else {
            // Isto não deve acontecer!
            return null;
        }

    }
}
