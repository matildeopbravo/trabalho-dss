package dss.exceptions;

public class NaoExisteException extends Exception{
    public NaoExisteException() {

    }
    public NaoExisteException(String erro) {
        super(erro);
    }
}
