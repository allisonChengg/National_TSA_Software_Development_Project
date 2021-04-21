package AOITServer.Observers;

public interface LoggingSubject {

    void updateLog(String log);

    void addLoggingObserver(LoggingObserver l);
    void removeLoggingObserver(LoggingObserver l);


}
