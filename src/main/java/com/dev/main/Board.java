package com.dev.main;

import java.awt.*;

public class Board {
    final int COL=8;
    final int ROW=8;
    public static final int P_SIZE=100;
    public static final int P_HALF_SIZE=P_SIZE/2;

    public void draw(Graphics2D  g2){
        int c=0;
        for(int i=0;i<ROW;i++){
            for(int j=0;j<COL;j++){
                if(c==0){
                    g2.setColor(new Color(210,165,125));
                    c=1;
                }else {
                    g2.setColor(new Color(175,115,70));
                    c=0;
                }
                g2.fillRect(j*P_SIZE,i*P_SIZE,P_SIZE,P_SIZE);
            }
            if(c==0){
                c=1;
            }else {
                c=0;
            }
        }
    }
}
