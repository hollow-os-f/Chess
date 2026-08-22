package com.dev.main;

import com.dev.piece.*;

import javax.swing.*;
import java.awt.*;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable{
    public static final int WIDTH=1100;
    public static final int HEIGHT=800;

    public boolean canMove;
    public boolean vaildMove;
    public boolean gameOver=false;

    public static final int FPS=60;

    List<Piece> promotions=new ArrayList<>();
    boolean promo=false;

    public Thread gameThead;

    public Mouse mouse=new Mouse();

    public Board board=new Board();

    public static ArrayList<Piece> pieces=new ArrayList<>();
    public static ArrayList<Piece> simPieces=new ArrayList<>();

    public Piece currentPiece,checkPiece;
    public static Piece castle;

    boolean statemate=false;

    public static final int w=1;
    public static final int b=0;
    public int currentColor=w;

    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setBackground(Color.BLACK);
        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);

        //setPieces();
        test();

        copyList(pieces,simPieces);
    }

    void test(){
        //逼和（和棋）测试局面：白后走到 c7 即形成逼和
        pieces.clear();

        //白方：后在 c2，王在 e1
        pieces.add(new Queen(2,6,w));
        pieces.add(new King(4,7,w));

        //黑方：只剩一个王在 a8，被逼进角落
        pieces.add(new King(0,0,b));
    }

    public boolean isIllegal(Piece king){
        if (king==null)return false;
        if(king.type==Type.King){
            for(Piece p:simPieces){
                if(p!=king&&p.color!=king.color&&p.canMove(king.col,king.row)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canPromo(){
        if (currentPiece.type == Type.Pawn) {
            if ((currentColor == w && currentPiece.row == 0) ||( currentColor == b && currentPiece.row == 7)) {
                promotions.clear();
                promotions.add(new Queen(9,2,currentColor));
                promotions.add(new Bishop(9,3,currentColor));
                promotions.add(new Knight(9,4,currentColor));
                promotions.add(new Rook(9,5,currentColor));
                return true;
            }
        }
        return false;
    }

    public boolean isKingInCheck(){
        Piece king=getKing(true);

        if(currentPiece.canMove(king.col,king.row)){
            checkPiece=currentPiece;
            return true;
        }else {
            checkPiece=null;
        }
        return false;
    }


    public Piece getKing(boolean opponent){
        Piece king=null;

        for(Piece p:simPieces){
            if(opponent){
                if(p.type==Type.King&&p.color!=currentColor){
                    king=p;
                    break;
                }
            }else {
                if(p.type==Type.King&&p.color==currentColor){
                    king=p;
                    break;
                }
            }
        }

        return king;
    }


    public void launchGame(){
        gameThead=new Thread(this);
        gameThead.start();
    }

    public void update(){
        if(promo){
            promote();
        }else if(gameOver==false&&statemate==false) {
            if(mouse.press){
                if(currentPiece==null){
                    for(Piece i:simPieces){
                        if(currentColor==i.color
                                &&mouse.x/Board.P_SIZE==i.col
                                &&mouse.y/Board.P_SIZE==i.row){
                            currentPiece=i;
                            break;
                        }
                    }
                }else {
                    stimulate();
                }
            }else {
                if(currentPiece!=null){
                    if(vaildMove){
                        copyList(simPieces,pieces);
                        currentPiece.updatePosition();
                        if(castle!=null){
                            castle.updatePosition();
                        }
                        if(isKingInCheck()&&isCheckmate()){
                            gameOver=true;
                        }else if(isStalemate()&&!isKingInCheck()){
                            statemate=true;
                        }
                        else {
                            if(canPromo()){
                                promo=true;
                            }else {
                                changePlay();
                                currentPiece=null;
                            }
                        }

                    }else {
                        copyList(pieces,simPieces);
                        currentPiece.resetPosition();
                        currentPiece=null;
                    }

                }
            }
        }

    }

    private void promote() {
        if(mouse.press)
        {
            for(Piece p:promotions){
                if(p.col==mouse.x/Board.P_SIZE&&p.row==mouse.y/Board.P_SIZE){
                    switch (p.type){
                        case Rook -> simPieces.add(new Rook(currentPiece.col,currentPiece.row,currentColor));
                        case Bishop -> simPieces.add(new Bishop(currentPiece.col,currentPiece.row,currentColor));
                        case Knight -> simPieces.add(new Knight(currentPiece.col,currentPiece.row,currentColor));
                        case Queen -> simPieces.add(new Queen(currentPiece.col,currentPiece.row,currentColor));
                    }
                    simPieces.remove(currentPiece.getIndex());
                    copyList(simPieces,pieces);
                    currentPiece=null;
                    promo=false;
                    changePlay();
                }
            }
        }
    }


    public boolean canKillKing(){
        Piece king=getKing(false);

        for(Piece P:simPieces){
            if(P.color!=currentColor&&P.canMove(king.col,king.row)){
                return true;
            }
        }
        return false;

    }

    public void changePlay(){
        if(currentColor==w){
            currentColor=b;
            for (Piece p:simPieces){
                if(p.color==b){
                    p.twoStep=false;
                }
            }
        }else {
            currentColor=w;
            for (Piece p:simPieces){
                if(p.color==w){
                    p.twoStep=false;
                }
            }
        }
        currentPiece=null;
    }

    public void stimulate(){
        canMove=false;
        vaildMove=false;

        copyList(pieces,simPieces);
        if(castle!=null){
            castle.col= castle.preCol;
            castle.x=castle.getX(castle.col);
            castle=null;
        }


        currentPiece.x=mouse.x-Board.P_HALF_SIZE;
        currentPiece.y=mouse.y-Board.P_HALF_SIZE;

        currentPiece.col=currentPiece.getCol(currentPiece.x);
        currentPiece.row=currentPiece.getRow(currentPiece.y);

        if(currentPiece.canMove(currentPiece.col,currentPiece.row)){
            canMove=true;
            if(currentPiece.hittingP!=null){
                simPieces.remove(currentPiece.hittingP.getIndex());
            }
            checkCastling();
            if(!isIllegal(currentPiece)&&!canKillKing())vaildMove=true;
        }
    }

    public void checkCastling(){
        if(castle!=null){
            if(castle.col==0){
                castle.col+=3;
            }else if (castle.col==7){
                castle.col-=2;
            }
            castle.x=castle.getX(castle.col);
        }
    }

    public void setPieces(){
        pieces.add(new Pawn(0,6,w));
        pieces.add(new Pawn(1,6,w));
        pieces.add(new Pawn(2,6,w));
        pieces.add(new Pawn(3,6,w));
        pieces.add(new Pawn(4,6,w));
        pieces.add(new Pawn(5,6,w));
        pieces.add(new Pawn(6,6,w));
        pieces.add(new Pawn(7,6,w));
        pieces.add(new Rook(0,7,w));
        pieces.add(new Rook(7,7,w));
        pieces.add(new Knight(1,7,w));
        pieces.add(new Knight(6,7,w));
        pieces.add(new Bishop(2,7,w));
        pieces.add(new Bishop(5,7,w));
        pieces.add(new Queen(3,7,w));
        pieces.add(new King(4,7,w));

        pieces.add(new Pawn(0,1,b));
        pieces.add(new Pawn(1,1,b));
        pieces.add(new Pawn(2,1,b));
        pieces.add(new Pawn(3,1,b));
        pieces.add(new Pawn(4,1,b));
        pieces.add(new Pawn(5,1,b));
        pieces.add(new Pawn(6,1,b));
        pieces.add(new Pawn(7,1,b));
        pieces.add(new Rook(0,0,b));
        pieces.add(new Rook(7,0,b));
        pieces.add(new Knight(1,0,b));
        pieces.add(new Knight(6,0,b));
        pieces.add(new Bishop(2,0,b));
        pieces.add(new Bishop(5,0,b));
        pieces.add(new Queen(3,0,b));
        pieces.add(new King(4,0,b));
    }

    public boolean isCheckmate(){
        Piece king=getKing(true);

        if (kingCanMove(king)){
            return false;
        }else {
            int colDiff=Math.abs(checkPiece.col-king.col);
            int rowDiff=Math.abs(checkPiece.row-king.row);

            if(colDiff==0){
                if (checkPiece.row < king.row) {
                    for (int r=checkPiece.row;r<king.row;r++){
                        for (Piece p:simPieces){
                            if(p!=king&&p.color!=currentColor&&p.canMove(checkPiece.col,r)){
                                return false;
                            }
                        }
                    }
                }

                if (checkPiece.row > king.row) {
                    for (int r=checkPiece.row;r>king.row;r--){
                        for (Piece p:simPieces){
                            if(p!=king&&p.color!=currentColor&&p.canMove(checkPiece.col,r)){
                                return false;
                            }
                        }
                    }
                }

            }
            else if(rowDiff==0){
                if (checkPiece.col < king.col) {
                    for (int r=checkPiece.col;r<king.col;r++){
                        for (Piece p:simPieces){
                            if(p!=king&&p.color!=currentColor&&p.canMove(r,checkPiece.row)){
                                return false;
                            }
                        }
                    }
                }

                if (checkPiece.col > king.col) {
                    for (int r=checkPiece.col;r>king.col;r--){
                        for (Piece p:simPieces){
                            if(p!=king&&p.color!=currentColor&&p.canMove(r,checkPiece.row)){
                                return false;
                            }
                        }
                    }
                }
            }else if (colDiff==rowDiff){
                if(checkPiece.row<king.row){
                    if (checkPiece.col<king.col){
                        for (int c=checkPiece.col,r=checkPiece.row;c<king.col;c++,r++){
                            for (Piece p:simPieces){
                                if(p!=king&&p.color!=currentColor&&p.canMove(c,r)){
                                    return false;
                                }
                            }
                        }
                    }
                    if(checkPiece.col>king.col){
                        for (int c=checkPiece.col,r=checkPiece.row;c>king.col;c--,r++){
                            for (Piece p:simPieces){
                                if(p!=king&&p.color!=currentColor&&p.canMove(c,r)){
                                    return false;
                                }
                            }
                        }
                    }

                }

                if(checkPiece.row>king.row){
                    if (checkPiece.col<king.col){
                        for (int c=checkPiece.col,r=checkPiece.row;c<king.col;c++,r--){
                            for (Piece p:simPieces){
                                if(p!=king&&p.color!=currentColor&&p.canMove(c,r)){
                                    return false;
                                }
                            }
                        }

                    }
                    if(checkPiece.col>king.col){
                        for (int c=checkPiece.col,r=checkPiece.row;c>king.col;c--,r--){
                            for (Piece p:simPieces){
                                if(p!=king&&p.color!=currentColor&&p.canMove(c,r)){
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }


        return true;
    }

    public boolean kingCanMove(Piece king){
        if (isValidMove(king,1,0)){
            return true;
        }
        if (isValidMove(king,-1,0)){
            return true;
        }
        if (isValidMove(king,0,1)){
            return true;
        }
        if (isValidMove(king,0,-1)){
            return true;
        }
        if (isValidMove(king,1,1)){
            return true;
        }
        if (isValidMove(king,1,-1)){
            return true;
        }
        if (isValidMove(king,-1,1)){
            return true;
        }
        if (isValidMove(king,-1,-1)){
            return true;
        }
        return false;
    }

    public boolean isValidMove(Piece king ,int colPlus,int rowPlus){
        boolean isValidMove=false;

        king.col+=colPlus;
        king.row+=rowPlus;


        if(king.canMove(king.col,king.row)){
            if(king.hittingP!=null){
                simPieces.remove(king.hittingP.getIndex());
            }
            if(!isIllegal(king)){
                isValidMove=true;
            }
        }

        king.resetPosition();
        copyList(pieces,simPieces);
        return isValidMove;
    }

    public boolean isStalemate(){
        int count=0;
        for (Piece p:simPieces){
            if(p.color!=currentColor){
                count++;
            }
        }
        if(count==1){
            if(!kingCanMove(getKing(true))){
                return true;
            }
        }
        return false;
    }

    public void copyList(ArrayList<Piece> sou,ArrayList<Piece> tar){
        tar.clear();
        for (Piece i:sou){
            tar.add(i);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D) g;
        draw(g2);
    }

    public void draw(Graphics2D g2){
        board.draw(g2);
        for (Piece i:simPieces){
            i.draw(g2);
        }
        if(currentPiece!=null||canKillKing()){
            if(canMove&&currentPiece!=null) {
                if(isIllegal(currentPiece)||canKillKing()){
                    g2.setColor(Color.GRAY);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(currentPiece.col * Board.P_SIZE, currentPiece.row * Board.P_SIZE, Board.P_SIZE, Board.P_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }else {
                    g2.setColor(Color.WHITE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(currentPiece.col * Board.P_SIZE, currentPiece.row * Board.P_SIZE, Board.P_SIZE, Board.P_SIZE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
            }
        }

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Book Antiqua",Font.PLAIN,40));

        if(promo){
            g2.drawString("Promote to",840,150);
            for (Piece p:promotions){
                g2.drawImage(p.image,p.getX(p.col),p.getY(p.row),Board.P_SIZE,Board.P_SIZE,null);
            }
        }else {
            if(currentColor==w){
                g2.drawString("White turning",830,550);
                if(checkPiece!=null){
                    g2.setColor(Color.RED);
                    g2.drawString("King",840,650);
                    g2.drawString("is in check",840,700);
                }
            }else {
                g2.drawString("Black turning",830,250);
                if(checkPiece!=null){
                    g2.setColor(Color.RED);
                    g2.drawString("King",840,100);
                    g2.drawString("is in check",840,150);
                }
            }
        }

        if(gameOver==true){
            String s="";
            if(currentColor==w){
                s="White Win";
            }else {
                s="Black Win";
            }
            g2.setFont(new Font("Arial",Font.PLAIN,90));
            g2.setColor(Color.GREEN);
            g2.drawString(s,200,240);
        }
        if(statemate){
            g2.setFont(new Font("Arial",Font.PLAIN,90));
            g2.setColor(Color.GRAY);
            g2.drawString("Statlemate",200,500);
        }
    }


    @Override
    public void run() {
        double inv=1000000000.0/FPS;
        long currentTime;
        long lastTime=System.nanoTime();
        double delta=0;

        while (gameThead != null) {
            currentTime=System.nanoTime();
            delta+=(currentTime-lastTime)/inv;
            lastTime=currentTime;

            if(delta>1){
                delta-=1;
                update();
                repaint();
            }
        }
    }
}
