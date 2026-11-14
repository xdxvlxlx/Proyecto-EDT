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
    private NodoAdyacente PrimerAdyacente;
    //Nuevos atributos para el funcionamiento de Dijkstra
    private int distancia = Integer.MAX_VALUE;
    private boolean visitado = false;
    private Estacion predecesor = null;
            
    public Estacion(String ID_Estacion, String Nombre, String Linea, String Zona, int Capacidad_Pasajeros_Hora) {
        this.ID_Estacion = ID_Estacion;
        this.Nombre = Nombre;
        this.Linea = Linea;
        this.Zona = Zona;
        this.Capacidad_Pasajeros_Hora = Capacidad_Pasajeros_Hora;
        this.PrimerAdyacente = null;
    }
    
    public String getNombreEstacion() {
        return this.Nombre;
    }
    
    public String getID_Estacion() {
        return this.ID_Estacion;
    }
    
    public String getLineaEstacion() {
        return this.Linea;
    }
    
    public String getZonaEstacion() {
        return this.Zona;
    }
    
    public int getCapacidad_Pasajeros_Hora() {
        return this.Capacidad_Pasajeros_Hora;
    }
    
    public NodoAdyacente getPrimerAdyacente() {
        return this.PrimerAdyacente;
    }
    
    public void setPrimerAdyacente(NodoAdyacente PrimerAdyacente) {
        this.PrimerAdyacente = PrimerAdyacente;
    }
    
    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public boolean isVisitado() {
        return visitado;
    }

    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }

    public Estacion getPredecesor() {
        return predecesor;
    }

    public void setPredecesor(Estacion predecesor) {
        this.predecesor = predecesor;
    }
}
