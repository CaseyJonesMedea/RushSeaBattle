package com.example.caseyjones.seabattlerush.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.example.caseyjones.seabattlerush.R;
import com.example.caseyjones.seabattlerush.activities.OnFragmentInteractionListener;
import com.example.caseyjones.seabattlerush.customviews.SeaBattleButton;
import com.example.caseyjones.seabattlerush.models.Coordinate;

import java.util.ArrayList;

/**
 * Created by CaseyJones on 01.05.2016.
 */
public class BotFieldAdapter extends RecyclerView.Adapter<BotFieldAdapter.BotViewHolder> implements View.OnClickListener {

    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;
    private static final int TYPE_THREE = 3;

    private static final int LINES = 10;
    private static final int COLUMNS = 10;


    private int oneWidth;

    private SendButton sendButton;
    private ArrayList<SeaBattleButton> buttons;
    private Context context;

    private SeaBattleButton seaBattleButton;
    private int position;
    private int numShip;
    private Coordinate coordinate;
    private OnFragmentInteractionListener onFragmentInteractionListener;


    public BotFieldAdapter(Context context, int[][] mass, int widthGrid) {
        this.context = context;
        oneWidth = widthGrid / COLUMNS;
        buttons = new ArrayList<>();
        buttons = generateSeaBattleButtons(mass);
        onFragmentInteractionListener = (OnFragmentInteractionListener) context;
    }


    @Override
    public BotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        seaBattleButton = new SeaBattleButton(context, new Coordinate(0, 0), 0);
        RecyclerView.LayoutParams par = new RecyclerView.LayoutParams(oneWidth, oneWidth);
        seaBattleButton.setLayoutParams(par);
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
                seaBattleButton.setBackgroundResource(R.drawable.killed_new);
                break;
            case TYPE_THREE:
                position = sendButton.getPosition();
                coordinate = buttons.get(position).getCoordinate();
                numShip = buttons.get(position).getNumShip();
                seaBattleButton.setCoordinate(coordinate);
                seaBattleButton.setNumShip(numShip);
                seaBattleButton.setBackgroundResource(R.drawable.miss);
                break;
        }
        return new BotViewHolder(seaBattleButton);
    }

    @Override
    public void onBindViewHolder(BotViewHolder holder, int position) {
        holder.thisSeaBattleButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SeaBattleButton seaBattleButton = (SeaBattleButton) v;
        onFragmentInteractionListener.userShot(seaBattleButton);
    }

    @Override
    public int getItemViewType(int position) {
        sendButton = new SendButton(position);
        if (buttons.get(position).getNumShip() == 5) {
            return TYPE_TWO;
        } else if (buttons.get(position).getNumShip() == 8) {
            return TYPE_THREE;
        } else return TYPE_ONE;
    }


    @Override
    public int getItemCount() {
        return buttons.size();
    }


    class BotViewHolder extends RecyclerView.ViewHolder {

        public SeaBattleButton thisSeaBattleButton;

        public BotViewHolder(View itemView) {
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
