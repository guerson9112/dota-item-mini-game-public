package com.mangugu.apps.dotaitemminigame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import modelos.Carta;
import modelos.Juego;
import modelos.ParDeCartas;
import utils.DataBase;

public class Game extends AppCompatActivity{



    private Juego juego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        getSupportActionBar().hide();


        DataBase dataBase = new DataBase(this);
        final int[] puntajeYMonedas = dataBase.obtnerPuntejeYMonedas();
        dataBase.cerrar();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                juego = crearJuego(puntajeYMonedas[0], puntajeYMonedas[1]);
                juego.nuevoJuego();
            }
        }, 500);



    }

    private Juego crearJuego(int puntaje, int monedas){
        TextView puntajeMaximo = findViewById(R.id.puntajeMaximo);
        TextView puntajeActual = findViewById(R.id.puntajeActual);
        TextView vidas = findViewById(R.id.monedas);

        LinearLayout carta0 = findViewById(R.id.carta0);
        TextView nombre = carta0.findViewById(R.id.nombre);
        ImageView imagen = carta0.findViewById(R.id.imagen);

        Juego juego = new Juego(this, obtenerCartas(), new Carta(carta0, imagen, nombre, null), puntajeMaximo, puntajeActual, vidas);
        juego.setPuntajeMaximoEntero(puntaje);
        juego.setMonedasEntero(monedas);

        return juego;
    }

    private ParDeCartas[] obtenerCartas(){
        int[] idsCartas = {R.id.carta1, R.id.carta2, R.id.carta3, R.id.carta4,
                R.id.carta5, R.id.carta6, R.id.carta7, R.id.carta8, };
        int[] idsPuestos = {R.id.carta9, R.id.carta10, R.id.carta11, R.id.carta12,
                R.id.carta13, R.id.carta14, R.id.carta15, R.id.carta16, };

        ParDeCartas[] cartas = new ParDeCartas[8];
        for(int i = 0 ; i < cartas.length ; i++){
            LinearLayout lCarta = findViewById(idsCartas[i]);
            ImageView cImage = lCarta.findViewById(R.id.imagen);
            TextView cText = lCarta.findViewById(R.id.nombre);
            Carta c = new Carta(lCarta, cImage, cText, null);

            LinearLayout lPuesto = findViewById(idsPuestos[i]);
            ImageView pImage = lPuesto.findViewById(R.id.imagen);
            TextView pText = lPuesto.findViewById(R.id.nombre);
            Carta p = new Carta(lPuesto, pImage, pText, null);

            cartas[i] = new ParDeCartas(c, p);
        }

        return cartas;
    }

}
