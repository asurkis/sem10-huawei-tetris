package ru.asurkis.tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Queue;

public class MyKeyListener implements KeyListener {
    Application application;

    MyKeyListener(Application application) {
        this.application = application;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> application.feedEvent(InputEvent.LEFT);
            case KeyEvent.VK_RIGHT -> application.feedEvent(InputEvent.RIGHT);
            case KeyEvent.VK_UP -> application.feedEvent(InputEvent.ROTATE);
            case KeyEvent.VK_DOWN -> application.feedEvent(InputEvent.DOWN);
            case KeyEvent.VK_SPACE -> application.feedEvent(InputEvent.PAUSE);
            case KeyEvent.VK_R -> application.feedEvent(InputEvent.RESET);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
