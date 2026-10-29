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
        hashTable = new String[20];
    }
    
    public Object[] getHashTable() {
        return hashTable;
    }
    
    public int getHashCode(String nombre) {
        char[] letras = nombre.toCharArray();
        int suma = 0;
        for (char letra : letras) {
            int ascii = letra;
            suma += ascii;
        }
        int hashcode = suma % hashTable.length;
        return hashcode;
    }
    
    public void insertar(Object objeto, int position) {
        if (position >= hashTable.length || position < 0) {
            System.out.println("el hashcode pasa de los limites del array");
            return;
        } else {
            objeto = hashTable[position];
            System.out.println("hashcode testestest");
            //hashTable[position] = objeto;
        }
    }
    
}
