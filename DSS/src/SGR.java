import ClassesUtilizador.Utilizador;

import java.util.LinkedHashMap;
import java.util.Map;

public class SGR {
    private Map<String, Utilizador> utilizadoresById;
    private Map<String,Equipamento> equipamentoById;
    private Map<String,Componente> componenteById;
    private Map<String,FichaCliente> fichaClienteById;
    private LinkedHashMap<String,FichaReparacaoProgramada> fichasReparacaoAtuais;
    private Map<String,FichaReparacao> fichasReparacaoConcluidas;
}
