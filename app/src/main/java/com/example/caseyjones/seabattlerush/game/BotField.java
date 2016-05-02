package com.example.caseyjones.seabattlerush.game;

import android.util.Log;

import com.example.caseyjones.seabattlerush.models.BlackList;
import com.example.caseyjones.seabattlerush.models.Coordinate;
import com.example.caseyjones.seabattlerush.models.Ship;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by CaseyJones on 01.05.2016.
 */
public class BotField {
    private String mass = "";
    private int[][] field;
    private int fieldSize;
    private int shipSize;
    private int shipCount;
    private BlackList bl;
    private ArrayList<Ship> shipList;


    public BotField(int size, int shipSize, int shipCount) {

        this.field = new int[size][size];
        this.fieldSize = size;
        this.shipCount = shipCount;
        this.shipSize = shipSize;
        this.bl = new BlackList(fieldSize);
        this.shipList = new ArrayList<Ship>();

    }

    public int[][] getField() {
        return field;
    }

    public void showField() {
        for (int[] is : field) {
            for (int i : is) {
                mass = mass + (" " + i);
            }
            Log.d("seaBattle", mass);
            mass = "";
        }
    }




    private Ship generateShip(int size) {
        ArrayList<Ship> arr = new ArrayList<Ship>();
        Coordinate startCoord = new Coordinate((int) (Math.random() * this.fieldSize), (int) (Math.random() * this.fieldSize));
        while (this.bl.contains(startCoord)) {
            startCoord = new Coordinate((int) (Math.random() * this.fieldSize), (int) (Math.random() * this.fieldSize));
        }
        ArrayList<Coordinate> tmp_left = new ArrayList<Coordinate>();
        ArrayList<Coordinate> tmp_right = new ArrayList<Coordinate>();
        ArrayList<Coordinate> tmp_top = new ArrayList<Coordinate>();
        ArrayList<Coordinate> tmp_bottom = new ArrayList<Coordinate>();

        for (int i = 0; i < size; i++) {


            Coordinate next_coord_left = new Coordinate(startCoord.getX(), startCoord.getY() - i);
            if (next_coord_left.getX() >= 0 && next_coord_left.getX() < this.fieldSize && next_coord_left.getY() >= 0 && next_coord_left.getY() < this.fieldSize && !this.bl.contains(next_coord_left))
                tmp_left.add(next_coord_left);

            Coordinate next_coord_right = new Coordinate(startCoord.getX(), startCoord.getY() + i);
            if (next_coord_right.getX() >= 0 && next_coord_right.getX() < this.fieldSize && next_coord_right.getY() >= 0 && next_coord_right.getY() < this.fieldSize && !this.bl.contains(next_coord_right))
                tmp_right.add(next_coord_right);


            Coordinate next_coord_top = new Coordinate(startCoord.getX() - i, startCoord.getY());
            if (next_coord_top.getX() >= 0 && next_coord_top.getX() < this.fieldSize && next_coord_top.getY() >= 0 && next_coord_top.getY() < this.fieldSize && !this.bl.contains(next_coord_top))
                tmp_top.add(next_coord_top);


            Coordinate next_coord_bottom = new Coordinate(startCoord.getX() + i, startCoord.getY());
            if (next_coord_bottom.getX() >= 0 && next_coord_bottom.getX() < this.fieldSize && next_coord_bottom.getY() >= 0 && next_coord_bottom.getY() < this.fieldSize && !this.bl.contains(next_coord_bottom))
                tmp_bottom.add(next_coord_bottom);


        }

        if (tmp_bottom.size() == size)
            arr.add(new Ship(tmp_bottom));
        if (tmp_right.size() == size)
            arr.add(new Ship(tmp_right));
        if (tmp_top.size() == size)
            arr.add(new Ship(tmp_top));
        if (tmp_left.size() == size)
            arr.add(new Ship(tmp_left));


        if (arr.size() > 0) {
            Collections.shuffle(arr);
            return arr.get(0);
        } else {
            return this.generateShip(size);
        }
    }

    public BlackList getBl() {
        return bl;
    }

    private void placeShip(Ship ship) {
        Log.d("seeBattle", "" + ship);
        for (Coordinate coord : ship.getShipCoordinates()) {
            this.bl.add(coord);
            this.bl.add(new Coordinate(coord.getX() - 1, coord.getY() + 1));
            this.bl.add(new Coordinate(coord.getX() - 1, coord.getY()));
            this.bl.add(new Coordinate(coord.getX() - 1, coord.getY() - 1));
            this.bl.add(new Coordinate(coord.getX(), coord.getY() - 1));
            this.bl.add(new Coordinate(coord.getX() + 1, coord.getY()));
            this.bl.add(new Coordinate(coord.getX(), coord.getY() + 1));
            this.bl.add(new Coordinate(coord.getX() + 1, coord.getY() - 1));
            this.bl.add(new Coordinate(coord.getX() + 1, coord.getY() + 1));
            this.field[coord.getX()][coord.getY()] = ship.getShipSize();

        }
        this.shipList.add(ship);
    }

    public void generateField() {
        int tmpShipSize = this.shipSize;
        int tmpShipCount = this.shipCount;
        for (int i = 0; i < this.shipSize; i++) {
            for (int j = 0; j < tmpShipCount; j++) {
                this.placeShip(this.generateShip(tmpShipSize));
            }
            tmpShipSize--;
            tmpShipCount++;
        }
    }
}
