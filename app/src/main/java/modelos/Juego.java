package modelos;

import android.app.Activity;
import android.media.MediaPlayer;
import android.widget.TextView;


import com.mangugu.apps.dotaitemminigame.R;

import utils.DataBase;
import utils.Sonidos;
import utils.Utils;

public class Juego {
    Activity activity;
    ParDeCartas[] cartas;
    Carta carta0;
    TextView puntajeMaximo;
    TextView puntajeActual;
    TextView monedas;
    DataBase dataBase;
    boolean puedoHacerClick = false;

    MediaPlayer[] perderSonidos;

    int[][] requisitos;
    int[] escogidos; // de las cartas que vayan clickando. (bandera 1, 0) si son correctas

    int puntajeActualEntero = 0;
    int puntajeMaximoEntero = 0;
    int monedasEntero = 0;


    public Juego(Activity activity, ParDeCartas[] cartas, Carta carta0, TextView puntajeMaximo, TextView puntajeActual, TextView monedas) {
        this.activity = activity;
        this.cartas = cartas;
        this.carta0 = carta0;
        this.puntajeMaximo = puntajeMaximo;
        this.puntajeActual = puntajeActual;
        this.monedas = monedas;

        dataBase = new DataBase(activity);


        perderSonidos = new MediaPlayer[4];
        perderSonidos[0] = MediaPlayer.create(activity, R.raw.audio_no_01);
        perderSonidos[1] = MediaPlayer.create(activity, R.raw.audio_no_02);
        perderSonidos[2] = MediaPlayer.create(activity, R.raw.audio_no_03);
        perderSonidos[3] = MediaPlayer.create(activity, R.raw.audio_no_04);

        asignarListener();
    }
    public void asignarListener(){
        for(ParDeCartas parDeCartas : cartas){
            //parDeCartas.getCarta().getLayout().setOnClickListener(new Traslador(cartaSonido, parDeCartas.getCarta().getLayout(), this));
            parDeCartas.getCarta().setEventoClickCarta(new EventoClickCarta(this, parDeCartas.getCarta()));
        }
    }
    public void nuevoJuego(){
        ItemCabecera carta0 =  elegirCarta0();// aqui cambia la carta 0!!!!
        requisitos = carta0.getRequiere();
        escogidos = new int[requisitos.length];
        asignarCartas(cartas, carta0);
    }

    private void asignarCartas(ParDeCartas[] cartas, ItemCabecera carta0){

        ItemCabecera [] itemsCabecera = dataBase.obtener8ItemsCabecera(carta0);
        int i = 0;
        String nombre;
        for (ParDeCartas par : cartas){
            par.getCarta().getEventoClickCarta().setMovido(false);// aqui le decimos que nunca se movio.

            int image = Utils.convertirNombreAID(activity, itemsCabecera[i].getImagen());
            nombre = itemsCabecera[i].getNombre();

            if(itemsCabecera[i].getId() >= Utils.CONSTANTE_RECETA_ID){
                nombre = Utils.NOMBRE_RECETA;
            }

            Carta c = par.getCarta();
            c.setCodigo(itemsCabecera[i].getId());
            c.setImagen(image);
            c.setNombre(nombre);
            Carta p = par.getPuesto();
            p.setImagen(image);
            p.setNombre(nombre);
            i++;
        }
        new Ordenador(activity,this,  cartas).ordenar();
    }
    private ItemCabecera elegirCarta0(){


        ItemCabecera itemCabecera = dataBase.obtenerCartaObjetivo();

        int image = Utils.convertirNombreAID(activity, itemCabecera.getImagen());

        carta0.getImageView().setImageResource(image);
        carta0.getTextView().setText(itemCabecera.getNombre());
        return itemCabecera;

    }

    public void aumentarVidasPorMerito(){
        aumentarVidas(1);
    }

    public void desminuirVidaPorPerdida(){
        int index = (int)(Math.random() * 4);
        if(perderSonidos[index].isPlaying())
            perderSonidos[index].seekTo(0);
        perderSonidos[index].start();
        desminuirVidas(1);

    }

    public void aumentarVidasPorAnuncio(){
        aumentarVidas(10);
    }

    public void aumentarPuntajeActual(){
        puntajeActualEntero++;
        puntajeActual.setText(puntajeActualEntero + "");
        if(puntajeActualEntero > puntajeMaximoEntero){
            puntajeMaximoEntero = puntajeActualEntero;
            puntajeMaximo.setText(puntajeMaximoEntero + "");
            grabarPuntajeMaximo();
        }
    }

    private void grabarPuntajeMaximo(){
        System.out.println("::::puntaje maximo nuevo alcanzado grandadndo...");
        dataBase.aumentarPuntaje(puntajeMaximoEntero);
    }

    private void aumentarVidas(int nVidas){
        monedasEntero += nVidas;
        monedas.setText(String.valueOf(monedasEntero));
        Sonidos.reproducirMonedas(activity);
        dataBase.aumentarMonedas(nVidas);
    }
    private void desminuirVidas(int nVidas){
        monedasEntero -= nVidas;
        if(monedasEntero < 0){
            //se acabo el juego! guardar o compartir puntaje
            activity.finish();
        }else {
            dataBase.desminuirMonedas(nVidas);
            monedas.setText(monedasEntero + "");
        }
    }

    public int[][] getRequisitos(){
        return requisitos;
    }
    public int[] getEscogidos(){
        return escogidos;
    }

    public boolean isPuedoHacerClick() {
        return puedoHacerClick;
    }

    public void setPuedoHacerClick(boolean puedoHacerClick) {
        this.puedoHacerClick = puedoHacerClick;
    }

    public int getPuntajeMaximoEntero() {
        return puntajeMaximoEntero;
    }

    public void setPuntajeMaximoEntero(int puntajeMaximoEntero) {
        this.puntajeMaximoEntero = puntajeMaximoEntero;
        puntajeMaximo.setText(String.valueOf(puntajeMaximoEntero));
    }

    public int getMonedasEntero() {
        return monedasEntero;
    }

    public void setMonedasEntero(int monedasEntero) {
        this.monedasEntero = monedasEntero;
        monedas.setText(String.valueOf(monedasEntero));
    }

    public int getPuntajeActualEntero(){
        return puntajeActualEntero;
    }
}
