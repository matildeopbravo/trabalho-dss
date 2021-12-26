package dss.reparacoes;

import dss.Intervencao;
import dss.equipamentos.Equipamento;
import dss.equipamentos.Fase;
import dss.Orcamento;
import dss.PlanoReparacao;
import dss.exceptions.NaoPodeSerReparadoAgoraException;
import dss.exceptions.NaoPodeSerReparadoException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReparacaoProgramada extends Reparacao {
    private Equipamento equipamentoAReparar;
    private PlanoReparacao planoReparacao = null;
    private Orcamento orcamento;
    private LocalDateTime dataEnvioOrcamento;
    // pausado indica se esta a ser reparado neste preciso momento ou nao
    private boolean pausado;

    public ReparacaoProgramada(String idCliente, String utilizadorCriador) {
        super(idCliente, utilizadorCriador);
        this.equipamentoAReparar = new Equipamento(idCliente, LocalDateTime.now());
        pausado = true;
        this.orcamento = null;
        this.dataEnvioOrcamento = null;
        fase = Fase.AEsperaOrcamento;
    }

    public String getOrcamentoMail(String nome) {
        return "Caro " + nome + ",\n" + orcamento.toString() + "\nAtenciosamente,\n Loja Reparações";
    }

    @Override
    public List<Intervencao> getIntervencoesRealizadas() {
        return planoReparacao.getPassosReparacaoConcluidos().stream().map(Intervencao.class::cast)
                .collect(Collectors.toList());
    }

    public LocalDateTime getDataEnvioOrcamento() {
        return dataEnvioOrcamento;
    }

    public boolean podeSerReparadoAgora() {
        return fase.equals(Fase.EmReparacao) && pausado;
    }

    // marca como realizado um passo ou subpasso, indicando o custo e o tempo que gastou na realidade
    public boolean efetuaReparacao(String id, int custoReal, Duration tempoReal) throws NaoPodeSerReparadoAgoraException {
        if (!this.podeSerReparadoAgora()) {
            throw new NaoPodeSerReparadoAgoraException();
        }
        if (!tecnicosQueRepararam.contains(id))
            tecnicosQueRepararam.add(id);

        return this.planoReparacao.repara(custoReal, tempoReal);
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

    public boolean ultrapassouOrcamento(float precoReal) {
        //Não pode exceder orçamento aprovado por 120%
        return precoReal > orcamento.getPreco() * 1.2;
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
}
