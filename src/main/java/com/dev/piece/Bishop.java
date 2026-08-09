package com.dev.piece;

import com.dev.main.Type;

import static com.dev.main.GamePanel.w;

public class Bishop extends Piece{
    public Bishop(int col, int row, int color) {
        super(col, row, color);
        type= Type.Bishop;
        if(color== w){
            image=getImage("/piece/w-bishop");
        }else {
            image=getImage("/piece/b-bishop");
        }
    }

    @Override
    public boolean canMove(int tarCol, int tarRow) {
        if (isWithBoard(tarCol,tarRow)&&!isSameSquare(tarCol,tarRow)){
            if(Math.abs(tarCol-preCol)==Math.abs(tarRow-preRow)){
                if(vaildHitting(tarCol,tarRow)&&!pieceIsOnDiagonalLine(tarCol,tarRow)){
                    return true;
                }
            }
        }
        return false;
    }
}
