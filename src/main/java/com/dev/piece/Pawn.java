package com.dev.piece;

import com.dev.main.GamePanel;

import static com.dev.main.GamePanel.w;

public class Pawn extends Piece{
    public Pawn(int col, int row, int color) {
        super(col, row, color);
        if(color== w){
            image=getImage("/piece/w-pawn");
        }else {
            image=getImage("/piece/b-pawn");
        }
    }

    @Override
    public boolean canMove(int tarCol, int tarRow) {
        if(isWithBoard(tarCol,tarRow)&&!isSameSquare(tarCol,tarRow)){
            int moveValue;
            if(color== w){
                moveValue=-1;
            }else {
                moveValue=1;
            }
            hittingP=getHitting(tarCol,tarRow);
            if(tarCol==preCol&&tarRow==preRow+moveValue&&hittingP==null){
                return true;
            }
            if(tarCol==preCol&&tarRow==preRow+2*moveValue&&!pieceIsOnStraightLine(tarCol,tarRow)&&moved==false&&hittingP==null){
                return true;
            }
            if(Math.abs(tarCol-preCol)==1&&tarRow==preRow+moveValue&&hittingP!=null&&hittingP.color!=color){
                return true;
            }

            return false;
        }
        return false;
    }
}
