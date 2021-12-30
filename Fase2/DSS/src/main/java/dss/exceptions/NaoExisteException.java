package dss.exceptions;

import java.util.function.Supplier;

public class NaoExisteException extends Exception {
    public NaoExisteException() {

    }
    public NaoExisteException(String erro) {
        super(erro);
    }
}
