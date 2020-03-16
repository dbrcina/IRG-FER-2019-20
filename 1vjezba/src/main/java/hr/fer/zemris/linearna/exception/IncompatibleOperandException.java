package hr.fer.zemris.linearna.exception;

public class IncompatibleOperandException extends RuntimeException {

    public IncompatibleOperandException() {
    }

    public IncompatibleOperandException(String message) {
        super(message);
    }

}
