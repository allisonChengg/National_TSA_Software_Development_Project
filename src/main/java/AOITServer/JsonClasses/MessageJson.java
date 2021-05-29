package AOITServer.JsonClasses;

/**
 * MessageJson class is a JSON wrapper class used for sending messages and errors to Client using JSON
 */
public class MessageJson {
    /**
     *
     * @param success boolean that states if request was successful
     * @param message a message that is returned to the client
     */
    public MessageJson(boolean success, String message){
        Success = success;
        this.Message = message;
    }

    public MessageJson(){}

    public boolean Success;
    public String Message;
}
