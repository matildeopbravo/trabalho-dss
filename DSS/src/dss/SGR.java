package dss;

import dss.utilizador.Utilizador;

import java.util.LinkedHashMap;
import java.util.Map;

public class SGR {
    private Map<String, Utilizador> utilizadoresById;
    // equipamentos deixados pelos clientes
    private Map<String,Equipamento> equipamentoById;
    // componentes em stock na loja
    private Map<String,Componente> componenteById;
    private Map<String,FichaCliente> fichaClienteById;
    // fichas de reparacao programadas apenas (expresso nao chegam a ser colocadas em espera)
    private LinkedHashMap<String,FichaReparacaoProgramada> fichasReparacaoAtuais;
    // fichas de reparacao programadas ou expresso
    private Map<String,FichaReparacao> fichasReparacaoConcluidas;
}
