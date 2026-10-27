/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoEDT;

/**
 *
 * @author daviel
 */
public class Estacion {
    private String ID_Estacion;
    private String Nombre;
    private String Linea;
    private String Zona;
    private int Capacidad_Pasajeros_Hora;
    private Estacion NextVertice;
    private NodoAdyacente PrimerAdyacente;
            
    public Estacion(String ID_Estacion, String Nombre, String Linea, String Zona, int Capacidad_Pasajeros_Hora) {
        this.ID_Estacion = ID_Estacion;
        this.Nombre = Nombre;
        this.Linea = Linea;
        this.Zona = Zona;
        this.Capacidad_Pasajeros_Hora = Capacidad_Pasajeros_Hora;
        this.NextVertice = null;
        this.PrimerAdyacente = null;
    }
    
    
}
