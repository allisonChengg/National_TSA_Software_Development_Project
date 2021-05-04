package AOITServer.Observers;

/**
 * Subclasses implementing UsernameSubject can be used to obtain username of current client.
 *
 * <p>{@link UsernameObserver} can get UsernameSubject to obtain username using {@link #getUsername().}
 * This can be called inside the {@link UsernameObserver#updateUsername} method.</p>
 * @see UsernameObserver
 */
public interface UsernameSubject {
    /**
     * getUsername method is used to obtain current clients username.
     * @return Returns current clients username.
     */
    String getUsername();

}
