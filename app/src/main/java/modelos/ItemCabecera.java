package modelos;

public class ItemCabecera {
    int id;
    String imagen;
    String nombre;
    boolean esReceta;
    //indice i: pre requisito
    //indice j: pre requisito multiple (power boots)
    int[][] requiere;


    public ItemCabecera(int id, String imagen, String nombre, boolean esReceta, int[][] requiere) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.esReceta = esReceta;
        this.requiere = requiere;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEsReceta() {
        return esReceta;
    }

    public void setEsReceta(boolean esReceta) {
        this.esReceta = esReceta;
    }

    public int[][] getRequiere() {
        return requiere;
    }

    public void setRequiere(int[][] requiere) {
        this.requiere = requiere;
    }
}
