package util;

/**
 * Created by Zeno Yuki on 9/20/2016.
 * Extends Exception class to allow for custom exception message when enqueued model is null
 */
public class NullEnqueuedException extends Exception {
    /**
     * CONSTRUCTORS
     */
    /** Instantiates class with simple string message and detailed throwable cause */
    public NullEnqueuedException(String message, Throwable cause) {super(message, cause);}
}