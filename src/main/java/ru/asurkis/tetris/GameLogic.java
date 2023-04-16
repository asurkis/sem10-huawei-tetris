package ru.asurkis.tetris;

import java.util.*;

public class GameLogic {
    public static final int FIELD_WIDTH = 10;
    public static final int FIELD_HEIGHT_VISIBLE = 20;
    public static final int FIELD_HEIGHT_HIDDEN = 24;

    private final List<Runnable> stateListeners = new ArrayList<>();
    private final Random random;

    private Tetramino currentTetramino;
    private int currentX;
    private int currentY;
    private int currentQuarter;
    private int currentRotation;
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
        isGameOver = false;
        nextTetramino.clear();
        refillQueue();
        for (int i = 0; i < FIELD_HEIGHT_HIDDEN; i++) {
            Arrays.fill(fieldState[i], -1);
        }
    }

    private void takeNext() {
        refillQueue();
        currentTetramino = nextTetramino.remove();
        refillQueue();
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
        if (isGameOver || isPaused)
            return;
    }

    public void togglePause() {
        isPaused ^= true;
    }

    public void tryLeft() {}

    public void tryRight() {}

    public void tryRotate() {}

    public void tryDown() {}

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

    public Tetramino getCurrentTetramino() {
        return currentTetramino;
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

    public int getCurrentRotation() {
        return currentRotation;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
