package ru.asurkis.tetris;

import javax.swing.*;
import java.util.Timer;

public class Application {
    private final Timer timer = new Timer();
    private final JFrame frame = new JFrame("Tetris");
    private final GameLogic game = new GameLogic();
    private final GameCanvas canvas = new GameCanvas(game);

    {
        frame.add(canvas);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addKeyListener(new MyKeyListener());
    }

    void start() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Application().start();
    }
}
