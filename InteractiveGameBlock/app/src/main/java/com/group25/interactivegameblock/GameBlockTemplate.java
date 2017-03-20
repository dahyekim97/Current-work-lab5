package com.group25.interactivegameblock;

/**
 * Created by Dahye on 2017-03-19.
 */
import android.content.Context;
import android.widget.ImageView;


public abstract class GameBlockTemplate extends ImageView {

    public GameBlockTemplate(Context gbCTX){
        super(gbCTX);
    }

    public abstract void setDestination(GameLoop.eDir myDir);

    public abstract void move();
}
