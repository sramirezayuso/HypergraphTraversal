package EDA;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class HyperArc{
	
	List<Node> tails;
	List<Node> heads;
	Set<HyperArc> path;
	String name;
	int counter;
	int weight;
	
	
	public HyperArc(int weight, String name){
		this.tails = new LinkedList<Node>();
		this.heads = new LinkedList<Node>();
		this.weight = weight;
		this.name = name;
		this.counter = 0;
		this.path = new HashSet<HyperArc>();
	}
	
	public void addHead(Node node){
		heads.add(node);
	}

	public void addTail(Node node){
		tails.add(node);
	}
	
	@Override
	public String toString() {
		String answer =  name + "(" + weight + ")" + " Heads: ";
		for (Node node: heads){
			answer = answer + node.name + " ";
		}
		answer += "\n" + " Tails: ";
		for (Node node: tails){
			answer = answer + node.name + " ";
		}
		return answer;
	}
	
	
	
}
