package ru.asurkis.tetris;

import ru.asurkis.tetris.canvas.GameBoardDisplay;
import ru.asurkis.tetris.canvas.TetraminoPeeker;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.locks.Condition;

import static ru.asurkis.tetris.GameLogic.FIELD_WIDTH;
import static ru.asurkis.tetris.GameLogic.FIELD_HEIGHT_VISIBLE;

public class Application {
    private final GameLogic game = new GameLogic();
    private final GameBoardDisplay gameBoardDisplay = new GameBoardDisplay();
    private final TetraminoPeeker tetraminoPeeker = new TetraminoPeeker();
    private final JFrame frame = new JFrame("Tetris");
    private final JLabel gameStatus = new JLabel();
    private final Queue<InputEvent> events = new ArrayBlockingQueue<>(128);

    private boolean isGamePaused = false;

    void start() {
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbcBoard = new GridBagConstraints();
        gbcBoard.gridx = 0;
        gbcBoard.gridy = 0;
        gbcBoard.gridwidth = FIELD_WIDTH;
        gbcBoard.gridheight = FIELD_HEIGHT_VISIBLE;
        gbcBoard.weightx = 1.0;
        gbcBoard.weighty = 1.0;
        gbcBoard.fill = GridBagConstraints.BOTH;

        GridBagConstraints gbcPeeker = new GridBagConstraints();
        gbcPeeker.gridx = FIELD_WIDTH;
        gbcPeeker.gridy = 0;
        gbcPeeker.gridwidth = 4;
        gbcPeeker.gridheight = 4;
        gbcPeeker.weightx = 1.0;
        gbcPeeker.weighty = 1.0;
        gbcPeeker.fill = GridBagConstraints.BOTH;

        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.gridx = FIELD_WIDTH;
        gbcInfo.gridy = 4;
        gbcInfo.gridwidth = 4;
        gbcInfo.gridheight = 1;
        gbcInfo.weightx = 1.0;
        gbcInfo.weighty = 1.0;
        gbcInfo.fill = GridBagConstraints.BOTH;

        frame.add(gameBoardDisplay, gbcBoard);
        frame.add(tetraminoPeeker, gbcPeeker);
        frame.add(gameStatus, gbcInfo);

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addKeyListener(new MyKeyListener(this));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                feedEvent(InputEvent.TIMER);
            }
        }, 250, 250);

        eventLoop();
    }

    private void eventLoop() {
        long updateStart = System.nanoTime();
        int loopRuns = 0;
        int redraws = 0;
        while (true) {
            long nanos = System.nanoTime();
            long elapsed = nanos - updateStart;
            if (elapsed >= 1_000_000_000) {
                double fps = 1e9 * redraws / elapsed;
                double tps = 1e9 * loopRuns / elapsed;
                frame.setTitle(String.format("Tetris | FPS: %.2f TPS: %.2f", fps, tps));
                redraws = 0;
                loopRuns = 0;
                updateStart = nanos;
            }
            InputEvent evt = events.poll();
            loopRuns++;
            if (evt != null) {
                switch (evt) {
                    case LEFT -> game.tryLeft();
                    case RIGHT -> game.tryRight();
                    case DOWN -> game.tryDown();
                    case ROTATE -> game.tryRotate();
                    case PAUSE -> isGamePaused ^= true;
                    case RESET -> game.reset();
                    case TIMER -> {
                        if (!isGamePaused)
                            game.gameTick();
                    }
                }
                continue;
            }

            if (game.isGameOver()) {
                gameStatus.setText("Game over!");
            } else if (isGamePaused) {
                gameStatus.setText("PAUSE");
            } else {
                gameStatus.setText("");
            }
            gameBoardDisplay.updateBackBuffer(game);
            tetraminoPeeker.updateBackBuffer(game);
            gameBoardDisplay.repaint();
            tetraminoPeeker.repaint();
            redraws++;

            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    public void feedEvent(InputEvent event) {
        events.add(event);
        synchronized (this) {
            this.notify();
        }
    }

    public static void main(String[] args) {
        new Application().start();
    }
}
