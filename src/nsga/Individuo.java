/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nsga;

import qap.Qap;

public class Individuo {
    private int N;
    private int[] cromosoma;
    private double fitness;
    private long fnValue[];
    private boolean dominado = false;

    public Individuo(int N)
    {
        this.N = N;
        cromosoma = new int[N];
        fnValue = new long[2];
        generarIndividuoRandom();
    }

    private void generarIndividuoRandom()
    {
        for(int i = 0; i < N; i++)
        {
            cromosoma[i] = i;
        }
        for(int i = 0; i < N; i++)
        {
            int j =  ((int)(Math.random()*100.0)%N);
            swapG(i,j);

        }
    }
    
    public void asignarFnValue(Qap qap){
        long sum1 = 0, sum2 = 0;
        int c[] = cromosoma;
        for(int i=0; i<N; i++)
        {
            for(int j=0; j<N; j++)
            {
                sum1 = sum1 + qap.getDistancia()[i][j]*qap.getFlujo(0)[c[i]][c[j]];
                sum2 = sum2 + qap.getDistancia()[i][j]*qap.getFlujo(1)[c[i]][c[j]];
            }
        }
        fnValue[0]=sum1;
        fnValue[1]=sum2;
    }
    
    public void swapG(int i, int j){
        int aux = cromosoma[i];
        cromosoma[i] = cromosoma[j];
        cromosoma[j] = aux;
    }

    /**
     * @return the N
     */
    public int getN() {
        return N;
    }

    /**
     * @param N the N to set
     */
    public void setN(int N) {
        this.N = N;
    }

    /**
     * @return the cromosoma
     */
    public int[] getCromosoma() {
        return cromosoma;
    }

    public int cromosoma(int i){
        return cromosoma[i];
    }
    /**
     * @param cromosoma the cromosoma to set
     */
    public void setCromosoma(int[] cromosoma) {
        this.cromosoma = cromosoma;
    }

    public void setCromosoma(int i, int xi)
    {
        cromosoma[i] = xi;
    }

    /**
     * @return the fitness
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * @param fitness the fitness to set
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public long getFnValue(int i) {
        return fnValue[i];
    }

    public void setFnValue(int i,long fnValue) {
        this.fnValue[i] = fnValue;
    }

    

    /**
     * @return the dominado
     */
    public boolean isDominado() {
        return dominado;
    }

    /**
     * @param dominado the dominado to set
     */
    public void setDominado(boolean dominado) {
        this.dominado = dominado;
    }

    public String toString(){
        /*String ret = "Cromosoma = ";
        for(int i:cromosoma){
            ret += i + " ";
        }*/
        String ret = ""+fnValue[0] +";"+fnValue[1];
        
        ret+="\n";
        return ret;
    }
}
