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
    // componentes reservado na loja descricao -> <Componente,Quantidade>
    private Map<String,Pair<Componente,Integer>> componenteReservadoById;

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
        if(fichaClienteById.containsKey(fichaCliente.getNIF()))
            //throw new UtilizadorJaExisteException("Utilizador ja existente:  "+ fichaCliente.getNome() + " NIF -> "+ fichaCliente.getNIF());
            throw new UtilizadorJaExisteException();
        fichaClienteById.put(fichaCliente.getNIF(), fichaCliente);
    }

    @Override
    public String adicionaEquipamento(Equipamento equipamento) throws EquipamentoJaExisteException {
        //if(equipamentoById.containsKey(equipamento.getIdEquipamento()))
        //    throw new EquipamentoJaExisteException();
        return null;
    }

    @Override
    public void adicionaServicoExpresso(FichaReparacaoExpresso servicoExpresso) throws ServicoJaExisteException {

    }

    @Override
    public void adicionaServicoProgramado(FichaReparacaoProgramada servicoProgramado) throws ServicoJaExisteException {
        // TODO Falta verificar se o servico ja existe

        HashMap<String, Pair<Componente,Integer>> componentes =
                servicoProgramado.planoReparacao.getComponentes();
        for( var entry :componentes.entrySet() ){
            Pair<Componente,Integer> c = entry.getValue();
            reservarEEmFaltaComponete(c.getFirst(),c.getSecond());
        }
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


    public FichaReparacaoProgramada obtemReparacaoProgramadaDisponivel(Tecnico t) {
        // ir buscar ficha de reparacao que esteja em fase propicia a ser reparada
        // e que esteja pausada
        for(FichaReparacaoProgramada f : fichasReparacaoAtuais.values() ) {
            if(f.podeSerReparadaAgora())
                return f;
        }
        return null;
    }
    public void efetuaReparacaoProgramada(Tecnico t, FichaReparacaoProgramada ficha, int custoReal, int duracaoReal)
            throws SemReparacoesAEfetuarException {
        boolean completa = false;
        if (ficha == null) {
            throw new SemReparacoesAEfetuarException();
        }
        else {
            completa = t.efetuaReparacao(ficha, custoReal, duracaoReal);
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

    public Pair<Componente,Integer> getComponeteByDescricao(String descricao){
        Optional<Pair<Componente,Integer>> componenteIntegerPair =
            componenteById
                .values()
                .stream()
                .filter(e-> e.getFirst().getDescricao().equals(descricao))
                .findFirst()
            ;
        return componenteIntegerPair.orElse(null);
    }

    public void reservarEEmFaltaComponete(Componente c, int quantidade){
        // vamos ao stock diminuir a quantidade
        Optional<Pair<Componente,Integer>> componenteIntegerPair =
            componenteById
                .values()
                .stream()
                .filter(e-> e.getFirst().getDescricao().equals(c.getDescricao()))
                .findFirst()
            ;
        Pair<Componente, Integer> p = componenteIntegerPair.orElse(null);
        int emFalta = 0;
        if (p==null) {
            // nao existe nada em stock
            emFalta = quantidade;
        } else {
            int emStock = p.getSecond();
            emFalta = quantidade - emStock;
            if(emFalta <= 0){
                // existe em stock a quantidade toda
                p.setY( p.getSecond()- quantidade );
                emFalta = 0;
            } else {
                // nao existe a quantidade toda necessaria
                emFalta = Math.abs(emFalta);
                p.setY( 0 );
            }
        }

        // vamos reservar
        if (emFalta > 0){
            Optional<Pair<Componente,Integer>> componenteReservarPair =
                componenteReservadoById
                    .values()
                    .stream()
                    .filter(e-> e.getFirst().getDescricao().equals(c.getDescricao()))
                    .findFirst()
                ;
            var p2 = componenteReservarPair.orElse(null) ;
            if (p2 == null){
                // caso nenhum do mesmo tipo já esteja reservado
                componenteReservadoById.put(c.getDescricao(), new Pair<>(c,emFalta));
            } else {
                // senao aumenta-se apenas a quantidade
                p2.setY( p2.getSecond() + emFalta);
            }
        }
    }
}
