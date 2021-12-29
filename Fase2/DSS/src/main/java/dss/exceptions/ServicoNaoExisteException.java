package dss.exceptions;

public class ServicoNaoExisteException extends NaoExisteException {
    public ServicoNaoExisteException(String erro) {
        super(erro);
    }
}
