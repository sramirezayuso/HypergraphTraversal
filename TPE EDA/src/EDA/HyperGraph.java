package EDA;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class HyperGraph {

	Map<String, HyperArc> arcs;
	Map<String, Node> nodes;
	Node source;
	Node sink;

	public static void main(String args[]) {
		HyperGraph hg = new HyperGraph("A.hg");
		hg.graphToDot("B");
		HyperGraph criticalPath = hg.criticalPathInviable2();
		hg.criticalToDot("highlightedB", criticalPath);
		criticalPath.graphToDot("criticalPathB");
		criticalPath.graphToHg("criticalPath");
	}

	public HyperGraph(Collection<Node> nodes, Collection<HyperArc> arcs) {
		this.nodes = new HashMap<String, Node>();
		this.arcs = new HashMap<String, HyperArc>();
		for (Node node : nodes)
			this.nodes.put(node.name, node);
		for (HyperArc arc : arcs)
			this.arcs.put(arc.name, arc);
	}

	public HyperGraph(String file) {
		try {
			Scanner sc = new Scanner(new File(file));

			nodes = new HashMap<String, Node>();
			arcs = new HashMap<String, HyperArc>();

			String aux;
			do
				aux = sc.nextLine();
			while (aux.startsWith("#"));
			source = new Node(aux);
			nodes.put(aux, source);
			do
				aux = sc.nextLine();
			while (aux.startsWith("#"));
			sink = new Node(aux);
			nodes.put(aux, sink);

			while (sc.hasNext()) {
				String name = sc.next();
				if (name.startsWith("#")) {
					sc.nextLine();
					continue;
				}
				int weight = sc.nextInt();
				HyperArc arc = new HyperArc(weight, name);
				arcs.put(name, arc);

				int heads = sc.nextInt();
				for (int i = 0; i < heads; i++) {
					String nodeName = sc.next();
					Node node;
					if (!nodes.containsKey(nodeName))
						node = new Node(nodeName);
					else
						node = nodes.get(nodeName);
					node.addTail(arc);
					nodes.put(nodeName, node);
					arc.addHead(node);
				}

				int tails = sc.nextInt();
				for (int j = 0; j < tails; j++) {
					String nodeName = sc.next();
					Node node;
					if (!nodes.containsKey(nodeName))
						node = new Node(nodeName);
					else
						node = nodes.get(nodeName);
					node.addHead(arc);
					nodes.put(nodeName, node);
					arc.addTail(node);
				}
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	void criticalToDot(String fileName, HyperGraph subgraph) {
		try {

			File file = new File(fileName + ".dot");
			if (!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("digraph " + fileName + " {\n");

			dataToDot(bw);
			highlightDot(bw, subgraph);

			bw.write("}");
			bw.close();

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	void graphToDot(String fileName) {
		try {

			File file = new File(fileName + ".dot");
			if (!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("digraph " + fileName + " {\n");

			dataToDot(bw);

			bw.write("}");
			bw.close();

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	void dataToDot(BufferedWriter bw) {
		try {
			for (Node node : nodes.values()) {
				for (HyperArc arc : node.heads) {
					bw.write("\"" + node.name + "\"" + " -> " + "\"" + arc.name
							+ "\"" + ";\n");
				}
			}

			for (HyperArc arc : arcs.values()) {
				bw.write("\"" + arc.name + "\"" + "[shape=box, label=\""
						+ arc.name + "(" + arc.weight + ")\"]" + ";\n");
				for (Node node : arc.heads) {
					bw.write("\"" + arc.name + "\"" + " -> " + "\"" + node.name
							+ "\"" + ";\n");
				}
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	void graphToHg(String fileName) {
		try {

			File file = new File(fileName + ".hg");
			if (!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			for (HyperArc arc : this.arcs.values()) {
				bw.write(arc.name);
				bw.write(" " + arc.weight);
				bw.write(" " + arc.heads.size());
				for (Node head : arc.heads)
					bw.write(" " + head.name);
				bw.write(" " + arc.tails.size());
				for (Node tail : arc.tails)
					bw.write(" " + tail.name);
				bw.write("\n");
			}

			bw.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	void highlightDot(BufferedWriter bw, HyperGraph subgraph) {
		try {
			for (HyperArc arc : subgraph.arcs.values())
				bw.write("\"" + arc.name + "\""
						+ "[style=filled, fillcolor=red]" + ";\n");
			for (Node node : subgraph.nodes.values())
				bw.write("\"" + node.name + "\""
						+ "[style=filled, fillcolor=red]" + ";\n");
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

	}

	public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
		Set<Set<T>> sets = new HashSet<Set<T>>();
		if (originalSet.isEmpty()) {
			sets.add(new HashSet<T>());
			return sets;
		}
		for (T elem : originalSet) {
			Set<T> newSet = new HashSet<T>(originalSet);
			newSet.remove(elem);
			sets.add(newSet);
		}
		return sets;
	}

	public void clearMarks() {
		for (HyperArc arc : arcs.values()) {
			arc.unmark();
			arc.counter = 0;
		}
		for (Node node : nodes.values())
			node.unmark();
	}

	public HyperGraph criticalPathInviable2() {
		Set<HyperArc> solution = new HashSet<HyperArc>();

//		TreeSet<ArcSet> neighbors = new TreeSet<ArcSet>();
//		TreeSet<ArcSet> auxqueue = new TreeSet<ArcSet>();
//		Set<Set<HyperArc>> notSols = new HashSet<Set<HyperArc>>();
//		for (HyperArc arc : arcs.values()) {
//			ArcSet aux = new ArcSet();
//			aux.addArc(arc);
//			neighbors.add(aux);
//		}
//
//		boolean cont = true;
//		while(cont){
//			solution = neighbors.pollFirst().arcs;
//			clearMarks();
//			for(HyperArc arc: solution)
//				arc.mark();
//			if(!isSolution()){
//				notSols.add(solution);
//				for(ArcSet neighbor : neighbors)
//					auxqueue.add(new ArcSet(neighbor.arcs, solution));
//				for(ArcSet arcSet : auxqueue){
//					if(!notSols.contains(arcSet.arcs))
//						neighbors.add(arcSet);
//				}
//			} else {
//				cont = false;
//			}
//		}
		
		PowerSet pset = new PowerSet(arcs.values());
		
		boolean cont = true;
		while(cont){
			solution = pset.next();
			System.out.println(solution);
			clearMarks();
			for(HyperArc arc: solution)
				arc.mark();
			if(isSolution())
				cont = false;
		}
		

		Set<HyperArc> subArcs = new HashSet<HyperArc>();
		Set<Node> subNodes = new HashSet<Node>();

		subNodes.add(sink);
		for (HyperArc arc : solution)
			subArcs.add(arc);
		for (HyperArc arc : subArcs)
			for (Node tail : arc.tails) {
				Node aux = new Node(tail.name, tail.heads, tail.preds);
				for (HyperArc auxArc : tail.heads)
					if (!subArcs.contains(auxArc))
						aux.removeHead(auxArc);
				subNodes.add(aux);
			}
		int value = 0;
		for(HyperArc arc : solution)
			value += arc.weight;
		System.out.println(value);
		return new HyperGraph(subNodes, subArcs);
	}

	public HyperGraph criticalPathInviable() {
		int min = Integer.MAX_VALUE;
		Set<HyperArc> solution = new HashSet<HyperArc>();

		Set<HyperArc> arcSet = new TreeSet<HyperArc>(arcs.values());
		int size = arcSet.size();

		clearMarks();
		min = Math.min(criticalRec(arcSet, size), min);

		Set<HyperArc> subArcs = new HashSet<HyperArc>();
		Set<Node> subNodes = new HashSet<Node>();

		subNodes.add(sink);
		for (HyperArc arc : solution)
			subArcs.add(arc);
		for (HyperArc arc : subArcs)
			for (Node tail : arc.tails) {
				Node aux = new Node(tail.name, tail.heads, tail.preds);
				for (HyperArc auxArc : tail.heads)
					if (!subArcs.contains(auxArc))
						aux.removeHead(auxArc);
				subNodes.add(aux);
			}

		System.out.println(min);
		return new HyperGraph(subNodes, subArcs);
	}

	private int criticalRec(Set<HyperArc> arcSet, int size) {
		int min = Integer.MAX_VALUE;

		System.out.println(size);
		Set<Set<HyperArc>> pset = powerSet(arcSet);

		for (Set<HyperArc> set : pset) {
			clearMarks();
			for (HyperArc arc : set)
				arc.mark();
			if (isSolution()) {
				int value = 0;
				for (HyperArc arc : set)
					value += arc.weight;
				value = Math.min(value, criticalRec(set, size - 1));
				min = Math.min(min, value);
			}
		}
		return min;
	}

	private boolean isSolution() {
		Queue<Node> q = new LinkedList<Node>();
		q.offer(source);

		while (!q.isEmpty()) {
			Node curr = q.poll();
			if (curr.equals(sink))
				return true;
			for (HyperArc arc : curr.heads) {
				if (!curr.isMarked())
					arc.counter++;
				if (arc.counter == arc.tails.size() && arc.isMarked()) {
					for (Node head : arc.heads) {
						if (!q.contains(head))
							q.offer(head);
					}
				}
			}
			curr.mark();
		}
		return false;
	}

	public int startCriticalPath() {

		Queue<Node> q = new LinkedList<Node>();

		// Recorre, asigna pesos y antecesores
		source.weight = 0;
		q.offer(source);

		while (!q.isEmpty()) {
			Node curr = q.poll();
			for (HyperArc arc : curr.heads) {
				arc.counter++;
				if (arc.counter == arc.tails.size()) {

					int upTo = 0;

					for (Node tail : arc.tails)
						for (HyperArc pred : tail.preds)
							if (!arc.preds.contains(pred)) {
								upTo += pred.weight;
								arc.preds.add(pred);
							}

					for (Node head : arc.heads) {
						if (head.weight > upTo + arc.weight) {
							if (!q.contains(head)) {
								q.offer(head);
								if (head.weight < Integer.MAX_VALUE)
									for (HyperArc forward : head.heads)
										forward.counter--;
							}
							head.weight = upTo + arc.weight;
							head.preds.clear();
							for (HyperArc pred : arc.preds)
								head.preds.add(pred);
							head.preds.add(arc);
						}
					}
				}
			}
		}

		Set<HyperArc> subArcs = new HashSet<HyperArc>();
		Set<Node> subNodes = new HashSet<Node>();

		subNodes.add(sink);
		for (HyperArc arc : sink.preds)
			subArcs.add(arc);
		for (HyperArc arc : subArcs)
			for (Node tail : arc.tails) {
				Node aux = new Node(tail.name, tail.heads, tail.preds);
				for (HyperArc auxArc : tail.heads)
					if (!subArcs.contains(auxArc))
						aux.removeHead(auxArc);
				subNodes.add(aux);
			}
		// System.out.println(sink.weight);
		return sink.weight;
	}

}
