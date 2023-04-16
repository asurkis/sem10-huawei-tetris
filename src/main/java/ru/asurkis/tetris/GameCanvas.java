package ru.asurkis.tetris;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameCanvas extends Canvas {
    private final GameLogic game;
    private BufferedImage backBuffer = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);

    public GameCanvas(GameLogic game) {
        setMinimumSize(new Dimension(640, 480));
        setPreferredSize(getMinimumSize());
        this.game = game;
        game.addStateListener(this::invalidate);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 100, 100);
    }
}
