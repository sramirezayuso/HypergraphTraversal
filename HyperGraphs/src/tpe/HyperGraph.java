package tpe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HyperGraph {
	
	private Map<String, HyperArc> arcs;
	private Map<String, Node> nodes;
	
	public static void main(String args[]){
		HyperGraph hg = new HyperGraph("test.hg");
		hg.graphToGraphviz("test");
	}
	
	
	private HyperGraph(String file){
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
					arc.addNode(node);
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
				}
			}
			
			for (HyperArc arc: arcs.values()){
				System.out.println(arc);
			}
			
			for (Node node: nodes.values()){
				System.out.println(node);
			}
			
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	private void graphToGraphviz(String fileName){
		try{
			
			File file = new File(fileName + ".dot");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write("digraph " + fileName + " {\n");
			
			for (Node node : nodes.values()){
				for (HyperArc arc : node.tasks){
					bw.write(arc.name + " -> " + node.name + ";\n");
				}
			}
			
			for (HyperArc arc : arcs.values()){
				bw.write(arc.name + "[label=\"" + arc.name + "(" + arc.weight + ")\"]" + ";\n");
				for (Node node : arc.conditions){
					bw.write(node.name + " -> " + arc.name + ";\n");
				}
			}
			
			bw.write("}");
			bw.close();
			
			
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
		
	}

}
