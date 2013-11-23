/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lectura;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import qap.Qap;

/**
 *
 * @author Julio
 */
public class LectorQAP {
    public static void leer(String file, Qap qap){
        try {
            // TODO code application logic here
            File a = new File("src/txt/"+file);
            Scanner in = new Scanner(a);
            Integer[][][] flujo;
            qap.setN(in.nextInt());
            flujo = new Integer[2][qap.getN()][qap.getN()];
            for(int i=0;i<2;i++){
                for(int j=0;j<qap.getN();j++){
                    for(int k=0;k<qap.getN();k++){
                        flujo[i][j][k] = in.nextInt();
                    }
                }
            }
            Integer[][] distancia;
            distancia = new Integer[qap.getN()][qap.getN()];
            for(int i=0;i<qap.getN();i++){
                    for(int j=0;j<qap.getN();j++){
                        distancia[i][j] = in.nextInt();
                    }
            }
            qap.setDistancia(distancia);
            qap.setFlujo(flujo);
        } catch (FileNotFoundException ex) {
            System.err.println("No se encuentra el Archivo.");
        } 
    }
}
