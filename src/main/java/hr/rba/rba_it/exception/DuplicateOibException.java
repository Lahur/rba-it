package hr.rba.rba_it.exception;

public class DuplicateOibException extends RuntimeException {

    public DuplicateOibException(String message) {
        super(message);
    }
}