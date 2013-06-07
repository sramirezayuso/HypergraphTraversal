package EDA;



import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class Neighbors implements Iterator<Set<HyperArc>>,Iterable<Set<HyperArc>>{
    private HyperArc[] arr = null;
    private HyperGraph graph;
    private boolean finished;
    private Set<HyperArc> next;
    private int index;

    public Neighbors(Set<HyperArc> set, HyperGraph graph)
    {	
    	this.graph = graph;
    	this.finished = false;
    	this.arr = new HyperArc[set.size()];
    	set.toArray(arr);
    	this.index = 0;
        this.next = calcNext();
    }

    @Override
    public boolean hasNext() {
        return !(next == null);
    }

    @Override
    public Set<HyperArc> next() {
    	Set<HyperArc> answer = next;
    	next = calcNext();
    	return answer;
    }
    
    private Set<HyperArc> calcNext(){
    	while(!finished){
    		Set<HyperArc> returnSet = new TreeSet<HyperArc>();
	        for(int i = 0; i < arr.length; i++)
	            if(i != index)
	                returnSet.add(arr[i]);
	        index++;
	        if(index == arr.length)
	        	finished = true;
	        graph.clearMarks();
	        for(HyperArc arc: returnSet)
	        	arc.mark();
	        if(graph.isSolution())
	        	return returnSet;
        }
    	return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not Supported!");
    }

    @Override
    public Iterator<Set<HyperArc>> iterator() {
        return this;
    }

}