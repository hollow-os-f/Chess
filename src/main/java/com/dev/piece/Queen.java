package com.dev.piece;

import com.dev.main.Type;

import static com.dev.main.GamePanel.w;

public class Queen extends Piece{
    public Queen(int col, int row, int color) {
        super(col, row, color);
        type= Type.Queen;
        if(color== w){
            image=getImage("/piece/w-queen");
        }else {
            image=getImage("/piece/b-queen");
        }
    }

    @Override
    public boolean canMove(int tarCol, int tarRow) {
        if(isWithBoard(tarCol,tarRow)&&!isSameSquare(tarCol,tarRow)){
            if(Math.abs(tarCol-preCol)==Math.abs(tarRow-preRow)){
                if(vaildHitting(tarCol,tarRow)&&!pieceIsOnDiagonalLine(tarCol,tarRow)){
                    return true;
                }
            }
            if(tarCol==preCol||tarRow==preRow){
                if(vaildHitting(tarCol,tarRow)&&!pieceIsOnStraightLine(tarCol,tarRow)){
                    return true;
                }
            }
        }
        return false;
    }
}
