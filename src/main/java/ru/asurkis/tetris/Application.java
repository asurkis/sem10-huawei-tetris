package ru.asurkis.tetris;

import ru.asurkis.tetris.canvas.GameBoardDisplay;
import ru.asurkis.tetris.canvas.TetraminoPeeker;

import javax.swing.*;
import java.awt.*;

import static ru.asurkis.tetris.GameLogic.FIELD_WIDTH;
import static ru.asurkis.tetris.GameLogic.FIELD_HEIGHT_VISIBLE;

public class Application {
    private final GameLogic game = new GameLogic();
    private final GameBoardDisplay gameBoardDisplay = new GameBoardDisplay(game);
    private final TetraminoPeeker tetraminoPeeker = new TetraminoPeeker(game);
    private final JFrame frame = new JFrame("Tetris");
    private final JLabel gameStatus = new JLabel();

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

        game.addUpdateListener(() -> {
            if (game.isGameOver()) {
                gameStatus.setText("Game over!");
            } else if (game.isPaused()) {
                gameStatus.setText("PAUSE");
            } else {
                gameStatus.setText("");
            }
        });

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addKeyListener(new MyKeyListener(game));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        new Timer(250, e -> game.gameTick()).start();
    }

    public static void main(String[] args) {
        new Application().start();
    }
}
