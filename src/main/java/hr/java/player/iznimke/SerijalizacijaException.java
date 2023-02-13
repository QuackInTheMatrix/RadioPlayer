package hr.java.player.iznimke;

public class SerijalizacijaException extends RuntimeException{
    public SerijalizacijaException() {
    }

    public SerijalizacijaException(String message) {
        super(message);
    }

    public SerijalizacijaException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerijalizacijaException(Throwable cause) {
        super(cause);
    }
}
