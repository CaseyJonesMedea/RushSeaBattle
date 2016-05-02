package com.example.caseyjones.seabattlerush.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.caseyjones.seabattlerush.R;

/**
 * Created by CaseyJones on 01.05.2016.
 */
public class UserLoseFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_lose,null);
        return view;
    }
}
