package tpe;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class HyperArc{
	
	String name;
	int counter;
	int weight;
	List<Node> tails;
	List<Node> heads;
	Set<HyperArc> preds;
	
	
	public HyperArc(int weight, String name){
		this.name = name;
		this.weight = weight;
		this.counter = 0;
		this.tails = new LinkedList<Node>();
		this.heads = new LinkedList<Node>();
		this.preds = new HashSet<HyperArc>();
	}
	
	public void addHead(Node node){
		heads.add(node);
	}
	
	public void removeHead(Node node){
		heads.remove(node);
	}

	public void addTail(Node node){
		tails.add(node);
	}
	
	public void removeTail(Node node){
		tails.remove(node);
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
