package EDA;

import java.util.LinkedList;
import java.util.List;

public class Node{


	List<HyperArc> arcs;
	String name;
	HyperArc path;
	int weight;
	boolean visited;
	
	public Node(String name){
		arcs = new LinkedList<HyperArc>();
		this.name = name;
		this.weight = Integer.MAX_VALUE;
		this.visited = false;
		this.path = null;
	}
	
	public void addArc(HyperArc arc){
		arcs.add(arc);
	}
	
	
	@Override
	public String toString() {
		String answer =  name + " Arcs: ";
		for ( HyperArc arc : arcs){
			answer = answer + arc.name + " ";
		}
		return answer;
	}
}