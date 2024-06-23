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

    public void ajustarInicio(){
        ajustada[0] = true;
    }

    public void ajustarFinal(){
        ajustada[1] = true;
    }

    public boolean isRecortada(){
        return recortada;
    }

    public boolean isAjustada() {
        for (boolean b : ajustada) {
            if (!b) {
                return false;
            }
        }
        return true;
    }
}
