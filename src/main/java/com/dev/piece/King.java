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
                if(tarCol-2==preCol&&tarRow==preRow&&!pieceIsOnStraightLine(tarCol,tarRow)&&getHitting(preCol+2,preRow)==null){
                    for (Piece p: GamePanel.simPieces){
                        if(p.col==preCol+3&&p.row==preRow&&p.moved==false&&this.hittingP==null){
                            GamePanel.castle=p;
                            return true;
                        }
                    }
                }
            }

            if (moved == false) {
                if(tarCol+2==preCol&&tarRow==preRow&&!pieceIsOnStraightLine(tarCol,tarRow)&&getHitting(preCol-2,preRow)==null&&getHitting(preCol-3,preRow)==null){
                    for (Piece p: GamePanel.simPieces){
                        if(p.col==preCol-4&&p.row==preRow&&p.moved==false){
                            GamePanel.castle=p;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
