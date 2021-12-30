package dss.gui.components;

import dss.business.estatisticas.EstatisticasFuncionario;
import dss.business.estatisticas.EstatisticasFuncionario;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class TabelaEstatisticasFuncionario extends TableView<EstatisticasFuncionario> {
    public TabelaEstatisticasFuncionario() {

    super();
    TableColumn<EstatisticasFuncionario,String> nif = new TableColumn<>("Id Funcionário");
        nif.setCellValueFactory(new PropertyValueFactory<>("idFuncionario"));

    TableColumn<EstatisticasFuncionario,String> rececoes = new TableColumn<>("Rececoes");
        rececoes.setCellValueFactory(new PropertyValueFactory<>("rececoes"));

    TableColumn<EstatisticasFuncionario,String> entregas = new TableColumn<>("Entregas");
        entregas.setCellValueFactory(new PropertyValueFactory<>("entregas"));

        this.getColumns().addAll(nif, rececoes,entregas );
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
}

}
