package com.example.caseyjones.seabattlerush.activities;

import com.example.caseyjones.seabattlerush.customviews.SeaBattleButton;

/**
 * Created by CaseyJones on 01.05.2016.
 */
public interface OnFragmentInteractionListener {
    void userShot(SeaBattleButton seaBattleButton);
    void drawField(int[][]mass);

}
