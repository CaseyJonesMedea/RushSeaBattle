package com.example.caseyjones.seabattlerush.game;

import android.util.Log;

import com.example.caseyjones.seabattlerush.activities.OnFragmentInteractionListener;
import com.example.caseyjones.seabattlerush.models.BlackList;
import com.example.caseyjones.seabattlerush.models.Coordinate;
import com.example.caseyjones.seabattlerush.models.Ship;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by CaseyJones on 01.05.2016.
 */
public class BotLogic {

    private static final int FIELDSIZE = 10;

    private int mass[][];
    private BlackList blackList;

    private String massiv = "";

    private boolean shotShipMethod = true;
    private boolean fireShipMethod = false;
    private boolean denyingShipMethod = false;

    private int one;
    private int two;
    private int three;
    private int four;

    private static final int TYPE_SHIP_ONE = 1;
    private static final int TYPE_SHIP_TWO = 2;
    private static final int TYPE_SHIP_THREE = 3;
    private static final int TYPE_SHIP_FOUR = 4;


    private ArrayList<Coordinate> coordinatesForFireShipMethod;

    private Coordinate startCoord; // 1 начальная координата для выстрела

    private Coordinate coordinate1ShotShip;

    private Coordinate coordinate2FireShip; // 2 координата для метода fireShip

    private Coordinate coordinate31DenyingShip; // 1 координата для метода denyingShip
    private Coordinate coordinate32DenyingShip; // 2 координата для метода denyingShip

    private Coordinate coordinateDenyingVerticalRandom;
    private Coordinate coordinateDenyingHorisontalRandom;

    private OnFragmentInteractionListener onFragmentInteractionListener;


    public BotLogic(OnFragmentInteractionListener onFragmentInteractionListener, int[][] mass) {
        this.mass = mass;
        this.blackList = new BlackList(100);
        this.onFragmentInteractionListener = onFragmentInteractionListener;
        one = 4;
        two = 3;
        three = 2;
        four = 1;
    }

    public int[][] getMass() {
        return mass;
    }

    public void go() {    //   Начали
        if (!gameOver()) {
            if (shotShipMethod) {
                shotShip();
            }
            if (fireShipMethod) {
                fireShip(coordinate1ShotShip);
            }
            if (denyingShipMethod) {
                denyingShip(coordinate1ShotShip, coordinate2FireShip);
            }
        }
    }

    private void shotShip() {       //          Прострел( пустых клеточек)
        startCoord = new Coordinate((int) (Math.random() * this.FIELDSIZE), (int) (Math.random() * this.FIELDSIZE));
        while (this.blackList.contains(startCoord)) {
            startCoord = new Coordinate((int) (Math.random() * this.FIELDSIZE), (int) (Math.random() * this.FIELDSIZE));
        }
        //Log.d("destroy", "" + startCoord);
        if (mass[startCoord.getX()][startCoord.getY()] == 1) {
            Ship ship = new Ship(startCoord);
            destroyShip(ship);
            blackList.add(startCoord);
            mass[startCoord.getX()][startCoord.getY()] = 5;
            coordinate1ShotShip = null;
            countShips(TYPE_SHIP_ONE);
            Log.d("destroy", "" + ship);
            shotShipMethod = true;
            fireShipMethod = false;
            denyingShipMethod = false;

//            botClickByField.drawField(mass);
            go();

        } else if (mass[startCoord.getX()][startCoord.getY()] == 0) {
            mass[startCoord.getX()][startCoord.getY()] = 8;
            blackList.add(startCoord);
            onFragmentInteractionListener.drawField(mass);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            shotShipMethod = false;
            fireShipMethod = true;
            denyingShipMethod = false;
            blackList.add(startCoord);
            mass[startCoord.getX()][startCoord.getY()] = 5;
            fireShip(startCoord);
        }
    }


    private void fireShip(Coordinate coord1) {         //         Обстрел
        if (coord1 != null) {
            coordinate1ShotShip = coord1;
            coordinatesForFireShipMethod = new ArrayList<>();
            if (coord1.getX() - 1 >= 0) {
                Coordinate coordUp = new Coordinate(coord1.getX() - 1, coord1.getY());
                coordinatesForFireShipMethod.add(coordUp);
            }
            if (coord1.getX() + 1 < 10) {
                Coordinate coordDown = new Coordinate(coord1.getX() + 1, coord1.getY());
                coordinatesForFireShipMethod.add(coordDown);
            }
            if (coord1.getY() - 1 >= 0) {
                Coordinate coordLeft = new Coordinate(coord1.getX(), coord1.getY() - 1);
                coordinatesForFireShipMethod.add(coordLeft);
            }
            if (coord1.getY() + 1 < 10) {
                Coordinate coordRight = new Coordinate(coord1.getX(), coord1.getY() + 1);
                coordinatesForFireShipMethod.add(coordRight);
            }
        }

        coordinate2FireShip = getRandomCoordinate(coordinatesForFireShipMethod);
        while (this.blackList.contains(coordinate2FireShip)) {
            coordinate2FireShip = getRandomCoordinate(coordinatesForFireShipMethod);
        }

        if (mass[coordinate2FireShip.getX()][coordinate2FireShip.getY()] == 2) {
            Ship ship = new Ship(startCoord, coordinate2FireShip);
            destroyShip(ship);
            blackList.add(coordinate2FireShip);
            mass[coordinate2FireShip.getX()][coordinate2FireShip.getY()] = 5;
            shotShipMethod = true;
            fireShipMethod = false;
            denyingShipMethod = false;
            coordinate1ShotShip = null;
            coordinate2FireShip = null;
            countShips(TYPE_SHIP_TWO);
            Log.d("destroy", "" + ship);
//            botClickByField.drawField(mass);
            go();
        } else if (mass[coordinate2FireShip.getX()][coordinate2FireShip.getY()] == 0) {
            blackList.add(coordinate2FireShip);
            mass[coordinate2FireShip.getX()][coordinate2FireShip.getY()] = 8;
//            botClickByField.drawField(mass);
        } else {
            shotShipMethod = false;
            fireShipMethod = false;
            denyingShipMethod = true;
            blackList.add(coordinate2FireShip);
            mass[coordinate2FireShip.getX()][coordinate2FireShip.getY()] = 5;
//            botClickByField.drawField(mass);
            denyingShip(coordinate1ShotShip, coordinate2FireShip);
        }


    }


    private void denyingShip(Coordinate coord1, Coordinate coord2) {        //       Добивание
        if (coord1 != null && coord2 != null) {
            coordinate31DenyingShip = coord1;
            coordinate32DenyingShip = coord2;
        }
        // Проверяем на горизонтальность и вертикальность
        if (horisontalOrVertical(coordinate31DenyingShip, coordinate32DenyingShip)) {
            denyingShipHorisontal(coordinate31DenyingShip, coordinate32DenyingShip);
        } else
            denyingShipVertical(coordinate31DenyingShip, coordinate32DenyingShip);


    }

    private void denyingShipVertical(Coordinate coordinate31DenyingShip, Coordinate coordinate32DenyingShip) {
        ArrayList<Coordinate> shipsVertical = new ArrayList<>();
        if (coordinate31DenyingShip.getX() - 1 >= 0) {
            Coordinate coordinateDenyingVertical1 = new Coordinate(coordinate31DenyingShip.getX() - 1, coordinate31DenyingShip.getY());
            shipsVertical.add(coordinateDenyingVertical1);
        }
        if (coordinate32DenyingShip.getX() - 1 >= 0) {
            Coordinate coordinateDenyingVertical3 = new Coordinate(coordinate32DenyingShip.getX() - 1, coordinate32DenyingShip.getY());
            shipsVertical.add(coordinateDenyingVertical3);
        }
        if (coordinate31DenyingShip.getX() + 1 < 10) {
            Coordinate coordinateDenyingVertical2 = new Coordinate(coordinate31DenyingShip.getX() + 1, coordinate31DenyingShip.getY());
            shipsVertical.add(coordinateDenyingVertical2);
        }
        if (coordinate32DenyingShip.getX() + 1 < 10) {
            Coordinate coordinateDenyingVertical4 = new Coordinate(coordinate32DenyingShip.getX() + 1, coordinate32DenyingShip.getY());
            shipsVertical.add(coordinateDenyingVertical4);
        }

        coordinateDenyingVerticalRandom = getRandomCoordinate(shipsVertical);
        while (this.blackList.contains(coordinateDenyingVerticalRandom)) {
            coordinateDenyingVerticalRandom = getRandomCoordinate(shipsVertical);
        }

        if (mass[coordinateDenyingVerticalRandom.getX()][coordinateDenyingVerticalRandom.getY()] == 3) {
            Ship ship = new Ship(startCoord, coordinate2FireShip, coordinateDenyingVerticalRandom);
            destroyShip(ship);
            blackList.add(coordinateDenyingVerticalRandom);
            mass[coordinateDenyingVerticalRandom.getX()][coordinateDenyingVerticalRandom.getY()] = 5;
            this.coordinate31DenyingShip = null;
            this.coordinate32DenyingShip = null;
            shotShipMethod = true;
            fireShipMethod = false;
            denyingShipMethod = false;
            countShips(TYPE_SHIP_THREE);
            Log.d("destroy", "" + ship);
//            botClickByField.drawField(mass);
            go();


        } else if (mass[coordinateDenyingVerticalRandom.getX()][coordinateDenyingVerticalRandom.getY()] == 0) {
            blackList.add(coordinateDenyingVerticalRandom);
            mass[coordinateDenyingVerticalRandom.getX()][coordinateDenyingVerticalRandom.getY()] = 8;
//            botClickByField.drawField(mass);


        } else {
            mass[coordinateDenyingVerticalRandom.getX()][coordinateDenyingVerticalRandom.getY()] = 5;
            ArrayList<Coordinate> shipsVerticalDenyingFinal = new ArrayList<>();
            if (coordinate31DenyingShip.getX() - 1 >= 0) {
                Coordinate coordinateDenyingVerticalFinal1 = new Coordinate(coordinate31DenyingShip.getX() - 1, coordinate31DenyingShip.getY());
                shipsVerticalDenyingFinal.add(coordinateDenyingVerticalFinal1);
            }
            if (coordinate31DenyingShip.getX() + 1 < 10) {
                Coordinate coordinateDenyingVerticalFinal2 = new Coordinate(coordinate31DenyingShip.getX() + 1, coordinate31DenyingShip.getY());
                shipsVerticalDenyingFinal.add(coordinateDenyingVerticalFinal2);
            }
            if (coordinate32DenyingShip.getX() - 1 >= 0) {
                Coordinate coordinateDenyingVerticalFinal3 = new Coordinate(coordinate32DenyingShip.getX() - 1, coordinate32DenyingShip.getY());
                shipsVerticalDenyingFinal.add(coordinateDenyingVerticalFinal3);
            }
            if (coordinate32DenyingShip.getX() + 1 < 10) {
                Coordinate coordinateDenyingVerticalFinal4 = new Coordinate(coordinate32DenyingShip.getX() + 1, coordinate32DenyingShip.getY());
                shipsVerticalDenyingFinal.add(coordinateDenyingVerticalFinal4);
            }
            if (coordinateDenyingVerticalRandom.getX() - 1 >= 0) {
                Coordinate coordinateDenyingVerticalFinal5 = new Coordinate(coordinateDenyingVerticalRandom.getX() - 1, coordinateDenyingVerticalRandom.getY());
                shipsVerticalDenyingFinal.add(coordinateDenyingVerticalFinal5);
            }
            if (coordinateDenyingVerticalRandom.getX() + 1 < 10) {
                Coordinate coordinateDenyingVerticalFinal6 = new Coordinate(coordinateDenyingVerticalRandom.getX() + 1, coordinateDenyingVerticalRandom.getY());
                shipsVerticalDenyingFinal.add(coordinateDenyingVerticalFinal6);
            }


            Coordinate coordinateDenyingVerticalFinalRandom = getRandomCoordinate(shipsVerticalDenyingFinal);
            while (this.blackList.contains(coordinateDenyingVerticalFinalRandom)) {
                coordinateDenyingVerticalFinalRandom = getRandomCoordinate(shipsVerticalDenyingFinal);
            }
            if (mass[coordinateDenyingVerticalFinalRandom.getX()][coordinateDenyingVerticalFinalRandom.getY()] == 4) {
                Ship ship = new Ship(startCoord, coordinate2FireShip, coordinateDenyingVerticalRandom, coordinateDenyingVerticalFinalRandom);
                destroyShip(ship);
                blackList.add(coordinateDenyingVerticalFinalRandom);
                mass[coordinateDenyingVerticalFinalRandom.getX()][coordinateDenyingVerticalFinalRandom.getY()] = 5;
                this.coordinate31DenyingShip = null;
                this.coordinate32DenyingShip = null;
                shotShipMethod = true;
                fireShipMethod = false;
                denyingShipMethod = false;
                countShips(TYPE_SHIP_FOUR);
                Log.d("destroy", "" + ship);
//                botClickByField.drawField(mass);

                go();


            } else if (mass[coordinateDenyingVerticalFinalRandom.getX()][coordinateDenyingVerticalFinalRandom.getY()] == 0) {
                blackList.add(coordinateDenyingVerticalFinalRandom);
                mass[coordinateDenyingVerticalFinalRandom.getX()][coordinateDenyingVerticalFinalRandom.getY()] = 8;
//                botClickByField.drawField(mass);

            }
        }
    }

    private void denyingShipHorisontal(Coordinate coordinate31DenyingShip, Coordinate coordinate32DenyingShip) {

        ArrayList<Coordinate> shipsHorisontal = new ArrayList<>();
        if (coordinate31DenyingShip.getY() + 1 < 10) {
            Coordinate coordinateDenyingHorisontal1 = new Coordinate(coordinate31DenyingShip.getX(), coordinate31DenyingShip.getY() + 1);
            shipsHorisontal.add(coordinateDenyingHorisontal1);
        }
        if (coordinate31DenyingShip.getY() - 1 >= 0) {
            Coordinate coordinateDenyingHorisontal2 = new Coordinate(coordinate31DenyingShip.getX(), coordinate31DenyingShip.getY() - 1);
            shipsHorisontal.add(coordinateDenyingHorisontal2);
        }
        if (coordinate32DenyingShip.getY() + 1 < 10) {
            Coordinate coordinateDenyingHorisontal3 = new Coordinate(coordinate32DenyingShip.getX(), coordinate32DenyingShip.getY() + 1);
            shipsHorisontal.add(coordinateDenyingHorisontal3);
        }
        if (coordinate32DenyingShip.getY() - 1 >= 0) {
            Coordinate coordinateDenyingHorisontal4 = new Coordinate(coordinate32DenyingShip.getX(), coordinate32DenyingShip.getY() - 1);
            shipsHorisontal.add(coordinateDenyingHorisontal4);
        }


        coordinateDenyingHorisontalRandom = getRandomCoordinate(shipsHorisontal);
        while (this.blackList.contains(coordinateDenyingHorisontalRandom)) {
            coordinateDenyingHorisontalRandom = getRandomCoordinate(shipsHorisontal);
        }

        if (mass[coordinateDenyingHorisontalRandom.getX()][coordinateDenyingHorisontalRandom.getY()] == 3) {
            Ship ship = new Ship(startCoord, coordinate2FireShip, coordinateDenyingHorisontalRandom);
            destroyShip(ship);
            blackList.add(coordinateDenyingHorisontalRandom);
            mass[coordinateDenyingHorisontalRandom.getX()][coordinateDenyingHorisontalRandom.getY()] = 5;
            this.coordinate31DenyingShip = null;
            this.coordinate32DenyingShip = null;
            shotShipMethod = true;
            fireShipMethod = false;
            denyingShipMethod = false;
            countShips(TYPE_SHIP_THREE);
            Log.d("destroy", "" + ship);
//            botClickByField.drawField(mass);

            go();


        } else if (mass[coordinateDenyingHorisontalRandom.getX()][coordinateDenyingHorisontalRandom.getY()] == 0) {
            blackList.add(coordinateDenyingHorisontalRandom);
            mass[coordinateDenyingHorisontalRandom.getX()][coordinateDenyingHorisontalRandom.getY()] = 8;
            onFragmentInteractionListener.drawField(mass);


        } else {
            mass[coordinateDenyingHorisontalRandom.getX()][coordinateDenyingHorisontalRandom.getY()] = 5;
            ArrayList<Coordinate> shipsHorisontalDenyingFinal = new ArrayList<>();
            if (coordinate31DenyingShip.getY() + 1 < 10) {
                Coordinate coordinateDenyingHorisontalFinal1 = new Coordinate(coordinate31DenyingShip.getX(), coordinate31DenyingShip.getY() + 1);
                shipsHorisontalDenyingFinal.add(coordinateDenyingHorisontalFinal1);
            }
            if (coordinate31DenyingShip.getY() - 1 >= 0) {
                Coordinate coordinateDenyingHorisontalFinal2 = new Coordinate(coordinate31DenyingShip.getX(), coordinate31DenyingShip.getY() - 1);
                shipsHorisontalDenyingFinal.add(coordinateDenyingHorisontalFinal2);
            }
            if (coordinate32DenyingShip.getY() + 1 < 10) {
                Coordinate coordinateDenyingHorisontalFinal3 = new Coordinate(coordinate32DenyingShip.getX(), coordinate32DenyingShip.getY() + 1);
                shipsHorisontalDenyingFinal.add(coordinateDenyingHorisontalFinal3);
            }
            if (coordinate32DenyingShip.getY() - 1 >= 0) {
                Coordinate coordinateDenyingHorisontalFinal4 = new Coordinate(coordinate32DenyingShip.getX(), coordinate32DenyingShip.getY() - 1);
                shipsHorisontalDenyingFinal.add(coordinateDenyingHorisontalFinal4);
            }
            if (coordinateDenyingHorisontalRandom.getY() + 1 < 10) {
                Coordinate coordinateDenyingHorisontalFinal5 = new Coordinate(coordinateDenyingHorisontalRandom.getX(), coordinateDenyingHorisontalRandom.getY() + 1);
                shipsHorisontalDenyingFinal.add(coordinateDenyingHorisontalFinal5);
            }
            if (coordinateDenyingHorisontalRandom.getY() - 1 >= 0) {
                Coordinate coordinateDenyingHorisontalFinal6 = new Coordinate(coordinateDenyingHorisontalRandom.getX(), coordinateDenyingHorisontalRandom.getY() - 1);
                shipsHorisontalDenyingFinal.add(coordinateDenyingHorisontalFinal6);
            }


            Coordinate coordinateDenyingHorisontalFinalRandom = getRandomCoordinate(shipsHorisontalDenyingFinal);
            while (this.blackList.contains(coordinateDenyingHorisontalFinalRandom)) {
                coordinateDenyingHorisontalFinalRandom = getRandomCoordinate(shipsHorisontalDenyingFinal);
            }
            if (mass[coordinateDenyingHorisontalFinalRandom.getX()][coordinateDenyingHorisontalFinalRandom.getY()] == 4) {
                Ship ship = new Ship(startCoord, coordinate2FireShip, coordinateDenyingHorisontalRandom, coordinateDenyingHorisontalFinalRandom);
                destroyShip(ship);
                blackList.add(coordinateDenyingHorisontalFinalRandom);
                mass[coordinateDenyingHorisontalFinalRandom.getX()][coordinateDenyingHorisontalFinalRandom.getY()] = 5;
                this.coordinate31DenyingShip = null;
                this.coordinate32DenyingShip = null;
                shotShipMethod = true;
                fireShipMethod = false;
                denyingShipMethod = false;
                countShips(TYPE_SHIP_FOUR);
                Log.d("destroy", "" + ship);
//                botClickByField.drawField(mass);
                go();


            } else if (mass[coordinateDenyingHorisontalFinalRandom.getX()][coordinateDenyingHorisontalFinalRandom.getY()] == 0) {
                blackList.add(coordinateDenyingHorisontalFinalRandom);
                mass[coordinateDenyingHorisontalFinalRandom.getX()][coordinateDenyingHorisontalFinalRandom.getY()] = 8;
//                botClickByField.drawField(mass);
            }
        }
    }

    private void destroyShip(Ship ship) {  // Метод обрисовывает наш  убитый корабль точками, заносит поле вокруг корабля в блэклист
        ArrayList<Coordinate> shipCoordinate = ship.getShipCoordinates();
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

    public Coordinate getRandomCoordinate(ArrayList<Coordinate> coordRandom) {   // Функция нахождения рандомного элемента из массива
        int itemIndex = (int) (Math.random() * coordRandom.size());
        return coordRandom.get(itemIndex);
    }

    public boolean horisontalOrVertical(Coordinate... coordinates) {
        // возвращает true, если горизонтально расположены
        // возвращает false, если вертикально расположены
        if (coordinates[0].getX() == coordinates[1].getX()) {
            return true;
        }
        return false;
    }

    public boolean gameOver() {

        if (four == 0 && three == 0 && two == 0 && one == 0) {
            showMassiv();
            Log.d("destroy", "------------------------------------");
            return true;
        } else {
            return false;
        }
    }

    public void countShips(int i) {
        if (i == TYPE_SHIP_FOUR) {
            four--;
        } else if (i == TYPE_SHIP_THREE) {
            three--;
        } else if (i == TYPE_SHIP_TWO) {
            two--;
        } else if (i == TYPE_SHIP_ONE) {
            one--;
        }
    }

    public void showMassiv() {
        for (int[] is : mass) {
            for (int i : is) {
                massiv = massiv + (" " + i);
            }
            Log.d("seaBattle", massiv);
            massiv = "";
        }
    }

}

