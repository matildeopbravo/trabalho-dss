package dss;

public class FichaReparacaoExpresso extends FichaReparacao{
    private final int idServicoExpresso; // agregacao

    /**
     * @param idCliente Id do cliente
     * @param funcionarioCriador 
     * @param idServicoExpresso id  do Servico expresso realizado
     */
    public FichaReparacaoExpresso(String idCliente, String funcionarioCriador, int idServicoExpresso) {
        super(idCliente, funcionarioCriador);
        this.idServicoExpresso = idServicoExpresso;
    }

    @Override
    public boolean efetuaReparacao(String id, int custo, int tempo) {
        if(!tecnicosQueRepararam.contains(id))
            tecnicosQueRepararam.add(id);
        return true;
    }
}