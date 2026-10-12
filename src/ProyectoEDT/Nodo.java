/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoEDT;

/**
 *
 * @author daviel
 */
public class Nodo <T> {
    private T info; 
    private Nodo<T> Next;

    public Nodo(T info) {
        this.info = info;
        this.Next = null;
    }

    /**
     * @return the info
     */
    public T getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(T info) {
        this.info = info;
    }

    /**
     * @return the Next
     */
    public Nodo<T> getNext() {
        return Next;
    }

    /**
     * @param Next the Next to set
     */
    public void setNext(Nodo<T> Next) {
        this.Next = Next;
    }

}


