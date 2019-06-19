package modelos;

import android.view.View;

import java.util.Random;

import utils.Sonidos;

public class EventoClickCarta implements View.OnClickListener {


    Juego juego;
    Carta carta;
    boolean movido = false;
    int[] rotaciones = {-40, -30, -20, -10, 10, 20, 30, 40};
    Random random;
    Traslador traslador;

    public EventoClickCarta(Juego juego, Carta carta){
        this.juego = juego;
        this.carta = carta;
        random = new Random();
        traslador = new Traslador();
    }

    @Override
    public void onClick(View v) {
        if (!movido){
            if(juego.isPuedoHacerClick()) {
                Sonidos.reproducirCartas(v.getContext());

                int indexRotacion = random.nextInt(rotaciones.length);
                carta.getLayout().startAnimation(Traslador.getTrasladores()[indexRotacion]);
                movido = true;

                boolean contiene = false;
                int hechos = 0;// cuantas cartas faltan para formar el item
                for (int i = 0; i < juego.getRequisitos().length; i++) {
                    if (juego.getEscogidos()[i] == 0) {
                        for (int j = 0; j < juego.getRequisitos()[i].length; j++) {
                            if (juego.getRequisitos()[i][j] == carta.getCodigo() && !contiene) {
                                contiene = true;
                                juego.getEscogidos()[i] = 1;
                                hechos++;
                                break;
                            }
                        }
                    } else {
                        hechos++;
                    }
                }
                if (contiene) {
                    if (hechos == juego.getRequisitos().length) {
                        juego.aumentarPuntajeActual();
                        if(juego.getPuntajeActualEntero() % 10 == 0){
                            juego.aumentarVidasPorMerito();
                        }
                        juego.nuevoJuego();
                    }
                } else {
                    juego.desminuirVidaPorPerdida();
                    juego.nuevoJuego();
                }
            }
        }

    }
    public void setMovido(boolean movido){
        this.movido = movido;
    }

}
