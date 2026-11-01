/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoEDT;

/**
 *
 * @author daviel
 */
public class Nodo {
    private Estacion estacion;
    private Nodo next;
    
    public Nodo(Estacion estacion) {
        this.estacion = estacion;
        this.next = null;
    }

    /**
     * @return the next
     */
    public Nodo getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(Nodo next) {
        this.next = next;
    }

    /**
     * @return the estacion
     */
    public Estacion getEstacion() {
        return estacion;
    }
}
