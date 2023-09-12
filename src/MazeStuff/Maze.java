package MazeStuff;

import IAStuff.IASolution;
import IAStuff.TreeNode;

public class Maze {
	private double fillFactor;
	private boolean mazeArray[][];

	// Todos los mazes para este caso serán cuadrados
	public Maze(boolean[][] mazeArray, double fillFactor) {
		this.mazeArray = mazeArray;
		this.fillFactor = fillFactor;
	}

	public boolean isWall(int i, int j) {
		// Cualquier valor fuera de las dimensiones de la matriz la consideraremos
		// pared.
		if (i < 0 || i >= this.mazeArray.length)
			return true;
		if (j < 0 || j >= this.mazeArray[0].length)
			return true;
		return this.mazeArray[i][j];
	}

	public int getNumberOfWalls() {
		int mazeDim = this.mazeArray.length - 2;
		return (int) Math.round(Math.pow(mazeDim, 2) * this.fillFactor);
	}

	public double getFillFactor() {
		return this.fillFactor;
	}

	public int getDimension() {
		return this.mazeArray.length - 2;
	}

	public void printWithSolution(IASolution solution) {
		for (int i = 0; i < this.mazeArray.length; i++) {
			for (int j = 0; j < this.mazeArray.length; j++) {
				if (coordCheckInPath(i, j, solution)) {
					System.out.print('*');
				} else if (this.mazeArray[i][j]) {
					System.out.print(Constants.WHITE_SQUARE);
				} else {
					System.out.print(' ');
				}
			}
			System.out.print('\n');

		}
	}

	private boolean coordCheckInPath(int i, int j, IASolution solution) {
		TreeNode node = solution.getSolution();
		while (node != null) {
			if (node.i == i && node.j == j)
				return true;
			node = node.father;
		}
		return false;
	}

	@Override
	public String toString() {

		String ret = "";
		ret += "Laberinto - Dimensión(" + getDimension() + ") - Factor(" + this.fillFactor + ") - Paredes("
				+ getNumberOfWalls() + ")\n";

		int dim = this.mazeArray.length;

		// ret+=Constants.ANGLE_UP_LEFT;
		/*
		 * for (int i = 0; i < dim; i++) { ret+=Constants.HORIZONTAL_LINE; }
		 */
		// ret+=Constants.ANGLE_UP_RIGHT + "\n";

		for (int i = 0; i < dim; i++) {
			// ret+=Constants.VERTICAL_LINE;
			for (int j = 0; j < dim; j++) {
				ret += this.mazeArray[i][j] ? Constants.BLACK_SQUARE : '*';
			}
			// ret+=Constants.VERTICAL_LINE + "\n";
			ret += "\n";

		}

		// ret+=Constants.ANGLE_DOWN_LEFT;
		/*
		 * for (int i = 0; i < dim; i++) { ret+=Constants.HORIZONTAL_LINE; }
		 */
		// ret+=Constants.ANGLE_DOWN_RIGHT+ "\n";
		ret += "\n";

		return ret;
	}
}
