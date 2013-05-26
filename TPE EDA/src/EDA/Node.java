package EDA;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class Node{

	String name;
	int weight;
	List<HyperArc> heads;
	List<HyperArc> tails;
	Set<HyperArc> preds;
	
	public Node(String name){
		this.name = name;
		this.weight = Integer.MAX_VALUE;
		this.heads = new LinkedList<HyperArc>();
		this.tails = new LinkedList<HyperArc>();
		this.preds = new HashSet<HyperArc>();
	}
	
	public Node(String name, List<HyperArc> arcs, Set<HyperArc> preds){
		this.name = name;
		this.weight = Integer.MAX_VALUE;
		this.heads = new LinkedList<HyperArc>(arcs);
		this.preds = new HashSet<HyperArc>(preds);
	}
	
	public void addHead(HyperArc arc){
		heads.add(arc);
	}
	
	public void removeHead(HyperArc arc){
		heads.remove(arc);
	}
	
	public void addTail(HyperArc arc){
		tails.add(arc);
	}
	
	public void removeTail(HyperArc arc){
		tails.remove(arc);
	}
	
	@Override
	public String toString() {
		String answer =  name + " Arcs: ";
		for ( HyperArc arc : heads){
			answer = answer + arc.name + " ";
		}
		return answer;
	}
}