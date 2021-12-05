package dss;

import dss.estatisticas.EstatisticasFuncionario;
import dss.estatisticas.EstatisticasTecnico;
import dss.exceptions.*;
import dss.utilizador.Funcionario;
import dss.utilizador.Tecnico;
import dss.utilizador.Utilizador;

import java.util.*;
import java.util.stream.Collectors;

public class SGR implements  SGRInterface{
    private Map<String, Utilizador> utilizadoresById;
    // equipamentos deixados pelos clientes
    private Map<String,Equipamento> equipamentoById;
    // componentes em stock na loja idComponente -> <Componente,Quantidade>
    private Map<Integer,Pair<Componente,Integer>> componenteById;

    // componentes em falta na loja descricao -> <Componente,Quantidade>
    private Map<String,Pair<Componente,Integer>> componentesEmFaltaById;

    private Map<String,FichaCliente> fichaClienteById;
    // fichas de reparacao programadas apenas (expresso nao chegam a ser colocadas em espera)
    private LinkedHashMap<Integer,FichaReparacaoProgramada> fichasReparacaoAtuais;
    // fichas de reparacao programadas ou expresso
    private Map<Integer,FichaReparacao> fichasReparacaoConcluidas;

    @Override
    public Utilizador autenticaUtilizador(String id, String senha) throws UtilizadorNaoExisteException {
        return null;
    }

    @Override
    public void adicionaFichaDeCliente(FichaCliente fichaCliente) throws UtilizadorJaExisteException {

    }

    @Override
    public String adicionaEquipamento(Equipamento equipamento) throws EquipamentoJaExisteException {
        return null;
    }

    @Override
    public void adicionaServicoExpresso(FichaReparacaoExpresso servicoExpresso) throws ServicoJaExisteException {

    }

    @Override
    public void adicionaServicoProgramado(FichaReparacaoProgramada servicoProgramado) throws ServicoJaExisteException {
        HashMap<String, Pair<Componente,Integer>> componentes =
                servicoProgramado.planoReparacao.getComponentes();

        for( var entry :componentes.entrySet() ){
            int quantidadeEmStock = getQuantidadeComponeteByDescricao(entry.getKey());
            //int quantidadeNecessaria
        }
        //TODO

    }

    @Override
    public Map<String, EstatisticasTecnico> estatisticasTecnicos() {
        return null;
    }

    @Override
    public Map<String, EstatisticasFuncionario> estatisticasFuncionarios() {
        return null;
    }

    @Override
    public Map<String, Integer> totalIntervencoesPorTecnico() {
        return null;
    }

    @Override
    public void alteraFaseServico(Fase fase, String idFicha) throws ServicoNaoExisteException {

    }

    @Override
    public void registaUtilizador(Utilizador utilizador) throws UtilizadorJaExisteException {

    }

    @Override
    public void removeUtilizador(String idUtilizador) throws UtilizadorNaoExisteException {

    }

    @Override
    public void adicionaComponente(Componente componente) {

    }

    @Override
    public void removeComponente(String codComponente) throws ComponenteNaoExisteException {

    }

    @Override
    public List<Utilizador> getUtilizadores() {
        return null;
    }

    @Override
    public List<Tecnico> getTecnicos() {
        return null;
    }

    @Override
    public List<Funcionario> getFuncionarios() {
        return null;
    }

    @Override
    public List<FichaCliente> getClientes() {
        return null;
    }

    @Override
    public Utilizador getUtilizador(String id) {
        return null;
    }

    @Override
    public FichaCliente getCliente(String id) {
        return null;
    }

    @Override
    public List<Equipamento> getEquipamentos() {
        return null;
    }

    @Override
    public List<Equipamento> getEquipamentosAbandonados() {
        return null;
    }

    @Override
    public Equipamento getEquipamento(String codigo) {
        return null;
    }

    @Override
    public List<FichaReparacao> getReparacoes() {
        return null;
    }

    @Override
    public List<FichaReparacao> getReparacoesCompletadas() {
        return null;
    }

    @Override
    public List<FichaReparacao> getReparacoesEmCurso() {
        return null;
    }

    @Override
    public List<FichaReparacaoProgramada> getReparacoesSemOrcamento() {
        return null;
    }

    @Override
    public FichaReparacao getServico(String id) {
        return null;
    }

    @Override
    public List<FichaReparacaoExpresso> getReparacoesExpresso() {
        return null;
    }

    @Override
    public List<FichaReparacaoProgramada> getReparacoesProgramadas() {
        return null;
    }

    @Override
    public List<Componente> getComponentes() {
        return null;
    }

    @Override
    public Componente getComponente(String id) {
        return null;
    }

    @Override
    public void marcaReparacaoCompleta(FichaReparacao f) {
        if(f instanceof FichaReparacaoProgramada) {
            fichasReparacaoAtuais.remove(f.getId());
        }
        fichasReparacaoConcluidas.put(f.getId(),f);
    }

    public void efetuaReparacaoProgramada(Tecnico t) throws SemReparacoesAEfetuarException {
        // ir buscar ficha de reparacao que esteja em fase propicia a ser reparada
        // e que esteja pausada
        FichaReparacaoProgramada ficha = null;
        boolean completa = false;
        for(FichaReparacaoProgramada f : fichasReparacaoAtuais.values() ) {
            if(f.podeSerReparadaAgora()) {
                ficha = f;
                break;
            }
        }
        if (ficha == null) {
            throw new SemReparacoesAEfetuarException();
        }
        else {
            completa = t.efetuaReparacao(ficha);
            if(completa) {
                this.marcaReparacaoCompleta(ficha);
            }
        }
    }

    // devolve todos os componentes que contêm stringPesquisa na descrição
    // TODO alterar para todas as palavras da string estarem presentes mas nao necessariamente
    //  substring
    public List<Pair<Componente,Integer>> pesquisaComponentes(String stringPesquisa ) {
        return componenteById
                .values()
                .stream()
                .filter(par -> par.getFirst().getDescricao().contains(stringPesquisa))
                .collect(Collectors.toList());
    }

    // devolve a quantidade existente de um dado componete por descricao
    public int getQuantidadeComponeteByDescricao(String descricao){
        Optional<Pair<Componente,Integer>> componenteIntegerPair =
                componenteById
                        .values()
                        .stream()
                        .filter(e-> e.getFirst().getDescricao().equals(descricao))
                        .findFirst()
                ;
        int quantidade = 0;
        if (componenteIntegerPair.isPresent())
            quantidade = componenteIntegerPair.get().getSecond();
        return quantidade;
    }
}
