package tpe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class HyperGraph {
	
	private Map<String, HyperArc> arcs;
	private Map<String, Node> nodes;
	
	public static void main(String args[]){
		HyperGraph hg = new HyperGraph("test.hg");
		HyperGraph criticalPath = hg.criticalPath();
		criticalPath.graphToGraphviz("criticalPath");
		criticalPath.graphToFile("criticalPath");
	}
	
	public HyperGraph(Map<String, Node> nodes, Map<String, HyperArc> arcs){
		this.nodes =  nodes;
		this.arcs =  arcs;
	}
	
	public HyperGraph(String file){
		try{
			Scanner sc = new Scanner(new File(file));
			sc.useDelimiter("<|>");

			nodes = new HashMap<String, Node>();
			arcs = new HashMap<String, HyperArc>();
			
			while (sc.hasNext()){
				
				String name = sc.next();
				sc.next();
				int weight = sc.nextInt();
				sc.next();
				HyperArc arc = new HyperArc(weight, name);
				arcs.put(name, arc);
				
				int heads = sc.nextInt();
				sc.next();
				for (int i = 0; i < heads; i++){
					String nodeName = sc.next();
					sc.next();
					Node node;
					if (!nodes.containsKey(nodeName))
						node = new Node(nodeName);
					else
						node = nodes.get(nodeName);
					nodes.put(nodeName, node);
					arc.addHead(node);
				}
				
				int tails = sc.nextInt();
				sc.next();
				for (int j = 0; j < tails; j++){
					String nodeName = sc.next();
					sc.next();
					Node node;
					if (!nodes.containsKey(nodeName))
						node = new Node(nodeName);
					else
						node = nodes.get(nodeName);
					node.addArc(arc);
					nodes.put(nodeName, node);
					arc.addTail(node);
				}
			}
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	private void graphToGraphviz(String fileName){
		try{	
			
			File file = new File(fileName + ".dot");
			if (!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write("digraph " + fileName + " {\n");
			
			for (Node node : nodes.values()){
				for (HyperArc arc : node.arcs){
					bw.write(node.name + " -> " + arc.name + ";\n");
				}
			}
			
			for (HyperArc arc : arcs.values()){
				bw.write(arc.name + "[shape=box, label=\"" + arc.name + "(" + arc.weight + ")\"]" + ";\n");
				for (Node node : arc.heads){
					bw.write(arc.name + " -> " + node.name + ";\n");
				}
			}
			
			bw.write("}");
			bw.close();
				
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
		
	}
	
	private void graphToFile(String fileName){
		try{	
			
			File file = new File(fileName + ".hg");
			if (!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(HyperArc arc : this.arcs.values()){
				bw.write("<" + arc.name + ">");
				bw.write(" <" + arc.weight + ">");
				bw.write(" <" + arc.heads.size() + ">");
				for(Node head : arc.heads)
					bw.write(" <" + head.name + ">");
				bw.write(" <" + arc.tails.size() + ">");
				for(Node tail : arc.tails)
					bw.write(" <" + tail.name + ">");
				bw.write("\n");
			}
			
			bw.close();
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public HyperGraph criticalPath(){
		
		Queue<Node> q = new LinkedList<Node>();
		
		//graph.clearMarks();
		//Hardcodeado
		Node source = nodes.get("A");
		Node sink = nodes.get("K");
		
		
		//Recorre, asigna pesos y antecesores
		source.weight = 0;
		q.offer(source);
		
		while(!q.isEmpty()){
			Node curr = q.poll();
			for(HyperArc arc: curr.arcs){
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
									for(HyperArc forward: head.arcs)
										forward.counter--;									
							}
							head.weight = upTo + arc.weight;
							for(HyperArc pred : arc.preds)
								head.preds.add(pred);
							head.preds.add(arc);
						}
					}
				}
			}
		}
		
		//Genera un Subgrafol
		
		Map<String, HyperArc> subArcs = new HashMap<String, HyperArc>();
		Map<String, Node> subNodes = new HashMap<String, Node>();
		
		for(HyperArc pred : sink.preds)
			subArcs.put(pred.name, pred);
		for(HyperArc arc : subArcs.values())
			for(Node tail : arc.tails){
				Node aux = new Node(tail.name, tail.arcs, tail.preds);
				for(HyperArc auxArc : tail.arcs)
					if(!subArcs.containsKey(auxArc.name))
						aux.removeArc(auxArc);
				subNodes.put(aux.name, aux);
			}
		
		return new HyperGraph(subNodes, subArcs);
	}

}

