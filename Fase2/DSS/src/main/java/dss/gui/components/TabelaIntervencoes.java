package dss.gui.components;


import dss.business.reparacao.Intervencao;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Duration;
import java.util.List;
import java.util.Map;

// Record é como uma classe mas gera os getters automaticamente
record LinhaIntervencao(String getTecnicoId, String getDescricao, Duration getDuracaoReal, Duration getDuracaoPrevista,
                        float getCustoTotalPrevisto, float getCustoTotalReal) {
}

public class TabelaIntervencoes extends TableView<LinhaIntervencao> {
    public TabelaIntervencoes() {
        super();

        TableColumn<LinhaIntervencao, String> nif = new TableColumn<>("Id Técnico");
        nif.setCellValueFactory(new PropertyValueFactory<>("idTecnico"));

        TableColumn<LinhaIntervencao, String> descricao = new TableColumn<>("Descrição");
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<LinhaIntervencao, String> duracaoReal = new TableColumn<>("Duração Real");
        duracaoReal.setCellValueFactory(cellData -> {
            Duration d = cellData.getValue().getDuracaoReal();
            if (d != null) {
                return new SimpleStringProperty(d.toSeconds() / 60 + " minutos");
            } else {
                return new SimpleStringProperty("N/A");
            }
        });

        TableColumn<LinhaIntervencao, String> duracaoPrevista = new TableColumn<>("Duracao Prevista");
        duracaoPrevista.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDuracaoPrevista().toSeconds() / 60 + ""));

        TableColumn<LinhaIntervencao, String> custoTotalPrevisto = new TableColumn<>("Custo Total Previsto");
        custoTotalPrevisto.setCellValueFactory(new PropertyValueFactory<>("custoTotalPrevisto"));

        TableColumn<LinhaIntervencao, String> custoTotalReal = new TableColumn<>("Custo Total Real");
        custoTotalReal.setCellValueFactory(new PropertyValueFactory<>("custoTotalReal"));

        this.getColumns().addAll(nif, descricao, duracaoReal, duracaoPrevista, custoTotalPrevisto, custoTotalReal);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void setIntervencoes(Map<String, List<Intervencao>> intervencoesTecnicos) {
        for (var intervencoes : intervencoesTecnicos.entrySet()) {
            String tecnico = intervencoes.getKey();
            for (var intervencao : intervencoes.getValue()) {
                LinhaIntervencao l = new LinhaIntervencao(tecnico, intervencao.getDescricao(), intervencao.getDuracaoReal(),
                        intervencao.getDuracaoPrevista(), intervencao.getCustoTotalPrevisto(), intervencao.getCustoTotalReal());
                this.getItems().add(l);
            }
        }
    }
}
