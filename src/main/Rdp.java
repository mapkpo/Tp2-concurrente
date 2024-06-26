package main;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class Rdp {
    private final double[][] incidenciam = {

        //  t0  t1  t2   t3  t4  t5  t6  t7  t8  t9  t10 t11 t12 t13 t14 t15 t16
            {1, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //p0
            {0, -1,  0,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //p1
            {0,  1,  0, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //p2
            {0, -1, -1,  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1,  1}, //p3
            {0,  0,  1,  0, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //p4
            {0,  0, -1,  0,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //p5
            {0,  0,  0,  1,  1, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //p6
            {0,  0,  0,  0,  0, -1,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0,  0}, //p7
            {0,  0,  0,  0,  0,  1,  0, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0}, //p8
            {0,  0,  0,  0,  0, -1, -1,  0,  0,  1,  1,  0,  0,  0,  0,  0,  0}, //p9
            {0,  0,  0,  0,  0,  0,  1,  0, -1,  0,  0,  0,  0,  0,  0,  0,  0}, //p10
            {0,  0,  0,  0,  0,  0, -1,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0}, //p11
            {0,  0,  0,  0,  0,  0,  0,  1,  0, -1,  0,  0,  0,  0,  0,  0,  0}, //p12
            {0,  0,  0,  0,  0,  0,  0,  0,  1,  0, -1,  0,  0,  0,  0,  0,  0}, //p13
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1, -1, -1,  0,  0,  0,  0}, //p14
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1, -1,  1,  1,  0,  0}, //p15
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  0, -1,  0,  0,  0}, //p16
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  0, -1,  0,  0}, //p17
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1, -1,  0}, //p18
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1, -1}, //p19
            {0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1,  1}  //p20
    };

                                        //p  0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20
    private final double[] marcadoinicial = {0, 1, 0, 3, 0, 1, 0, 1, 0, 2, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1};

    private final double[] transicionm = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    List<Integer> transicionsleeptime = Collections.unmodifiableList
            (Arrays.asList(100, 0, 0, 100, 100, 0, 0, 100, 100, 100, 100, 0, 0, 100, 100, 0, 100));

    private final long[] transiciontime = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private final int[] contadordedisparos = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private String secuencia = "";

    public Rdp() {
        //-1 significa que la transicion no esta sensibilizada por lo que el tiempo aun no corre.
        Arrays.fill(transiciontime, -1);
        transiciontime[0] = System.currentTimeMillis();
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

            if (transiciontime[a] == -1)
                transiciontime[a] = System.currentTimeMillis();
            return (System.currentTimeMillis() - transiciontime[a] > transicionsleeptime.get(a));
        }
        System.out.println(a + "esta fuera de rango");
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
        testinvarianteplaza();
        if(issensibilizada(a)){
            RealVector adisparar1 = transicion.copy();

            adisparar1.setEntry(a, 1);

            RealVector nuevomarcado = incidencia.operate(adisparar1).add(marcado);
            actualizarmarcado(nuevomarcado);

            transiciontime[a] = -1;
            issensibilizada(a);

            secuencia += "T" + a + "-";

            contadordedisparos[a]++;
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
            System.out.print("contador del balanceo de la politica: ");
            System.out.print(contadordedisparos[13]);
            System.out.print(" , ");
            System.out.print(contadordedisparos[14]);
        //}
        System.out.println();
        //System.out.println("secuencia: " + secuencia);
    }

    private void testinvarianteplaza(){
        boolean p1, p2, p3, p4, p5, p6, p7, p8;

        /*
        for(int i=0; i<21; i++){
            System.out.print("plaza: "+i+""+marcado.getEntry(i)+" , ");
        }
        System.out.println();
        */

        p1 = ((((int)marcado.getEntry(1))+((int)marcado.getEntry(2))) == 1);
        p2 = ((((int)marcado.getEntry(4))+((int)marcado.getEntry(5))) == 1);
        p3 = ((((int)marcado.getEntry(19))+((int)marcado.getEntry(20))) == 1);
        p4 = ((((int)marcado.getEntry(15))+((int)marcado.getEntry(16))+((int)marcado.getEntry(17))) == 1);
        p4 = ((((int)marcado.getEntry(7))+((int)marcado.getEntry(8))+((int)marcado.getEntry(12))) == 1);
        p5 = ((((int)marcado.getEntry(15))+((int)marcado.getEntry(16))+((int)marcado.getEntry(17))) == 1);
        p6 = ((((int)marcado.getEntry(10))+((int)marcado.getEntry(11))+((int)marcado.getEntry(13))) == 1);
        p7 = ((((int)marcado.getEntry(8))+((int)marcado.getEntry(9))+((int)marcado.getEntry(10))+((int)marcado.getEntry(12))+((int)marcado.getEntry(13))) == 2);
        p8 = ((((int)marcado.getEntry(2))+((int)marcado.getEntry(3))+((int)marcado.getEntry(4))+((int)marcado.getEntry(19))) == 3);

        if (!(p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8)){
       
            System.out.println("ERROR EN INVARIANTE DE TRANSICION, CERRANDO EJECUCION.");
            System.exit(0);
        }
    }

    public String getSecuencia(){
        return secuencia;
    }
}
