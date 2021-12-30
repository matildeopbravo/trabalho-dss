package dss.gui.components;


import dss.business.cliente.Cliente;
import dss.business.utilizador.Funcionario;
import dss.business.utilizador.Tecnico;
import dss.business.utilizador.Utilizador;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URI;

public class TabelaUtilizadores extends TableView<Utilizador> {
    public abstract static class UtilizadorCallback {
        public abstract void run(Utilizador user);
    }

    public TabelaUtilizadores() {
        super();

        TableColumn<Utilizador,String> nif = new TableColumn<>("NIF");
        nif.setCellValueFactory(new PropertyValueFactory<>("Id"));

        TableColumn<Utilizador,String> nome = new TableColumn<>("Nome");
        nome.setCellValueFactory(new PropertyValueFactory<>("Nome"));

        TableColumn<Utilizador,String> tipo = new TableColumn<>("Tipo");
        tipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClass().getSimpleName()));

        this.getColumns().addAll(nif, nome, tipo);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void setOnDoubleClick(UtilizadorCallback callback) {
        this.setRowFactory(tv -> {
            TableRow<Utilizador> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                if (ev.getClickCount() == 2 && !row.isEmpty()) {
                    Utilizador utilizador = row.getItem();
                    callback.run(utilizador);
                }
            });
            return row;
        });
    }
}
