package EDA;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ApproxAlgs {
	
	public static void approxAlg1(HyperGraph graph, Node top, Node root, int time) {
		List<HyperArc> minpath = new ArrayList<HyperArc>();
		Node node = root;
		Iterator<HyperArc> arcsIt;
		HyperArc arc;
		Iterator<Node> nodesIt;
		Boolean ready = false;

		while(node != root){ //VER SI ACÁ SE PONE LA CONDICION DEL TIEMPO, EN EL CASO DE QUE NO SE LLEGUE SE DEBERIA IMPRIMIR UN MENSAJE QUE NO SE LOGRO ENCONTRAR UN CAMINO
			arcsIt = node.arcs.iterator(); //ARCS TIENE QUE SER LA LISTA DE ARCOS HACER UN GET
			if(arcsIt.hasNext()) {//ESTO ES ASUMIENDO QUE LA LISTA ESTÁ ORDENADA, SI NO HAY QUE CAMBIARLO
				arc = arcsIt.next();
				arc.visit(); //PARA MARCAR QUE VISITE UN ARCO
				minpath.add(arc);
			}
			else{
				return;
			}
			
			nodesIt = arc.nodes.iterator(); //NODES TIENE QUE SER LA LISTA DE NODOS HACER UN GET
			while(nodesIt.hasNext()){
				node = nodesIt.next();
				arcsIt = node.arcs.iterator();//ARCS TIENE QUE SER LA LISTA DE ARCOS HACER UN GET 
				while(arcsIt.hasNext() && !ready){
					arc = arcsIt.next();
					if(arc.isVisited()){
						ready = true;
					}
					else{
						
					}
				}
			}
		}
	}
	
}
