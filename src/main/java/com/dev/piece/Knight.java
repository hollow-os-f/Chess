package com.dev.piece;

import static com.dev.main.GamePanel.w;

public class Knight extends Piece{
    public Knight(int col, int row, int color) {
        super(col, row, color);
        if(color== w){
            image=getImage("/piece/w-knight");
        }else {
            image=getImage("/piece/b-knight");
        }
    }

    @Override
    public boolean canMove(int tarCol, int tarRow) {
        if(isWithBoard(tarCol,tarRow)&&!isSameSquare(tarCol,tarRow)){
            if(Math.abs(tarCol-preCol)*Math.abs(tarRow-preRow)==2){
                if(vaildHitting(tarCol,tarRow)){
                    return true;
                }
            }
        }
        return false;
    }
}