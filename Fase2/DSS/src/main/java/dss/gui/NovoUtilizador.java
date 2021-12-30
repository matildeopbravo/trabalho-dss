package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.utilizador.*;
import dss.exceptions.JaExisteException;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class NovoUtilizador extends Form implements Navigatable {
    private SGRInterface sgr;
    private Navigator navigator;

    protected TextField nome;
    protected TextField nif;
    protected TextField password;
    protected ComboBox<TipoUtilizador> tipoDeUser;

    public NovoUtilizador(SGRInterface sgr, Navigator frame) {
        this.sgr = sgr;
        this.navigator = frame;

        LinkedHashMap<String, Node> inputs = new LinkedHashMap<>();

        this.nome = new TextField();
        this.nif = new TextField();
        this.password = new PasswordField();

        ArrayList<TipoUtilizador> list = new ArrayList<>();
        list.add(TipoUtilizador.Funcionario);
        list.add(TipoUtilizador.Tecnico);
        list.add(TipoUtilizador.Gestor);

        this.tipoDeUser = new ComboBox<>(FXCollections.observableArrayList(list));
        tipoDeUser.setValue(TipoUtilizador.Funcionario);

        inputs.put("Nome", this.nome);
        inputs.put("NIF", this.nif);
        inputs.put("Password", this.password);
        inputs.put("Tipo de Utilizador", this.tipoDeUser);
        init("Novo Utilizador", inputs, "Criar Utilizador");
    }

    @Override
    protected boolean validateSubmit() {
        return !this.nome.getText().isEmpty() && !this.nif.getText().isEmpty()
                && (!this.password.getText().isEmpty() );
    }

    @Override
    protected List<String> submit() {
        if (validateSubmit()) {
            try {
                sgr.registaUtilizador(nome.getText(),nif.getText(), password.getText(),tipoDeUser.getValue());
                navigator.navigateBack("Utilizador " + nome.getText() + " criado!");
                return List.of();
            } catch (JaExisteException e) {
                return List.of("Utilizador já existe");
            }
        } else {
            // Isto não deve acontecer!
            return null;
        }
    }

}
