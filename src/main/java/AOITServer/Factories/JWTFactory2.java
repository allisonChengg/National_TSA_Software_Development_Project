package AOITServer.Factories;

/**
 *JWTFactory2 is responsible for creating a common interface for
 * creating JWT tokens.
 *
 * @param <T> Type of object that contains JWT token claims
 */
public interface JWTFactory2<T> {
    /**
     *
     * @param obj Object whose public fields will be extracted and put as JWT token claims
     * @return Returns JWT token.
     */
    String createToken(T obj);
}
