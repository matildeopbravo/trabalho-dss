package dss;

public class FichaReparacaoExpresso extends FichaReparacao{
    public FichaReparacaoExpresso(String idCliente, String funcionarioCriador) {
        super(idCliente, funcionarioCriador);
    }

    @Override
    public boolean efetuaReparacao(String id, int custo, int tempo) {
        if(!tecnicosQueRepararam.contains(id))
            tecnicosQueRepararam.add(id);
        return true;
    }
}
