package modelos;

import android.app.Activity;
import android.os.Handler;

import utils.Sonidos;

public class Ordenador {
    Activity activity;
    Juego juego;
    ParDeCartas[] parDeCartas;

    public Ordenador(Activity activity, Juego juego, ParDeCartas[] parDeCartas){
        this.activity = activity;
        this.juego = juego;
        this.parDeCartas = parDeCartas;


    }
    public void ordenar(){
        juego.setPuedoHacerClick(false);
        for(int i = 0 ; i< parDeCartas.length ; i++){
            parDeCartas[i].getCarta().getLayout().startAnimation(Traslador.getEntrarCartaAnimation(juego)[i]);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                Sonidos.reproducirBarajear(activity);
            }
        }, 280);
    }
}
