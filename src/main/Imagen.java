package main;

public class Imagen {

    private boolean recortada;
    private boolean[] ajustada;

    public Imagen(){
        recortada = false;
        ajustada = new boolean[]{false, false};

    }

    public void recortar(){
        recortada = true;
    }

    public void ajustarinicio(){
        ajustada[0] = true;
    }

    public void ajustarfinal(){
        ajustada[1] = true;
    }

    public boolean isrecortada(){
        return recortada;
    }

    public boolean isajustada() {
        for (boolean b : ajustada) {
            if (!b) {
                return false;
            }
        }
        return true;
    }
}
