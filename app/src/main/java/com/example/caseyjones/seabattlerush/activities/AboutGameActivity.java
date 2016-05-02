package com.example.caseyjones.seabattlerush.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.caseyjones.seabattlerush.R;

/**
 * Created by CaseyJones on 01.05.2016.
 */
public class AboutGameActivity extends AppCompatActivity {



    private TextView regulations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_game);
        initView();
        regulations.setText(R.string.regulations);
    }

    private void initView() {
        regulations = (TextView)findViewById(R.id.regulations);
    }
}
