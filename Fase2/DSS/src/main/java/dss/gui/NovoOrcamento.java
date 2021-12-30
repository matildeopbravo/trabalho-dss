package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.reparacao.PassoReparacao;
import dss.business.reparacao.PlanoReparacao;
import dss.business.reparacao.Reparacao;
import dss.business.reparacao.ReparacaoProgramada;
import dss.gui.components.TabelaPlanoReparacao;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class NovoOrcamento implements Navigatable {
    private final SGRInterface sgr;
    private final Navigator navigator;
    private final Reparacao reparacao;
    private PassoReparacao selected;

    private final Button addPasso = new Button("Adicionar passo");
    private final TabelaPlanoReparacao tabela;
    private PlanoReparacao planoReparacao = new PlanoReparacao();

    public NovoOrcamento(SGRInterface sgr, Navigator navigator, Reparacao reparacao) {
        this.sgr = sgr;
        this.navigator = navigator;
        this.reparacao = reparacao;
        this.tabela = new TabelaPlanoReparacao(false);
        this.tabela.getSelectionModel().selectedItemProperty().addListener((observableValue, old, passoReparacaoTreeItem) -> {
            if (passoReparacaoTreeItem == this.tabela.getRoot() || passoReparacaoTreeItem == null) {
                selected = null;
            } else {
                selected = passoReparacaoTreeItem.getValue();
            }
        });

        this.addPasso.setOnAction(ev -> {
            PassoReparacaoPopup p = new PassoReparacaoPopup(sgr);
            p.setOnDone((passo, mensagem) -> {
                if (selected != null) {
                    selected.addSubpasso(passo);
                } else {
                    planoReparacao.addPasso(passo);
                }
                tabela.setPlano(planoReparacao);
            });
            navigator.openPopup(p);

        });
    }

    public NovoOrcamento(SGRInterface sgr, Navigator navigator, Reparacao reparacao, PlanoReparacao p) {
        this(sgr, navigator, reparacao);
        this.planoReparacao = p;
        tabela.setPlano(planoReparacao);
    }

    @Override
    public Node getScene() {
        VBox mainVBox = new VBox();
        mainVBox.setSpacing(5);

        HBox labelBox = new HBox();
        Pane spacingPane = new Pane();
        spacingPane.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(spacingPane, Priority.ALWAYS);
        labelBox.setAlignment(Pos.CENTER);
        labelBox.getChildren().addAll(new Label("Passos"), spacingPane, addPasso);

        tabela.setMaxWidth(Double.MAX_VALUE);
        tabela.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(tabela, Priority.ALWAYS);

        Label descricaoNome = new Label("Descrição");
        descricaoNome.setFont(new Font(18));
        Label descricao = new Label(reparacao.getDescricao());
        descricao.setWrapText(true);

        Label idEquipamento = new Label("ID do equipamento: #" + ((ReparacaoProgramada) reparacao).getEquipamentoAReparar().getIdEquipamento());
        idEquipamento.setStyle("-fx-font-weight: bold");
        idEquipamento.setFont(new Font(18));

        HBox botoes = new HBox();
        botoes.setSpacing(5);

        mainVBox.getChildren().addAll(descricaoNome, descricao, idEquipamento, labelBox, tabela, botoes);

        return mainVBox;
    }
}
