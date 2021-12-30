package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.cliente.Cliente;
import javafx.scene.Node;

import java.util.List;

public class DetalhesCliente implements Navigatable {
    private class UpdateForm extends NovoCliente {
        public UpdateForm(SGRInterface sgr, Navigator navigator, Cliente cliente) {
            super(sgr, navigator);
            this.title = "Editar cliente";
            this.buttonText = " Guardar alterações";
        }

        @Override
        public Node getScene() {
            Node n = super.getScene();
            this.nif.setText(cliente.getNIF());
            this.nif.setEditable(false);
            this.email.setText(cliente.getEmail());
            this.telemovel.setText(cliente.getNumeroTelemovel());
            this.nome.setText(cliente.getNome());

            return n;
        }

        @Override
        protected List<String> submit() {
            cliente.setEmail(email.getText());
            //cliente.setNIF(nif.getText());
            cliente.setNome(nome.getText());
            cliente.setNumeroTelemovel(telemovel.getText());
            navigator.navigateBack("Cliente alterado com sucesso!");
            return List.of();
        }
    }

    private final SGRInterface sgr;
    private final Navigator navigator;
    private final Form updateForm;
    private final Cliente cliente;

    public DetalhesCliente(SGRInterface sgr, Navigator navigator, Cliente cliente) {
        this.sgr = sgr;
        this.navigator = navigator;
        this.cliente = cliente;
        this.updateForm = new UpdateForm(sgr, navigator, cliente);
    }

    @Override
    public Node getScene() {
        return updateForm.getScene();
    }
}
