package dss.reparacoes;

import dss.data.IDAO;
import dss.exceptions.ReparacaoNaoExisteException;
import dss.equipamentos.Fase;
import java.util.Collection;
import java.time.Duration;

public interface IReparacoes extends IDAO<Reparacao,Integer> {
    public void adicionaReparacaoProgramadaAtual(ReparacaoProgramada reparacao);
    public void setFase(Integer reparacaoID, Fase fase) throws ReparacaoNaoExisteException ;
    public Collection<Reparacao> getReparacoesConcluidas();
    public Collection<ReparacaoProgramada> getReparacoesProgramadasAtuais();
    public Collection<ReparacaoExpresso> getReparacoesExpressoAtuais();
    public void concluiExpresso(int id, Duration duracaoReal) throws ReparacaoNaoExisteException;
    public void adicionaReparacaoExpressoAtual(ReparacaoExpresso reparacaoExpresso);
    public void arquivaReparacoesDeEquipamento(int idEquipamento);
    public void marcarOrcamentoComoArquivado(ReparacaoProgramada reparacao);
    public void arquivaReparacoesAntigas();
}
