package AOITServer.Controllers;

import AOITServer.JsonClasses.MessageJson;
import io.javalin.http.Handler;

/**
 * DailyEventsController is used to set and get the daily career message.
 */
public class DailyEventsController {
    private String json = "";

    /**
     *  getCareerEvents used for clients to get json career event.
     * @return Returns Handler needed for initializing accessible url.
     */
    public Handler getCareerEvents(){
        return ctx ->{
            ctx.result(json);
        };
    }

    /**
     * setCareerEvents used for clients to set json career event message.
     * @return Returns Handler needed for initializing accessible url.
     */
    public Handler setCareerEvents(){
        return ctx ->{
            String careerEvent = ctx.queryParam("CareerEvents");
            if(careerEvent == null){
                ctx.status(404).json(new MessageJson(false,"Query param \"CareerEvents\" is not found"));
            }
            else{
                json = careerEvent;
            }
        };
    }
}
