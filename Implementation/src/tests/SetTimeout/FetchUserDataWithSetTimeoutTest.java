package tests.SetTimeout;

import domain.entities.tasks.concrete.promises.PromiseTask;
import presentation.EventLoop;

public class FetchUserDataWithSetTimeoutTest {
    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();

        System.out.println("Start of request");

        eventLoop.execute(new PromiseTask<>(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return new User(1, "Juan Pérez", "juan@example.com");
        })).then(user -> {
            System.out.println("User obtained: " + user);
            return user.getName();
        }).then(name -> {
            System.out.println("User name: " + name);
            return null;
        }).catchError(error -> {
            System.err.println("Error: " + error.getMessage());
        });

        System.out.println("End of request");

        eventLoop.run();

        // Console output:
        // Start of request
        // End of request
        // User obtained: { id: 1, name: 'Juan Pérez', email: 'juan@example.com' }
        // User name: Juan Pérez
    }
}

class User {
    private final int id;
    private final String name;
    private final String email;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{ id: " + id + ", name: '" + name + "', email: '" + email + "' }";
    }
}