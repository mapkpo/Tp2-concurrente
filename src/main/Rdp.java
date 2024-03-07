package main;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import java.util.ArrayList;
import java.util.List;
public class Rdp {
    private final double[][] incidenciam = {
            {1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, -1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.},
            {0, -1, -1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1.},
            {0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, -1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, -1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, -1, -1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, -1, -1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, -1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 1}
    };
    private final double[] marcadoinicial = {0, 1, 0, 3, 0, 1, 0, 1, 0, 2, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1};
    private final double[] transicionm = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private final int[] contadordedisparos = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public Rdp() {
    }

    private final RealMatrix incidencia = MatrixUtils.createRealMatrix(incidenciam);
    private RealVector marcado = new ArrayRealVector(marcadoinicial);
    private final RealVector transicion = new ArrayRealVector(transicionm);


    public boolean issensibilizada(int a){
        if(a>=0 && a<transicion.getDimension()) {
            RealVector adisparar = transicion.copy();
            adisparar.setEntry(a, 1);

            RealVector result = incidencia.operate(adisparar).add(marcado);
            for (int i = 0; i < result.getDimension(); i++) {
                if (result.getEntry(i) < 0) {
                    return false;
                }
            }
            return true;
    }
        System.out.println("Fuera de rango");
        return false;
    }

    public List<Integer> cualessensibilizadas() {
        List<Integer> lista = new ArrayList<>();

        for (int i = 0; i < transicion.getDimension(); i++) {
            RealVector aprobar = transicion.copy();
            aprobar.setEntry(i, 1);

            int contador = 0;

            RealVector result = incidencia.operate(aprobar).add(marcado);
            for (int j = 0; j < result.getDimension(); j++) {
                if (result.getEntry(j) < 0) {
                    //System.out.println("Fuera de rango");
                    contador++;
                }
            }
            if (contador == 0){
                lista.add(i);
            }
        }
        return lista;
    }

    public void disparar(int a){
        if (a < transicionm.length){
            if(issensibilizada(a)){
                RealVector adisparar1 = transicion.copy();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                adisparar1.setEntry(a, 1);

                RealVector nuevomarcado = incidencia.operate(adisparar1).add(marcado);
                actualizarmarcado(nuevomarcado);

                contadordedisparos[a]++;
            }
        }
        else {
            System.out.println("Fuera de rango");
        }
    }

    private void actualizarmarcado(RealVector a){
        marcado = a;
    }

    public void imprimirmarcado(){
        for(int i=0; i<marcado.getDimension(); i++){
            System.out.println(marcado.getEntry(i));
        }
    }

    public void imprimircontador(){
        //for(int i=0; i<17; i++){
            System.out.print(contadordedisparos[13]);
            System.out.print(",");
            System.out.print(contadordedisparos[14]);
        //}
        System.out.println("");
    }

}
