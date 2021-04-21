package AOITServer.JsonClasses;

public class MessageJson {
    public MessageJson(boolean success, String message){
        Success = success;
        this.Message = message;
    }

    public MessageJson(){}

    public boolean Success;
    public String Message;
}
