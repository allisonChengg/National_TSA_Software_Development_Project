package AOITServer.Controllers;


import AOITServer.Adapters.HttpRequestClient;
import AOITServer.JsonClasses.MessageJson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.http.Handler;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MapController {
    private String token;
    private HttpRequestClient reqClient;
    public MapController(String mapToken, HttpRequestClient rc){
        token = mapToken;
        reqClient= rc;
    }

    private String[] sendGeoLocRequest(String addr){
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
                URLEncoder.encode(addr, StandardCharsets.UTF_8),token);

        String geoString = reqClient.request(url);
        JsonObject j = JsonParser.parseString(geoString).getAsJsonObject().
                getAsJsonArray("results").get(0).getAsJsonObject().get("geometry").
                getAsJsonObject().get("location").getAsJsonObject();

        String[] coords = {j.get("lat").toString(),j.get("lng").toString()};

        return coords;


    }

    private String sendNearbySearchRequest(String latitude,String longitude,String radius,String keyword){
        String format = String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%s,%s&radius=%s&keyword=%s&key=%s"
                ,latitude,longitude,radius,keyword,token);

        return reqClient.request(format);
    }

    public Handler getBanks(){
        return ctx ->{
            String address = ctx.queryParam("Address");
            String radius = ctx.queryParam("Radius");

            if(radius == null){
                radius = "300";
            }
            if(address != null) {
                String[] coords = sendGeoLocRequest(address);

                ctx.result(sendNearbySearchRequest(coords [0],coords[1],radius,"bank" ));

            }
            else{
               ctx.json(new MessageJson(false," Address query parameter not provided ")) ;
            }
        };

    }

    public Handler getShelter(){
        return ctx ->{
            String address = ctx.queryParam("Address");
            String radius = ctx.queryParam("Radius");

            if(radius == null){
                radius = "300";
            }
            if(address != null) {
                String[] coords = sendGeoLocRequest(address);

                ctx.result(sendNearbySearchRequest(coords [0],coords[1],radius,"shelter" ));

            }
        };
    }

    public Handler getFoodBanks(){
        return ctx ->{
            String address = ctx.queryParam("Address");
            String radius = ctx.queryParam("Radius");

            if(radius == null){
                radius = "300";
            }
            if(address != null) {
                String[] coords = sendGeoLocRequest(address);

                ctx.result(sendNearbySearchRequest(coords [0],coords[1],radius,"food%20bank" ));

            }
        };
    }

    public Handler getThriftStores(){
        return ctx ->{
            String address = ctx.queryParam("Address");
            String radius = ctx.queryParam("Radius");

            if(radius == null){
                radius = "300";
            }
            if(address != null) {
                String[] coords = sendGeoLocRequest(address);

                ctx.result(sendNearbySearchRequest(coords [0],coords[1],radius,"thrift%20store" ));

            }
        };
    }

    public Handler getPublicServices(){
        return ctx ->{
            String address = ctx.queryParam("Address");
            String radius = ctx.queryParam("Radius");

            if(radius == null){
                radius = "300";
            }
            if(address != null) {
                String[] coords = sendGeoLocRequest(address);

                String electric = sendNearbySearchRequest(coords [0],coords[1],radius,"electric%20company");
                String sewer = sendNearbySearchRequest(coords [0],coords[1],radius,"water%20company");
                String water = sendNearbySearchRequest(coords [0],coords[1],radius,"water%20store");

                System.out.println(electric);
                System.out.println(sewer);
                System.out.println(water);

                JsonArray jsonArray = new JsonArray();
                jsonArray.add(JsonParser.parseString(electric).getAsJsonObject());
                jsonArray.add(JsonParser.parseString(sewer).getAsJsonObject());
                jsonArray.add(JsonParser.parseString(water).getAsJsonObject());

                System.out.println(jsonArray.toString());

                ctx.result(jsonArray.toString());



            }
        };
    }
}
