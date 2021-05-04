package AOITServer.Factories;

/**
 * Implementing JWTFactory allows for subclasses to specify how to create JWT token.
 */
public interface JWTFactory {
    /**
     *
     * @param args Claims to be put into JWT token.
     * @return Returns JWT token.
     */
    String createToken(String... args);
}
