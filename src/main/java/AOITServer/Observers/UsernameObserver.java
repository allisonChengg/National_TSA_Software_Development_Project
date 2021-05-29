package AOITServer.Observers;

/**
 * Subclasses that implement UsernameObserver can get current users username from {@link UsernameSubject}
 *
 * <p>Part of an observer pattern.By calling {@link UsernameSubject#getUsername()} inside {@link #updateUsername(UsernameSubject)} subclasses
 * can get current username</p>
 *
 * @see UsernameSubject
 */
public interface UsernameObserver {
    /**
     * updateUsername is used to get current users username.
     * @param a {@link UsernameSubject} that username will be gotten from using {@link UsernameSubject#getUsername()}
     * @return Returns the username
     */
    String updateUsername(UsernameSubject a);

}
