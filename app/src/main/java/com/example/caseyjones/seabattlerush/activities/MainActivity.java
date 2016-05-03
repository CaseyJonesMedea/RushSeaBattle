package com.example.caseyjones.seabattlerush.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caseyjones.seabattlerush.R;
import com.example.caseyjones.seabattlerush.customviews.SeaBattleButton;
import com.example.caseyjones.seabattlerush.fragments.BotFragment;
import com.example.caseyjones.seabattlerush.fragments.UserFragment;
import com.example.caseyjones.seabattlerush.fragments.UserLoseFragment;
import com.example.caseyjones.seabattlerush.fragments.UserWinFragment;
import com.example.caseyjones.seabattlerush.game.BotField;
import com.example.caseyjones.seabattlerush.game.BotLogic;
import com.example.caseyjones.seabattlerush.game.UserLogic;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnFragmentInteractionListener {

    private Toolbar toolbar;
    private Button changeField;
    private TextView progress;

    private BotFragment botFragment;
    private UserFragment userFragment;


    private BotLogic botLogic;
    private UserLogic userLogic;

    private BotField userField;

    private static final int SHIPS = 10;
    private static final int MAXDECK = 4;
    private static final int MAXDECKCOUNT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setSupportActionBar(toolbar);
        changeField.setOnClickListener(this);

        BotField botField = new BotField(SHIPS, MAXDECK, MAXDECKCOUNT);
        botField.generateField();
        botFragment = BotFragment.newInstance(botField.getField());
        userLogic = new UserLogic(botField.getField());

        userField = new BotField(SHIPS, MAXDECK, MAXDECKCOUNT);
        userField.generateField();
        userFragment = UserFragment.newInstance(userField.getField());
        botLogic = new BotLogic(this, userField.getField());

        replaceFragment(userFragment);
        progress.setText(R.string.user_field);
    }


    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        changeField = (Button) findViewById(R.id.change_field);
        progress = (TextView) findViewById(R.id.progress);
    }

    @Override
    public void onClick(View v) {
        changeField();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game: {
                Intent i = new Intent(this, this.getClass());
                finish();
                this.startActivity(i);
                break;
            }
            case R.id.about_game: {
                Intent intent = new Intent(this, AboutGameActivity.class);
                this.startActivity(intent);
                break;
            }
            case R.id.exit: {
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void changeField() {
        BotFragment fragmentBot = (BotFragment) getSupportFragmentManager().findFragmentByTag(BotFragment.class.getSimpleName());
        UserFragment fragmentUser = (UserFragment) getSupportFragmentManager().findFragmentByTag(UserFragment.class.getSimpleName());
        if (fragmentBot == null) {
            replaceFragment(botFragment);
            progress.setText(R.string.bot_field);
        } else if (fragmentUser == null) {
            replaceFragment(userFragment);
            progress.setText(R.string.user_field);
        }
    }

    @Override
    public void userShot(final SeaBattleButton seaBattleButton) {
        userLogic.greatShot(seaBattleButton);
        botFragment.onDrawField(userLogic.getMass()); // перерисовывает поле
        if (userLogic.gameOver()) {
            Toast.makeText(this, "Game Win!", Toast.LENGTH_SHORT).show();
            UserWinFragment winFragment = new UserWinFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, winFragment, winFragment.getClass().getSimpleName());
            fragmentTransaction.commitAllowingStateLoss();
            changeField.setVisibility(View.INVISIBLE);
        } else {
            if (!userLogic.isCanPlay()) {
                final Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        changeField();
                        botLogic.go();
                        if (botLogic.gameOver()) {
                            drawField(botLogic.getMass());
                            Toast.makeText(getApplicationContext(), "Game Over!", Toast.LENGTH_SHORT).show();
                            UserLoseFragment loseFragment = new UserLoseFragment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.container, loseFragment, loseFragment.getClass().getSimpleName());
                            fragmentTransaction.commitAllowingStateLoss();
                            changeField.setVisibility(View.INVISIBLE);
                        } else {
                            userLogic.setCanPlay(true);
                            drawField(botLogic.getMass());
                            final Handler handler1 = new Handler();
                            final Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    changeField();
                                }
                            };
                            handler1.postDelayed(runnable, 2000);
                        }
                    }
                };
                handler.postDelayed(r, 200);
            }
        }
    }

    @Override
    public void drawField(int[][] mass) {
        userFragment.onDrawField(mass);
    }
}
