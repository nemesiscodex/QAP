/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tpia3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import lectura.LectorQAP;
import nsga.Nsga;
import qap.Qap;

/**
 *
 * @author Julio
 */
public class TPIA3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        Qap p1 = new Qap();
        Qap p2 = new Qap();
        LectorQAP.leer("qapUni.75.0.1.qap.txt",p1);
        LectorQAP.leer("qapUni.75.p75.1.qap.txt",p2);
        Nsga n1 = new Nsga(500, 10, p1);
        System.out.println(n1.run());
    }
    
}
