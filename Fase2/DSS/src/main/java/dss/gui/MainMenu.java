package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.utilizador.Funcionario;
import dss.business.utilizador.Gestor;
import dss.business.utilizador.Tecnico;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MainMenu implements Navigatable {
    private final SGRInterface sgr;
    private final Navigator navigator;

    public MainMenu(SGRInterface sgr, Navigator navigator) {
        this.sgr = sgr;
        this.navigator = navigator;
    }

    public Node getScene() {
        VBox vbox = new VBox();
        Label title = new Label("Menu inicial");
        title.setFont(new Font(24.0));
        vbox.getChildren().add(title);
        vbox.setSpacing(5);

        if (sgr.getUtilizadorAutenticado() instanceof Funcionario) {
            Button allClientsButton = new Button("Clientes");
            allClientsButton.setOnAction(e -> navigator.navigateTo(new TodosClientes(sgr, navigator)));

            Button aguardaAprovacaoButton = new Button("Reparações a aguardar aprovação");
            aguardaAprovacaoButton.setOnAction(s -> navigator.navigateTo(new AprovarRejeitarOrcamento(sgr, navigator)));

            Button listaReparacoesButton = new Button("Lista de Reparações");
            listaReparacoesButton.setOnAction(s -> navigator.navigateTo(new TodasReparacoes(sgr, navigator)));

            Button listaReparacoesTabeladasButton = new Button("Serviços Expresso Tabelados");
            listaReparacoesTabeladasButton.setOnAction(e -> navigator.navigateTo(new ReparacoesTabeladas(sgr)));

            Button concluirServicoButton = new Button("Concluir Serviço");
            concluirServicoButton.setOnAction(e -> navigator.navigateTo(new ConcluirServico(sgr, navigator)));

            vbox.getChildren().addAll(allClientsButton, aguardaAprovacaoButton, listaReparacoesButton, listaReparacoesTabeladasButton, concluirServicoButton);
        } else if (sgr.getUtilizadorAutenticado() instanceof Tecnico) {
            Button aguardarOrcamentoButton = new Button("Reparações a aguardar Orçamento");
            aguardarOrcamentoButton.setOnAction(s -> navigator.navigateTo(new AguardarOrcamento(sgr, navigator)));

            Button reparacoesEmCurso = new Button("Reprações em Curso");
            reparacoesEmCurso.setOnAction(s -> navigator.navigateTo(new ReparacoesEmCurso(sgr, navigator)));

            vbox.getChildren().addAll(aguardarOrcamentoButton, reparacoesEmCurso);
        } else if (sgr.getUtilizadorAutenticado() instanceof Gestor) {
            Button allUsersButton = new Button("Utilizadores");
            allUsersButton.setOnAction(e -> navigator.navigateTo(new TodosUtilizadores(sgr, navigator)));
            Button intervencoesTecnicos = new Button("Intervenções Dos Técnicos");
            intervencoesTecnicos.setOnAction(e -> navigator.navigateTo(new EstatisticasIntervencoesTecnicos(sgr)));
            Button estatisticasFuncionarios = new Button("Estatísticas sobre os Funcionários");
            estatisticasFuncionarios.setOnAction(e -> navigator.navigateTo(new EstatisticasFuncionarios(sgr)));
            Button estatisticasDasReparacoesDosTecnicos = new Button("Estatísticas sobre as reparações dos Técnicos");
            estatisticasDasReparacoesDosTecnicos.setOnAction(e -> navigator.navigateTo(new EstatisticasTecnico(sgr)));
            vbox.getChildren().addAll(allUsersButton, intervencoesTecnicos, estatisticasFuncionarios, estatisticasDasReparacoesDosTecnicos);
        }

        return vbox;
    }
}
