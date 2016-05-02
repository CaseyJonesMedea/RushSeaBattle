package com.example.caseyjones.seabattlerush.models;

import java.util.ArrayList;

/**
 * Created by CaseyJones on 01.05.2016.
 */
public class Ship {

    private ArrayList<Coordinate> ship;

    public Ship(Coordinate ... coordinates){
        this.ship = new ArrayList<Coordinate>();
        for (Coordinate coordinate : coordinates) {
            this.ship.add(coordinate);
        }
    }

    public Ship(ArrayList<Coordinate> ship) {
        this.ship = ship;
    }

    public void addCoordinateToShip(Coordinate coord){
        this.ship.add(coord);
    }


    public int getShipSize(){
        return this.ship.size();
    }

    public ArrayList<Coordinate> getShipCoordinates(){
        return this.ship;
    }

    @Override
    public String toString() {
        return "Ship [ship=" + ship + "]";
    }
}
