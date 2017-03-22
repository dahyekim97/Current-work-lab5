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

    public int myVelocity;
    private GameLoop.eDir targetDirection;

    public boolean toBeRemoved = false;

    private RelativeLayout myRL;

    public GameBlock(Context gbCTX, RelativeLayout gbRL, int coordX, int coordY, GameLoop gbGL){

        super(gbCTX);
        this.setImageResource(R.drawable.gameblock);
        this.myRL = gbRL;
        this.setX(coordX);
        this.setY(coordY);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        gbRL.setBackgroundColor(Color.WHITE);
        gbRL.setBackgroundResource(R.drawable.gameboard);
        gbRL.getLayoutParams().width = 1080;
        gbRL.getLayoutParams().height = 1080;
        gbRL.addView(this);

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


    }

    //Allowing blocks to report is own location
    public int[] getCoordinate(){
        int[] thisCoord = new int[2];
        thisCoord[0] = myCoordX;
        thisCoord[1] = myCoordY;
        return thisCoord;
    }

    public int[] getTargetCoordinate(){
        int[] thisCoord = new int[2];
        thisCoord[0] = targetCoordX;
        thisCoord[1] = targetCoordY;
        return thisCoord;
    }

    public String getTextOfTV(){
        return myTV.getText()+"";
    }

    public void getBlockNum(){

    }
    @Override
    public void setDestination(GameLoop.eDir thisDir){

        Log.d("setDestination Method", "called" );
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
        int currentX = myCoordX/GameLoop.SLOT_ISOLATION;
        int currnetY = myCoordY/GameLoop.SLOT_ISOLATION;

        switch(thisDir){

            case LEFT:
                Log.d("LLL", "called" );

                currentDir = 'L';

                for(GameBlock gb : myGL.myGBList){

                    if(gb.myCoordY == this.myCoordY){

                        int i = gb.myCoordX/GameLoop.SLOT_ISOLATION;
                        tempArray[i] = Integer.parseInt(""+gb.myTV.getText());
                    }
                }
                Log.d("bbb", "tempArray[0]: "+tempArray[0]+" tempArray[1]: "+tempArray[1]+" tempArray[2]: "+tempArray[2]);

                mergingAlgorithm(currentDir,tempArray,currentX,currnetY);



                break;
            case RIGHT:
                currentDir = 'R';
                for(GameBlock gb : myGL.myGBList){
                    if(gb.myCoordY == this.myCoordY){
                        int i = gb.myCoordX/GameLoop.SLOT_ISOLATION;
                        tempArray[i] = Integer.parseInt(""+gb.myTV.getText());
                    }
                }

                mergingAlgorithm(currentDir,tempArray,currentX,currnetY);

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

                mergingAlgorithm(currentDir,tempArray,currentX,currnetY);

                break;
            case DOWN:
                currentDir = 'D';
                for(GameBlock gb : myGL.myGBList){
                        if(gb.myCoordX == this.myCoordX){
                        int i = gb.myCoordY/GameLoop.SLOT_ISOLATION;
                        tempArray[i] = Integer.parseInt(""+gb.myTV.getText());
                    }
                }

                mergingAlgorithm(currentDir,tempArray,currentX,currnetY);

                break;

            default:

                break;
        }

    }

    @Override
    public void move(){


        switch(targetDirection){

            case LEFT:

                //targetCoordX = GameLoop.LEFT_BOUNDARY;

                if(myCoordX > targetCoordX){
                    if((myCoordX - myVelocity) <= targetCoordX){
                        myCoordX = targetCoordX;
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

        if(myVelocity == 0) {
            targetDirection = GameLoop.eDir.NM;

        }
    }


    //this method sets the target destination
    //and mark the block which should be removed
    public void mergingAlgorithm(char dir, int[] array, int currentBlockX, int currentBlockY){
        int BlockCount=0;
        int slotCount = 0;
        int adjacentBlockIndexforArray = 0;

        switch (dir){
            case 'L':

                //if the current block is not at the left edge, it can move, so we are checking if it's at the left edge.
                if(currentBlockX != 0) {

                    //adgacentBlockIndexforArray gets the index in array of the nearest block to the current block
                    for(int i = 0 ; i < currentBlockX ; i++){
                        if(array[i]>0){
                            adjacentBlockIndexforArray = i;
                        }
                    }
                    //check if the nearest block and the current block has the same number
                    if (array[currentBlockX] == array[adjacentBlockIndexforArray]) {

                        int adjacentBlockIndex = this.getIndexOfBlock(adjacentBlockIndexforArray,currentBlockY);
                        int numberInBlock = Integer.parseInt(""+myGL.myGBList.get(adjacentBlockIndex).myTV.getText());

                        myGL.myGBList.get(adjacentBlockIndex).myTV.setText(""+numberInBlock*2);
                        toBeRemoved = true;


                    }else{
                        for(int i =0; i < currentBlockX; i++){
                            if(array[i] > 0){
                                // this means there exists a block
                                BlockCount++;
                            }
                            slotCount++;
                        }


                        targetCoordX = (myCoordX - (GameLoop.LEFT_BOUNDARY+((slotCount-BlockCount)*GameLoop.SLOT_ISOLATION)));


                        Log.d("XXX", "slotCount : " + slotCount +" BlockCount : " + BlockCount);
                        Log.d("targetCoordX", "targetCoordX : " + (myCoordX - (GameLoop.LEFT_BOUNDARY+((slotCount-BlockCount)*GameLoop.SLOT_ISOLATION))) +"///"+"slotCount : " + slotCount +" BlockCount : " + BlockCount);
                        Log.d("ZZZ", "index : " + myGL.myGBList.indexOf(this)+" targetX is : " + targetCoordX);


                    }
                }
                break;



            case 'R':
                //if the current block is not at the right edge, it can move, so we are checking if it's at the left edge.
                if(currentBlockX != 3) {

                    //adgacentBlockIndexforArray gets the index in array of the nearest block to the current block
                    for(int i = 3 ; i > currentBlockX ; i--){
                        if(array[i]>0){
                            adjacentBlockIndexforArray = i;
                        }
                    }

                    //check if the nearest block and the current block has the same number
                    if (array[currentBlockX] == array[adjacentBlockIndexforArray]) {
                        int adjacentBlockIndex = this.getIndexOfBlock(adjacentBlockIndexforArray,currentBlockY);
                        int numberInBlock = Integer.parseInt(""+myGL.myGBList.get(adjacentBlockIndex).myTV.getText());

                        myGL.myGBList.get(adjacentBlockIndex).myTV.setText(""+numberInBlock*2);
                        toBeRemoved = true;

                    }else{
                        //if the left of the current block has not the same number, just move, don't combine them
                        //to do so, calculate the number of blocks exists to the left of the current block and the number of slots total to the left of current block
                        for(int i = 3; i > currentBlockX; i--){
                            if(array[i] > 0){
                                // this means there exists a block
                                BlockCount++;
                            }
                            slotCount++;
                        }
                        targetCoordX = myCoordX + (((slotCount-BlockCount)*GameLoop.SLOT_ISOLATION));

                        Log.d("AAAB", "targetCoordX : " +  myCoordX + (((slotCount-BlockCount)*GameLoop.SLOT_ISOLATION))+"///"+"slotCount : " + slotCount +" BlockCount : " + BlockCount);

                    }
                }



                break;
            case 'U':

                //if the current block is not at the upper edge, it can move, so we are checking if it's at the left edge.
                if(currentBlockY != 0) {

                    //adgacentBlockIndexforArray gets the index in array of the nearest block to the current block
                    for(int i = 0 ; i < currentBlockY ; i++){
                        if(array[i]>0){
                            adjacentBlockIndexforArray = i;
                        }
                    }
                    //check if the nearest block and the current block has the same number
                    if (array[currentBlockY] == array[adjacentBlockIndexforArray]) {

                        int adjacentBlockIndex = this.getIndexOfBlock(currentBlockX,adjacentBlockIndexforArray);
                        int numberInBlock = Integer.parseInt(""+myGL.myGBList.get(adjacentBlockIndex).myTV.getText());

                        myGL.myGBList.get(adjacentBlockIndex).myTV.setText(""+numberInBlock*2);
                        toBeRemoved = true;

                    }else{

                        for(int i = 0; i < currentBlockY; i++){
                            if(array[i] > 0){
                                // this means there exists a block
                                BlockCount++;
                            }
                            slotCount++;
                        }
                        targetCoordY = (myCoordY - (GameLoop.UP_BOUNDARY+((slotCount-BlockCount)*GameLoop.SLOT_ISOLATION)));

                    }
                }

                break;
            case 'D':


                //if the current block is not at the right edge, it can move, so we are checking if it's at the left edge.
                if(currentBlockY != 3) {

                    //adgacentBlockIndexforArray gets the index in array of the nearest block to the current block
                    for(int i = 3 ; i > currentBlockY ; i--){
                        if(array[i]>0){
                            adjacentBlockIndexforArray = i;
                        }
                    }
                    //check if the nearest block and the current block has the same number
                    if (array[currentBlockY] == array[adjacentBlockIndexforArray]) {

                        int adjacentBlockIndex = this.getIndexOfBlock(currentBlockX,adjacentBlockIndexforArray);
                        int numberInBlock = Integer.parseInt(""+myGL.myGBList.get(adjacentBlockIndex).myTV.getText());

                        myGL.myGBList.get(adjacentBlockIndex).myTV.setText(""+numberInBlock*2);
                        toBeRemoved = true;


                    }else{
                        //if the left of the current block has not the same number, just move, don't combine them
                        //to do so, calculate the number of blocks exists to the left of the current block and the number of slots total to the left of current block
                        for(int i = 3; i > currentBlockY; i--){
                            if(array[i] > 0){
                                // this means there exists a block
                                BlockCount++;
                            }
                            slotCount++;
                        }
                        targetCoordY = myCoordY + (((slotCount-BlockCount)*GameLoop.SLOT_ISOLATION));
                        Log.d("AABB", "targetCoordX : " +  myCoordY + (((slotCount-BlockCount)*GameLoop.SLOT_ISOLATION))+"///"+"slotCount : " + slotCount +" BlockCount : " + BlockCount);


                    }
                }

                break;
        }
    }

    //this method returns the index of block in myGBList by it's index in array
    public  int getIndexOfBlock(int x, int y){
        int temp = 0;
        for(GameBlock gb : myGL.myGBList){
            if(gb.myCoordY == y*GameLoop.SLOT_ISOLATION && gb.myCoordX == x*GameLoop.SLOT_ISOLATION){
                temp = myGL.myGBList.indexOf(gb);
            }
        }
        return  temp;
    }

    public void destroyMe(){
        myRL.removeView(myTV);
        myRL.removeView(this);
    }


}
