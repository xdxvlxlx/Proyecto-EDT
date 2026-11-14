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
                    if (((Estacion) estacion_destino).getPrimerAdyacente() != null || ((Estacion) estacion_origen).getPrimerAdyacente() != null) {
                        NodoAdyacente SiguienteAdyacente = new NodoAdyacente((Estacion) estacion_origen, Integer.parseInt(palabra[2]));
                        if (((Estacion) estacion_destino).getPrimerAdyacente() != null && ((Estacion) estacion_origen).getPrimerAdyacente() != null) { //caso cuando ambas estaciones tienen nodos adyacentes
                            NodoAdyacente AdyacenteAux = ((Estacion) estacion_origen).getPrimerAdyacente();
                            while (AdyacenteAux != null) {
                            if (AdyacenteAux.getSiguienteAdyacente() == null) {
                                AdyacenteAux.setSiguienteAdyacente(adyacente);
                                //((Estacion)estacion_destino).setPrimerAdyacente(SiguienteAdyacente);
                                break;
                            }
                            AdyacenteAux = AdyacenteAux.getSiguienteAdyacente();
                        }
                        }
                        else if (((Estacion) estacion_destino).getPrimerAdyacente() == null) {
                        NodoAdyacente AdyacenteAux = ((Estacion) estacion_origen).getPrimerAdyacente();
                        while (AdyacenteAux != null) {
                            if (AdyacenteAux.getSiguienteAdyacente() == null) {
                                AdyacenteAux.setSiguienteAdyacente(adyacente);
                                ((Estacion)estacion_destino).setPrimerAdyacente(SiguienteAdyacente);
                                break;
                            }
                            AdyacenteAux = AdyacenteAux.getSiguienteAdyacente();
                        }
                    }
                        else if (((Estacion) estacion_origen).getPrimerAdyacente() == null) {
                        ((Estacion) estacion_destino).getPrimerAdyacente().setSiguienteAdyacente(SiguienteAdyacente);
                        System.out.println("La estacion destino "+((Estacion) estacion_destino).getNombreEstacion()+" contiene un primer adyacente que es "+((Estacion)estacion_destino).getPrimerAdyacente().getEstacionDestino().getNombreEstacion()+ ". por lo que se asigna al siguiente despues del primero");    
                        ((Estacion) estacion_origen).setPrimerAdyacente(adyacente);
                        }
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
                        boolean entraenbucle = false;
                        if (nodoActual.getEstacion().getPrimerAdyacente() == null) {
                            System.out.println("en la estacion actual: "+nodoActual.getEstacion().getID_Estacion()+ " no contiene un siguiente, por lo que se omite y no se guarda");
                            nodoActual = nodoActual.getNext();
                        } else { 
                            NodoAdyacente AdyacenteAux = nodoActual.getEstacion().getPrimerAdyacente();
                            while (AdyacenteAux.getSiguienteAdyacente() != null) {
                                pw.println(nodoActual.getEstacion().getID_Estacion() + "," + AdyacenteAux.getEstacionDestino().getID_Estacion() + "," + AdyacenteAux.getTiempo_Minutos());
                                System.out.println(nodoActual.getEstacion().getID_Estacion() + "," + AdyacenteAux.getEstacionDestino().getID_Estacion() + "," + AdyacenteAux.getTiempo_Minutos());
                                entraenbucle = true;
                                AdyacenteAux = AdyacenteAux.getSiguienteAdyacente();
                            }
                        System.out.println(nodoActual.getEstacion().getID_Estacion() + "," + nodoActual.getEstacion().getPrimerAdyacente().getEstacionDestino().getID_Estacion() + "," + nodoActual.getEstacion().getPrimerAdyacente().getTiempo_Minutos());
                        if (!entraenbucle) {
                            pw.println(nodoActual.getEstacion().getID_Estacion() + "," + nodoActual.getEstacion().getPrimerAdyacente().getEstacionDestino().getID_Estacion() + "," + nodoActual.getEstacion().getPrimerAdyacente().getTiempo_Minutos());
                        }
                        nodoActual = nodoActual.getNext();
                        }
                    }
                } else if (estacion instanceof Estacion) {
                    boolean entraenbucle = false;
                    Estacion estacionActual = (Estacion) estacion;
                    if (estacionActual.getPrimerAdyacente() == null) {
                            System.out.println("en la estacion actual: "+estacionActual.getID_Estacion()+ " no contiene un siguiente, por lo que se omite y no se guarda");
                        } else { 
                            NodoAdyacente AdyacenteAux = estacionActual.getPrimerAdyacente();
                            while (AdyacenteAux.getSiguienteAdyacente() != null) {
                                pw.println(estacionActual.getID_Estacion() + "," + AdyacenteAux.getEstacionDestino().getID_Estacion() + "," + AdyacenteAux.getTiempo_Minutos());
                                System.out.println(estacionActual.getID_Estacion() + "," + AdyacenteAux.getEstacionDestino().getID_Estacion() + "," + AdyacenteAux.getTiempo_Minutos());
                                entraenbucle = true;
                                AdyacenteAux = AdyacenteAux.getSiguienteAdyacente();
                            }
                        System.out.println(estacionActual.getID_Estacion() + "," + estacionActual.getPrimerAdyacente().getEstacionDestino().getID_Estacion() + "," + estacionActual.getPrimerAdyacente().getTiempo_Minutos());
                        if (!entraenbucle) {
                            pw.println(estacionActual.getID_Estacion() + "," + estacionActual.getPrimerAdyacente().getEstacionDestino().getID_Estacion() + "," +estacionActual.getPrimerAdyacente().getTiempo_Minutos());
                        }
                    }
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
    public String ejecutarDijkstra(String origenStr, String destinoStr, TablaHash hashTable) {
        String texto = "";
        // 1. Validaciones básicas
        if (origenStr.isEmpty() || destinoStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Debe ingresar la estación de origen y de destino.", "Campos vacíos", javax.swing.JOptionPane.WARNING_MESSAGE);
            return texto;
        }
        String origenLimpio = origenStr.trim();
        String destinoLimpio = destinoStr.trim();

        // 2. BÚSQUEDA SEGURA DE LAS ESTACIONES USANDO TU TABLA HASH Y NODOS ENCADENADOS
        Estacion estacion_origen = null;
        Estacion estacion_destino = null;

        int key_origen = hashTable.getHashCode(origenLimpio);
        int key_destino = hashTable.getHashCode(destinoLimpio);

        Object objOrigen = hashTable.getValue(key_origen);
        Object objDestino = hashTable.getValue(key_destino);

        // Buscar origen
        if (objOrigen instanceof Nodo) {
            estacion_origen = hashTable.buscarEstacionEncadenada((Nodo) objOrigen, origenLimpio);
        } else if (objOrigen instanceof Estacion) {
            Estacion est = (Estacion) objOrigen;
            if (est.getID_Estacion().equalsIgnoreCase(origenLimpio)) {
                estacion_origen = est;
            }
        }

        // Buscar destino
        if (objDestino instanceof Nodo) {
            estacion_destino = hashTable.buscarEstacionEncadenada((Nodo) objDestino, destinoLimpio);
        } else if (objDestino instanceof Estacion) {
            Estacion est = (Estacion) objDestino;
            if (est.getID_Estacion().equalsIgnoreCase(destinoLimpio)) {
                estacion_destino = est;
            }
        }

        // Validaciones de existencia de los objetos encontrados
        if (estacion_origen == null && estacion_destino == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "Ambas estaciones no existen en el sistema.", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
            return texto;
        } else if (estacion_origen == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "La estación de origen '" + origenLimpio + "' no existe.", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
            return texto;
        } else if (estacion_destino == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "La estación de destino '" + destinoLimpio + "' no existe.", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
            return texto;
        }

        // 2. INICIALIZACIÓN: Recorrer toda la Tabla Hash para resetear valores
        Object[] buckets = hashTable.getHashTable(); // O el método que uses para acceder al arreglo interno de la tabla hash
        for (int i = 0; i < buckets.length; i++) {
            Object obj = hashTable.getValue(i); // O como recorras los buckets de tu TablaHash
            if (obj instanceof Nodo) {
                Nodo nodoActual = (Nodo) obj;
                while (nodoActual != null) {
                    Estacion est = nodoActual.getEstacion();
                    if (est != null) {
                        est.setDistancia(Integer.MAX_VALUE);
                        est.setVisitado(false);
                        est.setPredecesor(null);
                    }
                    nodoActual = nodoActual.getNext();
                }
            } else if (obj instanceof Estacion) {
                Estacion est = (Estacion) obj;
                est.setDistancia(Integer.MAX_VALUE);
                est.setVisitado(false);
                est.setPredecesor(null);
            }
        }

        // La distancia al origen es 0
        ((Estacion)estacion_origen).setDistancia(0);

        // 3. BUCLE PRINCIPAL DE DIJKSTRA (Búsqueda secuencial O(V^2) sin colas de prioridad externas)
        boolean continuar = true;
        while (continuar) {
            Estacion estacionActual = null;
            int menorDistancia = Integer.MAX_VALUE;

            // Buscar la estación NO VISITADA con la menor distancia acumulada recorriendo toda la tabla hash
            for (int i = 0; i < buckets.length; i++) {
                Object obj = hashTable.getValue(i);
                if (obj instanceof Nodo) {
                    Nodo nodoActual = (Nodo) obj;
                    while (nodoActual != null) {
                        Estacion est = nodoActual.getEstacion();
                        if (est != null && !est.isVisitado() && est.getDistancia() < menorDistancia) {
                            menorDistancia = est.getDistancia();
                            estacionActual = est;
                        }
                        nodoActual = nodoActual.getNext();
                    }
                } else if (obj instanceof Estacion) {
                    Estacion est = (Estacion) obj;
                    if (!est.isVisitado() && est.getDistancia() < menorDistancia) {
                        menorDistancia = est.getDistancia();
                        estacionActual = est;
                    }
                }
            }

            // Si no encontramos nodo con distancia finita o ya llegamos a estaciones inalcanzables, salimos
            if (estacionActual == null || menorDistancia == Integer.MAX_VALUE) {
                break;
            }

            // Si llegamos al destino, podemos detener el algoritmo antes de tiempo
            if (estacionActual.equals(((Estacion)estacion_destino))) {
                break;
            }

            estacionActual.setVisitado(true);

            // 4. RELAJACIÓN DE ARISTAS (Recorrer los adyacentes de la estación actual)
            NodoAdyacente adyacente = estacionActual.getPrimerAdyacente();
            while (adyacente != null) {
                Estacion vecino = adyacente.getEstacionDestino();
                int pesoArista = adyacente.getTiempo_Minutos();

                if (!vecino.isVisitado()) {
                    int nuevaDistancia = estacionActual.getDistancia() + pesoArista;
                    if (nuevaDistancia < vecino.getDistancia()) {
                        vecino.setDistancia(nuevaDistancia);
                        vecino.setPredecesor(estacionActual);
                    }
                }
                adyacente = adyacente.getSiguienteAdyacente(); // O .getNext() según tu implementación
            }
        }

        // 5. RECONSTRUCCIÓN DEL CAMINO Y RESULTADOS
        if (((Estacion)estacion_destino).getDistancia() == Integer.MAX_VALUE) {
            javax.swing.JOptionPane.showMessageDialog(null, "No existe una ruta posible entre " + origenStr + " y " + destinoStr + ".", "Sin ruta", javax.swing.JOptionPane.WARNING_MESSAGE);
            return texto;
        }

        // Reconstruir el camino hacia atrás usando los predecesores
        StringBuilder rutaConstruida = new StringBuilder();
        Estacion paso = ((Estacion)estacion_destino);

        // Como invertiremos el camino, podemos guardarlo en una lista propia o un array estático, 
        // o usar un StringBuilder y un enfoque simple:
        String caminoTexto = "";
        while (paso != null) {
            caminoTexto = paso.getID_Estacion() + (caminoTexto.isEmpty() ? "" : " <- " + caminoTexto);
            paso = paso.getPredecesor();
        }

        // Mostrar el resultado en el JTextArea de tu interfaz
        String resultadoFinal = "=== RUTA ÓPTIMA (DIJKSTRA) ===\n" +
                                "Origen: " + origenStr + "\n" +
                                "Destino: " + destinoStr + "\n" +
                                "Tiempo total estimado: " + ((Estacion)estacion_destino).getDistancia() + " minutos\n\n" +
                                "Recorrido:\n" + caminoTexto;

        return resultadoFinal;
    }
    
    public String ejecutarDFS(String origenStr, TablaHash hashTable) {
        // 1. Validar que exista la estación de origen
        String texto = "";
        Estacion estacionOrigen = null;
        int key = hashTable.getHashCode(origenStr.trim());
        Object obj = hashTable.getValue(key);

        if (obj instanceof Nodo) {
            estacionOrigen = hashTable.buscarEstacionEncadenada((Nodo) obj, origenStr.trim());
        } else if (obj instanceof Estacion) {
            if (((Estacion) obj).getID_Estacion().equalsIgnoreCase(origenStr.trim())) {
                estacionOrigen = (Estacion) obj;
            }
        }

        if (estacionOrigen == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "La estación de origen no existe.", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
            return texto;
        }

        // 2. Resetear el estado 'visitado' en toda la tabla hash antes de empezar
        Object[] buckets = hashTable.getHashTable();
        for (int i = 0; i < buckets.length; i++) {
            Object bucketObj = hashTable.getValue(i);
            if (bucketObj instanceof Nodo) {
                Nodo nodoActual = (Nodo) bucketObj;
                while (nodoActual != null) {
                    if (nodoActual.getEstacion() != null) {
                        nodoActual.getEstacion().setVisitado(false);
                    }
                    nodoActual = nodoActual.getNext();
                }
            } else if (bucketObj instanceof Estacion) {
                ((Estacion) bucketObj).setVisitado(false);
            }
        }

        // 3. Iniciar el recorrido DFS recursivo y acumular el texto
        StringBuilder resultado = new StringBuilder("=== RECORRIDO DFS (PROFUNDIDAD) ===\n");
        recorridoDFS(estacionOrigen, resultado);

        texto = resultado.toString();
        return texto;
    }

    // Método auxiliar recursivo para el DFS
    private void recorridoDFS(Estacion estacion, StringBuilder sb) {
        estacion.setVisitado(true);
        sb.append("Estación visitada: ").append(estacion.getID_Estacion()).append("\n");

        NodoAdyacente adyacente = estacion.getPrimerAdyacente();
        while (adyacente != null) {
            Estacion vecino = adyacente.getEstacionDestino();
            if (vecino != null && !vecino.isVisitado()) {
                recorridoDFS(vecino, sb);
            }
            adyacente = adyacente.getSiguienteAdyacente(); // O .getNext() según tu clase
        }
    }
}
   
