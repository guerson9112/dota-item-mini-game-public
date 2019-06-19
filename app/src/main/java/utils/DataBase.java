package utils;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.mangugu.apps.dotaitemminigame.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import modelos.ItemCabecera;

public class DataBase {
    SQLiteDatabase database;
    Activity activity;
    public DataBase(Activity activity){
        this.activity = activity;
        database = activity.openOrCreateDatabase("database.db", Activity.MODE_PRIVATE, null);
        crearTablas();
        try{
            insertarItems();
        }catch (Exception e){
            System.err.println("el errore essss: " + e);
        }
    }
    public void crearTablas(){
        database.execSQL("CREATE TABLE IF NOT EXISTS usuario (" +
                "id TEXT, " +
                "tipo_cuenta INTEGER, " +
                "puntaje INTEGER, " +
                "monedas INTEGER, " +
                "puntaje_publicado INTEGER)");
        database.execSQL("CREATE TABLE IF NOT EXISTS \"relacion\" (\n" +
                "\t\"padre\"\tINTEGER,\n" +
                "\t\"hijo\"\tINTEGER,\n" +
                "\t\"multiple\"\tINTEGER\n" +
                ");");
        database.execSQL("CREATE TABLE IF NOT EXISTS \"item_cabecera\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"imagen\"\tTEXT,\n" +
                "\t\"nombre\"\tTEXT,\n" +
                "\t\"receta\"\tINTEGER,\n" +
                "\tPRIMARY KEY(\"id\")\n" +
                ");");
    }

    public void agregarItem(int id, String imagen, String nombre, boolean esReceta){
        int rec = esReceta ? 1 : 0;
        database.execSQL("INSERT INTO item_cabecera VALUES(" + id + ", '" + imagen + "', '" + nombre + "', " + rec + ")");
    }

    public void insertarItems() throws Exception {

        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM item_cabecera", null);
        cursor.moveToFirst();
        int cantidad = cursor.getInt(0);
        if(cantidad != 0){
            return;
        }

        InputStream inputStream = activity.getResources().openRawResource(R.raw.items);
        BufferedReader lector = new BufferedReader(new InputStreamReader(inputStream));
        String linea = "";
        while (true){
            linea = lector.readLine();
            if(linea == null){
                break;
            }
            if(linea.isEmpty()){
                continue;
            }
            String[] partes = linea.split("-");
            int id = Integer.parseInt(partes[0]);
            agregarItem(id, partes[1], partes[2], partes[3].equals("1"));
            String[] hijos = partes[4].split("\\.");

            for(String hijo : hijos){

                if (hijo.contains(":")){
                    String[] multiples = hijo.split(":");
                    for (String multiple :  multiples){
                        agregarRelacion(id, Integer.parseInt(multiple), 1);
                    }
                }else{
                    if(!hijo.equals("0")) {
                        agregarRelacion(id, Integer.parseInt(hijo), 0);

                    }
                }

            }
        }

    }

    public void agregarRelacion(int padre, int hijo, int multiple){
        database.execSQL("INSERT INTO relacion VALUES(" + padre + ", " + hijo + ", " + multiple + ")");
    }

    public ItemCabecera obtenerCartaObjetivo(){
        Cursor cursor = database.rawQuery("SELECT DISTINCT padre FROM relacion", null);
        cursor.moveToFirst();
        int cantidad = cursor.getCount();

        int indexItem = new Random().nextInt(cantidad);
        for(int i = 0 ; i < indexItem ; i++){
            cursor.moveToNext();
        }

        int idPadre = cursor.getInt(0);
        cursor.close();

        return  obtenerItemCabecera(idPadre);
    }

    public ItemCabecera obtenerItemCabecera(int id){
        Cursor cursor = database.rawQuery("SELECT * FROM item_cabecera WHERE id = " + id, null);
        cursor.moveToFirst();

        return new ItemCabecera(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) == 1, obtenerRequisitos(cursor.getInt(0)));
    }


    public ItemCabecera[] obtenerItemsCabecera(String wheres){

        //String consulta = "SELECT item_cabecera.id, item_cabecera.imagen, item_cabecera.nombre, item_cabecera.receta FROM relacion RIGHT JOIN (SELECT DISTINCT hijo FROM relacion) AS hijos ON hijos.hijo = item_cabecera.id";
        //Cursor cursor = database.rawQuery("SELECT * FROM item_cabecera WHERE receta = 0 " + wheres + " ORDER BY RANDOM() LIMIT 6", null);
        Cursor cursor = database.rawQuery("SELECT item_cabecera.id, item_cabecera.imagen, item_cabecera.nombre, item_cabecera.receta FROM (SELECT DISTINCT hijo FROM relacion) AS hijos  LEFT JOIN item_cabecera ON hijos.hijo = item_cabecera.id WHERE receta = 0 " + wheres + " ORDER BY RANDOM() LIMIT 6", null);
        cursor.moveToFirst();
        ItemCabecera [] itemsCabecera = new ItemCabecera[cursor.getCount()];



        for (int i = 0 ; i < itemsCabecera.length ; i++){

            itemsCabecera[i] = new ItemCabecera(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) == 1, obtenerRequisitos(cursor.getInt(0)));
            cursor.moveToNext();
        }

        return itemsCabecera;
    }

    public ItemCabecera[] obtener8ItemsCabecera(ItemCabecera carta0){

        String wheres = "";
        int[][] requisitos = carta0.getRequiere();
        int cantidad = 0;
        for(int i = 0 ; i < requisitos.length ; i++){
            cantidad += requisitos[i].length;
        }

        ItemCabecera[] items = new ItemCabecera[8];// antes aqui habia 'cantidad'

        int index = 0 ;

        int idDeLaReceta = -1;
        for(int i = 0 ; i < requisitos.length ; i++){
            for(int j = 0 ; j < requisitos[i].length ; j++){
                items[index] = obtenerItemCabecera(requisitos[i][j]);
                wheres += " AND id != " + requisitos[i][j] + " ";
                if(requisitos[i][j] >= Utils.CONSTANTE_RECETA_ID){
                    idDeLaReceta = requisitos[i][j];
                }
                index++;
            }
        }

        ItemCabecera[] itemFaltantes = obtenerItemsCabecera(wheres);

        for(int i = 0 ; i < itemFaltantes.length && index + i < 8 ; i++){
            items[index + i] = itemFaltantes[i];
        }


        if(idDeLaReceta == -1){
            items[7] = obtenerItemCabecera(300);
        }

        items = Utils.barajearRequisitos(items);

        return items;
    }

    public int[][] obtenerRequisitos(int padre){
        int[] multiple = null;
        Cursor cursorMultiple = database.rawQuery("SELECT hijo, multiple FROM relacion WHERE padre = " + padre + " AND multiple = 1", null);
        if(cursorMultiple.getCount() > 0){
            cursorMultiple.moveToFirst();
            multiple = new int[cursorMultiple.getCount()];
            for(int i = 0 ; i < cursorMultiple.getCount() ; i++){
                multiple[i] = cursorMultiple.getInt(0);
                cursorMultiple.moveToNext();
            }
        }
        cursorMultiple.close();

        Cursor cursorNoMultiple = database.rawQuery("SELECT hijo, multiple FROM relacion WHERE padre = " + padre + " AND multiple = 0", null);
        int[][] requisitos = new int[(multiple == null ? 0 : 1) + cursorNoMultiple.getCount()][];

        cursorNoMultiple.moveToFirst();
        int i;
        for(i = 0 ; i < cursorNoMultiple.getCount() ; i++){
            requisitos[i] = new int[1];
            requisitos[i][0] = cursorNoMultiple.getInt(0);
            cursorNoMultiple.moveToNext();
        }
        if (multiple != null){
            requisitos[i] = multiple;
        }
        cursorNoMultiple.close();
        return requisitos;
    }

    public void iniciarSesion(String id, int tipoCuenta, int puntaje, int monedas){
        database.execSQL("INSERT INTO usuario VALUES ('" + id + "', " + tipoCuenta + ", " + puntaje + ", " + monedas + ", 0)");
    }

    public void aumentarMonedas(int monedas){
        database.execSQL("UPDATE usuario SET monedas = monedas + " + monedas);
    }

    public void desminuirMonedas(int monedas){
        database.execSQL("UPDATE usuario SET monedas = monedas - " + monedas);
    }

    public void aumentarPuntaje(int puntaje){
        Cursor cursor = database.rawQuery("SELECT puntaje FROM usuario", null);
        cursor.moveToFirst();
        if(puntaje > cursor.getInt(0)){
            database.execSQL("UPDATE usuario SET puntaje = " + puntaje + ", puntaje_publicado = 0");
        }
    }

    public void setPuntajePublicado(){
        database.execSQL("UPDATE usuario SET puntaje_publicado = 1");
    }

    public void cerrarSesion(){
        database.execSQL("DELETE FROM usuario");
    }

    public int[] obtnerPuntejeYMonedas(){
        int[] puntajeYmonedas = new int[2];

        Cursor cursor =  database.rawQuery("SELECT puntaje, monedas FROM usuario", null);

        cursor.moveToFirst();
        puntajeYmonedas[0] = cursor.getInt(0);
        puntajeYmonedas[1] = cursor.getInt(1);

        return puntajeYmonedas;
    }
    public void cerrar(){
        database.close();
    }
}
