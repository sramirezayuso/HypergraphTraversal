package EDA;




import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class HyperArc implements Comparable<HyperArc>{
	
	String name;
	int counter;
	int weight;
	boolean mark;
	List<Node> tails;
	List<Node> heads;
	Set<HyperArc> preds;
	
	
	public HyperArc(int weight, String name){
		this.name = name;
		this.weight = weight;
		this.counter = 0;
		this.mark = false;
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
	
	public boolean isMarked(){
		return this.mark;
	}
	
	public void mark(){
		this.mark = true;
	}
	
	public void unmark(){
		this.mark = false;
	}
	
	@Override
	public String toString() {
		String answer =  name + "(" + weight + ")" + " " ;
//		for (Node node: heads){
//			answer = answer + node.name + " ";
//		}
//		answer += "\n" + " Tails: ";
//		for (Node node: tails){
//			answer = answer + node.name + " ";
//		}
//		answer += "\n";
		return answer;
	}
	
	public int compareTo(HyperArc o){
		int aux = this.weight - o.weight;
		if(aux == 0){
			if(this.name.equals(o.name))
				return 0;
			return -1;
		}
		return aux;
	}
	
	public boolean equals(Object o){
		if(o == null)
			return false;
		return this.name.equals(((HyperArc)o).name);
	}
}
