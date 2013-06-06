package EDA;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ApproxAlgs {

	public static int minRelAlg(HyperGraph graph, Node sink, int time, List<Node> minpathnodes, List<HyperArc> minpath) {
		Queue<HyperArc> auxarcs = new LinkedList<HyperArc>();
		HyperArc arc;
		int pathweight = 0;
		Iterator<Node> nodesit;
		Node node = sink; 

		minpathnodes.add(node);
		arc = minArc(node, minpath);
		minpath.add(arc);
		auxarcs.add(arc);
		
		
			while(!auxarcs.isEmpty()){ 
				arc = auxarcs.remove();
				nodesit =  arc.tails.iterator();
				while(nodesit.hasNext()){
					node = nodesit.next();
					minpathnodes.add(node);
					HyperArc nodeminarc = minArc(node,minpath);
					if(nodeminarc == null)
						break;
					if(!nodeminarc.isMarked()){
						auxarcs.add(nodeminarc);
						minpath.add(nodeminarc);
						nodeminarc.mark();
						pathweight += nodeminarc.weight;
					}
				}
			}
			for(Node anode: minpathnodes){
				anode.mark();
			}
			for(HyperArc anarc: minpath){
				anarc.mark();
			}

			return pathweight;
	}

	private static HyperArc minArc(Node node, List<HyperArc> minpath){
		Iterator<HyperArc> arcsIt = node.tails.iterator();
		Boolean ready = false;
		HyperArc arc = new HyperArc(0, null);;
		HyperArc minarc = new HyperArc(0, null);

		while(arcsIt.hasNext()){//ESTO ENCUENTRA EL M�NIMO ARCO  O UNO QUE YA EST� MARCADO ARRIBA DE UN NODO
			ready = true;
			arc = arcsIt.next();
			if(arc.isMarked()){
				minarc.unmark();
				return arc;
			}
			else{
				
				if(minarc.weight > arc.weight){
					minarc.unmark();
					arc.mark();
					minarc = arc;							
				}
				else
				minarc = arc;
			}
		}
		if(!ready)
			return null;
		minarc.unmark();
		return minarc;
	}

}


