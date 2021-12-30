package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.reparacao.PassoReparacao;
import dss.business.reparacao.PlanoReparacao;
import dss.gui.components.TabelaPlanoReparacao;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.Duration;
import java.util.ArrayList;

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


        Button newUserButton = new Button("Novo Utilizador");
        newUserButton.setOnAction(e -> navigator.navigateTo(new NovoUtilizador(sgr, navigator)));

        Button allUsersButton = new Button("Mostra Utilizadores");
        allUsersButton.setOnAction(e -> navigator.navigateTo(new TodosUtilizadores(sgr, navigator)));

        Button newClientButton = new Button("Novo cliente");
        newClientButton.setOnAction(e -> navigator.navigateTo(new NovoCliente(sgr, navigator)));

        Button allClientsButton = new Button("Mostra Clientes");
        allClientsButton.setOnAction(e -> navigator.navigateTo(new TodosClientes(sgr, navigator)));

        Button criaReparacaoExpresso = new Button("Cria Reparação Expresso");
        criaReparacaoExpresso.setOnAction(e -> navigator.navigateTo(new NovaReparacaoExpresso(sgr, navigator)));

        Button aguardarOrcamentoButton = new Button("Reparações a aguardar Orçamento");
        aguardarOrcamentoButton.setOnAction(s -> navigator.navigateTo(new AguardarOrcamento(sgr, navigator)));

        Button aguardaAprovacaoButton = new Button("Reparações a aguardar aprovação");
        aguardaAprovacaoButton.setOnAction(s -> navigator.navigateTo(new AprovarRejeitarOrcamento(sgr, navigator)));

        Button reparacoesEmCurso = new Button("Reprações em Curso");
        reparacoesEmCurso.setOnAction(s -> navigator.navigateTo(new ReparacoesEmCurso(sgr, navigator)));

        Button listaReparacoesButton = new Button("Lista de Reparações");
        listaReparacoesButton.setOnAction(s -> navigator.navigateTo(new TodasReparacoes(sgr, navigator)));

        Button intervencoesTecnicos = new Button("Intervenções Dos Técnicos");
        intervencoesTecnicos.setOnAction(e -> navigator.navigateTo(new EstatisticasIntervencoesTecnicos(sgr)));
        Button estatisticasFuncionarios = new Button("Estatísticas sobre os Funcionários");
        estatisticasFuncionarios.setOnAction(e -> navigator.navigateTo(new EstatisticasFuncionarios(sgr)));
        Button estatisticasDasReparacoesDosTecnicos = new Button("Estatísticas sobre as reparações dos Técnicos");
        estatisticasDasReparacoesDosTecnicos.setOnAction(e -> navigator.navigateTo(new EstatisticasTecnico(sgr)));

        Button previewTabelaPasso = new Button("Experimentar tabela de passos");
        previewTabelaPasso.setOnAction(s -> navigator.navigateTo(() -> {
            PlanoReparacao plano = new PlanoReparacao();
            plano.addPasso("Trocar motherboard", Duration.ofMinutes(5), 2, new ArrayList<>());
            PassoReparacao p1 = plano.addPasso("Reparar câmara", Duration.ZERO, 0, new ArrayList<>());
            plano.addSubPasso(p1, "Desmontar ecrã", Duration.ofMinutes(5), 5, new ArrayList<>());
            plano.addSubPasso(p1, "Retirar câmara estragada", Duration.ofMinutes(1), 1, new ArrayList<>());
            PassoReparacao p2 = plano.addSubPasso(p1, "Colocar nova câmara", Duration.ofMinutes(0), 0, new ArrayList<>());
            plano.addSubPasso(p2, "Pegar na câmara", Duration.ofMinutes(1), 1, new ArrayList<>());
            plano.addSubPasso(p2, "Colocar a câmara", Duration.ofMinutes(5), 5, new ArrayList<>());
            TabelaPlanoReparacao tabelaPlanoReparacao = new TabelaPlanoReparacao(true);
            tabelaPlanoReparacao.setPlano(plano);
            return tabelaPlanoReparacao;
        }));

        vbox.getChildren().addAll(newUserButton, allUsersButton, newClientButton, allClientsButton, criaReparacaoExpresso,
                aguardarOrcamentoButton, aguardaAprovacaoButton, reparacoesEmCurso, listaReparacoesButton, previewTabelaPasso,
                estatisticasDasReparacoesDosTecnicos, estatisticasFuncionarios, intervencoesTecnicos);

        return vbox;
    }
}
