package com.example.caseyjones.seabattlerush.game;

import android.util.Log;

import com.example.caseyjones.seabattlerush.customviews.SeaBattleButton;
import com.example.caseyjones.seabattlerush.models.BlackList;
import com.example.caseyjones.seabattlerush.models.Coordinate;
import com.example.caseyjones.seabattlerush.models.Ship;

import java.util.ArrayList;

/**
 * Created by CaseyJones on 01.05.2016.
 */
public class UserLogic {



    private int mass[][];
    private BlackList blackList;
    private BlackList blackListForFindShips;


    private String massiv = "";

    private static int one;
    private static int two;
    private static int three;
    private static int four;

    private ArrayList<Ship> ships;


    private boolean canPlay; // Определяем, кто ходит



    public UserLogic(int[][] mass) {
        this.mass = mass;
        blackList = new BlackList(100);
        blackListForFindShips = new BlackList(20);
        ships = new ArrayList<>();
        findShip();
        canPlay = true;
        one = 4;
        two = 3;
        three = 2;
        four = 1;
    }

    public int[][] getMass() {
        return mass;
    }

    public boolean isCanPlay() {
        return canPlay;
    }

    public void setCanPlay(boolean canPlay) {
        this.canPlay = canPlay;
    }

    public void findShip() {

        int tmpShipSize = 4;   // количество палуб
        int tmpShipCount = 1;  // количество кораблей с таким количеством палуб


        while (tmpShipSize > 0) {
            for (int m = 0; m < tmpShipCount; m++) {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (mass[i][j] == tmpShipSize && !blackListForFindShips.contains(new Coordinate(i, j))) {
                            ships.add(generateShip(i, j, tmpShipSize));
                        }
                    }
                }
            }
            tmpShipCount++;
            tmpShipSize--;
        }
    }

    private Ship generateShip(int i, int j, int tmpShipSize) {
        Ship ship = new Ship();
        if (i + 1 <= 9 && (mass[i + 1][j] == tmpShipSize)) {
            for (int l = 0; l < tmpShipSize; l++) {
                Coordinate coordinate = new Coordinate(i + l, j);
                blackListForFindShips.add(coordinate);
                ship.addCoordinateToShip(coordinate);
            }
        } else if (j + 1 <= 9 && mass[i][j + 1] == tmpShipSize) {
            for (int l = 0; l < tmpShipSize; l++) {
                Coordinate coordinate = new Coordinate(i, j + l);
                blackListForFindShips.add(coordinate);
                ship.addCoordinateToShip(coordinate);
            }
        } else {
            Coordinate coordinate = new Coordinate(i, j);
            blackListForFindShips.add(coordinate);
            ship.addCoordinateToShip(coordinate);
        }
        return ship;
    }


    public void showShips() {
        for (int i = 0; i < ships.size(); i++) {
            Log.d("seaBattle", "" + ships.get(i));
        }
    }


    public void greatShot(SeaBattleButton seaBattleButton) {

        Coordinate coordinate = seaBattleButton.getCoordinate();

        if (!blackList.contains(coordinate)) {
            int xd = coordinate.getX();
            int yd = coordinate.getY();

            if (mass[xd][yd] == 1 || mass[xd][yd] == 2 || mass[xd][yd] == 3 || mass[xd][yd] == 4) {
                mass[xd][yd] = 5;
                canPlay = true;
                isDestroyed(coordinate);
                blackList.add(coordinate);
            } else if (mass[xd][yd] == 0) {
                mass[xd][yd] = 8;
                canPlay = false;
                blackList.add(coordinate);
            } else {

            }
        }
    }

    public void isDestroyed(Coordinate coordinate) {
        for (int i = 0; i < ships.size(); i++) {
            Ship ship = ships.get(i);
            int count = 0; // счетчик подбитых палуб в корабле
            ArrayList<Coordinate> list = ship.getShipCoordinates();
            for (int j = 0; j < list.size(); j++) {
                Coordinate coord = list.get(j);
                if (!coord.isAlive()) {
                    count++;
                }
                if (coordinate.equals(coord)) {
                    coord.setAlive(false);
                    count++;
                }
            }
            if (count == list.size()) {
                destroyShip(ships.get(i));
                ships.remove(i);
            }
        }
    }


    public boolean gameOver() {
        if (four == 0 && three == 0 && two == 0 && one == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void countShipsInUser(int i) {
        switch (i) {
            case 4:
                four--;
                break;
            case 3:
                three--;
                break;
            case 2:
                two--;
                break;
            case 1:
                one--;
                break;
        }
    }


    public void showMassiv() {
        for (int[] is : mass) {
            for (int i : is) {
                massiv = massiv + (" " + i);
            }
            Log.d("destroy", massiv);
            massiv = "";
        }
    }


    private void destroyShip(Ship ship) {  // Метод обрисовывает наш  убитый корабль точками, заносит поле вокруг корабля в блэклист
        ArrayList<Coordinate> shipCoordinate = ship.getShipCoordinates();
        countShipsInUser(ship.getShipSize());
        for (int i = 0; i < shipCoordinate.size(); i++) {
            Coordinate coordinate = shipCoordinate.get(i);
            if (coordinate.getY() + 1 < 10) {
                Coordinate coordinate1 = new Coordinate(coordinate.getX(), coordinate.getY() + 1);
                if (mass[coordinate1.getX()][coordinate1.getY()] == 0) {
                    mass[coordinate1.getX()][coordinate1.getY()] = 8;
                }
                if (!blackList.contains(coordinate1)) {
                    blackList.add(coordinate1);
                }
            }

            if (coordinate.getY() + 1 < 10 && coordinate.getX() - 1 >= 0) {
                Coordinate coordinate2 = new Coordinate(coordinate.getX() - 1, coordinate.getY() + 1);
                if (mass[coordinate2.getX()][coordinate2.getY()] == 0) {
                    mass[coordinate2.getX()][coordinate2.getY()] = 8;
                }
                if (!blackList.contains(coordinate2)) {
                    blackList.add(coordinate2);
                }
            }

            if (coordinate.getX() - 1 >= 0) {
                Coordinate coordinate3 = new Coordinate(coordinate.getX() - 1, coordinate.getY());
                if (mass[coordinate3.getX()][coordinate3.getY()] == 0) {
                    mass[coordinate3.getX()][coordinate3.getY()] = 8;
                }
                if (!blackList.contains(coordinate3)) {
                    blackList.add(coordinate3);
                }
            }

            if (coordinate.getX() - 1 >= 0 && coordinate.getY() - 1 >= 0) {
                Coordinate coordinate4 = new Coordinate(coordinate.getX() - 1, coordinate.getY() - 1);
                if (mass[coordinate4.getX()][coordinate4.getY()] == 0) {
                    mass[coordinate4.getX()][coordinate4.getY()] = 8;
                }
                if (!blackList.contains(coordinate4)) {
                    blackList.add(coordinate4);
                }
            }

            if (coordinate.getY() - 1 >= 0) {
                Coordinate coordinate5 = new Coordinate(coordinate.getX(), coordinate.getY() - 1);
                if (mass[coordinate5.getX()][coordinate5.getY()] == 0) {
                    mass[coordinate5.getX()][coordinate5.getY()] = 8;
                }
                if (!blackList.contains(coordinate5)) {
                    blackList.add(coordinate5);
                }
            }

            if (coordinate.getY() - 1 >= 0 && coordinate.getX() + 1 < 10) {
                Coordinate coordinate6 = new Coordinate(coordinate.getX() + 1, coordinate.getY() - 1);
                if (mass[coordinate6.getX()][coordinate6.getY()] == 0) {
                    mass[coordinate6.getX()][coordinate6.getY()] = 8;
                }
                if (!blackList.contains(coordinate6)) {
                    blackList.add(coordinate6);
                }
            }

            if (coordinate.getX() + 1 < 10) {
                Coordinate coordinate7 = new Coordinate(coordinate.getX() + 1, coordinate.getY());
                if (mass[coordinate7.getX()][coordinate7.getY()] == 0) {
                    mass[coordinate7.getX()][coordinate7.getY()] = 8;
                }
                if (!blackList.contains(coordinate7)) {
                    blackList.add(coordinate7);
                }
            }


            if (coordinate.getX() + 1 < 10 && coordinate.getY() + 1 < 10) {
                Coordinate coordinate8 = new Coordinate(coordinate.getX() + 1, coordinate.getY() + 1);
                if (mass[coordinate8.getX()][coordinate8.getY()] == 0) {
                    mass[coordinate8.getX()][coordinate8.getY()] = 8;
                }
                if (!blackList.contains(coordinate8)) {
                    blackList.add(coordinate8);
                }
            }
        }
    }
}
