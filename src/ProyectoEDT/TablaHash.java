/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoEDT;

/**
 *
 * @author ile1
 */
public class TablaHash {
    private Object[] hashTable;

    
    public TablaHash() {
        this.hashTable = new Object[20];
    }
    
    public Object[] getHashTable() {
        return this.hashTable;
    }
    
    public boolean esVacia() {
        boolean vacia = true;
        for (int i = 0; i < this.hashTable.length; i++) {
            if (this.hashTable[i] != null) {
                vacia = false;
                break;
            }
        }
        return vacia;
    }
    
    public int getHashCode(String nombre) {
        char[] letras = nombre.toCharArray();
        int suma = 0;
        int i = 0;
        for (char letra : letras) {
            i++;
            int ascii = letra;
            ascii *= i;
            suma += ascii;
        }
        int hashcode = suma % this.hashTable.length;
        return hashcode;
    }
    
    public void insertar(Estacion estacion, int position) {
        if (position >= this.hashTable.length || position < 0) {
            System.out.println("el hashcode pasa de los limites del array");
        } else {
            if (this.hashTable[position] == null) {
                this.hashTable[position] = estacion;
                System.out.println("estacion "+estacion.getNombreEstacion()+" agregado en la posicion "+position+" de la hash table");
            } else {
                encadenarNodo(estacion, position);
                System.out.println("Se encadeno la estacion "+estacion.getNombreEstacion()+" junto a la posicion "+position+" de la hash table");
            }
        }
    }
    
    public void encadenarNodo(Estacion nuevaEstacion, int position) {
        Object miObjeto = this.hashTable[position];
        if (miObjeto instanceof Nodo) {
            Nodo nodoActual = (Nodo) miObjeto;
            Nodo nodoAux = nodoActual;
            while (nodoAux.getNext() != null) {
                nodoAux = nodoAux.getNext();
            }
            Nodo newNodo = new Nodo(nuevaEstacion);
            nodoAux.setNext(newNodo);
        } else if (miObjeto instanceof Estacion) {
            Estacion estacionActual = (Estacion) miObjeto;
            Nodo newNodo1 = new Nodo(estacionActual);
            this.hashTable[position] = newNodo1;
            Nodo newNodo2 = new Nodo(nuevaEstacion);
            newNodo1.setNext(newNodo2);
        }
        //Estacion estacionActual = this.hashTable[position];
        //Nodo newNodo = new Nodo(this.hashTable[position]);
        
    }
    
    
}
