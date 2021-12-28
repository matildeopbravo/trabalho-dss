package dss.gui;

import dss.SGR;
import dss.exceptions.UtilizadorJaExisteException;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.util.LinkedHashMap;
import java.util.List;

public class NovoCliente extends Form {
    private SGR sgr;
    private Frame frame;

    private TextField nome;
    private TextField nif;
    private TextField email;
    private TextField telemovel;

    public NovoCliente(SGR sgr, Frame frame) {
        this.sgr = sgr;
        this.frame = frame;

        LinkedHashMap<String, Node> inputs = new LinkedHashMap<>();

        this.nome = new TextField();
        this.nif = new TextField();
        this.email = new TextField();
        this.telemovel = new TextField();

        inputs.put("Nome", this.nome);
        inputs.put("NIF", this.nif);
        inputs.put("Email", this.email);
        inputs.put("Telemóvel", this.telemovel);

        init("Novo Cliente", inputs, "Criar cliente");
    }

    @Override
    protected boolean validateSubmit() {
        return !this.nome.getText().isEmpty() && !this.nif.getText().isEmpty()
                && (!this.email.getText().isEmpty() || !this.telemovel.getText().isEmpty());
    }

    @Override
    protected List<String> submit() {
        if (validateSubmit()) {
            try {
                sgr.criaCliente(nif.getText(), nome.getText(), email.getText(), telemovel.getText(),
                        sgr.getUtilizadorAutenticado().getId());
                frame.navigateBack("Cliente " + nome.getText() + " criado!");
                return List.of();
            } catch (UtilizadorJaExisteException e) {
                return List.of("Cliente já existe");
            }
        } else {
            // Isto não deve acontecer!
            return null;
        }
    }
}
