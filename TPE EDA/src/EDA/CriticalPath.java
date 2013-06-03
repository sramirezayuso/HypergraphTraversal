package EDA;


public class CriticalPath {
	public static void main(String[] args) {
		if (args.length <= 1) {
			System.out.println("Ingrese un archivo y un comando.");
			return;
		}

		HyperGraph graph = new HyperGraph(args[0]);

		if (args[1].equals("exact") && args.length ==  2 ) {
			exactAlg(graph);
			return;
		}

		if (args[1].equals("approx") && args.length == 3) {
			int time = Integer.parseInt(args[2]);
			approxAlg(graph, time);
			return;
		}
		System.out.println("Error en el comando");
		return;
	}

	private static void approxAlg(HyperGraph graph, int time) {
		System.out.println("Algoritmo aproximado en: " + time + "segundos");
		return;
	}

	private static void exactAlg(HyperGraph graph) {
		System.out.println("Algoritmo exacto");
		return;
	}
}
