package org.example.ReactionGameEventLoop;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import domain.entities.tasks.concrete.timers.TimerTask;
import presentation.EventLoop;

public class EventManager {
    private final EventLoop eventLoop;

    public EventManager(EventLoop eventLoop) {
        this.eventLoop = eventLoop;
    }

    public void executeImmediateTask(Runnable task) {
        eventLoop.execute(new ImmediateTask(task));
    }

    public void setTimeout(Runnable task, int delay) {
        eventLoop.setTimeout(new TimerTask(task, delay));
    }
}
