package ru.asurkis.tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener {
    private final GameLogic game;

    MyKeyListener(GameLogic game) {
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> game.tryLeft();
            case KeyEvent.VK_RIGHT -> game.tryRight();
            case KeyEvent.VK_UP -> game.tryRotate();
            case KeyEvent.VK_DOWN -> game.tryDown();
            case KeyEvent.VK_SPACE -> game.togglePause();
            case KeyEvent.VK_R -> game.reset();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
