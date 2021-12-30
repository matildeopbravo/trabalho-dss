package dss.gui.components;

import dss.business.reparacao.ServicoExpressoTabelado;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class TabelaServicosTabelados extends TableView<ServicoExpressoTabelado> {
    public TabelaServicosTabelados() {
        super();

        TableColumn<ServicoExpressoTabelado,String> descricao = new TableColumn<>("Descricao");
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<ServicoExpressoTabelado,String> custo = new TableColumn<>("Custo");
        custo.setCellValueFactory(new PropertyValueFactory<>("custo"));
        TableColumn<ServicoExpressoTabelado,String> duracao = new TableColumn<>("Duracao");
        duracao.setCellValueFactory(cellData -> new SimpleStringProperty(Long.toString(cellData.getValue().getDuracaoPrevista().getSeconds() / 60)));


        this.getColumns().addAll(descricao,custo,duracao);
        this.setColumnResizePolicy(javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY);
    }

}
