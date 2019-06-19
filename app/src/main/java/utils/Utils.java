package utils;

import android.app.Activity;

import java.util.Random;

import modelos.ItemCabecera;

public class Utils {
    public static int CONSTANTE_RECETA_ID = 300;
    public static String NOMBRE_RECETA = "Receta";
    public static int convertirNombreAID(Activity activity, String nombre){
        return activity.getResources().getIdentifier(nombre, "mipmap", activity.getPackageName());
    }
    public static ItemCabecera[] barajearRequisitos(ItemCabecera[] items){
        Random random = new Random();
        ItemCabecera temporal;
        for(int i = 0 ; i < items.length ; i++){
            int indexRandom = random.nextInt(items.length);
            temporal = items[indexRandom];
            items[indexRandom] = items[i];
            items[i] = temporal;
        }
        return items;
    }

}
