package dss.gui.components;


import dss.business.estatisticas.EstatisticasReparacoesTecnico;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TabelaEstatisticasTecnico extends TableView<EstatisticasReparacoesTecnico> {

    public TabelaEstatisticasTecnico() {
        super();

        TableColumn<EstatisticasReparacoesTecnico,String> nif = new TableColumn<>("Id Técnico");
        nif.setCellValueFactory(new PropertyValueFactory<>("idTecnico"));

        TableColumn<EstatisticasReparacoesTecnico,String> expresso = new TableColumn<>("Expresso");
        expresso.setCellValueFactory(new PropertyValueFactory<>("numReparacoesExpresso"));

        TableColumn<EstatisticasReparacoesTecnico,String> programadas = new TableColumn<>("Programadas");
        programadas.setCellValueFactory(new PropertyValueFactory<>("numReparacoesProgramadas"));

        TableColumn<EstatisticasReparacoesTecnico,String> duracao = new TableColumn<>("Duracao Media");
        duracao.setCellValueFactory(new PropertyValueFactory<>("duracaoMedia"));

        TableColumn<EstatisticasReparacoesTecnico,String> desvio = new TableColumn<>("Media Desvio Duracao");
        desvio.setCellValueFactory(new PropertyValueFactory<>("mediaDesvioDuracao"));

        this.getColumns().addAll(nif, expresso, programadas,duracao,desvio);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

}
