package EDA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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
	
	public HyperGraph(String file) throws FileNotFoundException {
			Scanner sc = new Scanner(new File(file+".hg"));

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
			sc.close();
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

			File file = new File(fileName + ".min.dot");
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
			System.out.println("Error de input/output");
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
			System.out.println("Error de input/output");
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
			System.out.println("Error de input/output");
		}
	}

	void graphToHg(String fileName) {
		try {

			File file = new File(fileName + ".min.hg");
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
			System.out.println("Error de input/output");
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
			System.out.println("Error de input/output");
		}

	}

	public HyperGraph exactPath(){

		source.mark();
		Queue<Node> queue = new LinkedList<Node>();
		solution = startCriticalPath();
		this.minWeight++;
		clearMarks();
		nodeRec(sink, queue, new HashSet<HyperArc>());
		return new HyperGraph(solution, sink);

	}
	
	@SuppressWarnings("unchecked")
	private void nodeRec(Node node, Queue<Node> q, Set<HyperArc> soFar){
		q.poll();
		
		Integer sum = 0;
		for(HyperArc globArc : soFar)
			sum += globArc.weight;
		
		if(sum.compareTo(minWeight) > 0){
			return;
		}		
		
		if(node.equals(source)){
			calcSolution(soFar);
			return;
		}
		for(HyperArc arc: node.tails){
			arc.counter++;
			soFar.add(arc);
			
			Queue<Node> auxQ = new LinkedList<Node>();
			auxQ = (Queue<Node>)((((LinkedList<Node>)q).clone()));
			for(Node tail: arc.tails){
				if(!auxQ.contains(tail))
					auxQ.offer(tail);
			}

			Node aux = auxQ.peek();
			nodeRec(aux, auxQ, soFar);
			
			arc.counter--;
			if(arc.counter == 0)
				soFar.remove(arc);
		}
	}
	
	private void calcSolution(Set<HyperArc> soFar){
		int value = 0;
		Set<HyperArc> currSol = new HashSet<HyperArc>();
		for(HyperArc arc : soFar){
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
	
	public HyperGraph hillClimbing(int time) {
		Set<HyperArc> currentSolution = new HashSet<HyperArc>(this.arcs.values());
		long allotedTime = time * 1000;
		long startTime = System.currentTimeMillis();
		while(System.currentTimeMillis() - startTime < allotedTime){
			int nextValue = Integer.MIN_VALUE;
			Set<HyperArc> nextSolution = null;
			Neighbors n = new Neighbors(currentSolution, this);
			for(Set<HyperArc> x : n){
				if (value(x) > nextValue){
					nextSolution = x;
					nextValue = value(x);
				}
			}
			if (nextValue <= value(currentSolution)){
				int value =0;
				for(HyperArc arc : currentSolution)
					value += arc.weight;
				System.out.println(value);
				this.minWeight = value;
				return new HyperGraph(currentSolution, sink);
			}
			currentSolution = nextSolution;

		}
		int value =0;
		for(HyperArc arc : currentSolution)
			value += arc.weight;
		System.out.println(value);
		this.minWeight = value;
		return new HyperGraph(currentSolution, sink);
	}

	private static int value(Set<HyperArc> set){
		int value = 0;
		for (HyperArc arc : set)
			value += arc.weight;
		return -value;
	}

	public boolean isSolution() {
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
	
	public Set<HyperArc> startCriticalPath() {

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
		int value = 0;
		for(HyperArc arc : sink.preds)
			value += arc.weight;
		this.minWeight = value;
		return sink.preds;
	}

}