package org.example.ReactionGameEventLoop;

import presentation.EventLoop;

import java.awt.Color;
import java.util.Random;

public class ReactionGame {
    private final UIManager uiManager;
    private final GameLogic gameLogic;
    private final EventManager eventManager;
    private final Random random = new Random();

    public ReactionGame(EventLoop eventLoop) {
        this.eventManager = new EventManager(eventLoop);
        this.gameLogic = new GameLogic();
        this.uiManager = new UIManager(e -> handleButtonClick());
    }

    public void start() {
        uiManager.renderUI();
        startGame();
    }

    private void startGame() {
        eventManager.executeImmediateTask(() -> {
            uiManager.updateButton("Wait...", Color.LIGHT_GRAY, false);
            uiManager.updateStatus("Wait for the button to change color...");
        });

        int delay = random.nextInt(3000) + 2000;
        eventManager.setTimeout(() -> {
            eventManager.executeImmediateTask(() -> {
                uiManager.updateButton("Click now!", Color.GREEN, true);
                uiManager.updateStatus("Press the button now!");
                gameLogic.startTimer();
            });
        }, delay);
    }

    private void handleButtonClick() {
        eventManager.executeImmediateTask(() -> {
            long reactionTime = gameLogic.calculateReactionTime();

            if (!"Click now!".equals(uiManager.reactionButton.getText())) {
                uiManager.updateStatus("Please try again.");
                startGame();
                return;
            }

            uiManager.updateStatus("Your reaction time: " + reactionTime + " ms");
            if (gameLogic.isBestTime(reactionTime)) {
                uiManager.updateBestTime(gameLogic.getBestTime());
            }
            startGame();
        });
    }
}
