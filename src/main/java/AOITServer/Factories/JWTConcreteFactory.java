package AOITServer.Factories;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import javalinjwt.JWTGenerator;
import javalinjwt.JWTProvider;

/**
 * JWTConcreteFactory creates JWT tokens with a username and role claims.
 *
 * <p>JWTConcreteFactory is part of a factory pattern, and also decouples
 * a JWT library for server implementation</p>
 */
public class JWTConcreteFactory implements JWTFactory{
    /**
     * User class is used to store claims the JWT toke will have.
     */
    private class User{

        public User(String username,String role){
            this.username = username;
            this.role = role;
        }
        public String username;
        public String role;
    }

    private JWTGenerator<User> gen;
    private Algorithm alg;
    private JWTVerifier verf;
    private JWTProvider prov;

    /**
     * createToken method takes in 2 parameters, username and role, and generates JWT token with those claims.
     *
     * @param args First arg should be the username and second arg should be the role
     * @return If more then two args function returns empty string, else returns token.
     */
    public String createToken(String... args) {
        if(args.length < 2){
            return "";
        }
        return prov.generateToken(new User(args[0],args[1]));
    }

    /**
     *
     * @param secret Secret is the private key used for the hashing algorithm
     */
    public JWTConcreteFactory(String secret){


        alg = Algorithm.HMAC512(secret);

        gen = (user,alg) ->{
            JWTCreator.Builder token = JWT.create()
                    .withClaim("Username", user.username)
                    .withClaim("Role", user.role);
            return token.sign(alg);
        };

        verf = JWT.require(alg).build();

        prov = new JWTProvider(alg,gen,verf);

    }

    /**
     * @return Returns JWTGenerator(library specific WARNING!) used to generate the claims
     */
    public JWTGenerator getGenerator(){
        return gen;
    }

    /**
     * @return Returns JWTProvider(library specific WARNING!) used to create token
     */
    public JWTProvider getProvider(){
        return prov;
    }
}
