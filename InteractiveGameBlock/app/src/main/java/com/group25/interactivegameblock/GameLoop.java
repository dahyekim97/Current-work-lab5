package com.group25.interactivegameblock;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Random;
import java.util.TimerTask;

/**
 * Created by keith on 2017-01-31.
 */

public class GameLoop extends TimerTask{

    private boolean end_game = false;


    public enum eDir{LEFT, RIGHT, UP, DOWN, NM}; //NM (No Movement)

    public static eDir myDirection;

    private RelativeLayout gameloopRL;
    private Context gameloopCTX;
    private Activity thisActivity;

    public static final int LEFT_BOUNDARY = 0;
    public static final int UP_BOUNDARY = 0;
    public static final int SLOT_ISOLATION = 270;

    public static final int RIGHT_BOUNDARY = LEFT_BOUNDARY + 3*SLOT_ISOLATION;
    public static final int DOWN_BOUNDARY = UP_BOUNDARY + 3*SLOT_ISOLATION;

    public LinkedList<GameBlock> myGBList;

    private Random myRandomGen;

    private void createBlock(){

        myRandomGen = new Random();

        int[] myCoord = {myRandomGen.nextInt(4)*SLOT_ISOLATION + LEFT_BOUNDARY,
                myRandomGen.nextInt(4)*SLOT_ISOLATION + UP_BOUNDARY};

        //isSet is for checking if I set the coordinates successfully(to go out of while loop)
        boolean isSet = false;
        //randomly generate new block's coordinate {x,y}
        int tempCoorX = myRandomGen.nextInt(4)*SLOT_ISOLATION + LEFT_BOUNDARY;
        int tempCoorY = myRandomGen.nextInt(4)*SLOT_ISOLATION + UP_BOUNDARY;
        //and check if the tempCoors exist already or not
        //if exist, regenerate random coordinate
        while(!isSet){
        if(!isOccupied(tempCoorX, tempCoorY)){
            myCoord[0] = tempCoorX;
            myCoord[1] = tempCoorY;
            isSet = true;
        }else {
            while (isOccupied(tempCoorX, tempCoorY)) {

                tempCoorX = myRandomGen.nextInt(4) * SLOT_ISOLATION + LEFT_BOUNDARY;
                tempCoorY = myRandomGen.nextInt(4) * SLOT_ISOLATION + UP_BOUNDARY;
            }
        }
        }


        //Create a Game Block on the board
        GameBlock newBlock = new GameBlock(gameloopCTX, gameloopRL, myCoord[0], myCoord[1], this);

        myGBList.add(newBlock);

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
        if(myDirection != targetDir) {
            myDirection = targetDir;
            for(GameBlock gb : myGBList)
                gb.setDestination(targetDir);
        }
        createBlock();

    }

    public boolean isOccupied(int coordX, int coordY){

        int[] checkCoord = new int[2];

        for(GameBlock gb : myGBList){
            checkCoord = gb.getCoordinate();
            if(checkCoord[0] == coordX && checkCoord[1] == coordY){
                Log.d("Game Loop Report: ", "Occupant Found!");
                return true;
            }
        }

        return false;

    }


    public void run(){

        thisActivity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        for (GameBlock gb : myGBList) {
                            Log.d("AAA", "AAA is called");

                            gb.move();
                        }
                    }
                }
        );
    }

}