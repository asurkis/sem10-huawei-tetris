package ru.asurkis.tetris.canvas;

import ru.asurkis.tetris.GameLogic;
import ru.asurkis.tetris.Tetramino;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static ru.asurkis.tetris.GameLogic.FIELD_HEIGHT_VISIBLE;
import static ru.asurkis.tetris.GameLogic.FIELD_WIDTH;
import static ru.asurkis.tetris.canvas.Constants.CELL_COLORS;
import static ru.asurkis.tetris.canvas.Constants.MIN_CELL_SIZE;

public class GameBoardDisplay extends JPanel {
    private BufferedImage backBuffer = new BufferedImage(FIELD_WIDTH * MIN_CELL_SIZE, FIELD_HEIGHT_VISIBLE * MIN_CELL_SIZE, BufferedImage.TYPE_3BYTE_BGR);
    private final GameLogic game;

    public GameBoardDisplay(GameLogic game) {
        this.game = game;
        game.addUpdateListener(this::repaint);
        setMinimumSize(new Dimension(FIELD_WIDTH * MIN_CELL_SIZE, FIELD_HEIGHT_VISIBLE * MIN_CELL_SIZE));
        setPreferredSize(getMinimumSize());
    }

    public void updateBackBuffer() {
        int w = getWidth();
        int h = getHeight();
        if (backBuffer.getWidth() < w || backBuffer.getHeight() < h)
            backBuffer = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

        int cellSize = Math.min(w / FIELD_WIDTH, h / FIELD_HEIGHT_VISIBLE);
        int boardW = cellSize * FIELD_WIDTH;
        int boardH = cellSize * FIELD_HEIGHT_VISIBLE;
        Graphics g = backBuffer.getGraphics();
        g.clearRect(0, 0, w, h);
        g.translate((w - boardW) / 2, (h - boardH) / 2);
        for (int y = 0; y < FIELD_HEIGHT_VISIBLE; y++) {
            for (int x = 0; x < FIELD_WIDTH; x++) {
                int tx = cellSize * x;
                int ty = cellSize * (FIELD_HEIGHT_VISIBLE - y - 1);
                int colorIdx = game.getTetraminoIndexAt(x, y) + 1;
                g.setColor(CELL_COLORS[colorIdx]);
                g.fillRect(tx, ty, cellSize, cellSize);
            }
        }

        Tetramino current = game.getFallingTetramino();
        int fallingX = game.getFallingX();
        int fallingY = game.getFallingY();
        g.setColor(CELL_COLORS[current.ordinal() + 1]);
        for (int[] xy : current.coords(game.getFallingRotation())) {
            int ey = fallingY + xy[1];
            if (ey >= FIELD_HEIGHT_VISIBLE)
                continue;
            int tx = cellSize * (fallingX + xy[0]);
            int ty = cellSize * (FIELD_HEIGHT_VISIBLE - 1 - ey);
            g.fillRect(tx, ty, cellSize, cellSize);
        }

        g.setColor(Color.WHITE);
        g.drawRect(0, 0, boardW - 1, boardH - 1);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        updateBackBuffer();
        graphics.drawImage(backBuffer, 0, 0, this);
    }
}
