package AOITServer.Observers;

/**
 * All subclasses that implement LoggingObserver interface receive log information automatically.
 * <p>LoggingObserver interface is part of a observer pattern. By registering to a {@link LoggingSubject} using
 * the {@link LoggingSubject#addLoggingObserver(LoggingObserver)}, LoggingSubject
 * will automatically call {@link LoggingObserver#setLog(String)} when new log information appears</p>
 *
 * @see LoggingSubject
 */

public interface LoggingObserver {
    /**
     * setLog function is called to push new log
     *
     * @param log The new log to be pushed.
     */
    void setLog(String log);
}
