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
    private final double[][] incidenceMatrix = {

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
    private final double[] initialMarking = {0, 1, 0, 3, 0, 1, 0, 1, 0, 2, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1};

    private final double[] transitionMatrix = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    List<Integer> transitionSleepTime = Collections.unmodifiableList
            (Arrays.asList(100, 0, 0, 100, 100, 0, 0, 100, 100, 100, 100, 0, 0, 100, 100, 0, 100));

    private final long[] transitionTime = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private final int[] firedCount = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private String sequence = "";

    public Rdp() {
        //-1 significa que la transicion no esta sensibilizada por lo que el tiempo aun no corre.
        Arrays.fill(transitionTime, -1);
        transitionTime[0] = System.currentTimeMillis();
    }

    private final RealMatrix incidence = MatrixUtils.createRealMatrix(incidenceMatrix);
    private RealVector marking = new ArrayRealVector(initialMarking);
    private final RealVector transition = new ArrayRealVector(transitionMatrix);


    public boolean isEnabled(int a){
        if(a>=0 && a< transition.getDimension()) {
            RealVector adisparar = transition.copy();
            adisparar.setEntry(a, 1);

            RealVector result = incidence.operate(adisparar).add(marking);
            for (int i = 0; i < result.getDimension(); i++) {
                if (result.getEntry(i) < 0) {
                    return false;
                }
            }

            if (transitionTime[a] == -1)
                transitionTime[a] = System.currentTimeMillis();
            return (System.currentTimeMillis() - transitionTime[a] > transitionSleepTime.get(a));
        }
        System.out.println(a + "esta fuera de rango");
        return false;
    }

    public List<Integer> whichEnabled() {
        List<Integer> lista = new ArrayList<>();

        for (int i = 0; i < transition.getDimension(); i++) {
            RealVector aprobar = transition.copy();
            aprobar.setEntry(i, 1);

            int contador = 0;

            RealVector result = incidence.operate(aprobar).add(marking);
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

    public void fire(int a){
        testPlaceInvariant();
        if(isEnabled(a)){
            RealVector adisparar1 = transition.copy();

            adisparar1.setEntry(a, 1);

            RealVector nuevomarcado = incidence.operate(adisparar1).add(marking);
            updateMarking(nuevomarcado);

            transitionTime[a] = -1;


            if(a<10){
                sequence += "T0" + a;
            } 
            else {
                sequence += "T" + a;
            }

            firedCount[a]++;
        }
    }

    private void updateMarking(RealVector a){
        marking = a;
    }

    public void printMarking(){
        for(int i = 0; i< marking.getDimension(); i++){
            System.out.println(marking.getEntry(i));
        }
    }

    public void printCounter(){ //este valor siempre va a ser mayor al numero de invariantes deseados debido a que la red 
        //se sigue disparando hasta que se paran todos los hilos.
            System.out.print("Contador del balanceo de la politica: ");
            System.out.print(firedCount[11]);
            System.out.print(" , ");
            System.out.print(firedCount[12]);
            System.out.println();
    }

    public String counterString(){
        return ("Contador del balanceo de la política: "+ firedCount[11]+" , "+ firedCount[12]);
    }

    private void testPlaceInvariant(){
        boolean p1, p2, p3, p4, p5, p6, p7, p8;

        /*
        for(int i=0; i<21; i++){
            System.out.print("plaza: "+i+""+marcado.getEntry(i)+" , ");
        }
        System.out.println();
        */

        p1 = ((((int) marking.getEntry(1))+((int) marking.getEntry(2))) == 1);
        p2 = ((((int) marking.getEntry(4))+((int) marking.getEntry(5))) == 1);
        p3 = ((((int) marking.getEntry(19))+((int) marking.getEntry(20))) == 1);
        p4 = ((((int) marking.getEntry(15))+((int) marking.getEntry(16))+((int) marking.getEntry(17))) == 1);
        p4 = ((((int) marking.getEntry(7))+((int) marking.getEntry(8))+((int) marking.getEntry(12))) == 1);
        p5 = ((((int) marking.getEntry(15))+((int) marking.getEntry(16))+((int) marking.getEntry(17))) == 1);
        p6 = ((((int) marking.getEntry(10))+((int) marking.getEntry(11))+((int) marking.getEntry(13))) == 1);
        p7 = ((((int) marking.getEntry(8))+((int) marking.getEntry(9))+((int) marking.getEntry(10))+((int) marking.getEntry(12))+((int) marking.getEntry(13))) == 2);
        p8 = ((((int) marking.getEntry(2))+((int) marking.getEntry(3))+((int) marking.getEntry(4))+((int) marking.getEntry(19))) == 3);

        if (!(p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8)){
       
            System.out.println("ERROR EN INVARIANTE DE TRANSICION, CERRANDO EJECUCION.");
            System.exit(0);
        }
    }

    public String getSequence(){
        return sequence;
    }
}
