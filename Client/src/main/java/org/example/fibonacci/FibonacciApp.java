package org.example.fibonacci;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import presentation.EventLoop;

public class FibonacciApp {

    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();
        Fibonacci fibonacci = new Fibonacci(eventLoop);

        eventLoop.execute(new ImmediateTask(fibonacci::renderUI));
        eventLoop.start();
    }
}
