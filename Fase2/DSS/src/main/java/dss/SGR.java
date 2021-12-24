package dss;


import dss.equipamentos.*;
import dss.estatisticas.*;
import dss.exceptions.*;
import dss.reparacoes.*;
import dss.utilizador.*;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SGR implements SGRInterface {
    //####ATRIBUTOS####
    private final UtilizadorDAO utilizadores;
    private Utilizador utilizadorAutenticado;

    // equipamentos deixados pelos clientes
    private final Map<String, Equipamento> equipamentoById;
    private final Map<Integer, Componente> componenteById;


    // servicos expressos
    private final Map<Integer, ServicoExpressoTabelado> servicoExpresso;
    //// componentes em stock na loja idComponente -> <Componente,Quantidade>
    //private Map<Integer,Pair<Componente,Integer>> componenteById;
    //// componentes reservado na loja descricao -> <Componente,Quantidade>
    //private Map<String,Pair<Componente,Integer>> componenteReservadoById;

    //// componentes em falta na loja descricao -> <Componente,Quantidade>
    //private Map<String,Pair<Componente,Integer>> componentesEmFaltaById;
    private final Map<String, Cliente> clienteById;

    // reparacoes programadas apenas (expresso nao chegam a ser colocadas em espera)
    private final LinkedHashMap<Integer, ReparacaoProgramada> reparacoesProgramadasAtuais;

    private final List<ReparacaoExpresso> expressoAtuais;
    // fichas de reparacao programadas ou expresso
    private final Map<Integer, Reparacao> reparacoesConcluidas;
    Email email ;

    //####CONSTRUTOR####

    public SGR() throws FileNotFoundException {
        this.utilizadores = new UtilizadorDAO();
        this.utilizadorAutenticado = null;

        this.equipamentoById = new HashMap<>();
        this.componenteById = new HashMap<>();

        this.servicoExpresso = new HashMap<>();
        this.clienteById = new HashMap<>();
        this.reparacoesProgramadasAtuais = new LinkedHashMap<>();
        this.expressoAtuais = new ArrayList<>();
        this.reparacoesConcluidas = new HashMap<>();
        this.email = new Email();
    }

    //####MÉTODOS####
    public void autenticaUtilizador(String id, String senha) {
        try {
            this.utilizadorAutenticado = this.utilizadores.validaCredenciais(id, senha);
            System.out.println("Utilizador autenticado com sucesso:" + utilizadores.getUtilizador(id));
        } catch (CredenciasInvalidasException e) {
            e.printStackTrace();
        }
    }

    public void criaCliente(String NIF, String nome, String email, String numeroTelemovel,
                                 String funcionarioCriador) throws UtilizadorJaExisteException {

        Cliente cliente = new Cliente(NIF,nome,email,numeroTelemovel,funcionarioCriador);

        if (clienteById.containsKey(cliente.getNIF()))
            throw new UtilizadorJaExisteException();
        clienteById.put(cliente.getNIF(), cliente);
    }

    public void criaFichaReparacaoProgramada(String NIFCliente) {
        ReparacaoProgramada f = new ReparacaoProgramada(NIFCliente,utilizadorAutenticado.getId());
        f.setFase(Fase.AEsperaOrcamento);
        reparacoesProgramadasAtuais.put(f.getId(),f);
    }

   public void marcaOrcamentoComoAceite(ReparacaoProgramada r)  {
        r.setFase(Fase.EmReparacao);
   }

   public void marcaOrcamentoComoRecusado(ReparacaoProgramada r) {
        r.setFase(Fase.Recusada);
        int id = r.getId();
        reparacoesProgramadasAtuais.remove(id);
        reparacoesConcluidas.put(id,r);
   }

    public void realizaOrcamento(ReparacaoProgramada ficha) {
        ficha.realizaOrcamento(utilizadorAutenticado.getId());
        Cliente c = clienteById.get(ficha.getIdCliente());
        email.enviaMail(c.getEmail(), "Orçamento",
                ficha.getOrcamentoMail(c.getNome()));;
    }

    public void togglePausaReparacao(ReparacaoProgramada ficha) {
        ficha.togglePausarReparacao();
    }

    @Override
    public String adicionaEquipamento(Equipamento equipamento) throws EquipamentoJaExisteException {
        // TODO
        //if(equipamentoById.containsKey(equipamento.getIdEquipamento()))
        //    throw new EquipamentoJaExisteException();
        return null;
    }

    public Stream<Tecnico> getStreamTecnicos() {
        return utilizadores
                .getUtilizadores()
                .stream()
                .filter(t-> t instanceof Tecnico)
                .map(Tecnico.class::cast);
    }
    public Stream<Funcionario> getStreamFuncionarios() {
        return utilizadores
                .getUtilizadores()
                .stream()
                .filter(t-> t instanceof Funcionario)
                .map(Funcionario.class::cast);
    }

    public Map<String,EstatisticasReparacoesTecnico> estatisticasReparacoesTecnicos () {
        return getStreamTecnicos()
                .collect(Collectors.toMap(Tecnico::getId, this::estatisticasReparacoesByTecnico));
    }

    private EstatisticasReparacoesTecnico estatisticasReparacoesByTecnico(Tecnico t) {
        Stream<Reparacao> reparacoes =
                reparacoesConcluidas.values()
                        .stream()
                        .filter(r -> r.getTecnicosQueRepararam().contains(t.getId()));

        Stream<Reparacao> reparacoesProgramadas =
                reparacoes
                        .filter(r -> r instanceof ReparacaoProgramada);

        int numReparacoesProgramadas = (int)
                reparacoesProgramadas
                .count();
        int numReparacoesExpresso = (int) reparacoes
                .filter(r -> r instanceof ReparacaoExpresso)
                .count();
        //duracaoMediaDasReparacosProgramadas
        double duracaoMedia = reparacoesProgramadas
                .map(Reparacao::getDuracaoReal)
                .map(Duration::toSeconds)
                .mapToLong(Long::longValue)
                .average()
                .orElse(Double.NaN);

        double desvioMedio = reparacoesProgramadas
                .map(r -> Math.abs(r.getDuracaoPrevista().getSeconds() - r.getDuracaoReal().getSeconds()))
                .mapToDouble(Long::doubleValue)
                .average()
                .orElse(Double.NaN);

        return new EstatisticasReparacoesTecnico(numReparacoesExpresso,numReparacoesProgramadas, duracaoMedia, desvioMedio);
    }

    public Map<String, List<Intervencao>> intervencoesTecnicos() {
        return getStreamTecnicos()
                .collect(Collectors.toMap(Tecnico::getId, this::getIntervencoesByTecnico));
    }

    private List<Intervencao> getIntervencoesByTecnico(Tecnico t) {
        return Stream.concat(reparacoesConcluidas.values().stream()
                        , reparacoesProgramadasAtuais.values().stream())
                .filter(r -> r.getTecnicosQueRepararam().contains(t.getId()))
                .map(Reparacao::getIntervencoesRealizadas)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, EstatisticasFuncionario> estatisticasFuncionarios() {
        return getStreamFuncionarios()
                .collect(Collectors.toMap(Funcionario::getId,
                        f -> new EstatisticasFuncionario(getNumRececoes(f), getNumEntregas(f))));
    }

    private int getNumRececoes(Funcionario f) {
        ArrayList<Reparacao> l = new ArrayList<>(reparacoesProgramadasAtuais.values());
        l.addAll(expressoAtuais);
        l.addAll(reparacoesConcluidas.values());
        return (int)
                l.stream()
                .filter(ficha -> ficha.getFuncionarioCriador().equals(f.getId()))
                .count();
    }

    private int getNumEntregas(Funcionario f) {
        return (int) reparacoesConcluidas.values()
                .stream()
                .filter(ficha -> ficha.getFuncionarioEntregou().equals(f.getId()))
                .count();
    }

    @Override
    public void registaUtilizador(Utilizador utilizador) throws UtilizadorJaExisteException {
        if (!this.utilizadores.adicionaUtilizador(utilizador))
            throw new UtilizadorJaExisteException();
    }

    @Override
    public void removeUtilizador(String idUtilizador) throws UtilizadorNaoExisteException {
        if (this.utilizadores.removeUtilizador(idUtilizador) == null)
            throw new UtilizadorNaoExisteException();
    }

    @Override
    public List<Utilizador> getUtilizadores() {
        return this.utilizadores.getUtilizadores();
    }

    @Override
    public List<Tecnico> getTecnicos() {
        return getStreamTecnicos().collect(Collectors.toList());
    }

    @Override
    public List<Funcionario> getFuncionarios() {
        return this.utilizadores.getUtilizadores().stream()
                .filter(e -> e instanceof Funcionario)
                .map(Funcionario.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    //TODO nao tem clone
    public List<Cliente> getClientes() {
        return new ArrayList<>(clienteById.values());
    }

    @Override
    public Utilizador getUtilizador(String id) throws UtilizadorNaoExisteException {
        Utilizador utilizador = this.utilizadores.getUtilizador(id);
        if (utilizador == null)
            throw new UtilizadorNaoExisteException();
        return utilizador;
    }

    @Override
    public Cliente getCliente(String id) {
        return clienteById.get(id);
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
    public List<Reparacao> getReparacoes() {
        return null;
    }

    @Override
    public List<Reparacao> getReparacoesCompletadas() {
        return null;
    }

    @Override
    public List<Reparacao> getReparacoesEmCurso() {
        return null;
    }


    @Override
    public Reparacao getServico(String id) {
        return null;
    }

    @Override
    public List<ReparacaoExpresso> getReparacoesExpresso() {
        return null;
    }

    @Override
    public List<ReparacaoProgramada> getReparacoesProgramadas() {
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
    public void marcaReparacaoCompleta(Reparacao f) {
        if (f instanceof ReparacaoProgramada) {
            reparacoesProgramadasAtuais.remove(f.getId());
        }
        reparacoesConcluidas.put(f.getId(), f);
        f.setFase(Fase.Reparado);
    }

    public void marcaReparacaoEntregue(Reparacao f) {
        f.setFase(Fase.Entregue);
        // poderá nao ser funcionario?
        f.setFuncionarioEntregou(utilizadorAutenticado.getId());
    }


    public ReparacaoProgramada obtemReparacaoProgramadaDisponivel(Tecnico t) {
        // ir buscar ficha de reparacao que esteja em fase propicia a ser reparada
        // e que esteja pausada
        for (ReparacaoProgramada f : reparacoesProgramadasAtuais.values()) {
            if (f.podeSerReparadaAgora())
                return f;
        }
        return null;
    }
    // executa Passo ou subpasso se programada ; executa a reparacao toda se for expresso
    public void efetuaReparacaoProgramada(ReparacaoProgramada ficha, int custoReal, Duration duracaoReal) {
        try {
            if(ficha.ultrapassouOrcamento()) {
                Cliente c = clienteById.get(ficha.getIdCliente());
                enviaMailOrcamentoUltrapassado(c);
                ficha.setFase(Fase.AEsperaResposta);
            }
            else {
                boolean completa = ficha.efetuaReparacao(utilizadorAutenticado.getId(), custoReal, duracaoReal);
                if (completa) {
                    this.marcaReparacaoCompleta(ficha);
                    enviaMailReparacaoConcluida(clienteById.get(ficha.getIdCliente()));
                }
            }
        }
        catch (NaoPodeSerReparadoAgoraException e) {
            e.printStackTrace();
        }
    }

    private void enviaMailReparacaoConcluida(Cliente cliente) {
        email.enviaMail(cliente.getEmail(),"Reparacao Concluida", "Caro " + cliente.getNome() +
                " a sua encomenda está completa\n");
    }

    private void marcaComoEntregue(Reparacao r){
        r.marcaComoEntregue(utilizadorAutenticado.getId());
    }

    private void enviaMailOrcamentoUltrapassado(Cliente c) {
        email.enviaMail(c.getEmail(), "Orçamento Ultrapassado", "Caro " + c.getNome() +
                ",\n O Orçamento Foi ultrapassado\n Atenciosamente, Centro de Reparações");
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
        for (ReparacaoExpresso f : expressoAtuais) {
            if (f.getIdTecnicoReparou().equals(t.getId())) {
                int id = f.getId();
                reparacoesConcluidas.put(id, f);
                expressoAtuais.remove(id);
                return;
            }
        }
    }
    public void iniciaReparacaoExpresso(Funcionario funcionario, String idCliente, int idReparacaoEfetuar) throws NaoHaTecnicosDisponiveisException {
        Tecnico t;
        if(utilizadorAutenticado instanceof Tecnico) {
            t = (Tecnico)  utilizadorAutenticado;
        }
        t = encontraTecnicoDisponivel();
        t.ocupaTecnico();
        ReparacaoExpresso reparacaoExpresso = new ReparacaoExpresso(servicoExpresso.get(idReparacaoEfetuar),
                idCliente,utilizadorAutenticado.getId(),utilizadorAutenticado.getId());
        this.expressoAtuais.add(reparacaoExpresso);
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

    // devolve todos os componentes que contêm todas as palavras da stringPesquisa na descrição
    public List<Componente> pesquisaComponentes(String stringPesquisa) {
        List<String> splitted = Arrays.asList(stringPesquisa.split(" "));
        return componenteById
                .values()
                .stream()
                .filter(comp -> List.of(comp.getDescricao()).containsAll(splitted))
                .collect(Collectors.toList());
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
    //                 c
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

    public Optional<Componente> getComponeteByDescricao(String descricao) {
        return componenteById
                .values()
                .stream()
                .filter(e -> e.getDescricao().equals(descricao))
                .findFirst();
    }

    public List<ReparacaoProgramada> getReparacoesAguardarOrcamento() {
        return this.reparacoesProgramadasAtuais
                .values()
                .stream()
                .filter(ReparacaoProgramada::estaPausado) // para garantir que nenhum tecnico esta a reparar
                .filter( r -> r.getFase().equals(Fase.AEsperaOrcamento))
                .collect(Collectors.toList());
    }
}