package com.dev.piece;

import com.dev.main.Board;
import com.dev.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Piece {
    public BufferedImage image;
    public int x,y;
    public int col,row,preCol,preRow;
    public int color;
    public Piece hittingP;
    public boolean moved;



    public Piece(int col, int row, int color) {
        this.col = col;
        this.row = row;
        this.color = color;
        x=getX(col);
        y=getY(row);
        preCol=col;
        preRow=row;
    }

    public BufferedImage getImage(String path){
        BufferedImage image1;

        try {
            image1= ImageIO.read(getClass().getResourceAsStream(path+".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image1;
    }

    private int getX(int col){
        return col* Board.P_SIZE;
    }
    private int getY(int row){
        return row*Board.P_SIZE;
    }

    public int getCol(int x){
        return (x+Board.P_HALF_SIZE)/Board.P_SIZE;
    }
    public int getRow(int y){
        return (y+Board.P_HALF_SIZE)/Board.P_SIZE;
    }

    public boolean isWithBoard(int col,int row){
        return col>=0&&col<8&&row>=0&&row<8;
    }

    public int getIndex(){
        for(int i=0;i<GamePanel.simPieces.size();i++){
            if(GamePanel.simPieces.get(i)==this){
                return i;
            }
        }
        return -1;
    }

    public void updatePosition(){
        x=getX(col);
        y=getY(row);
        preCol=getCol(x);
        preRow=getRow(y);
        moved=true;
    }

    public boolean canMove(int tarCol,int tarRow){
        return false;
    }
    public void draw(Graphics2D g2){
        g2.drawImage(image,x,y,Board.P_SIZE,Board.P_SIZE,null);
    }

    public void resetPosition() {
        x=getX(preCol);
        y=getY(preRow);
        col=preCol;
        row=preRow;
    }

    public Piece getHitting(int tarCol,int tarRow){
        for(Piece i: GamePanel.simPieces){
            if(i.row==tarRow&&i.col==tarCol&&i!=this){
                return i;
            }
        }
        return null;
    }

    public boolean pieceIsOnDiagonalLine(int tarCol,int tarRow){
        if(tarRow<preRow){
            for(int c=preCol-1;c>tarCol;c--){
                int diff=Math.abs(c-preCol);
                for(Piece p:GamePanel.simPieces){
                    if(p.col==c&&p.row==preRow-diff){
                        hittingP=p;
                        return true;
                    }
                }
            }

            for(int c=preCol+1;c<tarCol;c++){
                int diff=Math.abs(c-preCol);
                for(Piece p:GamePanel.simPieces){
                    if(p.col==c&&p.row==preRow-diff){
                        hittingP=p;
                        return true;
                    }
                }
            }
        }

        if(tarRow>preRow){
            for(int c=preCol-1;c>tarCol;c--){
                int diff=Math.abs(c-preCol);
                for (Piece p:GamePanel.simPieces){
                    if(p.col==c&&p.row==preRow+diff){
                        hittingP=p;
                        return true;
                    }
                }
            }

            for(int c=preCol+1;c<tarCol;c++){
                int diff=Math.abs(c-preCol);
                for(Piece p:GamePanel.simPieces){
                    if(p.col==c&&p.row==preRow+diff){
                        hittingP=p;
                        return true;
                    }
                }
            }
        }
        return false;
    }


    //这个给车用
    public boolean pieceIsOnStraightLine(int tarCol,int tarRow){
        //向右走
        for(int c=preCol-1;c>tarCol;c--){
            for(Piece p:GamePanel.simPieces){
                if(p.col==c&&p.row==tarRow){
                    hittingP=p;
                    return true;
                }
            }
        }

        for(int c=preCol+1;c<tarCol;c++){
            for(Piece p:GamePanel.simPieces){
                if(p.col==c&&p.row==tarRow){
                    hittingP=p;
                    return true;
                }
            }
        }

        for(int r=preRow+1;r<tarRow;r++){
            for(Piece p:GamePanel.simPieces){
                if(p.row==r&&p.col==tarCol){
                    hittingP=p;
                    return true;
                }
            }
        }

        for(int r=preRow-1;r>tarRow;r--){
            for(Piece p:GamePanel.simPieces){
                if(p.row==r&&p.col==tarCol){
                    hittingP=p;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSameSquare(int tarCol,int tarCow){
        if(tarCol==preCol&&tarCow==preRow){
            return true;
        }
        return false;
    }

    public boolean vaildHitting(int tarCol,int tarRow){
        hittingP=getHitting(tarCol,tarRow);
        if(hittingP==null){
            return true;
        }else {
            if(hittingP.color!=this.color){
                return true;
            }else {
                hittingP=null;
            }
        }
        return false;
    }
}
