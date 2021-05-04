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

/**
 * AccessManagerJWT class is used to authorize all clients that connect to server.
 *
 * <p>AccessManagerJWT is part of two observer patterns and an adapter pattern.
 * AccessManagerJWT is responsible for pushing new logs to {@link ServerLogging} for them to be inserted into logging table.
 * AccessManagerJWT is responsible for updating username to all {@link UsernameObserver} that registered.
 * AccessManager is responsible for checking if clients have correct permissions to access certain resources.
 * Warning this class may be monolithic. This is caused due to javalin library.</p>
 * @see LoggingSubject,UsernameSubject,AccessManager
 */
public class AccessManagerJWT implements AccessManager,LoggingSubject,UsernameSubject {
    private String userRoleClaim;
    private Map<String, Roles> rolesMapping;
    private Roles defaultRole;
    private String userUsernameClaim;
    private ArrayList<LoggingObserver> logObservers;
    private String username;

    /**
     *
     * @param userUsernameClaim The name of the claim that contains the username of client in JWT token.
     * @param userRoleClaim The name of the claim that contains the role of client in JWT token.
     * @param rolesMapping All the roles that the client can possibly be.
     * @param defaultRole The role the client gets if he is not authorized.
     */
    public AccessManagerJWT(String userUsernameClaim, String userRoleClaim, Map<String, Roles> rolesMapping, Roles defaultRole) {
        this.userUsernameClaim = userUsernameClaim;
        this.userRoleClaim = userRoleClaim;
        this.rolesMapping = rolesMapping;
        this.defaultRole = defaultRole;
        username = "";
        logObservers = new ArrayList<>();
    }

    /**
     * getUsername is used to get username of current client
     * @return Returns username of current client.
     */
    public synchronized String getUsername() {
        return username;
    }

    /**
     * Is responsible for managing authorization,updating logs and username.
     * @param handler Maybe the url handler?
     * @param ctx ? Probably the current context?
     * @param permittedRoles ? Roles permitted to be used?
     */
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

        updateLog("Username:" + username + " Role:" + role + " Path:" + ctx.url() + "IP:" + ctx.ip());

    }

    /**
     *extractRole method extracts role from JWT token.
     *
     * @param context The context used for getting role from JWT token.
     * @return Returns default role if no JWT token, else returns role of client.
     */
    private Roles extractRole(Context context) {
        if (!JavalinJWT.containsJWT(context)) {
            return this.defaultRole;
        } else {
            DecodedJWT jwt = JavalinJWT.getDecodedFromContext(context);
            String userLevel = jwt.getClaim(this.userRoleClaim).asString();

            return Optional.ofNullable(this.rolesMapping.get(userLevel)).orElse(this.defaultRole);
        }
    }

    /**
     *extractUsername extracts username from JWT token.
     *
     * @param ctx The context used for getting username from JWT token.
     * @return Returns "" if no JWT token found,else returns username from token.
     */
    private String extractUsername(Context ctx){
        if (!JavalinJWT.containsJWT(ctx)) {
            return "";
        } else {
            DecodedJWT jwt = JavalinJWT.getDecodedFromContext(ctx);
            return jwt.getClaim(this.userUsernameClaim).asString();
        }
    }

    /**
     * Pushes log ot all registered {@link LoggingObserver}
     * @param log the new log to be pushed.
     */
    public synchronized void updateLog(String log) {
        for(int i = 0;i < logObservers.size();i++){
            logObservers.get(i).setLog(log);
        }
    }

    /**
     * addLoggingObserver adds {@link LoggingObserver} to be sent logs
     * @param l LoggingObserver to be added.
     */
    @Override
    public synchronized void addLoggingObserver(LoggingObserver l) {
        logObservers.add(l);
    }

    /**
     * removeLoggingObserver removes logging observer for it to not be updated with logs
     * @param l LoggingObserver to remove
     */
    @Override
    public synchronized void removeLoggingObserver(LoggingObserver l) {
        logObservers.remove(l);
    }
}
