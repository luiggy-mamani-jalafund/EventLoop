package org.example.CirclePopGame;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import presentation.EventLoop;

public class CirclePopGameApp {
    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();
        CirclePopGame circlePopGame = new CirclePopGame(eventLoop);

        eventLoop.execute(new ImmediateTask(circlePopGame::renderUI));
        eventLoop.start();
    }
}
