package AOITServer.Observers;

import AOITServer.JsonClasses.MessageJson;
import AOITServer.Roles;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.Role;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import javalinjwt.JavalinJWT;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


public class AccessManagerJWT implements AccessManager,LoggingSubject,UsernameSubject {
    private String userRoleClaim;
    private Map<String, Roles> rolesMapping;
    private Roles defaultRole;
    private String userUsernameClaim;
    private ArrayList<LoggingObserver> logObservers;
    private String username;

    public AccessManagerJWT(String userUsernameClaim, String userRoleClaim, Map<String, Roles> rolesMapping, Roles defaultRole) {
        this.userUsernameClaim = userUsernameClaim;
        this.userRoleClaim = userRoleClaim;
        this.rolesMapping = rolesMapping;
        this.defaultRole = defaultRole;
        username = "";
        logObservers = new ArrayList<>();
    }


    public synchronized String getUsername() {
        return username;
    }


    public synchronized void manage(Handler handler, Context ctx, Set<Role> permittedRoles)  {
        Roles role = extractRole(ctx);

        username = extractUsername(ctx);

        if (permittedRoles.contains(role)) {
            try {
                handler.handle(ctx);
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(404).json(new MessageJson(false,"Failed accessing URL"));
            }

        } else {
            ctx.status(401).json(new MessageJson(false,"Unauthorized"));

        }

        updateLog("Username:" + username + " Role:" + role + " Path:" + ctx.url());

    }

    //used to extract role from JWT token
    private Roles extractRole(Context context) {
        if (!JavalinJWT.containsJWT(context)) {
            return this.defaultRole;
        } else {
            DecodedJWT jwt = JavalinJWT.getDecodedFromContext(context);
            String userLevel = jwt.getClaim(this.userRoleClaim).asString();



            return Optional.ofNullable(this.rolesMapping.get(userLevel)).orElse(this.defaultRole);
        }
    }

    //Extracts username from JWT token
    private String extractUsername(Context ctx){
        if (!JavalinJWT.containsJWT(ctx)) {
            return "";
        } else {
            DecodedJWT jwt = JavalinJWT.getDecodedFromContext(ctx);
            return jwt.getClaim(this.userUsernameClaim).asString();
        }
    }


    public synchronized void updateLog(String log) {
        for(var x : logObservers){
            x.setLog(log);
        }
    }

    @Override
    public synchronized void addLoggingObserver(LoggingObserver l) {
        logObservers.add(l);
    }

    @Override
    public synchronized void removeLoggingObserver(LoggingObserver l) {
        logObservers.remove(l);
    }
}
