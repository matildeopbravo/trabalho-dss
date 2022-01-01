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

import java.util.List;

public class TabelaReparacoes extends TableView<Reparacao> {
    private SGRInterface sgr;

    public TabelaReparacoes(SGRInterface sgr) {
        super();

        this.sgr = sgr;
        TableColumn<Reparacao, String> idReparacao = new TableColumn<>("ID");
        idReparacao.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Reparacao, String> cliente = new TableColumn<>("ID Cliente");
        cliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        TableColumn<Reparacao, String> idEquipamento = new TableColumn<>("Id Equipamento");
        idEquipamento.setCellValueFactory(cellData ->
                new SimpleStringProperty(sgr.getEquipamentoByIdCliente(cellData.getValue().getIdCliente()).toString()));
        TableColumn<Reparacao, String> fase = new TableColumn<>("Fase");
        fase.setCellValueFactory(new PropertyValueFactory<>("fase"));

        TableColumn<Reparacao, String> ultimoTecnicoAReparar = new TableColumn<>("Tecnico");
        ultimoTecnicoAReparar.setCellValueFactory(cellData -> {
            try {
                List<String> l = cellData.getValue().getTecnicosQueRepararam();
                return new SimpleStringProperty(sgr.getUtilizador(l.get(l.size()-1)).toString());
            } catch (NaoExisteException e) {
                e.printStackTrace();
                return null;
            }
        });


        this.getColumns().addAll(idReparacao, cliente, idEquipamento, fase, ultimoTecnicoAReparar);
    }
}
