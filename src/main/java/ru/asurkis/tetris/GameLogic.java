package ru.asurkis.tetris;

import java.util.*;

public class GameLogic {
    public static final int FIELD_WIDTH = 10;
    public static final int FIELD_HEIGHT_VISIBLE = 20;
    public static final int FIELD_HEIGHT_HIDDEN = 24;

    private final List<Runnable> stateListeners = new ArrayList<>();
    private final Random random;

    private Tetramino fallingTetramino;
    private int fallingX;
    private int fallingY;
    private int fallingRotation;
    private final Queue<Tetramino> nextTetramino = new ArrayDeque<>();
    private final int[][] fieldState = new int[FIELD_HEIGHT_HIDDEN][FIELD_WIDTH];

    private boolean isPaused;
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
        fireUpdateState();
    }

    private void takeNext() {
        refillQueue();
        fallingTetramino = nextTetramino.remove();
        refillQueue();
        fallingY = 20;
        fallingRotation = 0;
        fallingX = 5;
        if (!checkPosition(fallingX, fallingY, fallingRotation)) {
            isGameOver = true;
            fireUpdateState();
        }
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
        if (!isGameOver && !isPaused)
            tryDown();
    }

    public void togglePause() {
        isPaused ^= true;
        fireUpdateState();
    }

    public void tryLeft() {
        if (isGameOver || isPaused)
            return;
        if (checkPosition(fallingX - 1, fallingY, fallingRotation)) {
            fallingX--;
            fireUpdateState();
        }
    }

    public void tryRight() {
        if (isGameOver || isPaused)
            return;
        if (checkPosition(fallingX + 1, fallingY, fallingRotation)) {
            fallingX++;
            fireUpdateState();
        }
    }

    public void tryRotate() {
        if (isGameOver || isPaused)
            return;
        fallingRotation = (fallingRotation + 1) % 4;
        fireUpdateState();
    }

    public void tryDown() {
        if (isGameOver || isPaused)
            return;
        if (checkPosition(fallingX, fallingY - 1, fallingRotation)) {
            fallingY--;
        } else {
            fixate();
        }
        fireUpdateState();
    }

    private void fixate() {
        for (int[] xy : fallingTetramino.coords(fallingRotation)) {
            int ex = xy[0] + fallingX;
            int ey = xy[1] + fallingY;
            fieldState[ey][ex] = fallingTetramino.ordinal();
        }
        takeNext();
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

    public void addStateListener(Runnable listener) {
        synchronized (stateListeners) {
            stateListeners.add(listener);
        }
    }

    public void removeStateListener(Runnable listener) {
        synchronized (stateListeners) {
            stateListeners.remove(listener);
        }
    }

    private void fireUpdateState() {
        synchronized (stateListeners) {
            for (Runnable listener : stateListeners) {
                listener.run();
            }
        }
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

    public boolean isGamePaused() {
        return isPaused;
    }
}
