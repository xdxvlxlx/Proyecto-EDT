/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoEDT;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Edge;

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
    return grafoVisual;
    }
    
    public void mostrarGrafo(Graph grafoVisual) {
        grafoVisual.display();
    }
    
}
