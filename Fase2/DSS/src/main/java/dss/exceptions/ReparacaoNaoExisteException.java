package dss.exceptions;

public class ReparacaoNaoExisteException extends NaoExisteException{
    public  ReparacaoNaoExisteException() {
        super();
    }
    public ReparacaoNaoExisteException(String erro) {
        super(erro);
    }
}
