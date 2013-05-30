package EDA;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ApproxAlgs {

	public static void approxAlg1(HyperGraph graph, Node top, Node root, int time) {
		List<HyperArc> minpath = new ArrayList<HyperArc>();
		Queue<HyperArc> auxarcs = new LinkedList<HyperArc>();
		HyperArc arc;
		int pathweight = 0;
		Iterator<Node> nodesit;
		Node node = root; 


		arc = minArc(node, minpath);
		minpath.add(arc);
		auxarcs.add(arc);
		
			while(!auxarcs.isEmpty()){ 
				arc = auxarcs.remove();
				nodesit =  arc.tails.iterator();
				while(nodesit.hasNext()){
					node = nodesit.next();
					HyperArc nodeminarc = minArc(node,minpath);
					if(!nodeminarc.isMarked()){
						auxarcs.add(nodeminarc);
						minpath.add(nodeminarc);
						nodeminarc.mark();
						pathweight += nodeminarc.weight;
					}
				}
			}
	}

	private static HyperArc minArc(Node node, List<HyperArc> minpath){
		Iterator<HyperArc> arcsIt = node.tails.iterator();
		Boolean ready = false;
		HyperArc arc = new HyperArc(0, null);;
		HyperArc minarc = new HyperArc(0, null);
		//if(!arcsIt.hasNext()) ESTO NO LO TIENE QUE HACER NUNCA PORQUE SI NO EL NODO SERÍA RAIZ
//			return false;

		while(arcsIt.hasNext() && !ready){//ESTO ENCUENTRA EL MÍNIMO ARCO  O UNO QUE YA ESTÉ MARCADO ARRIBA DE UN NODO
			arc = arcsIt.next();
			if(arc.isMarked()){
				//if(arc != minarc) NO SE SI HACE FALTA ESTO, MEPA QUE NO
				minarc.unmark();
				return arc;
			}
			else{
				if(minarc == null)
					minarc = arc;
				else if(minarc.weight > arc.weight){
					minarc.unmark();
					arc.mark();
					minarc = arc;							
				}
			}
		}
		minarc.unmark();
		return minarc;
	}

}


