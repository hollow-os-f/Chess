package com.dev.piece;

import static com.dev.main.GamePanel.w;

public class Queen extends Piece{
    public Queen(int col, int row, int color) {
        super(col, row, color);
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
            if(preCol==col||preRow==row){
                if(vaildHitting(tarCol,tarRow)&&!pieceIsOnStraightLine(tarCol,tarRow)){
                    return true;
                }
            }
        }
        return false;
    }
}
