package tpe;

import java.util.LinkedList;
import java.util.List;

public class Node{


	List<HyperArc> tasks;
	String name;
	
	public Node(String name){
		tasks = new LinkedList<HyperArc>();
		this.name = name;
	}
	
	public void addArc(HyperArc arc){
		tasks.add(arc);
	}
	
	
	@Override
	public String toString() {
		String answer =  name + " Arcs: ";
		for ( HyperArc arc : tasks){
			answer = answer + arc.name + " ";
		}
		return answer;
	}
}