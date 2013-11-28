/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nsga;

import java.util.ArrayList;
import java.util.List;
import qap.Qap;

/**
 *
 * @author Julio
 */
public class Nsga {

    private int generaciones;
    private int tamPoblacion;
    private Qap qap;
    private List<Individuo> poblacion;
    private List<Individuo> fParetoOptimo;
    private final double SIGMA_SHARE = 50000.0;
    private final double EPSILON = 0.1;
    private final double PORCENTAJE_CRUZAMIENTO = 1;
    private final double PORCENTAJE_MUTACION = 0.1;

    public Nsga(int gen, int tam, Qap qap) {
        generaciones = gen;
        tamPoblacion = tam;
        this.qap = qap;
    }

    private void generarPoblacionInicial() {
        poblacion = new ArrayList<Individuo>();
        for (int i = 0; i < tamPoblacion; i++) {
            poblacion.add(new Individuo(qap.getN()));
        }
    }

    private void asignarFnValues() {
        for (Individuo ind : poblacion) {
            ind.asignarFnValue(qap);
        }
    }

    private void asignarFitness() {
        //Marcar la poblacion como no dominada
        for (Individuo ind : poblacion) {
            ind.setDominado(false);
        }

        int frentes = 0; // cuenta de frentes
        double df = (double) qap.getN(); // dummyFitness para la poblacion inicial

        List<Individuo> poblacionaux = new ArrayList<Individuo>();

        poblacionaux.addAll(poblacion); //poblacionaux para sacar individuos

        while (!poblacionaux.isEmpty()) {
            frentes++;
            for (int i = 0; i < poblacionaux.size(); i++) {
                for (int j = 0; j < poblacionaux.size(); j++) {
                    //comparar los individuos
                    if ((i != j)
                            && dominaA(poblacionaux.get(j), poblacionaux.get(i))) {

                        poblacionaux.get(i).setDominado(true);
                        break;
                    }
                }
            }
            // sacar todos los individuos no dominados
            List<Individuo> frentePareto = new ArrayList<Individuo>();
            for (Individuo ind : poblacionaux) {
                //boolean b = ind.isDominado();
                if (!ind.isDominado()) {
                    //asignarles el dummyFitness
                    ind.setFitness((double) df);
                    frentePareto.add(ind);
                }
            }
            fitnessSharing(frentePareto);

            // df para el siguiente frente
            df = siguienteDF(frentePareto);

            removerDePoblacion(poblacionaux, frentePareto);

            for (Individuo ind : poblacionaux) {
                ind.setDominado(false);
            }
        }

    }

    private boolean dominaA(Individuo x, Individuo y) {
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

    private void fitnessSharing(List<Individuo> frentePareto) {
        double[] ncount = new double[frentePareto.size()];
        double SH = 0.0;

        for (int i = 0; i < frentePareto.size(); i++) {
            ncount[i] = 0;
        }

        for (int i = 0; i < frentePareto.size(); i++) {
            for (int j = 0; j < frentePareto.size(); j++) {
                double dij = distancia(frentePareto.get(i), frentePareto.get(j));
                if (dij <= SIGMA_SHARE) {
                    SH = 1 - Math.pow(dij / SIGMA_SHARE, 2);
                } else {
                    SH = 0;
                }
                ncount[i] += SH;
            }
            Individuo actual = frentePareto.get(i);
            actual.setFitness(actual.getFitness() / ncount[i]);
        }
    }

    private double siguienteDF(List<Individuo> frentePareto) {
        double df = Double.MAX_VALUE;
        for (Individuo ind : frentePareto) {
            double fitness = ind.getFitness();
            if (fitness < df) {
                df = fitness;
            }
        }
        return df - EPSILON;
    }

    private double distancia(Individuo x, Individuo y) {
        long x1 = x.getFnValue(0);
        long x2 = x.getFnValue(1);
        long y1 = y.getFnValue(0);
        long y2 = y.getFnValue(1);
        return Math.sqrt(Math.pow(x1 - y1, 2) + Math.pow(x2 - y2, 2));

    }

    private void removerDePoblacion(List<Individuo> poblacion,
            List<Individuo> frentePareto) {
        for (Individuo ind : frentePareto) {
            poblacion.remove(ind);
        }
    }

    private Individuo seleccionRuleta(){
        //Calcular la suma de fitness
        double sum = 0;
        for(Individuo ind:poblacion){
            sum += ind.getFitness();
        }
        double r = Math.random()*sum;
        for(int i=0; i<poblacion.size(); i++)
        {
            r = r - poblacion.get(i).getFitness();
            if (r <= 0)
                return poblacion.get(i);
        }
        return null;

    }
    
        private Individuo crossover(Individuo padre1, Individuo padre2)
    {
        int tamSubcadena = (int)((padre1.getN() - 1) /2);
        int inicioSubcadena = (int)(Math.random()*(padre1.getN() - tamSubcadena));
        int[] cromosomaNuevo = new int[(int)padre1.getN()];
        for(int i = 0; i<cromosomaNuevo.length; i++){
            cromosomaNuevo[i] = -1;
        }
        for (int i = inicioSubcadena; i < (inicioSubcadena +tamSubcadena); i++)
        {
            cromosomaNuevo[i] = padre1.cromosoma(i);
        }
        int []cromosoma2 = padre2.getCromosoma().clone();
        for(int i = 0; i< padre2.getN(); i++)
        {
            for (int j=inicioSubcadena; j<(inicioSubcadena +tamSubcadena); j++)
            {
                if(cromosoma2[i] == cromosomaNuevo[j])
                {
                    cromosoma2[i] = -1;
                }
            }
        }
        int cont = 0;
        for(int i = 0; i< padre1.getN(); i++)
        {
            if (cromosomaNuevo[i] != -1)
            {
                //no hacer naa Dx
            }
            else{
                if(cromosoma2[cont] != -1){
                    cromosomaNuevo[i] = cromosoma2[cont];
                    cont++;
                }
                else
                {
                    cont++;
                    i--;
                }
            }

        }
        Individuo hijo = new Individuo(padre1.getN());
        hijo.setCromosoma(cromosomaNuevo);
        return hijo;
    }
    
        private void mutar(Individuo ind)
    {
        if (Math.random()> PORCENTAJE_MUTACION)
            return;
        
        int gen1 = (int)(Math.random()*ind.getN());
        int gen2;
        do{
            gen2 = (int)(Math.random()*ind.getN());
        }while(gen2==gen1);

        ind.swapG(gen1, gen2);
    }
        
        private boolean estaEnFOP(Individuo ind)
    {
        int[] crom1 = ind.getCromosoma();
        for(Individuo paretoInd:fParetoOptimo){
            int []crom2 = paretoInd.getCromosoma();
            boolean es = true;
            for(int i = 0; i<ind.getN(); i++)
            {
                if(crom1[i] != crom2[i])
                {
                    es = false;
                    break;
                }
            }
            if(es){
                return true;
            }
        }
        return false;
    }    
        
        private void actualizarFrentePareto(){


        if (fParetoOptimo == null)
        {
            fParetoOptimo = new ArrayList<Individuo>();
        }

        for(Individuo ind:poblacion){
            ind.setDominado(false);
        }


        //Agregar al frente pareto los individuos de la poblacion que no son dominados por el frente
        for(int i = 0; i< poblacion.size(); i++)
        {
            for(int j = 0; j<fParetoOptimo.size(); j++)
            {
                if(dominaA(fParetoOptimo.get(j), poblacion.get(i)))
                {
                    poblacion.get(i).setDominado(true);
                    break;
                }
            }
            if(!poblacion.get(i).isDominado()){
                if(!estaEnFOP(poblacion.get(i))){
                    fParetoOptimo.add(poblacion.get(i));
                }
            }
        }

        for(Individuo ind:fParetoOptimo){
            ind.setDominado(false);
        }

        // Eliminar soluciones dominada en el frenteParetoOptimo
        for(int i = 0; i< fParetoOptimo.size(); i++)
        {
             for(int j = 0; j< fParetoOptimo.size(); j++)
             {
                 if(dominaA(fParetoOptimo.get(j), fParetoOptimo.get(i)))
                 {
                     fParetoOptimo.get(i).setDominado(true);
                     break;
                 }
             }
        }
        List<Individuo> frentenew = new ArrayList<Individuo>();
        for(Individuo ind:fParetoOptimo){
            if(!ind.isDominado())
                frentenew.add(ind);
        }
        fParetoOptimo = frentenew;


    }    
    
        public List<Individuo> run(){
        //frenteParetoOptimo = null;
        generarPoblacionInicial();

        int gen = 0;
        while(gen < generaciones){
            asignarFnValues();
            asignarFitness();

            actualizarFrentePareto();

            List<Individuo> poblacionNueva = new ArrayList<Individuo>();
            while(poblacionNueva.size()< poblacion.size())
            {
                Individuo padre1 = seleccionRuleta();
                Individuo padre2 = seleccionRuleta();

                double chance = Math.random();
                if(chance <= PORCENTAJE_CRUZAMIENTO)
                {
                    Individuo hijo1 = crossover(padre1, padre2);
                    Individuo hijo2 = crossover(padre2, padre1);
                    poblacionNueva.add(hijo1);
                    mutar(hijo1);
                    poblacionNueva.add(hijo2);
                    mutar(hijo2);

                }
                else
                {
                    poblacionNueva.add(padre1);
                    mutar(padre1);
                    poblacionNueva.add(padre2);
                    mutar(padre2);
                }
            }

            poblacion = poblacionNueva;
            gen++;
        }
        return fParetoOptimo;
    }
        
    public int getGeneraciones() {
        return generaciones;
    }

    public void setGeneraciones(int generaciones) {
        this.generaciones = generaciones;
    }

    public int getTamPoblacion() {
        return tamPoblacion;
    }

    public void setTamPoblacion(int tamPoblacion) {
        this.tamPoblacion = tamPoblacion;
    }

    public Qap getQap() {
        return qap;
    }

    public void setQap(Qap qap) {
        this.qap = qap;
    }

    public List<Individuo> getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(List<Individuo> poblacion) {
        this.poblacion = poblacion;
    }

    public List<Individuo> getfParetoOptimo() {
        return fParetoOptimo;
    }

    public void setfParetoOptimo(List<Individuo> fParetoOptimo) {
        this.fParetoOptimo = fParetoOptimo;
    }

}
