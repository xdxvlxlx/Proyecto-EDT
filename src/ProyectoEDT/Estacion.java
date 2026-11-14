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
    /**
     * 
     * @return el nombre de la estacion
     */
    public String getNombreEstacion() {
        return this.Nombre;
    }
    /**
     * 
     * @return  el id de la estacion
     */
    public String getID_Estacion() {
        return this.ID_Estacion;
    }
    /**
     * 
     * @return la linea de la estacion
     */
    public String getLineaEstacion() {
        return this.Linea;
    }
    /**
     * 
     * @return la zona de la estacion
     */
    public String getZonaEstacion() {
        return this.Zona;
    }
    /**
     * 
     * @return la capacidad de pasajeros por hora de la estacion
     */
    public int getCapacidad_Pasajeros_Hora() {
        return this.Capacidad_Pasajeros_Hora;
    }
    
    /**
     * 
     * @return el primer adyacente de la estacion
     */
    public NodoAdyacente getPrimerAdyacente() {
        return this.PrimerAdyacente;
    }
    /**
     * 
     * @param PrimerAdyacente al primerAdyacente de la estacion
     */
    public void setPrimerAdyacente(NodoAdyacente PrimerAdyacente) {
        this.PrimerAdyacente = PrimerAdyacente;
    }
    
    //Setters y Getters usados para el algoritmo de Dijkstra
    
    /**
     * 
     * @return la distancia de la estacion
     */
    public int getDistancia() {
        return distancia;
    }
    /**
     * 
     * @param distancia  a la distancia de la estacion
     */
    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }
    /**
     * 
     * @return si la estacion es visitada o no
     */
    public boolean isVisitado() {
        return visitado;
    }
    /**
     * 
     * @param visitado al atributo visitado de la estacion
     */
    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }
    /**
     * 
     * @return el predecesor de la estacion
     */
    public Estacion getPredecesor() {
        return predecesor;
    }
    /**
     * 
     * @param predecesor al atributo predecesor de la estacion 
     */
    public void setPredecesor(Estacion predecesor) {
        this.predecesor = predecesor;
    }
}
