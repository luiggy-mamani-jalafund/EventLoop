package org.example.fibonacci.ReactionGameEventLoop;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import presentation.EventLoop;

public class ReactionGameApp {
    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();
        ReactionGame game = new ReactionGame(eventLoop);

        eventLoop.execute(new ImmediateTask(game::start));
        eventLoop.start();
    }
}
