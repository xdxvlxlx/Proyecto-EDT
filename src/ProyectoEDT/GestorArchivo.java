/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoEDT;
import javax.swing.JFileChooser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 *
 * @author daviel
 */
public class GestorArchivo {
    
    public File CargarArchivo() {
        File archivo = null;
        JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
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
    public void CargarEstacion(File archivo) { //Solo para cargar .csv de 5 filas, el mismo formato que contiene el dataset Estacion
        //archivo = j.getSelectedFile(); 
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
                int hashcode = GUI1.hashTable.getHashCode(palabra[0]); //palabra[0] calcula el hascode con el ID, palabra[1] calcula el hashcode con el nombre
                GUI1.hashTable.insertar(nuevaEstacion, hashcode);
            }
        } catch (Exception e) {
                System.out.println("A ocurrido un error: " + e.getMessage());
        }
    }
    
    public void CargarGrafo(File archivo) { //Solo para cargar .csv de 3 filas, el mismo formato que contiene el grafo.csv de ejemplo
        try {
            BufferedReader lector = new BufferedReader(new FileReader(archivo)); 
            String linea = lector.readLine();
            String encabezado[] = linea.split(",");
            System.out.println(encabezado[0]+"\t"+encabezado[1]+"\t"+encabezado[2]);
            while ((linea = lector.readLine()) != null) {
                String palabra[] = linea.split(",");
                System.out.println(palabra[0]+"\t"+palabra[1]+"\t"+palabra[2]);
                for (int i = 0; i < GUI1.hashTable.getHashTable().length; i++) {
                    Object ID = GUI1.hashTable.getValue(i);
                    if (ID instanceof Nodo) {
                        Nodo IDs = (Nodo) ID;
                        while (IDs != null) {
                            if (palabra[0].equals(IDs)) {
                                //hace algo
                                
                                break;
                            }
                            IDs = IDs.getNext();
                        }
                    } else if (ID instanceof Estacion) {
                        Estacion IDe = (Estacion) ID;
                        if (palabra[0].equals(IDe.getID_Estacion())) {
                            //IDe.getPrimerAdyacente() = NodoAdyacente();
                            //Primer ejemplo: estoy en PRO, le debo asignar adyacente la estacion PER, peso de 2 y el siguiente adyacente
                            //problema: buscar la estacion PER en la tabla hash, implica recorrer la tabla y comprobar si la clave contiene la estacion deseada
                            //
                            
                        }
                    }
                }
            }
                
                //Estacion nuevaEstacion = new Estacion(palabra[0], palabra[1], Integer.parseInt(palabra[2]));
                //int hashcode = GUI1.hashTable.getHashCode(palabra[1]);
                //GUI1.hashTable.insertar(nuevaEstacion, hashcode);
        } catch (Exception e) {
                System.out.println("A ocurrido un error: " + e.getMessage());
        }
    }
}
   
