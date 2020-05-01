package hr.fer.zemris.irg.linearna.exception;

public class IncompatibleOperandException extends RuntimeException {

    public IncompatibleOperandException() {
    }

    public IncompatibleOperandException(String message) {
        super(message);
    }

}
