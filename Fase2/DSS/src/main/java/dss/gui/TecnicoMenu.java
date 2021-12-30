package dss.gui;

import dss.business.SGR.SGRInterface;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class TecnicoMenu implements Navigatable {
    private SGRInterface sgr;
    private Navigator navigator;

    public TecnicoMenu(SGRInterface sgr, Navigator navigator) {
        this.sgr = sgr;
        this.navigator = navigator;
    }

    public Node getScene() {
        VBox vbox = new VBox();
        Label title = new Label("Menu inicial");
        title.setFont(new Font(24.0));
        vbox.getChildren().add(title);

        Button allUsersButton = new Button("Mostra Utilizadores");
        allUsersButton.setOnAction(e -> navigator.navigateTo(new TodosUtilizadores(sgr, navigator)));

        Button newClientButton = new Button("Novo cliente");
        newClientButton.setOnAction(e -> navigator.navigateTo(new NovoCliente(sgr, navigator)));

        Button allClientsButton = new Button("Mostra Clientes");
        allClientsButton.setOnAction(e -> navigator.navigateTo(new TodosClientes(sgr, navigator)));

        Button aguardarOrcamentoButton = new Button("Reparações a aguardar Orçamento");
        aguardarOrcamentoButton.setOnAction(s -> navigator.navigateTo(new AguardarOrcamento(sgr, navigator)));

        Button listaReparacoesButton = new Button("Lista de Reparações");
        listaReparacoesButton.setOnAction(s -> navigator.navigateTo(new TodasReparacoes(sgr, navigator)));

        Button intervencoesTecnicos = new Button("Intervenções Dos Técnicos");
        Button estatisticasFuncionarios = new Button("Estatísticas sobre os Funcionários");
        Button estatisticasDasReparacoesDosTecnicos = new Button("Estatísticas sobre as reparações dos Técnicos");

        vbox.getChildren().addAll(allUsersButton, newClientButton, allClientsButton, aguardarOrcamentoButton, listaReparacoesButton);

        return vbox;
    }
}
