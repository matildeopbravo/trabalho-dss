package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.cliente.Cliente;
import javafx.scene.Node;
import dss.business.utilizador.Utilizador;
import java.util.List;

public class DetalhesUtilizador implements Navigatable {
    private class UpdateForm extends NovoUtilizador {
        public UpdateForm(SGRInterface sgr, Navigator navigator, Utilizador utilizador) {
            super(sgr, navigator);
            this.title = "Editar utilizador";
            setButtonText("Guardar alterações");
        }

        @Override
        public Node getScene() {
            Node n = super.getScene();
            this.nif.setText(utilizador.getId());
            this.nif.setEditable(false);
            this.nome.setText(utilizador.getNome());
            return n;
        }

        @Override
        protected List<String> submit() {
            utilizador.setNome(nome.getText());
            if(password.getText() != null)
                utilizador.changePassword(password.getText());
            navigator.navigateBack("Utilizador alterado com sucesso!");
            return List.of();
        }
        @Override
        protected boolean validateSubmit() {
            return !this.nome.getText().isEmpty() && !this.nif.getText().isEmpty();
        }

    }

    private final SGRInterface sgr;
    private final Navigator navigator;
    private final Form updateForm;
    private final Utilizador utilizador;

    public DetalhesUtilizador(SGRInterface sgr, Navigator navigator, Utilizador utilizador) {
        this.sgr = sgr;
        this.navigator = navigator;
        this.utilizador = utilizador;
        this.updateForm = new UpdateForm(sgr, navigator, utilizador);
    }

    @Override
    public Node getScene() {
        return updateForm.getScene();
    }
}
