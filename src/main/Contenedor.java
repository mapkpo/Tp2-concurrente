package main;

import java.util.ArrayList;
public class Contenedor {
    private final ArrayList<Image> imagenes;
    private int agregadas;
    //private final int totales;

    //public Contenedor(int cantidad) {
    public Contenedor() {
        this.imagenes =  new ArrayList<>();
        this.agregadas = 0;
        //this.totales = cantidad;
    }

    /*void agregar(Imagen imagen){
        if(agregadas < totales){
            this.imagenes.add(imagen);
            agregarContador();
        }
        else {
            System.out.println("Ya se ha alcanzado el límite");
        }
    }*/
    void agregar(Image image){
        this.imagenes.add(image);
        agregarContador();
    }

    private void agregarContador(){
        this.agregadas++;
    }

    public int getAgregadas(){
        return agregadas;
    }

    public Image getImagen() {
        if (!imagenes.isEmpty()) {
            Image img = imagenes.remove(imagenes.size() - 1);
            //imagenes.remove(img); al pedo

            return img;
        }
        return null;
    }

    public void removerImagen(Image img){
        imagenes.remove(img);
    }

}
