package EDA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ApproxAlgs2 {
	public static void main(String[] args) {
		HyperGraph hg = new HyperGraph("B.hg");
		hg.graphToDot("A");
//		HyperGraph criticalPath = 
				app21(hg,0);
//		hg.criticalToDot("highlightedA", criticalPath);
//		criticalPath.graphToDot("criticalPathA");
//		criticalPath.graphToHg("criticalPath");
	}

	public static void app21(HyperGraph graph, int time) {
		Deque<HyperArc> sarcs = new LinkedList<HyperArc>();
		List<HyperArc> arcs = new ArrayList<HyperArc>(graph.arcs.values());
		int weight = Integer.MAX_VALUE;
		int stime;
		stime = (int) System.currentTimeMillis() /1000;

		Collections.sort(arcs, new Comparator<HyperArc>() {
			@Override
			public int compare(HyperArc o1, HyperArc o2) {
				return o2.weight - o1.weight;
			}
		});
		for(HyperArc arc: arcs)
			arc.mark();

		neigh(0,arcs.size(),stime,time, arcs, graph);
		
		
		
	}

	private static void neigh(int start, int end, int stime, int time, List<HyperArc> arcs, HyperGraph graph) {
		if(stime - System.currentTimeMillis() / 1000 < time){
			return;
		}
		HyperArc arc = arcs.get(start);
		arc.unmark();
		if(!graph.isSolution()){
			arc.mark();
			return;
		}
		
		for(int i = start + 1; i < end; i++){
			neigh(i,end,stime,time,arcs, graph);
		}
		return;
	
	}
}
