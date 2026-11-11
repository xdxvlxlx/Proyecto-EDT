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
import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.io.PrintWriter;
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
            if (!hashTable.esVacia()) {
                hashTable.vaciarTablaHash();
            }
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
                System.out.println("A ocurrido un error: " + e.getClass().getName()+"\nDetalle: "+e.getMessage());
                message = "A ocurrido un error: " + e.getClass().getName()+"\nDetalle: "+e.getMessage();
        } if (message.equals("")) {
            message = "Se ha cargado correctamente el archivo de Estaciones";
        } 
        return message;
    }
    public String LeerGrafo(File archivo, TablaHash hashTable, Graph grafoVisual) { //Solo para leer .csv de 3 filas, el mismo formato que contiene el grafo.csv de ejemplo        
        Grafo grafo = new Grafo();
        if (grafoVisual.getNodeCount() > 0) {
            grafoVisual.clear();
        }
        String message = "";
        try {
            BufferedReader lector = new BufferedReader(new FileReader(archivo)); 
            String linea = lector.readLine();
            String encabezado[] = linea.split(",");
            System.out.println(encabezado[0]+"\t"+encabezado[1]+"\t"+encabezado[2]);
            while ((linea = lector.readLine()) != null) {
                if (hashTable.esVacia()) {
                    message = "No se puede cargar el grafo porque el archivo de estaciones no se ha cargado";
                    break;
                }
                String palabra[] = linea.split(",");
                System.out.println(palabra[0]+"\t"+palabra[1]+"\t"+palabra[2]);
                int key_origen = hashTable.getHashCode(palabra[0]);
                int key_destino = hashTable.getHashCode(palabra[1]);
                Object estacion_origen = hashTable.getValue(key_origen);
                Object estacion_destino = hashTable.getValue(key_destino); //error, cuando no existe la estacion pero coincide el indice, toma como destino el objeto encontrado en ese indice
                if (estacion_origen instanceof Nodo) {
                    estacion_origen = hashTable.buscarEstacionEncadenada((Nodo) estacion_origen, palabra[0]);
                } if (estacion_destino instanceof Nodo) {
                    estacion_destino = hashTable.buscarEstacionEncadenada((Nodo) estacion_destino, palabra[1]);
                } if (estacion_origen instanceof Estacion && estacion_destino != null) {
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
                } if(estacion_destino == null) {
                    System.out.println("La estacion "+((Estacion)estacion_origen).getNombreEstacion()+" no contiene un destino, por lo que se omite");
                } else if(estacion_origen == null) {
                    //deberia crear el nodo pero sin conectarlo
                    //crear una funcion en la clase grafo para añadir nodos sin tener que conectarlos
                } else {
                grafo.registrarNodosEnGrafo(grafoVisual, ((Estacion)estacion_origen).getID_Estacion(), ((Estacion)estacion_destino).getID_Estacion(), Integer.parseInt(palabra[2])); //metodo para construir el grafo Visual
                }
            }        
        } catch (Exception e) {
                System.out.println("A ocurrido un error: " + e.getClass().getName()+"\nDetalle: "+e.getMessage());
                message = "A ocurrido un error: " + e.getClass().getName()+"\nDetalle: "+e.getMessage();
        } if (message.equals("")) {
            message = "Se ha cargado correctamente el archivo grafo";
        } 
        return message;
    }
    
    
    public String GuardarDatos(TablaHash hashTable, File nuevoArchivo) {
        String texto = "";
        JFileChooser fileChooser = new JFileChooser();
        //fileChooser.setDialogTitle("Guardar");
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Solo archivos .csv", "csv");
        fileChooser.addChoosableFileFilter(restrict);
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            nuevoArchivo = fileChooser.getSelectedFile();

            // Asegurar que tenga la extensión .csv
            if (!nuevoArchivo.getName().toLowerCase().endsWith(".csv")) {
                nuevoArchivo = new File(nuevoArchivo.getAbsolutePath() + ".csv");
            }

            // VALIDACIÓN: ¿El archivo ya existe? Preguntar si desea sobrescribirlo
            if (nuevoArchivo.exists()) {
                int respuesta = JOptionPane.showConfirmDialog(
                    null,
                    "El archivo ya existe. ¿Deseas sobrescribirlo?",
                    "Confirmar sobrescritura",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (respuesta != JOptionPane.YES_OPTION) {
                    texto = "Se cancelo el guardado del archivo";
                    return texto; // Si el usuario dice que NO, cancelamos el guardado
                }
            }
        try (PrintWriter pw = new PrintWriter(new FileWriter(nuevoArchivo))) {
            pw.println("Estacion_Origen,Estacion_Destino,Tiempo_Minutos");
            for (int i = 0; i < hashTable.getHashTable().length; i++) {
                Object estacion = hashTable.getValue(i);
                if (estacion instanceof Nodo) {
                    Nodo nodoActual = (Nodo)estacion;
                    while (nodoActual != null) {
                        if (nodoActual.getEstacion().getPrimerAdyacente() == null) {
                            System.out.println("en la estacion actual: "+nodoActual.getEstacion().getID_Estacion()+ " no contiene un siguiente, por lo que se omite y no se guarda");
                            nodoActual = nodoActual.getNext();
                        } else {
                        System.out.println(nodoActual.getEstacion().getID_Estacion() + "," + nodoActual.getEstacion().getPrimerAdyacente().getEstacionDestino().getID_Estacion() + "," + nodoActual.getEstacion().getPrimerAdyacente().getTiempo_Minutos());
                        pw.println(nodoActual.getEstacion().getID_Estacion() + "," + nodoActual.getEstacion().getPrimerAdyacente().getEstacionDestino().getID_Estacion() + "," + nodoActual.getEstacion().getPrimerAdyacente().getTiempo_Minutos());
                        nodoActual = nodoActual.getNext();
                        }
                    }
                } else if (estacion instanceof Estacion) {
                    Estacion estacionActual = (Estacion) estacion;
                    pw.println(estacionActual.getID_Estacion() + "," + estacionActual.getPrimerAdyacente().getEstacionDestino().getID_Estacion() + "," + estacionActual.getPrimerAdyacente().getTiempo_Minutos());
                }
            }
        } catch (Exception e) {
            texto = "A ocurrido un error: " + e.getClass().getName()+"\nDetalle: "+e.getMessage();
        }
        texto = "Se ha guardado el archivo "+nuevoArchivo.getName()+" correctamente\nruta de archivo: "+nuevoArchivo.getPath();
        System.out.println("Se ha guardado el archivo "+nuevoArchivo.getName()+" correctamente\nruta de archivo: "+nuevoArchivo.getPath());
        
        } else if(nuevoArchivo == null) {
            texto = "Se cancelo el guardado del archivo";
        }
        return texto;
    }
    
    public File HayDatosModificados(boolean cambios, TablaHash hashTable, File nuevoArchivo) {
        File file = null;
        if (cambios) {
            int opcion = JOptionPane.showConfirmDialog(
            null, 
            "Ya existen datos en memoria. ¿Desea guardarlos antes de cargar un nuevo archivo?", 
            "Advertencia - Datos sin guardar", 
            JOptionPane.YES_NO_CANCEL_OPTION, 
            JOptionPane.WARNING_MESSAGE
        );

        // 3. Evaluar la respuesta del usuario
        if (opcion == JOptionPane.YES_OPTION) {
            // El usuario quiere guardar: Llama a tu función de guardado
            GuardarDatos(hashTable, nuevoArchivo); 
            // Después de guardar, puedes proceder a abrir el explorador de archivos
            file = CargarArchivo();
            
        } else if (opcion == JOptionPane.NO_OPTION) {
            // El usuario NO quiere guardar, pero SÍ quiere cargar el nuevo archivo de una vez
            file =  CargarArchivo();
            
        } else {
            // Si elige CANCELAR (CANCEL_OPTION) o cierra la ventana, no haces nada
            return file; 
        }
        }
        //file =  CargarArchivo();
        return file;
    }
    
    public String GuardadoAutomatico(TablaHash hashTable, File archivoMaestro) {
        String texto = "";
        // Si es la primera vez y no hay archivo previo, abrimos el selector
        if (archivoMaestro == null) {
            GuardarDatos(hashTable, archivoMaestro);
            texto = "test";
            return texto;
        }

        // Si ya tenemos un archivo de referencia, sobrescribimos de forma silenciosa
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoMaestro))) {
            pw.println("ID_Estacion,Nombre,Linea,Zona,Capacidad_Pasajeros_Hora");
            for (int i = 0; i < hashTable.getHashTable().length; i++) {
                Object actual = hashTable.getValue(i);
                if (actual instanceof Nodo) {
                    while ((Nodo)actual != null) {
                        Estacion estacionActual = ((Nodo) actual).getEstacion();
                        pw.println(estacionActual.getID_Estacion() + "," + estacionActual.getNombreEstacion() + "," + estacionActual.getLineaEstacion() + "," + estacionActual.getZonaEstacion() + "," + estacionActual.getCapacidad_Pasajeros_Hora());
                        actual = ((Nodo)actual).getNext();
                    }
                } else if (actual instanceof Estacion) {
                    Estacion estacionActual = (Estacion) actual;
                    pw.println(estacionActual.getID_Estacion() + "," + estacionActual.getNombreEstacion() + "," + estacionActual.getLineaEstacion() + "," + estacionActual.getZonaEstacion() + "," + estacionActual.getCapacidad_Pasajeros_Hora());
                }
            }
            System.out.println("Cambios guardados automáticamente en: " + archivoMaestro.getName());
            texto = "Cambios guardados automáticamente";
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar automáticamente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            texto = "Error: "+e.getMessage();
            
        }
        return texto;
    }
    
}
   
