/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoEDT;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 *
 * @author daviel
 */
public class Grafo {
    
    public void registrarNodosEnGrafo(TablaHash hashTable) {
    // 1. Activar el motor gráfico de Swing
    System.setProperty("org.graphstream.ui", "swing");

    // 2. Crear el grafo visual
    Graph grafoVisual = new SingleGraph("UrbanRoute");
    
    // 3. Recorrer la Tabla Hash completa (del 0 al tamaño del arreglo)
    for (int i = 0; i < hashTable.getHashTable().length; i++) {
        Object actual = hashTable.getHashTable()[i];

        // Mientras haya elementos en esta posición (puede ser una estación o una cadena de nodos)
        while (actual != null) {
            Estacion estacionActual = null;

            // Extraemos la estación dependiendo de si es un objeto Estacion directo o un Nodo de colisión
            if (actual instanceof Estacion) {
                estacionActual = (Estacion) actual;
                actual = null; // Terminamos esta posición porque era una estación única
            } else if (actual instanceof Nodo) {
                Nodo nodoColision = (Nodo) actual;
                estacionActual = nodoColision.getEstacion(); // Extraemos la estación del nodo
                actual = nodoColision.getNext(); // Avanzamos al siguiente nodo de la colisión
            }

            // Si obtuvimos una estación válida, la registramos en GraphStream
            if (estacionActual != null) {
                String idEstacion = estacionActual.getID_Estacion(); // O el atributo que uses como identificador único

                // Verificamos que el nodo no haya sido agregado antes para evitar duplicados
                if (grafoVisual.getNode(idEstacion) == null) {
                    grafoVisual.addNode(idEstacion);
                    
                    // Opcional: ponerle una etiqueta visible con el nombre de la estación
                    grafoVisual.getNode(idEstacion).setAttribute("ui.label", estacionActual.getNombreEstacion());
                }
            }
        }
    }
}
