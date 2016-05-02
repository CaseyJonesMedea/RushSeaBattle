package com.example.caseyjones.seabattlerush.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.caseyjones.seabattlerush.R;
import com.example.caseyjones.seabattlerush.adapters.BotFieldAdapter;


/**
 * Created by CaseyJones on 01.05.2016.
 */
public class BotFragment extends Fragment {

    private RecyclerView recyclerViewBot;
    private int[][] mass;
    private int COLUMNS = 10;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mass = (int[][]) bundle.getSerializable("botfield");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bot, container, false);
        initViews(view);
        GridLayoutManager glm = new GridLayoutManager(getActivity(), COLUMNS, GridLayoutManager.VERTICAL, false);
        recyclerViewBot.setLayoutManager(glm);
        recyclerViewBot.setHasFixedSize(true);
        onDrawField(mass);
        return view;
    }

    public void onDrawField(final int[][] mass) {
        recyclerViewBot.post(new Runnable() {
            @Override
            public void run() {
                BotFieldAdapter botFieldAdapter = new BotFieldAdapter(getContext(), mass, recyclerViewBot.getMeasuredWidth());
                recyclerViewBot.setAdapter(botFieldAdapter);
            }
        });
    }


    private void initViews(View view) {
        recyclerViewBot = (RecyclerView) view.findViewById(R.id.field_bot);
    }

    public static BotFragment newInstance(int[][] mass) {
        BotFragment fragment = new BotFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("botfield", mass);
        fragment.setArguments(bundle);
        return fragment;
    }
}


