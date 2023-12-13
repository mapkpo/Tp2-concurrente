package main;

import java.util.ArrayList;
public class Contenedor {
    private final ArrayList<Imagen> imagenes;
    private int agregadas;
    private final int totales;

    public Contenedor(int cantidad) {
        this.imagenes =  new ArrayList<>();
        this.agregadas = 0;
        this.totales = cantidad;
    }

    void agregar(Imagen imagen){
        if(agregadas < totales){
            this.imagenes.add(imagen);
            agregarContador();
        }
        else {
            System.out.println("Ya se ha alcanzado el límite");
        }
    }

    private void agregarContador(){
        this.agregadas++;
    }

    public int getAgregadas(){
        return agregadas;
    }

    public Imagen getImagen() {
        if (!imagenes.isEmpty()) {
            Imagen img = imagenes.get(imagenes.size() - 1);
            return img;
        }
        return null;
    }

    public void removerImagen(Imagen img){
        imagenes.remove(img);
    }

}
