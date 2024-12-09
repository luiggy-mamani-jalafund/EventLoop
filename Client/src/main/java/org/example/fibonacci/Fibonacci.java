package org.example.fibonacci;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import domain.entities.tasks.concrete.promises.PromiseTask;
import infrastructure.utils.Sleeper;
import presentation.EventLoop;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class Fibonacci {

    private final EventLoop eventLoop;
    private int number = 35;

    private JLabel numberLabel;
    private JLabel resultLabel;
    private JButton goButton;
    private JProgressBar progressBar;

    public Fibonacci(EventLoop eventLoop) {
        this.eventLoop = eventLoop;
    }

    public void renderUI() {
        JFrame frame = new JFrame("Event Loop Fibonacci");
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 18);
        Color fontColor = Color.decode("#fafafa");

        JButton incrementButton = new JButton("Increase");
        JButton decrementButton = new JButton("Decrease");
        JPanel buttonPanel = new JPanel(new FlowLayout());

        eventLoop.execute(new ImmediateTask(() -> frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)));
        eventLoop.execute(new ImmediateTask(() -> frame.setSize(400, 400)));
        eventLoop.execute(new ImmediateTask(() -> frame.setLayout(new GridLayout(5, 1))));

        eventLoop.execute(new ImmediateTask(() -> numberLabel = new JLabel("Number: " + number, SwingConstants.CENTER)));
        eventLoop.execute(new ImmediateTask(() -> numberLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24))));
        eventLoop.execute(new ImmediateTask(() -> numberLabel.setForeground(Color.decode("#000"))));

        eventLoop.execute(new ImmediateTask(() -> incrementButton.setFont(font)));
        eventLoop.execute(new ImmediateTask(() -> incrementButton.setForeground(fontColor)));
        eventLoop.execute(new ImmediateTask(() -> incrementButton.setBackground(Color.decode("#4756F5"))));
        eventLoop.execute(new ImmediateTask(() -> decrementButton.setFont(font)));
        eventLoop.execute(new ImmediateTask(() -> decrementButton.setForeground(fontColor)));
        eventLoop.execute(new ImmediateTask(() -> decrementButton.setBackground(Color.decode("#F52658"))));
        eventLoop.execute(new ImmediateTask(() -> incrementButton.addActionListener(e -> changeNumber(1))));
        eventLoop.execute(new ImmediateTask(() -> decrementButton.addActionListener(e -> changeNumber(-1))));

        eventLoop.execute(new ImmediateTask(() -> buttonPanel.add(decrementButton)));
        eventLoop.execute(new ImmediateTask(() -> buttonPanel.add(incrementButton)));

        eventLoop.execute(new ImmediateTask(() -> goButton = new JButton("Calculate Fibonacci")));
        eventLoop.execute(new ImmediateTask(() -> goButton.setFont(font)));
        eventLoop.execute(new ImmediateTask(() -> goButton.setBackground(Color.decode("#51F5A0"))));
        eventLoop.execute(new ImmediateTask(() -> goButton.addActionListener(e -> startCalculation())));

        eventLoop.execute(new ImmediateTask(() -> progressBar = new JProgressBar(0, 100)));
        eventLoop.execute(new ImmediateTask(() -> progressBar.setFont(font)));
        eventLoop.execute(new ImmediateTask(() -> progressBar.setBackground(Color.decode("#333"))));
        eventLoop.execute(new ImmediateTask(() -> progressBar.setForeground(Color.decode("#3158F5"))));
        eventLoop.execute(new ImmediateTask(() -> progressBar.setStringPainted(true)));

        eventLoop.execute(new ImmediateTask(() -> resultLabel = new JLabel("Result: ", SwingConstants.CENTER)));
        eventLoop.execute(new ImmediateTask(() -> resultLabel.setFont(font)));
        eventLoop.execute(new ImmediateTask(() -> resultLabel.setForeground(Color.decode("#3158F5"))));

        eventLoop.execute(new ImmediateTask(() -> frame.add(numberLabel)));
        eventLoop.execute(new ImmediateTask(() -> frame.add(buttonPanel)));
        eventLoop.execute(new ImmediateTask(() -> frame.add(goButton)));
        eventLoop.execute(new ImmediateTask(() -> frame.add(progressBar)));
        eventLoop.execute(new ImmediateTask(() -> frame.add(resultLabel)));

        eventLoop.execute(new ImmediateTask(() -> frame.setVisible(true)));
        eventLoop.execute(new ImmediateTask(() -> frame.setBackground(Color.BLACK)));
    }

    private void changeNumber(int delta) {
        eventLoop.execute(new ImmediateTask(() -> {
            number = Math.max(1, Math.min(100, number + delta));
            numberLabel.setText("Number: " + number);
        }));
    }

    private void startCalculation() {
        eventLoop.execute(new ImmediateTask(() -> goButton.setEnabled(false)));
        eventLoop.execute(new ImmediateTask(() -> progressBar.setValue(0)));
        eventLoop.execute(new ImmediateTask(() -> resultLabel.setText("Calculating...")));

        eventLoop.execute(new PromiseTask<>(() -> calculateFibonacci(number)))
                .then(result -> {
                    progressBar.setValue(100);
                    return "Result: " + result;
                })
                .thenAccept(finalResult -> {
                    resultLabel.setText(finalResult);
                    goButton.setEnabled(true);
                }).catchError(exception -> {
                    String message = exception.getCause() != null
                            ? exception.getCause().getMessage()
                            : "Something bad happened";
                    progressBar.setValue(0);
                    resultLabel.setText(message);
                    goButton.setEnabled(true);
                });
    }

    private long calculateFibonacci(int number) {
        if (number <= 1) {
            return number;
        }
        long a = 0, b = 1, sum;
        for (int i = 2; i <= number; i++) {
            sum = a + b;
            a = b;
            b = sum;
            final int progressValue = (int) ((i / (double) number) * 100);
            eventLoop.execute(new ImmediateTask(() -> progressBar.setValue(progressValue)));
            Sleeper.tryToSleepOrDie(100);
        }
        return b;
    }
}
