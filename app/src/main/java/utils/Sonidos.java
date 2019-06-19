package utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.mangugu.apps.dotaitemminigame.R;


public class Sonidos {
    static MediaPlayer cartaSonido;
    static MediaPlayer monedaSonido;
    static MediaPlayer barajearSonido;

    public static void reproducirBarajear(Context activity){
        if(barajearSonido == null){
            barajearSonido = MediaPlayer.create(activity, R.raw.ordenar);
        }
        if(barajearSonido.isPlaying()){
            barajearSonido.seekTo(0);
        }

        barajearSonido.start();
    }
    public static void reproducirMonedas(Context activity){
        if(monedaSonido == null){
            monedaSonido = MediaPlayer.create(activity, R.raw.coins);
        }
        if(monedaSonido.isPlaying()){
            monedaSonido.seekTo(0);
        }

        monedaSonido.start();
    }

    public static void reproducirCartas(Context activity){
        if(cartaSonido == null){
            cartaSonido = MediaPlayer.create(activity, R.raw.carta);
        }
        if(cartaSonido.isPlaying()){
            cartaSonido.seekTo(0);
        }
        //cartaSonido.
        cartaSonido.start();
    }


}
