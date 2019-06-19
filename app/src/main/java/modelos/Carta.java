package modelos;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Carta {
    int codigo;
    String nombre;
    int imagen;
    LinearLayout layout;
    ImageView imageView;
    TextView textView;
    EventoClickCarta eventoClickCarta;

    public Carta(LinearLayout layout, ImageView imageView, TextView textView, EventoClickCarta eventoClickCarta) {
        this.layout = layout;
        this.imageView = imageView;
        this.textView = textView;
        this.eventoClickCarta = eventoClickCarta;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        textView.setText(nombre);
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
        imageView.setImageResource(imagen);
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public void setLayout(LinearLayout layout) {
        this.layout = layout;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public EventoClickCarta getEventoClickCarta() {
        return eventoClickCarta;
    }

    public void setEventoClickCarta(EventoClickCarta eventoClickCarta) {
        this.eventoClickCarta = eventoClickCarta;
        layout.setOnClickListener(eventoClickCarta);
    }
}
