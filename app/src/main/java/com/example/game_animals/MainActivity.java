package com.example.game_animals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    public void PlayGame(View view){
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }

    public void exit(View view){
        android.os.Process.killProcess(android.os.Process.myPid());   //获取PID
        System.exit(0);
    }
}
