package dss.utilizador;

import dss.fichas.FichaCliente;
import dss.fichas.FichaReparacaoExpresso;
import dss.fichas.FichaReparacaoProgramada;

public class Funcionario extends Utilizador {
    public Funcionario(String nome, String id, String password) {
        super(nome, id, password);
    }

    public FichaCliente criaFichaCliente(String NIF, String nome, String email, String numeroTelemovel) {
        return new FichaCliente(NIF,nome, email, numeroTelemovel, this.getId());
    }
    public FichaReparacaoProgramada criaFichaReparacaoProgramada(String NIFCliente) {
        return new FichaReparacaoProgramada(NIFCliente,this.getId());
    }
    public FichaReparacaoExpresso criaFichaReparacaoExpresso(String idTecnico, String NIFCliente, int idServicoExpresso) {
        return new FichaReparacaoExpresso(NIFCliente,idServicoExpresso,this.getId(),idTecnico);

    }
}
