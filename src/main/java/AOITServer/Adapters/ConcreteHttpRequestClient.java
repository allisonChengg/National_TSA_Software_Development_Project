package AOITServer.Adapters;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConcreteHttpRequestClient implements HttpRequestClient{
    private HttpClient client;
    public ConcreteHttpRequestClient(){
        client = HttpClient.newBuilder().
                version(HttpClient.Version.HTTP_2).
                build();
    }

    @Override
    public String request(String request) {
        try {
            return client.send(HttpRequest.newBuilder(URI.create(request)).build(), HttpResponse.BodyHandlers.ofString()).body();

        } catch (IOException e) {
            System.out.println(e);

        } catch (InterruptedException e) {
            System.out.println(e);

        }
        return "";
    }
}
