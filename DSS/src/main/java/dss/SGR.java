package dss;

import dss.equipamentos.Componente;
import dss.equipamentos.Equipamento;
import dss.equipamentos.Fase;
import dss.estatisticas.EstatisticasFuncionario;
import dss.estatisticas.EstatisticasTecnico;
import dss.exceptions.*;
import dss.fichas.FichaCliente;
import dss.fichas.FichaReparacao;
import dss.fichas.FichaReparacaoExpresso;
import dss.fichas.FichaReparacaoProgramada;
import dss.utilizador.Funcionario;
import dss.utilizador.Tecnico;
import dss.utilizador.Utilizador;
import dss.utilizador.UtilizadorDAO;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class SGR implements  SGRInterface{
    //####ATRIBUTOS####
    private UtilizadorDAO utilizadores;
    private Utilizador utilizadorAutenticado;

    // equipamentos deixados pelos clientes
    private Map<String, Equipamento> equipamentoById;
    private Map<Integer, Componente> componenteById;


    // servicos expressos
    private Map<Integer,ServicoExpresso> servicoExpresso;
    //// componentes em stock na loja idComponente -> <Componente,Quantidade>
    //private Map<Integer,Pair<Componente,Integer>> componenteById;
    //// componentes reservado na loja descricao -> <Componente,Quantidade>
    //private Map<String,Pair<Componente,Integer>> componenteReservadoById;

    //// componentes em falta na loja descricao -> <Componente,Quantidade>
    //private Map<String,Pair<Componente,Integer>> componentesEmFaltaById;
    private Map<String,FichaCliente> fichaClienteById;

    // fichas de reparacao programadas apenas (expresso nao chegam a ser colocadas em espera)
    private LinkedHashMap<Integer, FichaReparacaoProgramada> fichasReparacaoAtuais;

    private List<FichaReparacaoExpresso> fichasExpressoAtuais;
    // fichas de reparacao programadas ou expresso
    private Map<Integer, FichaReparacao> fichasReparacaoConcluidas;

    private Map<Integer, FichaReparacao> fichasReparacaoExpressoConcluidas;

    //####CONSTRUTOR####

    public SGR() {
        this.utilizadores = new UtilizadorDAO();
        this.utilizadorAutenticado = null;

        this.equipamentoById = new HashMap<>();
        this.componenteById = new HashMap<>();

        this.servicoExpresso = new HashMap<>();
        this.fichaClienteById = new HashMap<>();
        this.fichasReparacaoAtuais = new LinkedHashMap<>();
        this.fichasExpressoAtuais = new ArrayList<>();
        this.fichasReparacaoConcluidas = new HashMap<>();
        this.fichasReparacaoExpressoConcluidas = new HashMap<>();
    }

    //####MÉTODOS####
    @Override
    public Utilizador autenticaUtilizador(String id, String senha) throws UtilizadorNaoExisteException {
        Utilizador utilizador = this.utilizadores.validaCredenciais(id, senha);
        if (utilizador == null)
            throw new UtilizadorNaoExisteException();
        this.utilizadorAutenticado = utilizador;
        return utilizador;
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
        // presuminos que ao adicionr fica concluida
        if(fichasReparacaoConcluidas.containsKey(servicoExpresso.getId()))
            throw new ServicoJaExisteException();

        fichasReparacaoConcluidas.put(servicoExpresso.getId(),servicoExpresso);
    }

    @Override
    public void adicionaServicoProgramado(FichaReparacaoProgramada servicoProgramado) throws ServicoJaExisteException {
        if (fichasReparacaoAtuais.containsKey(servicoProgramado.getId()))
            throw new ServicoJaExisteException();

        fichasReparacaoAtuais.put(servicoProgramado.getId(),servicoProgramado);
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
        if(!this.utilizadores.adicionaUtilizador(utilizador))
            throw new UtilizadorJaExisteException();
    }

    @Override
    public void removeUtilizador(String idUtilizador) throws UtilizadorNaoExisteException {
        if(this.utilizadores.removeUtilizador(idUtilizador) == null)
            throw new UtilizadorNaoExisteException();
    }

    @Override
    public List<Utilizador> getUtilizadores() {
        return this.utilizadores.getUtilizadores();
    }

    @Override
    public List<Tecnico> getTecnicos() {
        return this.utilizadores.getUtilizadores().stream()
                .filter(e-> e instanceof Tecnico)
                .map(Tecnico.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public List<Funcionario> getFuncionarios() {
        return this.utilizadores.getUtilizadores().stream()
                .filter(e-> e instanceof Funcionario)
                .map(Funcionario.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    //TODO nao tem clone
    public List<FichaCliente> getClientes() {
        return new ArrayList<>(fichaClienteById.values());
    }

    @Override
    public Utilizador getUtilizador(String id) throws UtilizadorNaoExisteException {
        Utilizador utilizador = this.utilizadores.getUtilizador(id);
        if (utilizador == null)
            throw new UtilizadorNaoExisteException();
        return utilizador;
    }

    @Override
    public FichaCliente getCliente(String id) {
        return fichaClienteById.get(id);
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
    //CLONE???
    public Equipamento getEquipamento(String codigo) {
        return equipamentoById.get(codigo);
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
            completa = t.efetuaReparacaoProgramada(ficha, custoReal, duracaoReal);
            if(completa) {
                this.marcaReparacaoCompleta(ficha);
            }
        }
    }
    public Tecnico encontraTecnicoDisponivel() throws NaoHaTecnicosDisponiveisException {
        return this.utilizadores.getUtilizadores()
                .stream()
                .filter(user -> user instanceof Tecnico t && !t.estaOcupado())
                .map(Tecnico.class::cast)
                .findFirst()
                .orElseThrow(NaoHaTecnicosDisponiveisException::new);
    }

    // uma reparacao expresso ativa nao podera estar associado ao mesmo tecnico mais que uma vez
    public void concluiReparacaoExpresso(Tecnico t) {
        t.libertaTecnico();
        for (FichaReparacaoExpresso f : fichasExpressoAtuais) {
            if (f.getIdTecnicoReparou().equals(t.getId())) {
                int id = f.getId();
                fichasReparacaoExpressoConcluidas.put(id,f);
                fichasExpressoAtuais.remove(id);
            }
        }
    }
    public void iniciaReparacaoExpresso(Funcionario funcionario , String idCliente, int idReparacaoEfetuar) throws NaoHaTecnicosDisponiveisException {
        Tecnico t = encontraTecnicoDisponivel();
        t.ocupaTecnico();
        FichaReparacaoExpresso ficha = funcionario.criaFichaReparacaoExpresso(t.getId(),idCliente,idReparacaoEfetuar);
        this.fichasExpressoAtuais.add(ficha);
        getCliente(idCliente).addFichaReparacaoConcluida(ficha.getId());
    }

    // devolve todos os componentes que contêm todas as palavras da stringPesquisa na descrição
    public List<Componente> pesquisaComponentes(String stringPesquisa ) {
        List<String> splitted = Arrays.asList(stringPesquisa.split(" "));
        return componenteById
                .values()
                .stream()
                .filter(comp -> List.of(comp.getDescricao()).containsAll(splitted))
                .collect(Collectors.toList());
    }

    // devolve a quantidade existente de um dado componete por descricao
    //public int getQuantidadeComponeteByDescricao(String descricao){
    //    Optional<Pair<Componente,Integer>> componenteIntegerPair =
    //            componenteById
    //                    .values()
    //                    .stream()
    //                    .filter(e-> e.getFirst().getDescricao().equals(descricao))
    //                    .findFirst()
    //            ;
    //    int quantidade = 0;
    //    if (componenteIntegerPair.isPresent())
    //        quantidade = componenteIntegerPair.get().getSecond();
    //    return quantidade;
    //}

    public Optional<Componente> getComponeteByDescricao(String descricao){
        return componenteById
            .values()
            .stream()
            .filter(e-> e.getDescricao().equals(descricao))
            .findFirst();
    }

    //public void reservarEEmFaltaComponete(Componente c, int quantidade){
    //    // vamos ao stock diminuir a quantidade
    //    Optional<Pair<Componente,Integer>> componenteIntegerPair =
    //        componenteById
    //            .values()
    //            .stream()
    //            .filter(e-> e.getFirst().getDescricao().equals(c.getDescricao()))
    //            .findFirst()
    //        ;
    //    Pair<Componente, Integer> p = componenteIntegerPair.orElse(null);
    //    int emFalta = 0;
    //    if (p==null) {
    //        // nao existe nada em stock
    //        emFalta = quantidade;
    //    } else {
    //        int emStock = p.getSecond();
    //        emFalta = quantidade - emStock;
    //        if(emFalta <= 0){
    //            // existe em stock a quantidade toda
    //            p.setY( p.getSecond()- quantidade );
    //            emFalta = 0;
    //        } else {
    //            // nao existe a quantidade toda necessaria
    //            emFalta = Math.abs(emFalta);
    //            p.setY( 0 );
    //        }
    //    }

    //    // vamos reservar
    //    if (emFalta > 0){
    //        Optional<Pair<Componente,Integer>> componenteReservarPair =
    //            componenteReservadoById
    //                .values()
    //                .stream()
    //                .filter(e-> e.getFirst().getDescricao().equals(c.getDescricao()))
    //                .findFirst()
    //            ;
    //        var p2 = componenteReservarPair.orElse(null) ;
    //        if (p2 == null){
    //            // caso nenhum do mesmo tipo já esteja reservado
    //            componenteReservadoById.put(c.getDescricao(), new Pair<>(c,emFalta));
    //        } else {
    //            // senao aumenta-se apenas a quantidade
    //            p2.setY( p2.getSecond() + emFalta);
    //        }
    //    }
    //}

    public static void main(String[] args) throws FileNotFoundException {
        EMail e = new EMail();
        e.enviaMail("pedroalves706@gmail.com", "Test", "Olá Mundo.");
    }
}
