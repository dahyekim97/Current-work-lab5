package com.group25.interactivegameblock;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;
@TargetApi(11)
/**
 * Created by keith on 2017-01-31.
 */

public class GameBlock extends GameBlockTemplate {

    private final float GB_ACC = 4.0f;
    private final float IMAGE_SCALE = 1.0f;
    private final int TV_OFFSET = 25;
    private int myCoordX;
    private int myCoordY;
    private int targetCoordX;
    private int targetCoordY;

    private GameLoop myGL;

    private TextView myTV;
    private int blockNumber;

    private int myVelocity;
    private GameLoop.eDir targetDirection;


    public GameBlock(Context gbCTX, RelativeLayout gbRL, int coordX, int coordY, GameLoop gbGL){

        super(gbCTX);
        this.setImageResource(R.drawable.gameblock);

        this.setX(coordX);
        this.setY(coordY);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        gbRL.setBackgroundColor(Color.WHITE);
        gbRL.setBackgroundResource(R.drawable.gameboard);
        gbRL.getLayoutParams().width = 1080;
        gbRL.getLayoutParams().height = 1080;
        gbRL.addView(this);
        //this.bringToFront();

        this.myGL = gbGL;

        Random myRandomGen = new Random();
        //initial block number can be either 2 or 4
        blockNumber = (myRandomGen.nextInt(2) + 1) * 2;

        myCoordX = coordX;
        myCoordY = coordY;
        targetCoordX = myCoordX;
        targetCoordY = myCoordY;
        myVelocity = 0;

        myTV = new TextView(gbCTX);
        myTV.setX(coordX + TV_OFFSET);
        myTV.setY(coordY + TV_OFFSET);
        myTV.setText(String.format("%d", blockNumber));
        myTV.setTextSize(40.0f);
        myTV.setTextColor(Color.BLACK);

        gbRL.addView(myTV);
        myTV.bringToFront();

        targetDirection = GameLoop.eDir.NM;

        Log.d("GameBLock: ", "My Position is: " + myCoordX + "," + myCoordY);

    }

    //Allowing blocks to report is own location
    public int[] getCoordinate(){
        int[] thisCoord = new int[2];
        thisCoord[0] = myCoordX;
        thisCoord[1] = myCoordY;
        return thisCoord;
    }

    public void getBlockNum(){

    }
    @Override
    public void setDestination(GameLoop.eDir thisDir){


        targetDirection = thisDir;

        int testCoord;
        int numOfOccupants;
        int slotCount;

        int tempArray[] = new int[4];
        tempArray[0]= 0;
        tempArray[1]= 0;
        tempArray[2]= 0;
        tempArray[3]= 0;

        char currentDir = ' ';

        switch(thisDir){

            case LEFT:
                currentDir = 'L';
                for(GameBlock gb : myGL.myGBList){
                    if(gb.myCoordY == this.myCoordY){
                        int i = gb.myCoordX/GameLoop.SLOT_ISOLATION;
                        tempArray[i] = Integer.parseInt(""+gb.myTV.getText());
                    }
                }

                testCoord = GameLoop.LEFT_BOUNDARY;
                numOfOccupants = 0;
                slotCount = 0;

                // check until the test coordinate becomes the current block's coordinate
                while(testCoord != myCoordX){

                    Log.d("Game Block Test Point", String.format("%d", testCoord));

                    if(myGL.isOccupied(testCoord, myCoordY)){
                        numOfOccupants++;
                    }
                    slotCount++ ;
                    //check the next space
                    testCoord += GameLoop.SLOT_ISOLATION;
                }
                Log.d("Slot Count", String.format("%d", slotCount)+" Num of Occupants"+ String.format("%d", numOfOccupants));


                for(GameBlock gb : myGL.myGBList){

                    //catch the closest block
                    if(GameLoop.LEFT_BOUNDARY + (slotCount - numOfOccupants-1)* GameLoop.SLOT_ISOLATION == gb.myCoordX && myCoordY == gb.myCoordY){

                        //check the closest block's number
                        if(gb.myTV.getText() == this.myTV.getText()){
                            //remove the closest block if the bumebr is equal to the current block
                            myGL.myGBList.remove(myGL.myGBList.indexOf(gb));
                            //double the number
                            this.myTV.setText((Integer.parseInt(""+this.myTV.getText()))*2+"");
                            //set the coord
                            targetCoordX = GameLoop.LEFT_BOUNDARY + (slotCount - numOfOccupants-1)* GameLoop.SLOT_ISOLATION;
                        }else {
                            //if the closest number is not the same as current number
                            targetCoordX = GameLoop.LEFT_BOUNDARY + (slotCount - numOfOccupants) * GameLoop.SLOT_ISOLATION;
                        }

                    }
                }


                break;
            case RIGHT:
                currentDir = 'R';
                for(GameBlock gb : myGL.myGBList){
                    if(gb.myCoordY == this.myCoordY){
                        int i = gb.myCoordX/GameLoop.SLOT_ISOLATION;
                        tempArray[i] = Integer.parseInt(""+gb.myTV.getText());
                    }
                }
                testCoord = GameLoop.RIGHT_BOUNDARY;
                numOfOccupants = 0;
                slotCount = 0;
                while(testCoord != myCoordX){

                    Log.d("Game Block Test Point", String.format("%d", testCoord));

                    if(myGL.isOccupied(testCoord, myCoordY)){
                        numOfOccupants++;
                    }
                    slotCount++ ;
                    testCoord -= GameLoop.SLOT_ISOLATION;
                }

                for(GameBlock gb : myGL.myGBList){

                    //catch the closest block
                    if(GameLoop.RIGHT_BOUNDARY - (slotCount - numOfOccupants-1)* GameLoop.SLOT_ISOLATION == gb.myCoordX && myCoordY == gb.myCoordY){

                        //check the closest block's number
                        if(gb.myTV.getText() == this.myTV.getText()){
                            //remove the closest block if the bumebr is equal to the current block
                            myGL.myGBList.remove(myGL.myGBList.indexOf(gb));
                            //double the number
                            this.myTV.setText((Integer.parseInt(""+this.myTV.getText()))*2+"");
                            //set the coord
                            Log.d("ZXZ", "ZZZZZZ");

                            targetCoordX = GameLoop.RIGHT_BOUNDARY - (slotCount - numOfOccupants-1) * GameLoop.SLOT_ISOLATION;
                        }else {
                            //if the closest number is not the same as current number
                            targetCoordX = GameLoop.RIGHT_BOUNDARY - (slotCount - numOfOccupants) * GameLoop.SLOT_ISOLATION;
                        }

                    }
                }


                Log.d("Game Block Report: ", String.format("Target X Coord: %d", targetCoordX));

                break;
            case UP:
                currentDir = 'U';
                for(GameBlock gb : myGL.myGBList){
                    if(gb.myCoordX == this.myCoordX){
                        int i = gb.myCoordY/GameLoop.SLOT_ISOLATION;
                        tempArray[i] = Integer.parseInt(""+gb.myTV.getText());
                    }
                }
                testCoord = GameLoop.UP_BOUNDARY;
                numOfOccupants = 0;
                slotCount = 0;
                while(testCoord != myCoordY){

                    Log.d("Game Block Test Point", String.format("%d", testCoord));

                    if(myGL.isOccupied(myCoordX, testCoord)){
                        numOfOccupants++;
                    }
                    slotCount++ ;
                    testCoord += GameLoop.SLOT_ISOLATION;
                }


                for(GameBlock gb : myGL.myGBList){

                    //catch the closest block
                    if(GameLoop.UP_BOUNDARY + (slotCount - numOfOccupants-1)* GameLoop.SLOT_ISOLATION == gb.myCoordY && this.myCoordX == gb.myCoordX){

                        //check the closest block's number
                        if(gb.myTV.getText() == this.myTV.getText()){
                            //remove the closest block if the bumebr is equal to the current block
                            myGL.myGBList.remove(myGL.myGBList.indexOf(gb));
                            //double the number
                            this.myTV.setText((Integer.parseInt(""+this.myTV.getText()))*2+"");
                            //set the coord
                            targetCoordY = GameLoop.UP_BOUNDARY + (slotCount - numOfOccupants-1) * GameLoop.SLOT_ISOLATION;
                        }else {
                            //if the closest number is not the same as current number
                            targetCoordY = GameLoop.UP_BOUNDARY + (slotCount - numOfOccupants) * GameLoop.SLOT_ISOLATION;
                        }

                    }
                }



                Log.d("Game Block Report: ", String.format("Target Y Coord: %d", targetCoordY));

                break;
            case DOWN:
                currentDir = 'D';
                for(GameBlock gb : myGL.myGBList){
                    if(gb.myCoordX == this.myCoordX){
                        int i = gb.myCoordY/GameLoop.SLOT_ISOLATION;
                        tempArray[i] = Integer.parseInt(""+gb.myTV.getText());
                    }
                }

                testCoord = GameLoop.DOWN_BOUNDARY;
                numOfOccupants = 0;
                slotCount = 0;
                while(testCoord != myCoordY){

                    Log.d("Game Block Test Point", String.format("%d", testCoord));

                    if(myGL.isOccupied(myCoordX, testCoord) ){
                        numOfOccupants++;
                    }
                    slotCount++ ;
                    testCoord -= GameLoop.SLOT_ISOLATION;
                }



                for(GameBlock gb : myGL.myGBList){

                    //catch the closest block
                    if(GameLoop.DOWN_BOUNDARY - (slotCount - numOfOccupants-1)* GameLoop.SLOT_ISOLATION == gb.myCoordY && this.myCoordX == gb.myCoordX){

                        //check the closest block's number
                        if(gb.myTV.getText() == this.myTV.getText()){
                            //remove the closest block if the bumebr is equal to the current block
                            myGL.myGBList.remove(myGL.myGBList.indexOf(gb));
                            //double the number
                            this.myTV.setText((Integer.parseInt(""+this.myTV.getText()))*2+"");
                            //set the coord
                            targetCoordY = GameLoop.DOWN_BOUNDARY - (slotCount - numOfOccupants-1) * GameLoop.SLOT_ISOLATION;
                        }else {
                            //if the closest number is not the same as current number
                            targetCoordY = GameLoop.DOWN_BOUNDARY - (slotCount - numOfOccupants) * GameLoop.SLOT_ISOLATION;
                        }

                    }
                }

                Log.d("Game Block Report: ", String.format("Target Y Coord: %d", targetCoordY));


                break;
            default:
                break;
        }

    }

    @Override
    public void move(){

        Log.d("QQQ: ", "QQQ is: " + myCoordX + "," + myCoordY);

        switch(targetDirection){

            case LEFT:

                //targetCoordX = GameLoop.LEFT_BOUNDARY;

                if(myCoordX > targetCoordX){
                    if((myCoordX - myVelocity) <= targetCoordX){
                        myCoordX = targetCoordX;
                        Log.d("amin: ", "anim is: " + myCoordX + "," + myCoordY + "velocity" + myVelocity);
                        myVelocity = 0;
                    }
                    else {
                        myCoordX -= myVelocity;
                        myVelocity += GB_ACC;
                    }
                }

                break;

            case RIGHT:

                //targetCoordX = GameLoop.RIGHT_BOUNDARY;

                if(myCoordX < targetCoordX){
                    if((myCoordX + myVelocity) >= targetCoordX){
                        myCoordX = targetCoordX;
                        myVelocity = 0;
                        Log.d("DDD","Right Movement Called");
                    }
                    else {
                        myCoordX += myVelocity;
                        myVelocity += GB_ACC;
                    }
                }

                break;

            case UP:

                //targetCoordY = GameLoop.UP_BOUNDARY;

                if(myCoordY > targetCoordY){
                    if((myCoordY - myVelocity) <= targetCoordY){
                        myCoordY = targetCoordY;
                        myVelocity = 0;
                    }
                    else {
                        myCoordY -= myVelocity;
                        myVelocity += GB_ACC;
                    }
                }

                break;

            case DOWN:

                //targetCoordY = GameLoop.DOWN_BOUNDARY;

                if(myCoordY < targetCoordY){
                    if((myCoordY + myVelocity) >= targetCoordY){
                        myCoordY = targetCoordY;
                        myVelocity = 0;
                    }
                    else {
                        myCoordY += myVelocity;
                        myVelocity += GB_ACC;
                    }
                }

                break;

            default:
                break;

        }

        this.setX(myCoordX);
        this.setY(myCoordY);

        myTV.setX(myCoordX + TV_OFFSET);
        myTV.setY(myCoordY + TV_OFFSET);
        myTV.bringToFront();

        if(myVelocity == 0)
            targetDirection = GameLoop.eDir.NM;

    }

    public void mergingAlgorithm(char dir, int[] array){
        switch (dir){
            case 'L':
                break;
            case 'R':
                break;
            case 'U':
                break;
            case 'B':
                break;
        }
    }

}
