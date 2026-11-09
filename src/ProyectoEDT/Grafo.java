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
        //Graph grafoVisual = new SingleGraph("UrbanRoute");
        String idOrigen = estacion_origen;   // Nodo que ya existe
        String idDestino = estacion_destino;  // Nodo nuevo que vas a conectar
        String idArista = idOrigen + "-" + idDestino;

        // 1. Asegurar el nodo de origen (si no existe, lo crea)
        if (grafoVisual.getNode(idOrigen) == null) {
            grafoVisual.addNode(idOrigen).setAttribute("ui.label", idOrigen);
        }

        // 2. Crear el nodo de destino SOLO si aún no existe en el grafo
        if (grafoVisual.getNode(idDestino) == null) {
            grafoVisual.addNode(idDestino).setAttribute("ui.label", idDestino);
        }

        // 3. Crear la arista que los conecta (evitando duplicar la arista si ya existe)
        if (grafoVisual.getEdge(idArista) == null) {
            Edge arista = grafoVisual.addEdge(idArista, idOrigen, idDestino);
            arista.setAttribute("weight", tiempo_minutos);
            arista.setAttribute("ui.label", tiempo_minutos);
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
        "   text-size: 11;" +                 // Tamaño del número (peso)
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
    
    
    public void mostrarGrafo(Graph grafoVisual) {
        Viewer viewer = grafoVisual.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    }
    
}
