package AOITServer.Factories;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import javalinjwt.JWTGenerator;
import javalinjwt.JWTProvider;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * JWTReflexiveFactory reads public fields from class and adds them as claims inside the JWT token.
 *
 * <p>Public fields should only be of type String,Integer,Date or Boolean. Each field of the JWT
 * token will have the name of the field and the value of the field as the value for the claim.
 * JWTReflexiveFactory is part of a Factory pattern</p>
 *
 * @param <T> Type of object that contains JWT token claims.
 * @see AOITServer.Factories.JWTFactory2
 */
public class JWTReflexiveFactory<T> implements JWTFactory2<T>{


    private JWTGenerator<T> gen;
    private Algorithm alg;
    private JWTVerifier verf;
    private JWTProvider prov;

    /**
     *
     * @param secret private key used for hashing algorithm.
     */
    public JWTReflexiveFactory(String secret){
        alg = Algorithm.HMAC512(secret);
        verf = JWT.require(alg).build();

        gen = (tokenClaims,alg) ->{
            JWTCreator.Builder token = JWT.create();

            Field[] fields = tokenClaims.getClass().getFields();

            for(Field f : fields){
                Object o = null;
                try {
                    o = f.get(tokenClaims);
                    if(o instanceof String) {
                        token.withClaim(f.getName(), (String) o);
                    }
                    else if(o instanceof Integer){
                        token.withClaim(f.getName(), (Integer) o);
                    }
                    else if(o instanceof Date){
                        token.withClaim(f.getName(),(Date) o);
                    }
                    else if(o instanceof Boolean){
                        token.withClaim(f.getName(),(Boolean) o);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }

            return token.sign(alg);
        };

        prov = new JWTProvider(alg,gen,verf);
    }


    /**
     * Creates JWT token.
     * @param obj Object whose public fields will be extracted and put as JWT token claims
     * @return Returns JWT token.
     */
    @Override
    public String createToken(T obj) {

        return prov.generateToken(obj);
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
