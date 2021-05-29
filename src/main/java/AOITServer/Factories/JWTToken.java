package AOITServer.Factories;
/**
 * JWTToken class is used for constructing tokens with Username and Role
 *
 * <p>JWTToken is used by {@link JWTReflexiveFactory} to construct token out of the public access fields
 * . Uses the field names as claims for JWT token and the values inside the variables as the values of those claims.</p>
 * @see JWTFactory2,JWTReflexiveFactory
 */
public class JWTToken {
    public JWTToken(String username,String role){
        this.Username = username;
        this.Role = role;
    }
    public String Username;
    public String Role;
}
