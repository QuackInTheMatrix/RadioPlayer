package hr.java.player.iznimke;

public class DatotekaException extends Exception{
    public DatotekaException() {
    }

    public DatotekaException(String message) {
        super(message);
    }

    public DatotekaException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatotekaException(Throwable cause) {
        super(cause);
    }
}
