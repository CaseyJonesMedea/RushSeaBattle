package com.example.caseyjones.seabattlerush.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.example.caseyjones.seabattlerush.R;
import com.example.caseyjones.seabattlerush.customviews.SeaBattleButton;
import com.example.caseyjones.seabattlerush.models.Coordinate;

import java.util.ArrayList;

/**
 * Created by CaseyJones on 01.05.2016.
 */
public class UserFieldAdapter extends RecyclerView.Adapter<UserFieldAdapter.UserViewHolder> {


    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;
    private static final int TYPE_THREE = 3;
    private static final int TYPE_FOUR = 4;

    private static final int COLUMNS = 10;
    private static final int LINES = 10;

    private int oneWidth;

    private SendButton sendButton;
    private ArrayList<SeaBattleButton> buttons;
    private Context context;

    private SeaBattleButton seaBattleButton;
    private int position;
    private int numShip;
    private Coordinate coordinate;


    public UserFieldAdapter(Context context, int[][] mass, int widthGrid) {
        this.context = context;
        oneWidth = widthGrid / COLUMNS;
        buttons = new ArrayList<>();
        buttons = generateSeaBattleButtons(mass);
    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        seaBattleButton = new SeaBattleButton(context, new Coordinate(0, 0), 0);
        RecyclerView.LayoutParams par = new RecyclerView.LayoutParams(oneWidth, oneWidth);
        seaBattleButton.setLayoutParams(par);
        seaBattleButton.setBackgroundColor(Color.LTGRAY);
        seaBattleButton.setGravity(Gravity.CENTER);
        switch (viewType) {
            case TYPE_ONE:
                position = sendButton.getPosition();
                coordinate = buttons.get(position).getCoordinate();
                numShip = buttons.get(position).getNumShip();
                seaBattleButton.setCoordinate(coordinate);
                seaBattleButton.setNumShip(numShip);
                seaBattleButton.setBackgroundResource(R.drawable.sea_new);
                break;
            case TYPE_TWO:
                position = sendButton.getPosition();
                coordinate = buttons.get(position).getCoordinate();
                numShip = buttons.get(position).getNumShip();
                seaBattleButton.setCoordinate(coordinate);
                seaBattleButton.setNumShip(numShip);
                seaBattleButton.setBackgroundResource(R.drawable.killedme_new);
                break;
            case TYPE_THREE:
                position = sendButton.getPosition();
                coordinate = buttons.get(position).getCoordinate();
                numShip = buttons.get(position).getNumShip();
                seaBattleButton.setCoordinate(coordinate);
                seaBattleButton.setNumShip(numShip);
                seaBattleButton.setBackgroundResource(R.drawable.miss);
                break;
            case TYPE_FOUR:
                position = sendButton.getPosition();
                coordinate = buttons.get(position).getCoordinate();
                numShip = buttons.get(position).getNumShip();
                seaBattleButton.setCoordinate(coordinate);
                seaBattleButton.setNumShip(numShip);
                seaBattleButton.setBackgroundResource(R.drawable.hit);
                break;
        }
        return new UserViewHolder(seaBattleButton);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
    }


    @Override
    public int getItemViewType(int position) {
        sendButton = new SendButton(position);
        if (buttons.get(position).getNumShip() == 5) {
            return TYPE_TWO;
        } else if (buttons.get(position).getNumShip() == 8) {
            return TYPE_THREE;
        } else if (buttons.get(position).getNumShip() == 1 || buttons.get(position).getNumShip() == 2
                || buttons.get(position).getNumShip() == 3 || buttons.get(position).getNumShip() == 4) {
            return TYPE_FOUR;
        } else return TYPE_ONE;
    }


    @Override
    public int getItemCount() {
        return buttons.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        public SeaBattleButton thisSeaBattleButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            thisSeaBattleButton = (SeaBattleButton) itemView;
        }
    }

    public ArrayList<SeaBattleButton> generateSeaBattleButtons(int[][] mass) {
        ArrayList<SeaBattleButton> buttons = new ArrayList<SeaBattleButton>();
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < LINES; j++) {
                buttons.add(new SeaBattleButton(context, new Coordinate(i, j), mass[i][j]));
            }
        }
        return buttons;
    }


    class SendButton {
        private int position;

        public SendButton(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }
}
