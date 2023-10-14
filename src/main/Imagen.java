package main;

public class Imagen {

    private boolean ajustada;
    private boolean[] recortada;

    public Imagen(){
        ajustada = false;
        recortada = new boolean[]{false, false};

    }

    public void ajustar(){
        ajustada = true;
    }

    public void recortarinicio(){
        recortada[0] = true;
    }

    public void recortarfinal(){
        recortada[1] = true;
    }

    public boolean isajustada(){
        return ajustada;
    }

    public boolean isrecortada() {
        for (boolean b : recortada) {
            if (!b) {
                return false;
            }
        }
        return true;
    }
}
