package dss.utilizador;

import dss.FichaCliente;
import dss.FichaReparacao;
import dss.FichaReparacaoExpresso;
import dss.FichaReparacaoProgramada;

public class Funcionario extends Utilizador {
    FichaCliente criaFichaCliente(String NIF, String nome, String email, String numeroTelemovel) {
        return new FichaCliente(NIF,nome, email, numeroTelemovel, this.getId());
    }
    FichaReparacaoProgramada criaFichaReparacaoProgramada(String NIFCliente) {
        return new FichaReparacaoProgramada(NIFCliente,this.getId());
    }
    FichaReparacaoExpresso criaFichaReparacaoExpresso(String NIFCliente) {
        return new FichaReparacaoExpresso(NIFCliente, this.getId());

    }
}
