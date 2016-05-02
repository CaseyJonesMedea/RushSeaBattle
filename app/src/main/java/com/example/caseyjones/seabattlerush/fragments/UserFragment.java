package com.example.caseyjones.seabattlerush.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.caseyjones.seabattlerush.R;
import com.example.caseyjones.seabattlerush.adapters.UserFieldAdapter;


/**
 * Created by CaseyJones on 01.05.2016.
 */
public class UserFragment extends Fragment {

    private RecyclerView recyclerViewUser;
    private int[][] mass;
    private int COLUMNS = 10;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mass = (int[][]) bundle.getSerializable("userfield");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initViews(view);
        GridLayoutManager glm = new GridLayoutManager(getActivity(), COLUMNS, GridLayoutManager.VERTICAL, false);
        recyclerViewUser.setLayoutManager(glm);
        recyclerViewUser.setHasFixedSize(true);
        onDrawField(mass);
        return view;
    }


    public void onDrawField(final int[][] mass) {
        recyclerViewUser.post(new Runnable() {
            @Override
            public void run() {

                UserFieldAdapter userFieldAdapter = new UserFieldAdapter(getContext(), mass, recyclerViewUser.getMeasuredWidth());
                recyclerViewUser.setAdapter(userFieldAdapter);
            }
        });
    }

    private void initViews(View view) {
        recyclerViewUser = (RecyclerView) view.findViewById(R.id.field_user);
    }

    public static UserFragment newInstance(int[][] mass) {
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userfield", mass);
        fragment.setArguments(bundle);
        return fragment;
    }
}
