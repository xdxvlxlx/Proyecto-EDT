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
}
