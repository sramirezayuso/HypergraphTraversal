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


public class HyperGraph {

	Map<String, HyperArc> arcs;
	Map<String, Node> nodes;
	Node source;
	Node sink;
	int minWeight;
	Set<HyperArc> solution;

	public static void main(String args[]) {
		HyperGraph hg = new HyperGraph("enunciado.hg");
		hg.graphToDot("enunciado");
		HyperGraph criticalPath = hg.exactPath();
		hg.criticalToDot("highlightedEnunciado", criticalPath);
		criticalPath.graphToDot("criticalPathEnunciado");
		criticalPath.graphToHg("criticalPathEnunciado");
	}

	public HyperGraph(Collection<Node> nodes, Collection<HyperArc> arcs) {
		this.nodes = new HashMap<String, Node>();
		this.arcs = new HashMap<String, HyperArc>();
		for (Node node : nodes)
			this.nodes.put(node.name, node);
		for (HyperArc arc : arcs)
			this.arcs.put(arc.name, arc);
		this.minWeight = Integer.MAX_VALUE;
	}

	public HyperGraph(String file) {
		try {
			Scanner sc = new Scanner(new File(file));

			nodes = new HashMap<String, Node>();
			arcs = new HashMap<String, HyperArc>();
			this.minWeight = Integer.MAX_VALUE;
			
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

	public HyperGraph(Collection<HyperArc> arcs, Node sink){
		this.nodes = new HashMap<String, Node>();
		this.arcs = new HashMap<String, HyperArc>();
		
		this.nodes.put(sink.name, sink);
		for (HyperArc arc : arcs)
			this.arcs.put(arc.name, arc);
		for (HyperArc arc : this.arcs.values())
			for (Node tail : arc.tails) {
				Node aux = new Node(tail.name, tail.heads);
				for (HyperArc auxArc : tail.heads)
					if (!this.arcs.values().contains(auxArc))
						aux.removeHead(auxArc);
				this.nodes.put(aux.name, aux);
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

	public HyperGraph exactPath(){

		source.mark();
		
		Queue<Node> queue = new LinkedList<Node>();
		minWeight = startCriticalPath()+1;
		clearMarks();
		nodeRec(sink, queue);

		return new HyperGraph(solution, sink);

	}
	
	@SuppressWarnings("unchecked")
	private void nodeRec(Node node, Queue<Node> q){
		q.poll();
		if(node.equals(source)){
			calcSolution();
			return;
		}
		for(HyperArc arc: node.tails){
			arc.counter++;
			
			int value = 0;
			for(HyperArc globArc : arcs.values())
				if(globArc.counter > 0)
					value += globArc.weight;
			if(value > minWeight){
				arc.counter--;
				return;
			}			
			
			Queue<Node> auxQ = new LinkedList<Node>();
			auxQ = (Queue<Node>)((((LinkedList<Node>)q).clone()));
			for(Node tail: arc.tails){
				if(!auxQ.contains(tail))
					auxQ.offer(tail);
			}

			Node aux = auxQ.peek();
			nodeRec(aux, auxQ);
			
			arc.counter--;
		}
	}
	
	private void calcSolution(){
		int value = 0;
		Set<HyperArc> currSol = new HashSet<HyperArc>();
		for(HyperArc arc : arcs.values())
			if(arc.counter > 0){
				value += arc.weight;
				currSol.add(arc);
			}
		if (value < minWeight){
			solution = currSol;
			minWeight = value;
		}
	}

	public void clearMarks() {
		for (HyperArc arc : arcs.values()) {
			arc.unmark();
			arc.counter = 0;
		}
		for (Node node : nodes.values()){
			node.unmark();
			node.counter = 0;
		}
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
		return sink.weight;
	}

}