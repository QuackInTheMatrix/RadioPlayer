package hr.java.player.iznimke;

public class KorisnikException extends RuntimeException{
    public KorisnikException() {
    }

    public KorisnikException(String message) {
        super(message);
    }

    public KorisnikException(String message, Throwable cause) {
        super(message, cause);
    }

    public KorisnikException(Throwable cause) {
        super(cause);
    }
}
