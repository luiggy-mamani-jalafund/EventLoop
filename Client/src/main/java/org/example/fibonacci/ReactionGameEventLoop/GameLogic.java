package org.example.fibonacci.ReactionGameEventLoop;

public class GameLogic {
    private long bestTime = Long.MAX_VALUE;
    private long startTime;

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public long calculateReactionTime() {
        return System.currentTimeMillis() - startTime;
    }

    public boolean isBestTime(long reactionTime) {
        if (reactionTime < bestTime) {
            bestTime = reactionTime;
            return true;
        }
        return false;
    }

    public long getBestTime() {
        return bestTime;
    }
}
