package dss.business.reparacao;

import dss.business.equipamento.Componente;
import dss.business.equipamento.Equipamento;
import dss.business.equipamento.Fase;
import dss.exceptions.NaoPodeSerReparadoAgoraException;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReparacaoProgramada extends Reparacao implements Serializable {
    private Equipamento equipamentoAReparar;
    private PlanoReparacao planoReparacao = null;
    private Orcamento orcamento;
    private LocalDateTime dataEnvioOrcamento;
    // pausado indica se esta a ser reparado neste preciso momento ou nao
    private boolean pausado;

    public ReparacaoProgramada(String idCliente, String utilizadorCriador, String descricao) {
        super(idCliente, utilizadorCriador, descricao);
        this.equipamentoAReparar = new Equipamento(idCliente, LocalDateTime.now());
        pausado = true;
        this.orcamento = null;
        this.dataEnvioOrcamento = null;
        fase = Fase.AEsperaOrcamento;
    }

    public String getOrcamentoMail(String nome) {
        return "Caro " + nome + ",\n\nO orçamento para a sua reparação está concluído:\n" + orcamento.toString() +
                "\nPor favor responda dentro de 90 dias indicando se aprova ou não deste orçamento.\n\n" +
                "Atenciosamente,\nLoja Reparações do Grupo 54";
    }

    @Override
    public Duration getDuracaoReal() {
        return planoReparacao.getCustoTotalEDuracaoReal().getSecond();
    }

    @Override
    public Duration getDuracaoPrevista() {
        return planoReparacao.getCustoTotalEDuracaoPrevista().getSecond();
    }

    @Override
    public float getCustoTotalPrevisto() {
        return planoReparacao.getCustoTotalEDuracaoPrevista().getFirst();
    }

    @Override
    public float getCustoTotalReal() {
        return planoReparacao.getCustoTotalEDuracaoReal().getFirst();
    }


    @Override
    public List<Intervencao> getIntervencoesRealizadas() {
        return planoReparacao.getPassosReparacaoConcluidos().stream().map(Intervencao.class::cast)
                .collect(Collectors.toList());
    }

    public LocalDateTime getDataEnvioOrcamento() {
        return dataEnvioOrcamento;
    }

    public void realizaOrcamento(String idTecnico) {
        // TODO Verifica que nao pode ser reparado e notififca
        this.tecnicosQueRepararam.add(idTecnico);
        this.orcamento = new Orcamento(planoReparacao);
    }

    public PlanoReparacao criaPlanoReparacao() {
        this.planoReparacao = new PlanoReparacao();
        return this.planoReparacao;
    }

    public void aprovaOrcamento() {
        this.fase = Fase.EmReparacao;
    }

    public void rejeitaOrcamento() {
        this.fase = Fase.Recusada;
    }

    public boolean ultrapassouOrcamento(float custoNovo) {
        //Não pode exceder orçamento aprovado por 120%
        return custoNovo > orcamento.getPreco() * 1.2;
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

    public boolean estaPausado() {
        return pausado;
    }

    public PlanoReparacao getPlanoReparacao() {
        return planoReparacao;
    }

    public Equipamento getEquipamentoAReparar() {
        return equipamentoAReparar;
    }

    public void setDataEnvioOrcamento(LocalDateTime data) {
        this.dataEnvioOrcamento = data;
    }

    public void marcaComoNaoNotificado() {
        this.notificado = false;
    }

    public boolean reparado() {
        return planoReparacao.reparado();
    }
}
