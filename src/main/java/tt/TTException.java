package tt;

/** Represents an expected error while processing a user command. */
public class TTException extends Exception {
    /** Creates an exception with a user-facing explanation. */
    public TTException(String message) {
        super(message);
    }

}
