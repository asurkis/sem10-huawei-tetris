package ru.asurkis.tetris.canvas;

import ru.asurkis.tetris.GameLogic;
import ru.asurkis.tetris.Tetramino;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static ru.asurkis.tetris.canvas.Constants.CELL_COLORS;
import static ru.asurkis.tetris.canvas.Constants.MIN_CELL_SIZE;

public class TetraminoPeeker extends JPanel {
    private final GameLogic game;
    private BufferedImage backBuffer = new BufferedImage(4 * MIN_CELL_SIZE, 4 * MIN_CELL_SIZE, BufferedImage.TYPE_3BYTE_BGR);

    public TetraminoPeeker(GameLogic game) {
        setMinimumSize(new Dimension(4 * MIN_CELL_SIZE, 4 * MIN_CELL_SIZE));
        setPreferredSize(getMinimumSize());
        this.game = game;
        game.addStateListener(this::repaint);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        if (backBuffer.getWidth() < w || backBuffer.getHeight() < h)
            backBuffer = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

        int cellSize = Math.min(w / 4, h / 4);
        int boardW = cellSize * 4;
        int boardH = cellSize * 4;
        Graphics g = backBuffer.getGraphics();
        g.clearRect(0, 0, w, h);
        g.translate((w - boardW) / 2, (h - boardH) / 2);

        Tetramino next = game.getNextTetramino();
        g.setColor(CELL_COLORS[next.ordinal() + 1]);
        for (int[] xy : next.coords(0)) {
            int tx = cellSize * (1 + xy[0]);
            int ty = cellSize * (2 - xy[1]);
            g.fillRect(tx, ty, cellSize, cellSize);
        }

        g.setColor(Color.WHITE);
        g.drawRect(0, 0, boardW - 1, boardH - 1);
        graphics.drawImage(backBuffer, 0, 0, this);
    }
}
