package com.mangugu.apps.dotaitemminigame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import utils.DataBase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        (findViewById(R.id.jugar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Game.class);
                startActivity(intent);
            }
        });

        DataBase dataBase = new DataBase(this);
        dataBase.iniciarSesion("24", 4, 0, 10);
        dataBase.cerrar();

    }

}