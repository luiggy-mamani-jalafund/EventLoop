package org.example.fibonacci.ReactionGameEventLoop;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;

public class UIManager {
    private final JFrame frame;
    private final JLabel statusLabel;
    private final JLabel bestTimeLabel;
    final JButton reactionButton;

    public UIManager(ActionListener buttonListener) {
        frame = new JFrame("Quick Reaction Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(3, 1));

        statusLabel = new JLabel("Wait for the button to change color...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        bestTimeLabel = new JLabel("Best time: -- ms", SwingConstants.CENTER);
        bestTimeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        reactionButton = new JButton("Wait...");
        reactionButton.setFont(new Font("Arial", Font.BOLD, 20));
        reactionButton.setBackground(Color.LIGHT_GRAY);
        reactionButton.setEnabled(false);
        reactionButton.addActionListener(buttonListener);

        frame.add(statusLabel);
        frame.add(reactionButton);
        frame.add(bestTimeLabel);
    }

    public void renderUI() {
        frame.setVisible(true);
    }

    public void updateStatus(String text) {
        statusLabel.setText(text);
    }

    public void updateBestTime(long bestTime) {
        bestTimeLabel.setText("Best time: " + bestTime + " ms");
    }

    public void updateButton(String text, Color color, boolean enabled) {
        reactionButton.setText(text);
        reactionButton.setBackground(color);
        reactionButton.setEnabled(enabled);
    }
}