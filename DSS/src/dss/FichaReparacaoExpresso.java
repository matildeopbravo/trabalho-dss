package dss;

public class FichaReparacaoExpresso extends FichaReparacao{
    private final ServicoExpresso servicoExpresso; // agregacao

    /**
     * @param idCliente Id do cliente
     * @param funcionarioCriador 
     * @param servicoExpresso Servico expresso associado à ficha de repar
     */
    public FichaReparacaoExpresso(String idCliente, String funcionarioCriador, ServicoExpresso servicoExpresso) {
        super(idCliente, funcionarioCriador);
        this.servicoExpresso = servicoExpresso;
    }

    @Override
    public boolean efetuaReparacao(String id, int custo, int tempo) {
        if(!tecnicosQueRepararam.contains(id))
            tecnicosQueRepararam.add(id);
        return true;
    }
}