package dss.gui;

import dss.SGR;
import dss.SGRInterface;
import dss.reparacoes.Reparacao;
import dss.reparacoes.ReparacaoProgramada;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Collection;

public class AguardarOrcamento implements Navigatable {

    private Collection<ReparacaoProgramada> lista ;
    public AguardarOrcamento(SGRInterface sgr, Navigator navigator) {
        lista = sgr.getReparacoesAguardarOrcamento();
    }

    public Node getScene() {
        TableView<Reparacao> tabela = new TableView<>();
        TableColumn<Reparacao,String> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Reparacao,String> nome = new TableColumn<>("Id Cliente");
        nome.setCellValueFactory(new PropertyValueFactory<>("idCliente"));

        TableColumn<Reparacao,String> descricao = new TableColumn<>("Descrição");
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        tabela.getColumns().addAll(id, nome, descricao);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.getItems().setAll(lista);
        return tabela;
    }
}
