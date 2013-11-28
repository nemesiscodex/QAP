/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpia3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import lectura.LectorQAP;
import nsga.Individuo;
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
    public static void main(String[] args) {
        Qap p1 = new Qap();
        Qap p2 = new Qap();
        LectorQAP.leer("qapUni.75.0.1.qap.txt", p1);
        LectorQAP.leer("qapUni.75.p75.1.qap.txt", p2);
        Nsga n1 = new Nsga(10000, 100, p1);
        crearYtrue(p1, "qapUni.75.0.1.qap.txt");
    }

    public static void crearYtrue(Qap qap, String name) {
        String s = "";
        List<Individuo> l = new ArrayList();
        Nsga n;
        try {

            OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(name + "-ytrue.csv"), "UTF-8");
            BufferedWriter fbw = new BufferedWriter(writer);
            for (int i = 0; i < 15; i++) {
                l.addAll(new Nsga(10000, 100, qap).run());
                
            }
            l = actualizarFrentePareto(l);
            for (Individuo in : l) {
                    s += in.toString();
                }
                fbw.write(s);
            fbw.newLine();
            fbw.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static List<Individuo> actualizarFrentePareto(List<Individuo> poblacion) {
        List<Individuo> fParetoOptimo = new ArrayList<Individuo>();

        for (Individuo ind : poblacion) {
            ind.setDominado(false);
        }

        //Agregar al frente pareto los individuos de la poblacion que no son dominados por el frente
        for (int i = 0; i < poblacion.size(); i++) {
            for (int j = 0; j < fParetoOptimo.size(); j++) {
                if (dominaA(fParetoOptimo.get(j), poblacion.get(i))) {
                    poblacion.get(i).setDominado(true);
                    break;
                }
            }
            if (!poblacion.get(i).isDominado()) {
                if (!estaEnFOP(poblacion.get(i), fParetoOptimo)) {
                    fParetoOptimo.add(poblacion.get(i));
                }
            }
        }

        for (Individuo ind : fParetoOptimo) {
            ind.setDominado(false);
        }

        // Eliminar soluciones dominada en el frenteParetoOptimo
        for (int i = 0; i < fParetoOptimo.size(); i++) {
            for (int j = 0; j < fParetoOptimo.size(); j++) {
                if (dominaA(fParetoOptimo.get(j), fParetoOptimo.get(i))) {
                    fParetoOptimo.get(i).setDominado(true);
                    break;
                }
            }
        }
        List<Individuo> frentenew = new ArrayList<Individuo>();
        for (Individuo ind : fParetoOptimo) {
            if (!ind.isDominado()) {
                frentenew.add(ind);
            }
        }
        fParetoOptimo = frentenew;
        return fParetoOptimo;

    }

    private static boolean dominaA(Individuo x, Individuo y) {
        if (x.getFnValue(0) < y.getFnValue(0)) {
            if (x.getFnValue(1) <= y.getFnValue(1)) {
                return true;
            }
        } else if (x.getFnValue(0) == y.getFnValue(0)) {
            if (x.getFnValue(1) < y.getFnValue(1)) {
                return true;
            }
        }
        return false;
    }

    private static boolean estaEnFOP(Individuo ind, List<Individuo> fParetoOptimo) {
        int[] crom1 = ind.getCromosoma();
        for (Individuo paretoInd : fParetoOptimo) {
            int[] crom2 = paretoInd.getCromosoma();
            boolean es = true;
            for (int i = 0; i < ind.getN(); i++) {
                if (crom1[i] != crom2[i]) {
                    es = false;
                    break;
                }
            }
            if (es) {
                return true;
            }
        }
        return false;
    }
}
