package AOITServer.Factories;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import javalinjwt.JWTGenerator;
import javalinjwt.JWTProvider;

/*
    JWTConcreteFactory creates a jwt token that contains a users role and username
 */
public class JWTConcreteFactory implements JWTFactory{
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

    //creates a token with the username and the users role
    public String createToken(String... args) {
        if(args.length < 2){
            return "";
        }
        return prov.generateToken(new User(args[0],args[1]));
    }

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

    public JWTGenerator getGenerator(){
        return gen;
    }

    public JWTProvider getProvider(){
        return prov;
    }
}
