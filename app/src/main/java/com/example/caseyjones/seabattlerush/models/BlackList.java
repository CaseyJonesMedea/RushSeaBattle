package com.example.caseyjones.seabattlerush.models;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by CaseyJones on 01.05.2016.
 */
public class BlackList {

    private ArrayList<Coordinate> list;
    private int fieldSize;

    public BlackList(int fieldSize) {
        this.list = new ArrayList<Coordinate>();
        this.fieldSize = fieldSize;

    }

    public void add(Coordinate coord){
        if(!this.list.contains(coord) && coord.getX()>=0 && coord.getY()>=0 && coord.getX()<this.fieldSize && coord.getY()<this.fieldSize)
            this.list.add(coord);
    }

    public boolean contains(Coordinate coord){
        return this.list.contains(coord);
    }

    public void show(){
        Log.d("seeBattle","~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Log.d("seeBattle",""+this.list.size());
        for (Coordinate coordinate : list) {
            Log.d("seeBattle",""+coordinate);
        }
        Log.d("seeBattle","~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
