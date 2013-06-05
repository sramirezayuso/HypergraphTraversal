package EDA;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HeurVec {

	public static void main(String args[]){
		HyperGraph hg = new HyperGraph("B.hg");
		hg.graphToDot("A");
		System.out.println(heur1(hg,hg.sink,10));
//		hg.criticalToDot("highlightedA", criticalPath);
//		criticalPath.graphToDot("criticalPathA");
//		criticalPath.graphToHg("criticalPath");
		
	}
	
	public static int heur1(HyperGraph graph, Node sink, int time){
		List<Node> nodes = new ArrayList<Node>();
		List<HyperArc> arcs = new ArrayList<HyperArc>();
		int weight = ApproxAlgs.approxAlg1(graph,sink,time, nodes, arcs);
		final int C = 5;
		
		long starttime = System.currentTimeMillis();
		int i = 0;
		while(i%C != 0 || System.currentTimeMillis() - starttime < time*1000){
			Node nodea, nodeb;
			HyperArc arc;
			arc = arcs.get((int)(Math.random()*(arcs.size()-1)+1));
			nodea = arc.tails.get((int) (Math.random()*(arc.tails.size())));
			nodeb = arc.heads.get((int) (Math.random()*(arc.heads.size())));
			
			System.out.println("No tiene solucion");
			
			
		}
		
		return weight;
	}
	
	

}
