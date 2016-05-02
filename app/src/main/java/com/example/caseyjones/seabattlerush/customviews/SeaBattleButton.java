package com.example.caseyjones.seabattlerush.customviews;

import android.content.Context;
import android.widget.Button;

import com.example.caseyjones.seabattlerush.models.Coordinate;

/**
 * Created by CaseyJones on 01.05.2016.
 */
public class SeaBattleButton extends Button {

    private Coordinate coordinate;
    private int numShip;
    private boolean isPressed;


//    public SeaBattleButton(Context context) {
//        super(context);
//    }

    public SeaBattleButton(Context context, Coordinate coordinate, int numShip) {
        super(context);
        this.coordinate = coordinate;
        this.numShip = numShip;
        if(coordinate.isAlive()){
            isPressed = false;
        }else isPressed = true;
    }



    @Override
    public boolean isPressed() {
        return isPressed;
    }

    @Override
    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public int getNumShip() {
        return numShip;
    }

    public void setNumShip(int numShip) {
        this.numShip = numShip;
    }
}
