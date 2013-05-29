package EDA;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class HyperGraph {
	
	private Map<String, HyperArc> arcs;
	private Map<String, Node> nodes;
	private Node source;
	private Node sink;
	
	public static void main(String args[]){
		HyperGraph hg = new HyperGraph("B.hg");
		hg.graphToDot("A");
		HyperGraph criticalPath = hg.criticalPath();
		hg.criticalToDot("highlightedA", criticalPath);
		criticalPath.graphToDot("criticalPathA");
		criticalPath.graphToHg("criticalPath");
	}
	
	public HyperGraph(Collection<Node> nodes, Collection<HyperArc> arcs){
		this.nodes = new HashMap<String, Node>();
		this.arcs = new HashMap<String, HyperArc>();
		for(Node node : nodes)
			this.nodes.put(node.name, node);
		for(HyperArc arc : arcs)
			this.arcs.put(arc.name, arc);
	}
	
	public HyperGraph(String file){
		try{
			Scanner sc = new Scanner(new File(file));

			nodes = new HashMap<String, Node>();
			arcs = new HashMap<String, HyperArc>();
			
			String aux;
			do
				aux = sc.nextLine();
			while(aux.startsWith("#"));
			source = new Node(aux);
			nodes.put(aux, source);			
			do
				aux = sc.nextLine();
			while(aux.startsWith("#"));
			sink = new Node(aux);
			nodes.put(aux, sink);
			
			while (sc.hasNext()){
				String name = sc.next();
				if(name.startsWith("#")){
					sc.nextLine();
					continue;
				}
				int weight = sc.nextInt();
				HyperArc arc = new HyperArc(weight, name);
				arcs.put(name, arc);
				
				int heads = sc.nextInt();
				for (int i = 0; i < heads; i++){
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
				for (int j = 0; j < tails; j++){
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
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	private void criticalToDot(String fileName, HyperGraph subgraph){
		try{	
			
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
				
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}	
	}
	
	private void graphToDot(String fileName){
		try{	
			
			File file = new File(fileName + ".dot");
			if (!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write("digraph " + fileName + " {\n");
			
			dataToDot(bw);
			
			bw.write("}");
			bw.close();
				
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}		
	}

	private void dataToDot(BufferedWriter bw){
		try{	
			for (Node node : nodes.values()){
				for (HyperArc arc : node.heads){
					bw.write("\"" + node.name + "\"" + " -> " + "\"" + arc.name + "\"" + ";\n");
				}
			}
			
			for (HyperArc arc : arcs.values()){
				bw.write("\"" + arc.name + "\"" + "[shape=box, label=\"" + arc.name + "(" + arc.weight + ")\"]" + ";\n");
				for (Node node : arc.heads){
					bw.write("\"" + arc.name + "\"" + " -> " + "\"" + node.name + "\"" + ";\n");
				}
			}
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	private void graphToHg(String fileName){
		try{	
			
			File file = new File(fileName + ".hg");
			if (!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(HyperArc arc : this.arcs.values()){
				bw.write(arc.name);
				bw.write(" " + arc.weight);
				bw.write(" " + arc.heads.size());
				for(Node head : arc.heads)
					bw.write(" " + head.name);
				bw.write(" " + arc.tails.size());
				for(Node tail : arc.tails)
					bw.write(" " + tail.name);
				bw.write("\n");
			}
			
			bw.close();
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}

	private void highlightDot(BufferedWriter bw, HyperGraph subgraph){
		try{
			for(HyperArc arc : subgraph.arcs.values())
				bw.write("\"" + arc.name + "\"" + "[style=filled, fillcolor=red]" + ";\n");
			for(Node node : subgraph.nodes.values())
				bw.write("\"" + node.name + "\"" + "[style=filled, fillcolor=red]" + ";\n");
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
		
	}
	
	public HyperGraph criticalPath(){
		
		Queue<Node> q = new LinkedList<Node>();
		
		//Recorre, asigna pesos y antecesores
		source.weight = 0;
		q.offer(source);
		
		while(!q.isEmpty()){
			Node curr = q.poll();
			for(HyperArc arc: curr.heads){
				arc.counter++;
				if(arc.counter == arc.tails.size()){
					
					int upTo = 0;
					
					for(Node tail: arc.tails)
						for(HyperArc pred : tail.preds)
							if(!arc.preds.contains(pred)){
								upTo += pred.weight;
								arc.preds.add(pred);
							}
						
					for(Node head : arc.heads){
						if(head.weight > upTo + arc.weight){
							if(!q.contains(head)){
								q.offer(head);
								if(head.weight < Integer.MAX_VALUE)
									for(HyperArc forward: head.heads)
										forward.counter--;									
							}
							head.weight = upTo + arc.weight;
							head.preds.clear();
							for(HyperArc pred : arc.preds)
								head.preds.add(pred);
							head.preds.add(arc);
						}
					}
				}
			}
		}
		
		//Genera un Subgrafol
		

		System.out.println(sink.weight);
		
		Map<String, HyperArc> subArcs = new HashMap<String, HyperArc>();
		Map<String, Node> subNodes = new HashMap<String, Node>();
		
		subNodes.put(sink.name, sink);
		for(HyperArc pred : sink.preds)
			subArcs.put(pred.name, pred);
		for(HyperArc arc : subArcs.values())
			for(Node tail : arc.tails){
				Node aux = new Node(tail.name, tail.heads, tail.preds);
				for(HyperArc auxArc : tail.heads)
					if(!subArcs.containsKey(auxArc.name))
						aux.removeHead(auxArc);
				subNodes.put(aux.name, aux);
			}
		
		return new HyperGraph(subNodes.values(), subArcs.values());
	}

	
	
}


