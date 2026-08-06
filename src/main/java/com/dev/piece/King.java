package com.dev.piece;

import com.dev.main.GamePanel;

import static com.dev.main.GamePanel.w;

public class King extends Piece{
    public King(int col, int row, int color) {
        super(col, row, color);
        if(color== w){
            image=getImage("/piece/w-king");
        }else {
            image=getImage("/piece/b-king");
        }
    }


    public boolean canMove(int tarCol,int tarRow){
        if(isWithBoard(tarCol,tarRow)&&!isSameSquare(tarCol,tarRow)){
            if(Math.abs(tarCol-preCol)+Math.abs(tarRow-preRow)==1||Math.abs(tarCol-preCol)*Math.abs(tarRow-preRow)==1){
                if(vaildHitting(tarCol,tarRow)){
                    return true;
                }
            }

            if (moved == false) {
                if(tarCol-2==preCol&&tarRow==preRow&&pieceIsOnStraightLine(tarCol,tarRow)){
                    for (Piece p: GamePanel.simPieces){
                        if(p.col==preCol+3&&p.row==preRow&&p.moved==false){
                            return true;
                        }
                    }
                }
            }

            if (moved == false) {
                if(tarCol+2==preCol&&tarRow==preRow&&pieceIsOnStraightLine(tarCol,tarRow)){
                    Piece[] ps=new Piece[2];
                    for (Piece p: GamePanel.simPieces){
                        if(p.col==preCol-3&&p.row==preRow){
                            ps[0]=p;
                        }
                        if(p.col==preCol-4&&p.row==preRow){
                            ps[1]=p;
                        }
                        if(ps[0]==null&&ps[1].moved==false){
                            GamePanel.castle=ps[1];
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
