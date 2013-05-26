package EDA;

public class CriticalPath {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Ingrese un archivo");
			return;
		}

		HyperGraph graph = new HyperGraph(args[0]);

		if (args[1] == "exact" || args[1] == null) {
			exactAlg(graph);
			return;
		}

		if (args[1] == "approx" && args[2] != null) {
			int time = Integer.getInteger(args[2]);
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
