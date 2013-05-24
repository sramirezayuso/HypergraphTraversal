package tpe;

import java.util.LinkedList;
import java.util.List;


public class HyperArc{
		
		List<Node> conditions;
		int weight;
		boolean visited;
		String name;
		
		public HyperArc(int weight, String name){
			this.conditions = new LinkedList<Node>();
			this.weight = weight;
			this.visited = false;
			this.name = name;
		}
		
		public void addNode(Node node){
			conditions.add(node);
		}

		@Override
		public String toString() {
			String answer =  name + "(" + weight + ")" + " Conditions: ";
			for (Node node: conditions){
				answer = answer + node.name + " ";
			}
			return answer;
		}
		
		
		
	}
