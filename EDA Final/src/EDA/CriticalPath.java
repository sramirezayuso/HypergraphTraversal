package EDA;

import java.io.FileNotFoundException;



public class CriticalPath {
	
	public static void main(String[] args) {
		if (args.length <= 1) {
			System.out.println("Ingrese un archivo y un comando.");
			return;
		}
		try{
		if (args[1].equals("exact") && args.length ==  2 ) {
			exactAlg(args[0].substring(0, args[0].length()-3));
			return;
		}

		if (args[1].equals("approx") && args.length == 3) {
			int time = Integer.parseInt(args[2]);
			approxAlg(args[0].substring(0, args[0].length()-3), time);
			return;
		}
		} catch(FileNotFoundException e){
			System.out.println("El archivo ingresado no existe");
			return;
		}
		System.out.println("Error en el comando");
		return;
	}

	private static void approxAlg(String fileName, int time) throws FileNotFoundException {
		HyperGraph hg = new HyperGraph(fileName);
		HyperGraph criticalPath = hg.hillClimbing(time);
		hg.criticalToDot(fileName, criticalPath);
		hg.graphToDot(fileName);
		criticalPath.graphToHg(fileName);
		System.out.println("El peso minimo encontrado para el algoritmo aproximado es: " + hg.minWeight);
		return;
	}

	private static void exactAlg(String fileName) throws FileNotFoundException {
		HyperGraph hg = new HyperGraph(fileName);
		HyperGraph criticalPath = hg.exactPath();
		hg.criticalToDot(fileName, criticalPath);
		hg.graphToDot(fileName);
		criticalPath.graphToHg(fileName);
		System.out.println("El peso minimo encontrado para el algoritmo exacto es: " + hg.minWeight);
		return;
	}
}
