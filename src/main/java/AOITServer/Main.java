package AOITServer;

import AOITServer.Adapters.ConcreteEmailClient;
import AOITServer.Adapters.ConcreteHttpRequestClient;
import AOITServer.Controllers.*;
import AOITServer.Factories.JWTConcreteFactory;
import AOITServer.Factories.JWTReflexiveFactory;
import AOITServer.Factories.JWTToken;
import AOITServer.Observers.AccessManagerJWT;
import AOITServer.Observers.ServerLogging;
import AOITServer.Singletons.DatabaseSingleton;
import io.javalin.Javalin;
import javalinjwt.JavalinJWT;

import java.util.Map;

import static io.javalin.core.security.SecurityUtil.roles;

/**
 * Main class contains main method which starts the program.
 */
public class Main {
	
    public static void main(String[] args){

        String databaseUsername = "woottontsa@wootton-tsa-mysql-server";
        //please no hack
        String databasePassword = "Woot@2020";
        String jdbcDriver = "com.mysql.cj.jdbc.Driver";
        String databaseURL = "jdbc:mysql://wootton-tsa-mysql-server.mysql.database.azure.com:3306/AOITDatabase?serverTimezone=UTC";
        String email = "dev.woottontsa.org@gmail.com";
        String emailPassword = "dev.woottontsa.org@Wootton2020";
        String googleMapsApiKey = "AIzaSyB0kTB0O417Co-wx3mm5lLIU3AdVuCICtc.";

        DatabaseSingleton ds = DatabaseSingleton.getInstance(jdbcDriver,databaseURL);

        System.out.println(ds.createConnection(databaseUsername,databasePassword));
        System.out.println("Test1");
        Javalin server = Javalin.create();

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            ds.close();
            server.stop();

        }));



        JWTReflexiveFactory<JWTToken> jwtFactory = new JWTReflexiveFactory<>("AAABBBCCC");
        server.before(JavalinJWT.createHeaderDecodeHandler(jwtFactory.getProvider()));

        AccessManagerJWT accessManager =new AccessManagerJWT("Username", "Role",
                Map.of("0",Roles.DEFAULT,"1",Roles.USER,"2",Roles.ADMIN), Roles.DEFAULT);
        server.config.accessManager(accessManager);


        UserController userController = new UserController(0,ds);
        server.post("/createUser",userController.createUser(),roles(Roles.DEFAULT,Roles.ADMIN));
        server.get("/validateToken",userController.validateToken(),roles(Roles.USER,Roles.ADMIN));
        server.post("/login",userController.loginUser(jwtFactory),roles(Roles.DEFAULT));

        PasswordController passwordController = new PasswordController(0,ds);

        ConcreteEmailClient emailClient = new ConcreteEmailClient(email,emailPassword);
        server.post("/sendVerification",passwordController.sendValidationKey(emailClient),roles(Roles.DEFAULT));
        server.post("/resetPassword",passwordController.changePassword(),roles(Roles.DEFAULT));

        DailyEventsController dailyEvents = new DailyEventsController();
        server.post("/setCareerEvents",dailyEvents.setCareerEvents(),roles(Roles.ADMIN));
        server.get("/getCareerEvents",dailyEvents.getCareerEvents(),roles(Roles.USER,Roles.ADMIN));

        ConcreteHttpRequestClient rc = new ConcreteHttpRequestClient();
        MapController mapController = new MapController(googleMapsApiKey,rc);
        server.get("/getBanks",mapController.getBanks(),roles(Roles.USER,Roles.ADMIN));
        server.get("/getShelters",mapController.getShelter(),roles(Roles.USER,Roles.ADMIN));
        server.get("/getFoodBanks",mapController.getFoodBanks(),roles(Roles.USER,Roles.ADMIN));
        server.get("/getThriftStores",mapController.getThriftStores(),roles(Roles.USER,Roles.ADMIN));
        server.get("/getPublicServices",mapController.getPublicServices(),roles(Roles.USER,Roles.ADMIN));

        InformationController informationController = new InformationController(0,ds,accessManager);
        server.get("/getName",informationController.getName(),roles(Roles.USER,Roles.ADMIN));
        server.get("/getPassword",informationController.getPassword(),roles(Roles.USER,Roles.ADMIN));
        server.get("/getEmail",informationController.getEmail(),roles(Roles.USER,Roles.ADMIN));
        server.get("/getPhone",informationController.getPhone(),roles(Roles.USER,Roles.ADMIN));
        server.get("/getBirth",informationController.getBirthday(),roles(Roles.USER,Roles.ADMIN));
        server.get("/getAddress",informationController.getAddress(),roles(Roles.USER,Roles.ADMIN));
        server.post("/setName",informationController.setName(),roles(Roles.USER,Roles.ADMIN));
        server.post("/setPassword",informationController.setPassword(),roles(Roles.USER,Roles.ADMIN));
        server.post("/setEmail",informationController.setEmail(),roles(Roles.USER,Roles.ADMIN));
        server.post("/setPhone",informationController.setPhoneNumber(),roles(Roles.USER,Roles.ADMIN));
        server.post("/setBirth",informationController.setBirthday(),roles(Roles.USER,Roles.ADMIN));
        server.post("/setAddress",informationController.setAddress(),roles(Roles.USER,Roles.ADMIN));

        ServerLogging serverLog = new ServerLogging(0,ds);

        accessManager.addLoggingObserver(serverLog);

        int port = 8001;
        server.start(port);
        

    }
}
