package EDA;


public class Node{

	String name;
	int weight;
	List<HyperArc> arcs;
	Set<HyperArc> preds;
	
	public Node(String name){
		this.name = name;
		this.weight = Integer.MAX_VALUE;
		this.arcs = new LinkedList<HyperArc>();
		this.preds = new HashSet<HyperArc>();
	}
	
	public Node(String name, List<HyperArc> arcs, Set<HyperArc> preds){
		this.name = name;
		this.weight = Integer.MAX_VALUE;
		this.arcs = new LinkedList<HyperArc>(arcs);
		this.preds = new HashSet<HyperArc>(preds);
	}
	
	public void addArc(HyperArc arc){
		arcs.add(arc);
	}
	
	public void removeArc(HyperArc arc){
		arcs.remove(arc);
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