package EDA;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author st0le
 *
 */
public class PowerSet implements Iterator<Set<HyperArc>>,Iterable<Set<HyperArc>>{
    private ArrayList<HyperArc> n;
    private ArrayList<Set<HyperArc>> l = new ArrayList<Set<HyperArc>>();
    private ArrayList<Integer> a = new ArrayList<Integer>();
    private ArrayList<Integer> s = new ArrayList<Integer>();
    private int j;

    public PowerSet(Collection<HyperArc> set)
    {
    	n = new ArrayList<HyperArc>(set);
    	a = new ArrayList<Integer>(n.size());
    	int size = n.size();
    	for(int i = 0; i< size; i++)
    		a.add(0);
    	s = new ArrayList<Integer>(n.size());
    	s.add(0);
    	l = new ArrayList<Set<HyperArc>>();
    	l.add(new HashSet<HyperArc>());
    	j = 0;
    	Collections.sort(n);
    }
    
    @Override
    public boolean hasNext() {
    	return true;

    }

    @Override
    public Set<HyperArc> next() {
    	//System.out.println(l);
		j++;
		int minValue = Integer.MAX_VALUE;
		int minIndex = 0;
		int size = n.size();
	    for(int i = 0; i < size; i++){
	    	int aux;
	    	if(!(a.get(i) == null))
	    		aux = s.get(a.get(i)) + n.get(i).weight;
	    	else
	    		aux = Integer.MAX_VALUE;
	    	if(aux<minValue){
	    		minIndex = i;
	    		minValue = aux;
	    	}
	    }
	    //System.out.println(minIndex + "  " + minValue);
	    Set<HyperArc> auxSet = new HashSet<HyperArc>(l.get(a.get(minIndex)));
	    auxSet.add(n.get(minIndex));
	    l.add(auxSet);
	    s.add(s.get(a.get(minIndex)) + n.get(minIndex).weight);
	    int sizeL = l.size();
	    int temp = a.get(minIndex);
	    a.set(minIndex, null);
	    for(int i = temp + 1;i < sizeL - 1;i++){
	    	Set<HyperArc> set = l.get(i);
	    	boolean flag = false;
	    	for(HyperArc arc : set){
	    		if(arc.weight > n.get(minIndex).weight){
	    			flag = true;
	    			break;
	    		}
	    	}
	    	if (flag == false){
	    		a.set(minIndex, i);
	    		break;
	    	}
	    }
	    return l.get(j);
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