package com.dev.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {
    public boolean press=false;
    public int x,y;

    @Override
    public void mouseMoved(MouseEvent e) {
        x=e.getX();
        y=e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        press=true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        press=false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x=e.getX();
        y=e.getY();
    }
}
