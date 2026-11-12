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
import java.io.RandomAccessFile;
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
                // if (!((Estacion)estacion_origen).getID_Estacion().equals(palabra[0])) {
                //     System.out.println("---ERROR: "+((Estacion)estacion_origen).getNombreEstacion()+" no coincide con el valor "+palabra[0]);
                } if (estacion_origen == null && estacion_destino == null) {
                    System.out.println("---ERROR: ambas estaciones son null");
                } else if(estacion_origen == null) {
                    System.out.println("---ERROR: la estacion origen es null");
                    grafo.registrarUnicoNodo(grafoVisual, ((Estacion)estacion_destino).getID_Estacion());
                    //deberia crear el nodo pero sin conectarlo
                    //crear una funcion en la clase grafo para añadir nodos sin tener que conectarlos
                } else if (estacion_destino == null) { //destino es null, entonces origen es un nodo que no conecta a nada
                    System.out.println("---ERROR: la estacion destino es null");
                    grafo.registrarUnicoNodo(grafoVisual, ((Estacion)estacion_origen).getID_Estacion());
                } else if (estacion_origen instanceof Estacion) {
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
    
    
    public String GuardarDatos(TablaHash hashTable, File archivoGrafo) {
        String texto = "";
        JFileChooser fileChooser = new JFileChooser();
        //fileChooser.setDialogTitle("Guardar");
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Solo archivos .csv", "csv");
        fileChooser.addChoosableFileFilter(restrict);
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            archivoGrafo = fileChooser.getSelectedFile();

            // Asegurar que tenga la extensión .csv
            if (!archivoGrafo.getName().toLowerCase().endsWith(".csv")) {
                archivoGrafo = new File(archivoGrafo.getAbsolutePath() + ".csv");
            }

            // VALIDACIÓN: ¿El archivo ya existe? Preguntar si desea sobrescribirlo
            if (archivoGrafo.exists()) {
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
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoGrafo))) {
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
        texto = "Se ha guardado el archivo "+archivoGrafo.getName()+" correctamente\nruta de archivo: "+archivoGrafo.getPath();
        System.out.println("Se ha guardado el archivo "+archivoGrafo.getName()+" correctamente\nruta de archivo: "+archivoGrafo.getPath());
        
        } else if(archivoGrafo == null) {
            texto = "Se cancelo el guardado del archivo";
        }
        return texto;
    }
    
    public File HayDatosModificados(boolean cambios, TablaHash hashTable, File archivoGrafo) { //Deprecated
        File archivoResultado = archivoGrafo; // Por defecto devolvemos el archivo actual

        if (cambios) {
            int opcion = JOptionPane.showConfirmDialog(
                null, 
                "Existen datos modificados. ¿Desea guardarlos antes de cargar un nuevo archivo?", 
                "Advertencia - Datos sin guardar", 
                JOptionPane.YES_NO_CANCEL_OPTION, 
                JOptionPane.WARNING_MESSAGE
            );

            if (opcion == JOptionPane.YES_OPTION) {
                // 1. Guarda los cambios en el mismo archivo que le pasaste por parámetro
                String resultadoGuardado = GuardarDatos(hashTable, archivoGrafo); 

                // Si el usuario canceló en el diálogo de guardado (por si usa un Guardar Como)
                if (resultadoGuardado != null && resultadoGuardado.equals("Se cancelo el guardado del archivo")) {
                    return archivoGrafo; // Retorna el archivo intacto y frena la carga
                }

                // 2. Una vez guardado, abre la ventana para cargar el nuevo archivo
                archivoResultado = CargarArchivo();

            } else if (opcion == JOptionPane.NO_OPTION) {
                // No guarda nada, pero procede directamente a cargar el nuevo archivo
                archivoResultado = CargarArchivo();

            } else {
                // Si elige CANCELAR (CANCEL_OPTION) o cierra la ventana (X)
                // Se cancela todo y se devuelve el archivo intacto sin modificar
                return archivoGrafo; 
            }
        } else {
            // Si NO hay cambios, salta el aviso y abre directamente la ventana para cargar el nuevo archivo
            archivoResultado = CargarArchivo();
            if (archivoResultado == null) { // si cancela, retorna el grafo original
                return archivoGrafo;
            }
        }

        return archivoResultado;
}
    
    public String GuardadoAutomatico(TablaHash hashTable, File archivoMaestro, String ID_Estacion) {
        String texto = "";
        // Si es la primera vez y no hay archivo previo, abrimos el selector
        if (archivoMaestro == null) {
            GuardarDatos(hashTable, archivoMaestro);
            texto = "test";
            return texto;
        }

        // Si ya tenemos un archivo de referencia, sobrescribimos de forma silenciosa
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoMaestro))) {
            hashTable.eliminarEstacionDeTablaHash(ID_Estacion);
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
    
    public String guardarEstacionLineaFinal(File archivoMaestro, String[] datosEstacion) {
    // El 'true' en el FileWriter le indica a Java que no sobrescriba, sino que escriba al final
        String texto = "";
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoMaestro, true))) {
            RandomAccessFile raf = new RandomAccessFile(archivoMaestro, "r");
            raf.seek(archivoMaestro.length() - 1);
            int ultimoByte = raf.read();
            // Si el último carácter NO es un salto de línea (\n o \r), lo agregamos nosotros
            if (ultimoByte != '\n' && ultimoByte != '\r') {
                pw.println(); 
            }
            // Escribe la nueva línea respetando el formato CSV
            pw.println(datosEstacion[0] + "," + datosEstacion[1] + "," + datosEstacion[2] + "," + datosEstacion[3] + "," + Integer.parseInt(datosEstacion[4]));
            System.out.println("Línea agregada exitosamente al final del archivo.");
            texto = "Cambios realizados a "+archivoMaestro.getName();
        } catch (Exception e) {
            texto = "Error: "+e.getMessage();
        }
        return texto;
    }
    
    public String guardarConexionLineaFinal(File archivoGrafo, String[] datosConexion, TablaHash hashTable, boolean datosModificados) {
    // El 'true' en el FileWriter le indica a Java que no sobrescriba, sino que escriba al final
        String texto = "";
        if (archivoGrafo == null) {
            int opcion = JOptionPane.showConfirmDialog(
            null, 
            "No se ha creado un archivo de guardado. ¿Desea crearlo?", 
            "Advertencia - Archivo no creado", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
        );
            if (opcion == JOptionPane.YES_OPTION) {
            // El usuario quiere guardar: Llama a tu función de guardado
            GuardarDatos(hashTable, archivoGrafo); 
            if (texto.equals("Se cancelo el guardado del archivo")) {
                datosModificados = true;
                return texto;
            } else {
                datosModificados = true;
            }
        }
        } else {
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivoGrafo, true))) {
                RandomAccessFile raf = new RandomAccessFile(archivoGrafo, "r");
                raf.seek(archivoGrafo.length() - 1);
                int ultimoByte = raf.read();
                // Si el último carácter NO es un salto de línea (\n o \r), lo agregamos nosotros
                if (ultimoByte != '\n' && ultimoByte != '\r') {
                    pw.println(); 
                }
                // Escribe la nueva línea respetando el formato CSV
                pw.println(datosConexion[0].trim() + "," + datosConexion[1].trim() + "," + datosConexion[2].trim());
                System.out.println("Línea agregada exitosamente al final del archivo.");
                texto = "Cambios guardados en "+archivoGrafo.getName();
                
            } catch (Exception e) {
                texto = "Error: "+e.getMessage();
            }
        }
        return texto;
    }
}
   
