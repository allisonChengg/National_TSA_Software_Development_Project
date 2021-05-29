package AOITServer.JsonClasses;

/**
 * JWTJson class is a JSON wrapper class used for sending JWT token to client in JSON
 */
public class JWTJson {
    public JWTJson(String jwt){
        JWT = jwt;
    }
    public String JWT;
}
