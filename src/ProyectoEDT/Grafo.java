/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoEDT;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Edge;
import org.graphstream.ui.view.Viewer;

/**
 *
 * @author daviel
 */
public class Grafo {
    
    public Graph registrarNodosEnGrafo(Graph grafoVisual, String estacion_origen, String estacion_destino, int tiempo_minutos) {
        System.setProperty("org.graphstream.ui", "swing");
        String idArista = estacion_origen + "-" + estacion_destino;
        String idArista2 = estacion_destino + "-" + estacion_origen;

        // 1. Asegurar el nodo de origen (si no existe, lo crea)
        if (grafoVisual.getNode(estacion_origen) == null) {
            grafoVisual.addNode(estacion_origen).setAttribute("ui.label", estacion_origen);
        }

        // 2. Crear el nodo de destino SOLO si aún no existe en el grafo
        if (grafoVisual.getNode(estacion_destino) == null) {
            grafoVisual.addNode(estacion_destino).setAttribute("ui.label", estacion_destino);
        }

        // 3. Buscar si la arista ya existe (en cualquiera de las dos direcciones)
        Edge aristaExistente = grafoVisual.getEdge(idArista);
        if (aristaExistente == null) {
            aristaExistente = grafoVisual.getEdge(idArista2);
        }

        if (aristaExistente != null) {
            // La arista ya existe, verificamos su tiempo actual
            int tiempoActual = 0;
            if (aristaExistente.hasAttribute("weight")) {
                tiempoActual = (int) aristaExistente.getNumber("weight");
            }

            // Si el tiempo ingresado es diferente, lo actualizamos
            if (tiempoActual != tiempo_minutos) {
                aristaExistente.setAttribute("weight", tiempo_minutos);
                aristaExistente.setAttribute("ui.label", tiempo_minutos);
                System.out.println("La conexión entre " + estacion_origen + " y " + estacion_destino + 
                                   " ya existía. Se actualizó el tiempo de " + tiempoActual + " a " + tiempo_minutos + " min.");
            } else {
                System.out.println("La conexión ya existe y el tiempo es el mismo (" + tiempo_minutos + " min).");
            }

        } else {
            // Si no existe ninguna de las dos, creamos la nueva arista
            Edge arista = grafoVisual.addEdge(idArista, estacion_origen, estacion_destino);
            arista.setAttribute("weight", tiempo_minutos);
            arista.setAttribute("ui.label", tiempo_minutos);
            System.out.println("Se creó una nueva conexión entre " + estacion_origen + " y " + estacion_destino);
        }
        String styleSheet = 
        // Estilo global del contenedor
        "graph { padding: 40; fill-color: #FAFAFA; }" +

        // Estilo de los Nodos
        "node {" +
        "   size: 20;" +                      // Tamaño del circulo
        "   fill-color: #73F4FF;" +             // Color del nodo
        "   stroke-mode: plain;" +              // Borde del nodo
        "   stroke-color: #000000;" +
        "   stroke-width: 1;" +
        "   text-size: 12;" +                 // Tamaño de la letra
        "   text-style: bold;" +                // Texto en negrita
        "   text-color: #222222;" +             // Color del texto
        "   text-alignment: at-right;" +        // Ubica el texto A LA DERECHA del nodo
        "   text-offset: 8, 0;" +           // Desplaza el texto para que no pise el punto
        "}" +

        // Estilo de las Aristas (Conexiones)
        "edge {" +
        "   fill-color: #000000;" +             // Color de la línea
        "   size: 2;" +                      // Grosor de la línea
        "   text-size: 14;" +                 // Tamaño del número (peso)
        "   text-color: #222222;" +             // Color del peso
        "   text-background-mode: plain;" +     // Fondo blanco para el texto del peso
        "   text-background-color: #FFFFFF;" +
        "   text-alignment: above;" +           // Ajuste para mover el peso fuera del centro
        "   text-offset: 5, 0;" +           // Desplaza el texto para que no pise el punto
        "}";

    // Aplicar al grafo
    grafoVisual.setAttribute("ui.stylesheet", styleSheet);
        return grafoVisual;
    }
    
    public Graph registrarUnicoNodo(Graph grafoVisual, String ID_Nodo) {
        System.setProperty("org.graphstream.ui", "swing");
        if (grafoVisual.getNode(ID_Nodo) == null) {
            grafoVisual.addNode(ID_Nodo).setAttribute("ui.label", ID_Nodo);;
        }
        
        String styleSheet = 
        // Estilo global del contenedor
        "graph { padding: 40; fill-color: #FAFAFA; }" +

        // Estilo de los Nodos
        "node {" +
        "   size: 20;" +                      // Tamaño del circulo
        "   fill-color: #73F4FF;" +             // Color del nodo
        "   stroke-mode: plain;" +              // Borde del nodo
        "   stroke-color: #000000;" +
        "   stroke-width: 1;" +
        "   text-size: 12;" +                 // Tamaño de la letra
        "   text-style: bold;" +                // Texto en negrita
        "   text-color: #222222;" +             // Color del texto
        "   text-alignment: at-right;" +        // Ubica el texto A LA DERECHA del nodo
        "   text-offset: 8, 0;" +           // Desplaza el texto para que no pise el punto
        "}";

    // Aplicar al grafo
    grafoVisual.setAttribute("ui.stylesheet", styleSheet);
        return grafoVisual;
    }
    

    public void mostrarGrafo(Graph grafoVisual) {
        Viewer viewer = grafoVisual.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    }
    
}
