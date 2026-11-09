/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoEDT;
import javax.swing.JFileChooser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
/**
 *
 * @author daviel
 */
public class GestorArchivo {
    
    public File CargarArchivo() {
        File archivo = null;
        JFileChooser j = new JFileChooser();
        j.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Solo archivos .csv", "csv");
        j.addChoosableFileFilter(restrict);
        int r = j.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            archivo = j.getSelectedFile(); 
        } else if (r == JFileChooser.CANCEL_OPTION) {
        }
        return archivo;
    }
    public String LeerEstacion(File archivo, TablaHash hashTable) { //Solo para leer .csv de 5 filas, el mismo formato que contiene el dataset Estacion
        //archivo = j.getSelectedFile(); 
        String message = "";
        try {
            BufferedReader lector = new BufferedReader(new FileReader(archivo)); 
            String linea = lector.readLine();
            String encabezado[] = linea.split(",");
            System.out.println(encabezado[0]+"\t"+encabezado[1]+"\t"+encabezado[2]+"\t"+encabezado[3]+"\t"+encabezado[4]);
            while ((linea = lector.readLine()) != null) {
                String palabra[] = linea.split(",");
                System.out.println(palabra[0]+"\t"+palabra[1]+"\t"+palabra[2]+"\t"+palabra[3]+"\t"+palabra[4]);
                //Crea nueva instancia de la clase estacion
                Estacion nuevaEstacion = new Estacion(palabra[0], palabra[1], palabra[2], palabra[3], Integer.parseInt(palabra[4]));
                int hashcode = hashTable.getHashCode(palabra[0]); //palabra[0] calcula el hascode con el ID, palabra[1] calcula el hashcode con el nombre
                hashTable.insertar(nuevaEstacion, hashcode);
            }
        } catch (Exception e) {
                System.out.println("A ocurrido un error: " + e.getMessage());
                message = "A ocurrido un error: " + e.getMessage();
        } if (message.equals("")) {
            message = "Se ha cargado el dataset de Estaciones";
        } 
        return message;
    }
    public String LeerGrafo(File archivo, TablaHash hashTable, Graph grafoVisual) { //Solo para leer .csv de 3 filas, el mismo formato que contiene el grafo.csv de ejemplo        
        Grafo grafo = new Grafo();
        String message = "";
        try {
            BufferedReader lector = new BufferedReader(new FileReader(archivo)); 
            String linea = lector.readLine();
            String encabezado[] = linea.split(",");
            System.out.println(encabezado[0]+"\t"+encabezado[1]+"\t"+encabezado[2]);
            while ((linea = lector.readLine()) != null) {
                if (hashTable.esVacia()) {
                    message = "no se puede cargar grafo porque el diccionario de estaciones no se ha cargado";
                    break;
                }
                String palabra[] = linea.split(",");
                System.out.println(palabra[0]+"\t"+palabra[1]+"\t"+palabra[2]);
                grafo.registrarNodosEnGrafo(grafoVisual, palabra[0], palabra[1], Integer.parseInt(palabra[2])); //metodo para construir el grafo Visual
                int key_origen = hashTable.getHashCode(palabra[0]);
                int key_destino = hashTable.getHashCode(palabra[1]);
                Object estacion_origen = hashTable.getValue(key_origen);
                Object estacion_destino = hashTable.getValue(key_destino);
                if (estacion_origen instanceof Nodo) {
                    estacion_origen = hashTable.buscarEstacion((Nodo) estacion_origen, palabra[0]);
                } if (estacion_destino instanceof Nodo) {
                    estacion_destino = hashTable.buscarEstacion((Nodo) estacion_destino, palabra[1]);
                } if (estacion_origen instanceof Estacion) {
                    NodoAdyacente adyacente = new NodoAdyacente((Estacion) estacion_destino, Integer.parseInt(palabra[2]));
                    System.out.println("Estacion: "+((Estacion) estacion_origen).getNombreEstacion()+" tiene adyacente a "+((Estacion)estacion_destino).getNombreEstacion()+" con un peso de "+palabra[2]);
                    if (((Estacion) estacion_destino).getPrimerAdyacente() != null) {
                        NodoAdyacente SiguienteAdyacente = new NodoAdyacente((Estacion) estacion_origen, Integer.parseInt(palabra[2]));
                        ((Estacion) estacion_destino).getPrimerAdyacente().setSiguienteAdyacente(SiguienteAdyacente);
                        System.out.println("La estacion destino "+((Estacion) estacion_destino).getNombreEstacion()+" contiene un primer adyacente que es "+((Estacion)estacion_destino).getPrimerAdyacente().getEstacionDestino().getNombreEstacion()+ ". por lo que se asigna al siguiente despues del primero");    
                        ((Estacion) estacion_origen).setPrimerAdyacente(adyacente);
                    } else {
                        ((Estacion) estacion_origen).setPrimerAdyacente(adyacente);
                    }
                }
            }        
        } catch (Exception e) {
                System.out.println("A ocurrido un error: " + e.getMessage());
                message = "A ocurrido un error: " + e.getMessage();
        } if (message.equals("")) {
            message = "Se ha cargado el archivo grafo";
        } 
        return message;
    }
}
   
