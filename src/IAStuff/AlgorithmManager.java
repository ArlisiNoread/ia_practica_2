package IAStuff;

import java.util.ArrayList;

import MazeStuff.Maze;

public class AlgorithmManager {

	public IASolution solveBySimpleWidthSearch(Maze maze, int maxTimeForSolution) {
		Execution search = new WidthSearch(maxTimeForSolution, maze);
		search.start();
		try {
			search.join();
			if (search.solution != null) {
				System.out.println("Terminó bien");
				return search.solution;
			} else {
				System.out.println("Terminó Mal");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();

		}
		return null;
	}
}

class WidthSearch extends Execution {
	Maze maze;

	public WidthSearch(long maxTimeForSolutionSeconds, Maze maze) {
		super(maxTimeForSolutionSeconds);
		this.maze = maze;
	}

	@Override
	public IASolution algorithm() throws Exception {
		IASolution solution = new IASolution();
		solution.setAlgorithm("Algoritmo por Anchura");
		Tree searchTree = new Tree();
		searchTree.root = this.generateStartPoint(maze);
		solution.setSearchTree(searchTree);

		NodesLine nodesLine = new NodesLine();
		TreeNode actualNode = searchTree.root;
		
		while (actualNode != null) {

			// Evaluamos victoria
			if (actualNode.j == 0 || actualNode.j == this.maze.getDimension() + 1 || actualNode.i == 0
					|| actualNode.i == this.maze.getDimension() + 1) {
				solution.setSolution(actualNode);

				return solution;
			}

			if (!this.maze.isWall(actualNode.i - 1, actualNode.j)) {
				actualNode.up = new TreeNode(actualNode).setCoords(actualNode.i - 1, actualNode.j);
				nodesLine.addToLine(actualNode.up);
			}
			if (!this.maze.isWall(actualNode.i, actualNode.j + 1)) {
				actualNode.right = new TreeNode(actualNode).setCoords(actualNode.i, actualNode.j + 1);
				nodesLine.addToLine(actualNode.right);
			}
			if (!this.maze.isWall(actualNode.i + 1, actualNode.j)) {
				actualNode.down = new TreeNode(actualNode).setCoords(actualNode.i + 1, actualNode.j);
				nodesLine.addToLine(actualNode.down);
			}
			if (!this.maze.isWall(actualNode.i, actualNode.j - 1)) {
				actualNode.left = new TreeNode(actualNode).setCoords(actualNode.i, actualNode.j - 1);
				nodesLine.addToLine(actualNode.left);
			}

			actualNode = nodesLine.next();
		}

		return solution;
	}

}

abstract class Execution extends Thread {
	long maxTimeForSolution; // En nanosegundos
	long startTime;
	IASolution solution;

	abstract public IASolution algorithm() throws Exception;

	public Execution(long maxTimeForSolutionSeconds) {
		this.maxTimeForSolution = maxTimeForSolutionSeconds * 1000000000;
	}

	public TreeNode generateStartPoint(Maze maze) throws Exception {
		int dim = maze.getDimension();
		int startPoint = dim / 2;

		TreeNode ret = new TreeNode();

		if (!maze.isWall(startPoint, startPoint))
			return ret.setCoords(startPoint, startPoint);
		if (!maze.isWall(startPoint - 1, startPoint))
			return ret.setCoords(startPoint - 1, startPoint);
		if (!maze.isWall(startPoint - 1, startPoint + 1))
			return ret.setCoords(startPoint - 1, startPoint + 1);
		if (!maze.isWall(startPoint, startPoint + 1))
			return ret.setCoords(startPoint, startPoint + 1);
		if (!maze.isWall(startPoint + 1, startPoint + 1))
			return ret.setCoords(startPoint + 1, startPoint + 1);
		if (!maze.isWall(startPoint + 1, startPoint))
			return ret.setCoords(startPoint + 1, startPoint);
		if (!maze.isWall(startPoint + 1, startPoint - 1))
			return ret.setCoords(startPoint + 1, startPoint - 1);
		if (!maze.isWall(startPoint, startPoint - 1))
			return ret.setCoords(startPoint, startPoint - 1);
		if (!maze.isWall(startPoint - 1, startPoint - 1))
			return ret.setCoords(startPoint - 1, startPoint - 1);

		throw new Exception("Imposible determinar Inicio en este laberinto.");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try {
			this.startTime = System.nanoTime();
			TimerMax timer = new TimerMax(this);
			timer.start();
			IASolution solution;
			solution = algorithm();
			timer.stop();
			long totalTime = System.nanoTime() - this.startTime;
			solution.setDuration(totalTime);
			this.solution = solution;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

class TimerMax extends Thread {
	Execution executionObject;

	public TimerMax(Execution executionObject) {
		this.executionObject = executionObject;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		long startTime = this.executionObject.startTime;
		System.out.println("Running timer - Max : " + (this.executionObject.maxTimeForSolution/1000000000) + " segundos.");

		while (true) {
			long elapsedTime = System.nanoTime() - startTime;
			// System.out.println("" + elapsedTime /1000000000);
			if (elapsedTime >= this.executionObject.maxTimeForSolution) {
				System.out.println("Tiempo Máximo Excedido");
				this.executionObject.stop();
				return;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

class NodesLine {
	ArrayList<TreeNode> line;

	public NodesLine() {
		this.line = new ArrayList<TreeNode>();
	}

	public TreeNode next() {
		if (line.size() == 0)
			return null;
		TreeNode ret = this.line.get(0);
		this.line.remove(0);
		return ret;
	}

	public void addToLine(TreeNode node) {
		this.line.add(node);
	}
}