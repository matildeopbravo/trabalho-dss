package dss.fichas;

public class FichaReparacaoExpresso extends FichaReparacao{
    private final int idServicoExpresso; // agregacao
    /**
     * @param idCliente Id do cliente
     * @param utilizadorCriador
     * @param idServicoExpresso id  do Servico expresso realizado
     */
    public FichaReparacaoExpresso( String idCliente, int idServicoExpresso,String utilizadorCriador,String idTecnico ) {
        super(idCliente, utilizadorCriador,idTecnico);
        this.idServicoExpresso = idServicoExpresso;
    }

    // no serviço expresso só sera um
    public String getIdTecnicoReparou() {
        return this.tecnicosQueRepararam.get(0);
    }
}