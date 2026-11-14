/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoEDT;

/**
 *
 * @author daviel
 */
public class NodoAdyacente {
    private Estacion Estacion_Destino;
    private int Tiempo_Minutos;
    private NodoAdyacente SiguienteAdyacente;
    
    public NodoAdyacente(Estacion Estacion_Destino, int Tiempo_Minutos) {
        this.Estacion_Destino = Estacion_Destino;
        this.Tiempo_Minutos = Tiempo_Minutos;
        this.SiguienteAdyacente = null;
    }
    /**
     * 
     * @return la estacion destino
     */
    public Estacion getEstacionDestino() {
        return this.Estacion_Destino;
    }
    /**
     * 
     * @return el tiempo en minutos
     */
    public int getTiempo_Minutos() {
        return this.Tiempo_Minutos;
    }
    /**
     * 
     * @return el siguiente adyacente
     */
    public NodoAdyacente getSiguienteAdyacente() {
        return this.SiguienteAdyacente;
    }
    /**
     * 
     * @param SiguienteAdyacente set el siguienteAdyacente 
     */
    public void setSiguienteAdyacente(NodoAdyacente SiguienteAdyacente) {
        this.SiguienteAdyacente = SiguienteAdyacente;
    }
    
}
