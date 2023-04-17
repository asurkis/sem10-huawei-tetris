package ru.asurkis.tetris;

import java.util.*;

public class GameLogic {
    public static final int FIELD_WIDTH = 10;
    public static final int FIELD_HEIGHT_VISIBLE = 20;
    public static final int FIELD_HEIGHT_HIDDEN = 24;

    private final Random random;

    private Tetramino fallingTetramino;
    private int fallingX;
    private int fallingY;
    private int fallingRotation;
    private final Queue<Tetramino> nextTetramino = new ArrayDeque<>();
    private final int[][] fieldState = new int[FIELD_HEIGHT_HIDDEN][FIELD_WIDTH];

    private boolean isGameOver;

    public GameLogic(long randomSeed) {
        random = new Random(randomSeed);
        reset();
    }

    public GameLogic() {
        random = new Random();
        reset();
    }

    public void reset() {
        for (int i = 0; i < FIELD_HEIGHT_HIDDEN; i++) {
            Arrays.fill(fieldState[i], -1);
        }
        isGameOver = false;
        nextTetramino.clear();
        takeNext();
    }

    private void takeNext() {
        refillQueue();
        fallingTetramino = nextTetramino.remove();
        refillQueue();
        fallingY = 20;
        fallingRotation = 0;
        fallingX = 5;
        if (!checkPosition(fallingX, fallingY, fallingRotation))
            isGameOver = true;
    }

    private void refillQueue() {
        Tetramino[] values = Tetramino.values();
        int len = values.length;
        if (nextTetramino.size() >= len) {
            return;
        }
        int[] idx = new int[len];
        for (int i = 0; i < len; i++)
            idx[i] = i;
        for (int i = 0; i < len; i++) {
            int j = random.nextInt(len - i);
            nextTetramino.add(values[idx[j]]);
            idx[j] = idx[i];
        }
    }

    public void gameTick() {
        if (!isGameOver)
            tryDown();
    }

    public void tryLeft() {
        if (isGameOver)
            return;
        if (checkPosition(fallingX - 1, fallingY, fallingRotation))
            fallingX--;
    }

    public void tryRight() {
        if (isGameOver)
            return;
        if (checkPosition(fallingX + 1, fallingY, fallingRotation))
            fallingX++;
    }

    public void tryRotate() {
        if (isGameOver)
            return;
        int nextRot = (fallingRotation + 1) % 4;
        if (checkPosition(fallingX, fallingY, nextRot))
            fallingRotation = nextRot;
    }

    public void tryDown() {
        if (isGameOver)
            return;
        if (checkPosition(fallingX, fallingY - 1, fallingRotation)) {
            fallingY--;
        } else {
            fixate();
        }
    }

    private void fixate() {
        for (int[] xy : fallingTetramino.coords(fallingRotation)) {
            int ex = xy[0] + fallingX;
            int ey = xy[1] + fallingY;
            fieldState[ey][ex] = fallingTetramino.ordinal();
        }
        clearRows();
        takeNext();
    }

    private void clearRows() {
        boolean[] toRemove = new boolean[FIELD_HEIGHT_HIDDEN];
        for (int y = FIELD_HEIGHT_HIDDEN - 1; y >= 0; y--) {
            int count = 0;
            for (int x = 0; x < FIELD_WIDTH; x++) {
                if (fieldState[y][x] >= 0)
                    count++;
            }
            toRemove[y] = count == FIELD_WIDTH;
        }

        int i = 0;
        for (int j = 0; j < FIELD_HEIGHT_HIDDEN; j++) {
            if(!toRemove[j]) {
                System.arraycopy(fieldState[j], 0, fieldState[i], 0, FIELD_WIDTH);
                i++;
            }
        }
        for (; i < FIELD_HEIGHT_HIDDEN; i++) {
            Arrays.fill(fieldState[i], -1);
        }
    }

    private boolean checkPosition(int x, int y, int rot) {
        for (int[] xy : fallingTetramino.coords(rot)) {
            int ex = xy[0] + x;
            int ey = xy[1] + y;
            if (ex < 0 || ey < 0 || ex >= FIELD_WIDTH || ey >= FIELD_HEIGHT_HIDDEN)
                return false;
            if (fieldState[ey][ex] >= 0)
                return false;
        }
        return true;
    }

    public Tetramino getFallingTetramino() {
        return fallingTetramino;
    }

    public Tetramino getNextTetramino() {
        return nextTetramino.peek();
    }

    public int getTetraminoIndexAt(int x, int y) {
        return fieldState[y][x];
    }

    public Tetramino getTetraminoAt(int x, int y) {
        int idx = fieldState[y][x];
        if (idx < 0)
            return null;
        return Tetramino.values()[idx];
    }

    public int getFallingRotation() {
        return fallingRotation;
    }

    public int getFallingX() {
        return fallingX;
    }

    public int getFallingY() {
        return fallingY;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
