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
    
    public void GestorArchivo() {
        JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        j.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Solo archivos .csv", "csv");
        j.addChoosableFileFilter(restrict);
        int r = j.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            File archivo = j.getSelectedFile(); 
            try {
                BufferedReader lector = new BufferedReader(new FileReader(archivo)); 
                String linea = lector.readLine();
                System.out.println(linea);
                while ((linea = lector.readLine()) != null) {
                    String palabra[] = linea.split(",");
                    System.out.println(palabra[0]+"\t"+palabra[1]+"\t"+palabra[2]+"\t"+palabra[3]+"\t"+palabra[4]);
                    //Crea nueva instancia de la clase estacion
                    Estacion nuevaEstacion = new Estacion(palabra[0], palabra[1], palabra[2], palabra[3], Integer.parseInt(palabra[4]));
                    int hashcode = GUI1.hashTable.getHashCode(palabra[1]);
                    GUI1.hashTable.insertar(nuevaEstacion, hashcode);
                    
                    
                }
            } catch (Exception e) {
                    System.out.println("A ocurrido un error: " + e.getMessage());
            }
        }
    }    
}

