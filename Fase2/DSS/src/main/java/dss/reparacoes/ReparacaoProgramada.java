package dss.reparacoes;

import dss.Intervencao;
import dss.equipamentos.Equipamento;
import dss.equipamentos.Fase;
import dss.Orcamento;
import dss.PlanoReparacao;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class ReparacaoProgramada extends Reparacao {
    Equipamento equipamentoAReparar;
    PlanoReparacao planoReparacao;
    Orcamento orcamento;
    // pausado indica se esta a ser reparado neste preciso momento ou nao
    boolean pausado ;

    public ReparacaoProgramada(String idCliente, String utilizadorCriador) {
        super(idCliente, utilizadorCriador);
        this.equipamentoAReparar = new Equipamento(idCliente);
        pausado = true;
        this.orcamento = null;
        fase = Fase.AEsperaOrcamento;
    }

    public String getOrcamentoMail(String nome) {
        return "Caro " + nome + ",\n" + orcamento.toString() + "\nAtenciosamente,\n Loja Reparações";
    }

    @Override
    public List<Intervencao> getIntervencoesRealizadas() {
        return planoReparacao.getPassosReparacaoConcluidos().stream().map(Intervencao.class::cast).collect(Collectors.toList());
    }

    // marca como realizado um passo ou subpasso, indicando o custo e o tempo que gastou na realidade
    public boolean efetuaReparacao(String id, int custoReal, Duration tempoReal) {
        if(!tecnicosQueRepararam.contains(id))
            tecnicosQueRepararam.add(id);
        return this.planoReparacao.repara(custoReal, tempoReal);
    }

    public void realizaOrcamento(String id) {
        this.tecnicosQueRepararam.add(id);
        this.orcamento = new Orcamento(planoReparacao);
    }

    public void notificaOrcamento() {


        this.fase = Fase.AEsperaResposta;
    }

    public void aprovaOrcamento() {
        this.fase = Fase.EmReparacao;
    }

    public void rejeitaOrcamento() {
        this.fase = Fase.Recusada;
    }

    public boolean ultrapassaOrcamento() {
        boolean ultrapassa = false;
        if (this.orcamento != null) {
            Orcamento orcamentoReal = new Orcamento(planoReparacao);
            //Não pode exceder orçamento aprovado por 120%
            if (orcamentoReal.getPreco() > this.orcamento.getPreco()*1.2)
                ultrapassa = true;
        }
        return ultrapassa;
    }

    public void togglePausarReparacao() {
        pausado = !pausado;
    }

    public boolean podeSerReparadaAgora() {
        return fase.equals(Fase.EmReparacao) && pausado;
    }

    public Fase getFase() {
        return fase;
    }

}
