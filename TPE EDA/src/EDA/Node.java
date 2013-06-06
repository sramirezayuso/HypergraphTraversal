package EDA;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class Node{

	String name;
	int counter;
	int weight;
	List<HyperArc> heads;
	List<HyperArc> tails;
	Set<HyperArc> preds;
	boolean mark;
	
	public Node(String name){
		this.name = name;
		this.counter=0;
		this.weight = Integer.MAX_VALUE;
		this.heads = new LinkedList<HyperArc>();
		this.tails = new LinkedList<HyperArc>();
		this.preds = new HashSet<HyperArc>();
		mark = false;
	}
	
	public Node(String name, List<HyperArc> arcs){
		this.name = name;
		this.counter = 0;
		this.weight = Integer.MAX_VALUE;
		this.heads = new LinkedList<HyperArc>(arcs);
		this.preds = new HashSet<HyperArc>();
		mark = false;
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
	
	public boolean isMarked(){
		return mark;
	}
	
	public void mark(){
		mark = true;
	}
	
	public void unmark(){
		mark = false;
	}
	@Override
	public String toString() {
		String answer =  name + " Arcs: ";
		for ( HyperArc arc : heads){
			answer = answer + arc.name + " ";
		}
		return answer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}