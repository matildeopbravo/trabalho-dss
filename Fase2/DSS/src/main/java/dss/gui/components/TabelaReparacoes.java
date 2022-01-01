package dss.gui.components;

import dss.business.SGR.SGRInterface;
import dss.business.cliente.Cliente;
import dss.exceptions.NaoExisteException;
import dss.business.reparacao.Reparacao;
import dss.business.utilizador.Utilizador;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TabelaReparacoes extends TableView<Reparacao> {
    private SGRInterface sgr;

    public TabelaReparacoes(SGRInterface sgr) {
        super();

        this.sgr = sgr;
        TableColumn<Reparacao, String> idReparacao = new TableColumn<>("ID");
        idReparacao.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Reparacao, String> fase = new TableColumn<>("Fase");
        fase.setCellValueFactory(new PropertyValueFactory<>("fase"));

        TableColumn<Reparacao, String> cliente = new TableColumn<>("Cliente");
        cliente.setCellValueFactory(cellData -> {
            try {
                Cliente c = sgr.getCliente(cellData.getValue().getIdCliente());
                return new SimpleStringProperty(c.getNome());
            } catch (NaoExisteException e) {
                e.printStackTrace();
                return null;
            }
        });

        TableColumn<Reparacao, String> funcionarioCriador = new TableColumn<>("Funcionário criador");
        funcionarioCriador.setCellValueFactory(cellData -> {
            try {
                Utilizador f = sgr.getUtilizador(cellData.getValue().getFuncionarioCriador());
                return new SimpleStringProperty(f.getNome());
            } catch (NaoExisteException e) {
                e.printStackTrace();
                return null;
            }
        });

        this.getColumns().addAll(idReparacao, fase, cliente, funcionarioCriador);
    }
}
