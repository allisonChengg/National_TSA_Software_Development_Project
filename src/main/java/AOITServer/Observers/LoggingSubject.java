package AOITServer.Observers;

/**
 * Subclasses that implement LoggingSubject get to push new logs to all registered {@link LoggingObserver}
 *
 * <p>LoggingSubject is part of an observer pattern. Logging Subject interface is used to
 *  push new logs to all registered observers ,{@link #addLoggingObserver(LoggingObserver)},
 *  which can be done by calling the {@link LoggingObserver#setLog(String)} </p>
 *
 * @see LoggingObserver
 */
public interface LoggingSubject {
    /**
     * updateLog is used to push new logs to registered observers.
     * @param log the new log to be pushed.
     */
    void updateLog(String log);

    /**
     * addLoggingObserver method is used to register {@link LoggingObserver}.
     * @param l LoggingObserver to be added.
     */
    void addLoggingObserver(LoggingObserver l);

    /**
     * removeLoggingObserver method is used to remove {@link LoggingObserver} from ones already registered.
     * @param l LoggingObserver to remove
     */
    void removeLoggingObserver(LoggingObserver l);


}
