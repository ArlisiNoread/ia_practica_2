package MazeStuff;

import java.util.ArrayList;
import java.util.Random;

public class MazeManager {

	public Maze createMaze(int dimension, double fillFactor) throws Exception {
		if (dimension <= 0)
			throw new Exception("Dimensión del laberinto debe ser mayor a 0");
		if (fillFactor < 0 || fillFactor > 1)
			throw new Exception("El factor de llenado debe ser mayor o igual a 0 y a lo más 1");
		
		// Determino el número de obstáculos del factor de llenado
		int numberOfObstacles = (int) Math.round(fillFactor * Math.pow(dimension, 2));

		// La matriz del laberinto tendrá 4 pasillos libres en las orillas
		int dimensionWithHalls = dimension + 2;
		boolean mazeArray[][] = new boolean[dimensionWithHalls][dimensionWithHalls];

		ArrayList<Coord> coordsList = new ArrayList<Coord>();

		// Creamos array con blancos con una dimensión cuadrada.
		for (int i = 0; i < dimensionWithHalls; i++) {
			for (int j = 0; j < dimensionWithHalls; j++) {
				mazeArray[i][j] = false;

				if (i != 0 && i != dimensionWithHalls - 1 && j != 0 && j != dimensionWithHalls - 1) {
					coordsList.add(new Coord(i, j));
				}
			}
		}

		Random ran = new Random();
		for (int x = 0; x < numberOfObstacles; x++) {
			int max = coordsList.size();
			int randomCoord = ran.nextInt(max);
			Coord coord = coordsList.get(randomCoord);
			coordsList.remove(randomCoord);
			mazeArray[coord.i][coord.j] = true;

		}
		return new Maze(mazeArray, fillFactor);
	}
}

class Coord {
	public int i, j;

	public Coord(int i, int j) {
		this.i = i;
		this.j = j;
	}
}