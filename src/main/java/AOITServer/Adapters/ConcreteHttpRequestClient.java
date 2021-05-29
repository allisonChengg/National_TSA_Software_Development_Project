package AOITServer.Adapters;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * ConcreteHttpRequestClient class is used for sending simple http/https requests.
 *
 * <p>ConcreteHttpRequestClient class is part of an adapter pattern used to decouple implementation
 *  of http requests with server code.
 * </p>
 *
 * @see AOITServer.Adapters.HttpRequestClient
 */
public class ConcreteHttpRequestClient implements HttpRequestClient{
    private HttpClient client;


    public ConcreteHttpRequestClient(){
        client = HttpClient.newBuilder().
                version(HttpClient.Version.HTTP_2).
                build();
    }

    /**
     *
     * @param request The URL link that the request is being sent to.
     * @return Returns empty string if request failed, else return requested response in string.
     */
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
