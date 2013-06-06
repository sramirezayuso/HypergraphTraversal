package EDA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HeurVec {

	public static void main(String args[]){
		HyperGraph hg = new HyperGraph("A.hg");
		hg.graphToDot("A");
		System.out.println(heur1(hg,hg.sink,100));
		
//		hg.criticalToDot("highlightedA", criticalPath);
//		criticalPath.graphToDot("criticalPathA");
//		criticalPath.graphToHg("criticalPath");
		
	}
	
	public static int heur1(HyperGraph graph, Node sink, int time){
		List<Node> nodes = new ArrayList<Node>();
		List<HyperArc> arcs = new ArrayList<HyperArc>();
		int weight = ApproxAlgs.minRelAlg(graph,sink,time, nodes, arcs);
		final int C = 5;
		long starttime = System.currentTimeMillis();
		int i = 0;
		Random r = new Random();

		while(i%C != 0 || System.currentTimeMillis() - starttime < time*1000){
			Node nodea, nodeb;
			HyperArc arc;
			
			arc = arcs.get(r.nextInt(arcs.size()));
			nodea = arc.tails.get(r.nextInt(arc.tails.size()));
			nodeb = arc.heads.get(r.nextInt(arc.heads.size()));
			
			
		}
		
		return weight;
	}
	
	

}
