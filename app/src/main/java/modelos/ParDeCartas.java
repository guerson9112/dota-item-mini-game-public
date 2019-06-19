package modelos;

public class ParDeCartas {
    Carta carta;
    Carta puesto;

    public ParDeCartas(Carta carta, Carta puesto) {
        this.carta = carta;
        this.puesto = puesto;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }

    public Carta getPuesto() {
        return puesto;
    }

    public void setPuesto(Carta puesto) {
        this.puesto = puesto;
    }
}
