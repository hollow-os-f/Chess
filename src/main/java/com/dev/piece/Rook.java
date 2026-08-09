package com.dev.piece;

import com.dev.main.Type;

import static com.dev.main.GamePanel.w;

public class Rook extends Piece{
    public Rook(int col, int row, int color) {
        super(col, row, color);
        type= Type.Rook;
        if(color== w){
            image=getImage("/piece/w-rook");
        }else {
            image=getImage("/piece/b-rook");
        }
    }

    @Override
    public boolean canMove(int tarCol, int tarRow) {
        if(isWithBoard(tarCol,tarRow)&&!isSameSquare(tarCol,tarRow)){
            if(tarCol==preCol||tarRow==preRow){
                if(vaildHitting(tarCol,tarRow)&&!pieceIsOnStraightLine(tarCol,tarRow)){
                    return true;
                }
            }
        }
        return false;
    }
}
