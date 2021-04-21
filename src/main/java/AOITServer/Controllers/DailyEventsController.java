package AOITServer.Controllers;

import AOITServer.JsonClasses.MessageJson;
import io.javalin.http.Handler;

public class DailyEventsController {
    private String json = "";

    public Handler getCareerEvents(){
        return ctx ->{
            ctx.result(json);
        };
    }
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
