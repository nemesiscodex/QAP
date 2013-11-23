/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qap;

/**
 *
 * @author Julio
 */
public class Qap {
    private Integer N;
    private Integer distancia[][];
    private Integer flujo[][][];

    public Integer getN() {
        return N;
    }

    public void setN(Integer N) {
        this.N = N;
    }

    public Integer[][] getDistancia() {
        return distancia;
    }

    public void setDistancia(Integer[][] distancia) {
        this.distancia = distancia;
    }

    public Integer[][][] getFlujo() {
        return flujo;
    }
    public Integer[][] getFlujo(Integer i){
        return flujo[i];
    }

    public void setFlujo(Integer[][][] flujo) {
        this.flujo = flujo;
    }
    
    
}
