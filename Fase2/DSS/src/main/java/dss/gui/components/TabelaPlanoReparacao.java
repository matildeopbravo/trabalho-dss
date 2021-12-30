package dss.gui.components;

import com.sun.source.tree.Tree;
import dss.business.reparacao.PassoReparacao;
import dss.business.reparacao.PlanoReparacao;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TabelaPlanoReparacao extends TreeTableView<PassoReparacao> {
    PlanoReparacao plano;

    public TabelaPlanoReparacao(boolean mostrarReal) {
        // dummy root
        super(new TreeItem<PassoReparacao>(new PassoReparacao("Passos", Duration.ZERO, 0, new ArrayList<>())));

        TreeTableColumn<PassoReparacao, String> descricaoColumn = new TreeTableColumn<>("Descrição");
        descricaoColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("descricao"));

        if (mostrarReal) {
            TreeTableColumn<PassoReparacao, String> maoDeObraPrevistoColumn = new TreeTableColumn<>("Custo de mão de obra (previsto)");
            maoDeObraPrevistoColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("custoMaoDeObraPrevisto"));
            TreeTableColumn<PassoReparacao, String> maoDeObraRealColumn = new TreeTableColumn<>("Custo de mão de obra (real)");
            maoDeObraRealColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("custoMaoDeObraReal"));
            TreeTableColumn<PassoReparacao, String> componentesPrevisto = new TreeTableColumn<>("Custo de componentes (previsto)");
            componentesPrevisto.setCellValueFactory(new TreeItemPropertyValueFactory<>("custoComponentesPrevisto"));
            TreeTableColumn<PassoReparacao, String> componentesReal = new TreeTableColumn<>("Custo de componentes (real)");
            componentesReal.setCellValueFactory(new TreeItemPropertyValueFactory<>("custoComponentesReal"));
            TreeTableColumn<PassoReparacao, String> totalPrevisto = new TreeTableColumn<>("Custo total (previsto)");
            totalPrevisto.setCellValueFactory(new TreeItemPropertyValueFactory<>("custoTotalPrevisto"));
            TreeTableColumn<PassoReparacao, String> totalReal = new TreeTableColumn<>("Custo total (real)");
            totalReal.setCellValueFactory(new TreeItemPropertyValueFactory<>("custoTotalReal"));

            TreeTableColumn<PassoReparacao, String> duracaoPrevista = new TreeTableColumn<>("Duração (prevista)");
            duracaoPrevista.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getDuracaoPrevista().getSeconds() / 60 + " minutos"));
            TreeTableColumn<PassoReparacao, String> duracaoReal = new TreeTableColumn<>("Duração (real)");
            duracaoReal.setCellValueFactory(cellData -> {
                Duration real = cellData.getValue().getValue().getDuracaoReal();
                if (real != null) {
                    long minutes = real.getSeconds() / 60;
                    if (minutes == 1) {
                        return new SimpleStringProperty(minutes + " minuto");
                    } else {
                        return new SimpleStringProperty(minutes + " minutos");
                    }
                } else {
                    return new SimpleStringProperty("N/A");
                }
            });

            this.getColumns().addAll(descricaoColumn, maoDeObraPrevistoColumn, maoDeObraRealColumn, componentesPrevisto, componentesReal, totalPrevisto, totalReal, duracaoPrevista, duracaoReal);
        } else {
            TreeTableColumn<PassoReparacao, String> maoDeObraPrevistoColumn = new TreeTableColumn<>("Custo de mão de obra");
            maoDeObraPrevistoColumn.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f €", c.getValue().getValue().getCustoMaoDeObraPrevisto())));
            TreeTableColumn<PassoReparacao, String> componentesPrevisto = new TreeTableColumn<>("Custo de componentes");
            componentesPrevisto.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f €", c.getValue().getValue().getCustoComponentesPrevisto())));
            TreeTableColumn<PassoReparacao, String> totalPrevisto = new TreeTableColumn<>("Custo total");
            totalPrevisto.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f €", c.getValue().getValue().getCustoTotalPrevisto())));

            TreeTableColumn<PassoReparacao, String> duracaoPrevista = new TreeTableColumn<>("Duração");
            duracaoPrevista.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getDuracaoPrevista().getSeconds() / 60 + " minutos"));

            this.getColumns().addAll(descricaoColumn, maoDeObraPrevistoColumn, componentesPrevisto, totalPrevisto, duracaoPrevista);
        }

        this.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
    }

    private TreeItem<PassoReparacao> toTreeItem(PassoReparacao passo) {
        TreeItem<PassoReparacao> item = new TreeItem<>(passo);
        for (PassoReparacao subpasso : passo.getSubpassos()) {
            item.getChildren().add(toTreeItem(subpasso));
        }
        item.setExpanded(true);
        return item;
    }

    public void setPlano(PlanoReparacao plano) {
        PassoReparacao root = new PassoReparacao("Passos", Duration.ZERO, 0, new ArrayList<>());
        plano.getPassosReparacao().forEach(root::addSubpasso);
        this.setRoot(toTreeItem(root));
    }

}
