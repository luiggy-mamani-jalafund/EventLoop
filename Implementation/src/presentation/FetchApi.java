package presentation;

import domain.entities.tasks.concrete.promises.Promise;
import domain.entities.tasks.concrete.promises.PromiseTask;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FetchApi {

    private final EventLoop eventLoop;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public FetchApi(EventLoop eventLoop) {
        this.eventLoop = eventLoop;
    }

    public Promise<HttpResponse<String>> fetch(String url) {
        return eventLoop.execute(new PromiseTask<>(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            try {
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception exception) {
                throw new RuntimeException(exception.getMessage(), exception);
            }
        }));
    }
}
