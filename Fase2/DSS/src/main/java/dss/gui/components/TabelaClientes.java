package dss.gui.components;


import dss.clientes.Cliente;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TabelaClientes extends TableView<Cliente> {
    public abstract static class ClienteCallback {
        public abstract void run(Cliente cliente);
    }

    public TabelaClientes() {
        super();

        TableColumn<Cliente,String> nif = new TableColumn<>("NIF");
        nif.setCellValueFactory(new PropertyValueFactory<>("NIF"));

        TableColumn<Cliente,String> nome = new TableColumn<>("Nome");
        nome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
        TableColumn<Cliente,String> mail = new TableColumn<>("Email");
        mail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        TableColumn<Cliente,String> telemovel = new TableColumn<>("Telemovel");
        telemovel.setCellValueFactory(new PropertyValueFactory<>("numeroTelemovel"));

        this.getColumns().addAll(nif, nome, mail, telemovel);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void setOnDoubleClick(ClienteCallback callback) {
        this.setRowFactory(tv -> {
            TableRow<Cliente> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                if (ev.getClickCount() == 2 && !row.isEmpty()) {
                    Cliente cliente = row.getItem();
                    callback.run(cliente);
                }
            });
            return row;
        });
    }
}
