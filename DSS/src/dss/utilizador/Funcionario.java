package dss.utilizador;

import dss.FichaCliente;
import dss.FichaReparacao;
import dss.FichaReparacaoExpresso;
import dss.FichaReparacaoProgramada;

public class Funcionario extends Utilizador {
    public FichaCliente criaFichaCliente(String NIF, String nome, String email, String numeroTelemovel) {
        return new FichaCliente(NIF,nome, email, numeroTelemovel, this.getId());
    }
    public FichaReparacaoProgramada criaFichaReparacaoProgramada(String NIFCliente) {
        return new FichaReparacaoProgramada(NIFCliente,this.getId());
    }
    public FichaReparacaoExpresso criaFichaReparacaoExpresso(String NIFCliente, int idServicoExpresso) {
        return new FichaReparacaoExpresso(NIFCliente, this.getId(),idServicoExpresso);

    }
}
