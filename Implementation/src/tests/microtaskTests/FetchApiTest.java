package tests.microtaskTests;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import presentation.EventLoop;
import presentation.FetchApi;

import java.net.http.HttpResponse;

public class FetchApiTest {

    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();
        FetchApi fetchApi = new FetchApi(eventLoop);

        eventLoop.execute(new ImmediateTask(() -> System.out.println("task 1")));

        fetchApi.fetch("https://jsonplaceholder.typicode.com/posts/1")
                .then(HttpResponse::body)
                .thenAccept(System.out::println)
                .catchError(error -> {
                    System.out.println(error.getMessage());
                });

        eventLoop.execute(new ImmediateTask(() -> System.out.println("task 2")));

        eventLoop.run();

        /*
        * Expected:
        *
        * task 1
        * task 2
        * {
        *   "userId": 1,
        *   "id": 1,
        *   "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
        *   "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
        * }
        * */
    }
}
