package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.reparacao.Reparacao;
import dss.business.reparacao.Reparacao;
import dss.business.reparacao.ReparacaoExpresso;
import dss.business.reparacao.ReparacaoProgramada;
import dss.business.utilizador.Utilizador;
import dss.exceptions.NaoExisteException;
import dss.exceptions.TecnicoNaoAtribuidoException;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Supplier;

public class ReparacoesEmCurso implements Navigatable {
    private SGRInterface sgr;
    private Navigator navigator;
    private TableView<Reparacao> tabela;
    private Button reparar = new Button("Reparar");
    private Button pausar =  new Button("Pausar/Retomar");
    private Button concluir =new Button("Concluir");
    private Reparacao selected;
    private Supplier<List<Reparacao>> fun;
    private boolean isProgramada ;

    public ReparacoesEmCurso(SGRInterface sgr, Navigator navigator, Supplier<List<Reparacao>> fun, boolean isProgramada) {
        this.sgr = sgr;
        this.navigator = navigator;
        this.tabela = new TableView<>();

        TableColumn<Reparacao, String> idReparacao = new TableColumn<>("ID");
        idReparacao.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Reparacao, String> nifCliente = new TableColumn<>("NIF Cliente");
        nifCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));

        TableColumn<Reparacao, String> fase = new TableColumn<>("Fase");
        fase.setCellValueFactory(new PropertyValueFactory<>("fase"));

        TableColumn<Reparacao, String> descricao = new TableColumn<>("Descrição");
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));


        TableColumn<Reparacao, String> ultimoTecnicoAReparar = new TableColumn<>("Tecnico");
        ultimoTecnicoAReparar.setCellValueFactory(cellData -> {
            try {
                List <String> l = cellData.getValue().getTecnicosQueRepararam();
                return new SimpleStringProperty(sgr.getUtilizador(l.get(l.size()-1)).toString());
            } catch (NaoExisteException e) {
                e.printStackTrace();
                return null;
            }
        });

        TableColumn<Reparacao, String> idEquipamento = new TableColumn<>("Id Equipamento");
        idEquipamento.setCellValueFactory(cellData -> new SimpleStringProperty(sgr.getEquipamentoByIdCliente(cellData.getValue().getIdCliente()).toString()));

        this.tabela.getColumns().setAll(idReparacao,nifCliente, idEquipamento, fase, ultimoTecnicoAReparar, descricao);

        this.reparar.setDisable(true);
        this.concluir.setDisable(true);
        this.pausar.setDisable(true);
        this.tabela.getSelectionModel().selectedItemProperty().addListener((observableValue, old, reparacao) -> {
            selected = reparacao;
            boolean isNull = selected == null;

            this.reparar.setDisable(isNull);
            this.concluir.setDisable(isNull);
            this.pausar.setDisable(isNull);

        });
        this.tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        if (isProgramada) {
            this.reparar.setOnAction(ev -> {
                if (selected != null) {
                    navigator.navigateTo(new Reparar(sgr, navigator, (ReparacaoProgramada) selected));
                }
            });
            TableColumn<Reparacao, String> pausada = new TableColumn<>("pausada");
            pausada.setCellValueFactory(cellData ->
                    new SimpleStringProperty( Boolean.toString(((ReparacaoProgramada) cellData.getValue()).estaPausado())));
            this.tabela.getColumns().setAll(idReparacao,nifCliente, idEquipamento, pausada, fase, ultimoTecnicoAReparar, descricao);

        }
        else {
            this.tabela.getColumns().setAll(idReparacao,nifCliente, idEquipamento, fase, ultimoTecnicoAReparar, descricao);
            this.reparar.setOnAction(ev -> {
                if (selected != null) {
                    try {
                        sgr.iniciaReparacaoExpresso((ReparacaoExpresso) selected);
                        navigator.navigateBack("Reparação Expresso Iniciada");
                    } catch (TecnicoNaoAtribuidoException e) {
                        // ESTA REPARACAO NAO ESTA ATRIBUIDA A ESTE TECNICO
                    }
                }
            });
            this.concluir.setOnAction(ev -> {
                if (selected != null) {
                    try {
                        sgr.concluiReparacao(selected);
                    } catch (NaoExisteException e) {
                        e.printStackTrace();
                    }
                    navigator.navigateBack("Reparação Expresso Concluida");
                }
            });


        }
        this.fun = fun;

    }


    @Override
    public Node getScene() {
        VBox vbox = new VBox();
        vbox.setSpacing(10.0);

        VBox.setVgrow(this.tabela, Priority.ALWAYS);
        this.tabela.setMaxHeight(Double.MAX_VALUE);

        this.tabela.getItems().setAll(fun.get());
        HBox buttons = new HBox();
        buttons.setSpacing(5.0);


        vbox.setSpacing(5);
        if(isProgramada) {
            buttons.getChildren().addAll(reparar,pausar);
        }
        else {
            buttons.getChildren().addAll(reparar,concluir);
        }
        vbox.getChildren().addAll(tabela, buttons );

        return vbox;
    }
}
