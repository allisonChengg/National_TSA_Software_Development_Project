package AOITServer.Tables;

/**
 * AOITEmailKeyTable class contains column names found in AOITEmailKeys table.
 * <p>Table used for storing information related to a reset password verification email</p>
 */

public class AOITEmailKeyTable {
    public static final String TABLENAME = "AOITEmailKeys";
    public static final String USERNAME = "Username";
    public static final String VERIFICATIONKEY = "VerificationKey";
    public static final String EXPIRATIONTIME = "ExpirationTime";

}
