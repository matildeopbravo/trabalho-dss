package dss.gui.components;

import dss.business.SGRInterface;
import dss.business.reparacao.PassoReparacao;
import dss.business.reparacao.PlanoReparacao;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TabelaPlanoReparacao extends TreeTableView<PassoReparacao> {
    PlanoReparacao plano;

    public TabelaPlanoReparacao() {
        // dummy root
        super(new TreeItem<PassoReparacao>(new PassoReparacao("Passos",Duration.ZERO,0)));
        List<TreeItem<PassoReparacao>> rootPassos = plano.getPassosPorRealizar()
                .stream()
                .map(p -> getPassoWithSubpassos(p, p.getSubPassosPorExecutar()))
                .collect(Collectors.toList());
        this.getChildren().addAll((Node) rootPassos);

        TreeTableColumn<PassoReparacao,String> column = new TreeTableColumn<>("Descricao");
        column.setCellValueFactory(new TreeItemPropertyValueFactory<>("descricao"));
    }

    private  TreeItem<PassoReparacao> getPassoWithSubpassos(PassoReparacao p, List<PassoReparacao> subs) {
        TreeItem<PassoReparacao> root = new TreeItem<>(p);
        root.setExpanded(true);
        root.getChildren().addAll(subs.stream().map(TreeItem::new).collect(Collectors.toList()));
        return root;
    }

}
