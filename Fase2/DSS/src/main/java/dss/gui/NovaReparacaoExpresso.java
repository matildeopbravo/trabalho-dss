package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.utilizador.Tecnico;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

import java.util.LinkedHashMap;
import java.util.List;

public class NovaReparacaoExpresso extends Form implements Navigatable {
    private SGRInterface sgr;
    private Navigator navigator;

    private ComboBox<Tecnico> tecnicos;

    public NovaReparacaoExpresso(SGRInterface sgr, Navigator navigator) {
        this.sgr = sgr;
        this.navigator = navigator;

        this.tecnicos = new ComboBox<>();
        this.tecnicos.getItems().addAll(sgr.getTecnicos());

        LinkedHashMap<String, Node> map = new LinkedHashMap<>();

        map.put("Técnico", tecnicos);

        init("Nova Reparação Expresso", map, "Criar Reparação Expresso");
    }

    @Override
    protected boolean validateSubmit() {
        return false;
    }

    @Override
    protected List<String> submit() {
        return null;
    }
}
