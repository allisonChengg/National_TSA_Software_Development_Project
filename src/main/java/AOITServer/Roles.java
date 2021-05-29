package AOITServer;

import io.javalin.core.security.Role;

/**
 * Enum Roles is responsible for containing a the possible roles a client can have.
 */
public enum Roles implements Role {
    DEFAULT,USER,ADMIN
}
