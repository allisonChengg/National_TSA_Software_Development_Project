package AOITServer.Adapters;

/**
 * HttpRequestClient class us an interface used for sending simple http/https requests.
 *
 * <p>Is part of an adapter pattern used to decouple HttpRequest library's with server code</p>
 */
public interface HttpRequestClient {
    /**
     *
     * @param request The URL link that the request is being sent to.
     * @return Returns all requested information in a string.
     *
     */
    public String request(String request);
}
