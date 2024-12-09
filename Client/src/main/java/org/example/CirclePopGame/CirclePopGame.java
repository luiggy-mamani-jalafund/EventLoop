package org.example.CirclePopGame;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import domain.entities.tasks.concrete.intervals.IntervalTask;
import domain.entities.tasks.concrete.timers.TimerTask;
import presentation.EventLoop;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.UUID;

public class CirclePopGame {
    private final EventLoop eventLoop;
    private JPanel gamePanel;
    private JLabel scoreLabel;
    private static int score = 0;
    private int balloonCount = 0;
    private UUID balloonSpawnerId;
    private int spawnInterval = 1000;

    public CirclePopGame(EventLoop eventLoop) {
        this.eventLoop = eventLoop;
    }

    public void renderUI() {
        JFrame frame = new JFrame("Balloon Pop Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
        frame.add(scoreLabel, BorderLayout.NORTH);

        gamePanel = new JPanel(null);
        gamePanel.setBackground(Color.decode("#000000"));
        frame.add(gamePanel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start Game");
        JButton stopButton = new JButton("Stop Game");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        startButton.addActionListener(e -> startGame());
        stopButton.addActionListener(e -> stopGame());

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void startGame() {
        if (balloonSpawnerId == null) {
            balloonSpawnerId = eventLoop.setInterval(new IntervalTask(() -> spawnBalloon(), spawnInterval));
        }
    }

    private void stopGame() {
        if (balloonSpawnerId != null) {
            eventLoop.clearInterval(balloonSpawnerId);
            balloonSpawnerId = null;
        }
        eventLoop.execute(new ImmediateTask(() -> {
            gamePanel.removeAll();
            gamePanel.repaint();
        }));
        score = 0;
        balloonCount = 0;
        spawnInterval = 1000;
        updateScoreLabel();
    }

    private void spawnBalloon() {
        if (balloonCount >= 15) {
            gameOver();
            return;
        }

        Random random = new Random();
        int x = random.nextInt(gamePanel.getWidth() - 50);
        int y = random.nextInt(gamePanel.getHeight() - 50);

        JButton balloon = new JButton();
        balloon.setBounds(x, y, 50, 50);
        balloon.setBackground(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        balloon.setOpaque(true);
        balloon.setBorderPainted(false);

        balloon.addActionListener(e -> {
            gamePanel.remove(balloon);
            gamePanel.repaint();
            balloonCount--;
            updateScore(10);

            if (score % 50 == 0) {
                increaseSpawnSpeed();
            }
        });

        eventLoop.execute(new ImmediateTask(() -> {
            gamePanel.add(balloon);
            gamePanel.repaint();
            balloonCount++;
            if (balloonCount >= 15) {
                gameOver();
            }
        }));

        eventLoop.setTimeout(new TimerTask(() -> {
            if (balloon.getParent() != null) {
                eventLoop.execute(new ImmediateTask(() -> {
                    gamePanel.remove(balloon);
                    gamePanel.repaint();
                    balloonCount--;
                }));
            }
        }, 5000));
    }

    private void updateScore(int points) {
        score += points;
        updateScoreLabel();
    }

    private void updateScoreLabel() {
        eventLoop.execute(new ImmediateTask(() -> scoreLabel.setText("Score: " + score)));
    }

    private void increaseSpawnSpeed() {
        if (spawnInterval > 200) {
            spawnInterval -= 100;
            if (balloonSpawnerId != null) {
                eventLoop.clearInterval(balloonSpawnerId);
                balloonSpawnerId = eventLoop.setInterval(new IntervalTask(() -> spawnBalloon(), spawnInterval));
            }
        }
    }

    private void gameOver() {
        stopGame();
        JOptionPane.showMessageDialog(null, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
}
