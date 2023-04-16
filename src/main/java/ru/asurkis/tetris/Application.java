package ru.asurkis.tetris;

import ru.asurkis.tetris.canvas.GameBoardDisplay;
import ru.asurkis.tetris.canvas.TetraminoPeeker;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import static ru.asurkis.tetris.GameLogic.FIELD_WIDTH;
import static ru.asurkis.tetris.GameLogic.FIELD_HEIGHT_VISIBLE;

public class Application {
    private final Timer timer = new Timer();
    private final JFrame frame = new JFrame("Tetris");
    private final GameLogic game = new GameLogic();

    {
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
        gbcInfo.gridheight = FIELD_HEIGHT_VISIBLE - 4;
        gbcInfo.weightx = 1.0;
        gbcInfo.weighty = 1.0;
        gbcInfo.fill = GridBagConstraints.BOTH;

        frame.add(new GameBoardDisplay(game), gbcBoard);
        frame.add(new TetraminoPeeker(game), gbcPeeker);
        JPanel panel = new JPanel();
        panel.setBackground(Color.YELLOW);
        frame.add(panel, gbcInfo);

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addKeyListener(new MyKeyListener(game));
    }

    void start() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                game.gameTick();
            }
        }, 250);
    }

    public static void main(String[] args) {
        new Application().start();
    }
}
