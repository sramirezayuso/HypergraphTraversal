package EDA;
import java.util.HashSet;
import java.util.Set;


public class ArcSet implements Comparable<ArcSet>{
	public int weight;
	public Set<HyperArc> arcs;
	
	public int compareTo(ArcSet o){
		int aux = this.weight - o.weight;
		if(aux == 0){
			boolean aux2 = arcs.equals(o.arcs);
			if(!aux2)
				return -1;
			return 0;
		}
		return aux;
		
	}
	
	public ArcSet(){
		this.weight = 0;
		this.arcs = new HashSet<HyperArc>();
	}
	
	public ArcSet(Set<HyperArc> set1, Set<HyperArc> set2){
		this.arcs = new HashSet<HyperArc>(set1); //SE PUEDE HACER MENOS VILLERO??? 
		arcs.addAll(set2);
		this.weight = 0;
		for(HyperArc arc: arcs)
			weight += arc.weight;
	}
	
	public void addArc(HyperArc arc){
		this.weight += arc.weight;
		this.arcs.add(arc);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		return arcs.equals(((ArcSet)obj).arcs);
	}
	
	public String toString(){
		return arcs.toString() + this.weight + "\n"; 
	}
	

	
}
