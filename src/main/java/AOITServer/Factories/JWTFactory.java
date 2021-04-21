package AOITServer.Factories;
//JWTFactory interface is used to create JWT tokens with arg parameters
public interface JWTFactory {
    String createToken(String... args);
}
