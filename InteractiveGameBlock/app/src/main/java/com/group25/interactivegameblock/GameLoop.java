package com.group25.interactivegameblock;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.TimerTask;

/**
 * Created by keith on 2017-01-31.
 */

public class GameLoop extends TimerTask{

    //to check if the game is ended, end_game is true when the existing block's number is 16.
    private boolean end_game = false;
    //win is true when there is a block which has a number over 8.
    private boolean win = false;

    //to count the number of existing blocks
    private int storeCount = 0;

    public enum eDir{LEFT, RIGHT, UP, DOWN, NM}; //NM (No Movement)

    public static eDir myDirection;

    private RelativeLayout gameloopRL;
    private Context gameloopCTX;
    private Activity thisActivity;

    public static final int LEFT_BOUNDARY = 0;
    public static final int UP_BOUNDARY = 0;
    public static final int SLOT_ISOLATION = 270; // 1080 / 4

    public static final int RIGHT_BOUNDARY = LEFT_BOUNDARY + 3*SLOT_ISOLATION;
    public static final int DOWN_BOUNDARY = UP_BOUNDARY + 3*SLOT_ISOLATION;

    public static LinkedList<GameBlock> myGBList;

    private Random myRandomGen;



    private void createBlock() {

        boolean thereIsSpace = false;

        int tempArray[] = new int[16];

        int i = 0;
        //initializing array with 0
        for(int q = 0; q < 16 ; q++){
            tempArray[q] = 0;
        }

        //tempArray stores the numbers of existing blocks in the board
        for (GameBlock gb : myGBList) {
            tempArray[i] = Integer.parseInt(""+gb.getTextOfTV());
            i++;
        }

        //checking if there is any empty slot in the board
        int count = 1;
        for(int k = 0; k < 16 ; k++){
            //if(tempArray[k]  ==  0) is true when tempArray[k] is empty
            if(tempArray[k]  ==  0){
                thereIsSpace = true;
            }else if(tempArray[k]>0){
                //tempArray[k]>0 is not 0 that means that there is a block
                //so it is counting the number of blocks in the board
                count++;
            }
            if(tempArray[k]  >=  8){
                win = true;

            }

        }

        storeCount = count ;

        //if the number of blocks in the board is 16, which means the board is full, game is over.
        if(storeCount >= 16){
            end_game = true;
            Log.d("GGGAMEOVER","Game over!!!!");
            thereIsSpace = false;
        }

        //if the board still has some slots empty, we allow it to create a new block
        if(thereIsSpace){
        myRandomGen = new Random();

        int[] myCoord = {myRandomGen.nextInt(4) * SLOT_ISOLATION + LEFT_BOUNDARY,
                myRandomGen.nextInt(4) * SLOT_ISOLATION + UP_BOUNDARY};

        //isSet is for checking if I set the coordinates successfully(to go out of while loop)
        boolean isSet = false;
        //randomly generate new block's coordinate {x,y}
        int tempCoorX = myRandomGen.nextInt(4) * SLOT_ISOLATION + LEFT_BOUNDARY;
        int tempCoorY = myRandomGen.nextInt(4) * SLOT_ISOLATION + UP_BOUNDARY;
        //and check if the tempCoors exist already or not
        //if exist, regenerate random coordinate
        while (!isSet) {
            if (!isOccupied(tempCoorX, tempCoorY)) {
                myCoord[0] = tempCoorX;
                myCoord[1] = tempCoorY;
                isSet = true;
            } else {
                while (isOccupied(tempCoorX, tempCoorY)) {
                    tempCoorX = myRandomGen.nextInt(4) * SLOT_ISOLATION + LEFT_BOUNDARY;
                    tempCoorY = myRandomGen.nextInt(4) * SLOT_ISOLATION + UP_BOUNDARY;
                }
            }
        }


        //Create a Game Block on the board
        GameBlock newBlock = new GameBlock(gameloopCTX, gameloopRL, myCoord[0], myCoord[1], this);

        myGBList.add(newBlock);

    }else{
            //there is no space on the board
            end_game = true;
        }
    }


    public GameLoop(Activity myActivity, RelativeLayout rl, Context ctx){

        thisActivity = myActivity;
        gameloopCTX = ctx;
        gameloopRL = rl;
        myDirection = eDir.NM;

        myGBList = new LinkedList<GameBlock>();

        createBlock();
    }

    //This method is used by the Accelerometer Handler to change the game direction
    public void setDirection(eDir targetDir){


        if(end_game){
            Log.d("Game over", "Game is over");

        }
        if(win){
            Log.d("WIN", "You win!");

        }

            myDirection = targetDir;
            for(GameBlock gb : myGBList) {
                gb.setDestination(targetDir);


            }

        createBlock();
    }

    public boolean isOccupied(int coordX, int coordY){

        int[] checkCoord = new int[2];

        for(GameBlock gb : myGBList){
            checkCoord = gb.getTargetCoordinate();
            if(checkCoord[0] == coordX && checkCoord[1] == coordY){
                Log.d("Game Loop Report: ", "Occupant Found!");
                return true;
            }
        }

        return false;

    }

    private int getIndexOfBlock(int x, int y){
        int temp = 0;
        for(GameBlock gb : myGBList){
            if(gb.getCoordinate()[1] == y*GameLoop.SLOT_ISOLATION && gb.getCoordinate()[0]== x*GameLoop.SLOT_ISOLATION){
                temp = myGBList.indexOf(gb);
            }
        }
        return  temp;
    }

    private void removeThings() {

        LinkedList<GameBlock> tempGBList = new LinkedList<GameBlock>();

        //copying the gameblocklist to tempGBList
        for (GameBlock gb : myGBList) {
            if (gb.toBeRemoved) {
                tempGBList.add(gb);
            }
        }

        //remove marked blocks from screen
        for (GameBlock gb : tempGBList) {
            gb.destroyMe();
            myGBList.remove(gb);
        }


        //remove the block from the list
        for (Iterator<GameBlock> iter = myGBList.iterator(); iter.hasNext(); ) {
            GameBlock data = iter.next();
            if (data.toBeRemoved) {
                iter.remove();
            }

        }

    }







    public void run(){

        thisActivity.runOnUiThread(
                new Runnable() {
                    public void run() {

                        for (GameBlock gb : myGBList) {

                            gb.move();
                        }

                        removeThings();
                    }
                }
        );
    }
}