package ru.asurkis.tetris.canvas;

import ru.asurkis.tetris.GameLogic;
import ru.asurkis.tetris.Tetramino;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ru.asurkis.tetris.GameLogic.FIELD_HEIGHT_VISIBLE;
import static ru.asurkis.tetris.GameLogic.FIELD_WIDTH;
import static ru.asurkis.tetris.canvas.Constants.CELL_COLORS;
import static ru.asurkis.tetris.canvas.Constants.MIN_CELL_SIZE;

public class GameBoardDisplay extends Canvas {
    private final GameLogic game;
    private BufferedImage backBuffer = new BufferedImage(FIELD_WIDTH * MIN_CELL_SIZE, FIELD_HEIGHT_VISIBLE * MIN_CELL_SIZE, BufferedImage.TYPE_3BYTE_BGR);

    public GameBoardDisplay(GameLogic game) {
        setMinimumSize(new Dimension(FIELD_WIDTH * MIN_CELL_SIZE, FIELD_HEIGHT_VISIBLE * MIN_CELL_SIZE));
        setPreferredSize(getMinimumSize());
        this.game = game;
        game.addStateListener(this::invalidate);
    }

    @Override
    public void paint(Graphics graphics) {
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
                g.setColor(Color.WHITE);
                g.fillRect(tx, ty, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(tx, ty, cellSize, cellSize);
            }
        }

        Tetramino current = game.getCurrentTetramino();


        graphics.drawImage(backBuffer, 0, 0, this);
        graphics.setColor(Color.RED);
        graphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}
