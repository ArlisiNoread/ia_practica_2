package IAStuff;

//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

import MazeStuff.Maze;

public class AlgorithmManager {

	public IASolution solveBySimpleWidthSearch(Maze maze, int maxTimeForSolution) {
		Execution search = new WidthSearch(maxTimeForSolution, maze);
		search.start();
		try {
			search.join();
			if (search.solution != null) {
				System.out.println("Terminó hilo bien");
				return search.solution;
			} else {
				System.out.println("Terminó hilo Mal");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();

		}
		return null;
	}

	public IASolution solveByAStarSearch(Maze maze, int maxTimeForSolution, String heuristic) {
		Execution search = new AStarSearch(maxTimeForSolution, maze, heuristic);
		search.start();
		try {
			search.join();
			if (search.solution != null) {
				System.out.println("Terminó hilo bien");
				return search.solution;
			} else {
				System.out.println("Terminó hilo Mal");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();

		}
		return null;
	}

	public IASolution solveByDeepSearch(Maze maze, int maxTimeForSolution) {
		Execution search = new DeepSearch(maxTimeForSolution, maze);
		search.start();
		try {
			search.join();
			if (search.solution != null) {
				System.out.println("Terminó hilo bien");
				return search.solution;
			} else {
				System.out.println("Terminó hilo Mal");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();

		}
		return null;
	}

}

class DeepSearch extends Execution {

	public DeepSearch(long maxTimeForSolutionSeconds, Maze maze) {
		super(maxTimeForSolutionSeconds);
		this.maze = maze;
	}

	@Override
	public boolean algorithm() throws Exception {
		int mazeDim = maze.getDimension();
		solution.setAlgorithm("Algoritmo por Profundidad.");
		Tree searchTree = new Tree();
		try {
			searchTree.root = this.generateStartPoint(maze);
		} catch (Exception e) {
			System.out.println("No puedo generar Start Point.");
			e.printStackTrace();
			return false;
		}
		solution.setSearchTree(searchTree);
		TreeNode actualNode = searchTree.root;
		Stack<TreeNode> stack = new Stack<TreeNode>();
		while (actualNode != null) {
			// Evaluamos victoria
			if (actualNode.j == 0 || actualNode.j == mazeDim + 1 || actualNode.i == 0 || actualNode.i == mazeDim + 1) {
				solution.setSolution(actualNode);
				return true;
			}

			// Expandimos Izquierda
			if (!this.maze.isWall(actualNode.i, actualNode.j - 1)
					&& !actualNode.isThisCoordFather(actualNode.i, actualNode.j - 1)) {
				actualNode.left = new TreeNode(actualNode).setCoords(actualNode.i, actualNode.j - 1);
				stack.push(actualNode.left);
			}
			// Expandimos Abajo
			if (!this.maze.isWall(actualNode.i + 1, actualNode.j)
					&& !actualNode.isThisCoordFather(actualNode.i + 1, actualNode.j)) {
				actualNode.down = new TreeNode(actualNode).setCoords(actualNode.i + 1, actualNode.j);
				stack.push(actualNode.down);
			}
			// Expandimos Derecha
			if (!this.maze.isWall(actualNode.i, actualNode.j + 1)
					&& !actualNode.isThisCoordFather(actualNode.i, actualNode.j + 1)) {
				actualNode.right = new TreeNode(actualNode).setCoords(actualNode.i, actualNode.j + 1);
				stack.push(actualNode.right);
			}
			// Expandimos Arriba
			if (!this.maze.isWall(actualNode.i - 1, actualNode.j)
					&& !actualNode.isThisCoordFather(actualNode.i - 1, actualNode.j)) {
				actualNode.up = new TreeNode(actualNode).setCoords(actualNode.i - 1, actualNode.j);
				stack.push(actualNode.up);
			}
			actualNode = stack.pop();
		}
		return false;
	}

}

class AStarSearch extends Execution {
	String heuristic;

	public AStarSearch(long maxTimeForSolutionSeconds, Maze maze, String heuristic) {
		super(maxTimeForSolutionSeconds);
		this.maze = maze;
		this.heuristic = heuristic;
	}

	@Override
	public boolean algorithm() {
		int mazeDim = maze.getDimension();
		solution.setAlgorithm("Algoritmo por A* - Heurística: " + this.heuristic + ".");
		Tree searchTree = new Tree();
		try {
			searchTree.root = this.generateStartPoint(maze);
		} catch (Exception e) {
			System.out.println("No puedo generar Start Point.");
			e.printStackTrace();
			return false;
		}
		searchTree.root.cost = 0;
		searchTree.root.heuristic = generateHeuristic(searchTree.root.i, searchTree.root.j, mazeDim);
		solution.setSearchTree(searchTree);
		OptimizedStorageForNodes storage = new OptimizedStorageForNodes();
		TreeNode actualNode = searchTree.root;
		while (actualNode != null) {
			// Evaluamos victoria
			if (actualNode.j == 0 || actualNode.j == mazeDim + 1 || actualNode.i == 0 || actualNode.i == mazeDim + 1) {
				solution.setSolution(actualNode);

				return true;
			}
			// Expandimos
			if (!this.maze.isWall(actualNode.i - 1, actualNode.j)
					&& !actualNode.isThisCoordFather(actualNode.i - 1, actualNode.j)) {
				actualNode.up = new TreeNode(actualNode).setCoords(actualNode.i - 1, actualNode.j, actualNode.cost + 1,
						generateHeuristic(actualNode.i - 1, actualNode.j, mazeDim));
				storage.add(actualNode.up);
			}
			if (!this.maze.isWall(actualNode.i, actualNode.j + 1)
					&& !actualNode.isThisCoordFather(actualNode.i, actualNode.j + 1)) {
				actualNode.right = new TreeNode(actualNode).setCoords(actualNode.i, actualNode.j + 1,
						actualNode.cost + 1, generateHeuristic(actualNode.i, actualNode.j + 1, mazeDim));
				storage.add(actualNode.right);
			}
			if (!this.maze.isWall(actualNode.i + 1, actualNode.j)
					&& !actualNode.isThisCoordFather(actualNode.i + 1, actualNode.j)) {
				actualNode.down = new TreeNode(actualNode).setCoords(actualNode.i + 1, actualNode.j,
						actualNode.cost + 1, generateHeuristic(actualNode.i + 1, actualNode.j, mazeDim));
				storage.add(actualNode.down);
			}
			if (!this.maze.isWall(actualNode.i, actualNode.j - 1)
					&& !actualNode.isThisCoordFather(actualNode.i, actualNode.j - 1)) {
				actualNode.left = new TreeNode(actualNode).setCoords(actualNode.i, actualNode.j - 1,
						actualNode.cost + 1, generateHeuristic(actualNode.i, actualNode.j - 1, mazeDim));
				storage.add(actualNode.left);
			}

			actualNode = storage.nextLowest();
		}
		return false;
	}

	private double generateHeuristic(int i, int j, int dim) {
		switch (this.heuristic) {
		case "manhatanUP": {
			// Defino Heurística como cantidad de espacios para salir arriba.
			// En teoría sería la dimensión, entonces es la coordenada i + 1....
			// Pero esa heurística no estaría normalizada....
			// Entonces.... puede ser h = (i+1))/dimensión con eso me encargo de que
			// h <= costo... normalizada.
			return (double) (i) / (double) (dim);
		}
		case "euclidianDistanceUpRight": {
			// Igual me encargo de que esté normalizada.
			// En este caso busco la mayor distancia posible para la dimensión NxN
			// De una esquina en diagonal a la otra.
			double maxDistancePosible = Math.sqrt(2 * Math.pow(dim + 2, 2));
			double iCoordFinal = 0;
			double jCoordFinal = dim + 1;
			double distance = Math.sqrt(Math.pow(iCoordFinal - i, 2) + Math.pow(jCoordFinal - j, 2));
			return distance / maxDistancePosible;
		}
		default:
			throw new IllegalArgumentException("Heurística no definida: " + this.heuristic);
		}
	}
}

class OptimizedStorageForNodes {
	PriorityQueue<TreeNode> queue = new PriorityQueue<TreeNode>();

	// ArrayList<TreeNode> storage = new ArrayList<TreeNode>();
	public TreeNode nextLowest() {
		// if (storage.size() == 0)
		// return null;
		// TreeNode lowestNode = storage.get(0);
		// storage.remove(0);
		// return lowestNode;
		return queue.poll();
	}

	public void add(TreeNode node) {
		// storage.add(node);
		// Collections.sort(storage);
		queue.add(node);
	}
}

class WidthSearch extends Execution {

	public WidthSearch(long maxTimeForSolutionSeconds, Maze maze) {
		super(maxTimeForSolutionSeconds);
		this.maze = maze;
	}

	@Override
	public boolean algorithm() throws Exception {
		this.solution.setAlgorithm("Algoritmo por Anchura.");
		this.solution.setSearchTree(new Tree());
		try {
			this.solution.getSearchTree().root = this.generateStartPoint(maze);
		} catch (Exception e) {
			System.out.println("No puedo generar Start Point.");
			e.printStackTrace();
			return false;
		}
		NodesLine nodesLine = new NodesLine();
		TreeNode actualNode = this.solution.getSearchTree().root;
		while (actualNode != null) {
			// Evaluamos victoria
			if (actualNode.j == 0 || actualNode.j == this.maze.getDimension() + 1 || actualNode.i == 0
					|| actualNode.i == this.maze.getDimension() + 1) {
				this.solution.setSolution(actualNode);
				if (this.solution.getSolution() == null)
					System.out.println("Acá da null 2");
				return true;
			}
			if (!this.maze.isWall(actualNode.i - 1, actualNode.j)
					&& !actualNode.isThisCoordFather(actualNode.i - 1, actualNode.j)) {
				actualNode.up = new TreeNode(actualNode).setCoords(actualNode.i - 1, actualNode.j);
				nodesLine.addToLine(actualNode.up);
			}
			if (!this.maze.isWall(actualNode.i, actualNode.j + 1)
					&& !actualNode.isThisCoordFather(actualNode.i, actualNode.j + 1)) {
				actualNode.right = new TreeNode(actualNode).setCoords(actualNode.i, actualNode.j + 1);
				nodesLine.addToLine(actualNode.right);
			}
			if (!this.maze.isWall(actualNode.i + 1, actualNode.j)
					&& !actualNode.isThisCoordFather(actualNode.i + 1, actualNode.j)) {
				actualNode.down = new TreeNode(actualNode).setCoords(actualNode.i + 1, actualNode.j);
				nodesLine.addToLine(actualNode.down);
			}
			if (!this.maze.isWall(actualNode.i, actualNode.j - 1)
					&& !actualNode.isThisCoordFather(actualNode.i, actualNode.j - 1)) {
				actualNode.left = new TreeNode(actualNode).setCoords(actualNode.i, actualNode.j - 1);
				nodesLine.addToLine(actualNode.left);
			}
			actualNode = nodesLine.next();
		}

		if (actualNode == null && this.solution.getSolution() == null) {
			System.out.println("Ruta Imposible.");
		}
		return false;
	}

}

abstract class Execution extends Thread {
	Maze maze;
	long maxTimeForSolution; // En nanosegundos
	long startTime;
	IASolution solution = new IASolution();

	abstract public boolean algorithm() throws Exception;

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
		else {
			throw new Exception("Imposible determinar Inicio en este laberinto.");
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try {
			this.startTime = System.nanoTime();
			TimerMax timer = new TimerMax(this);
			// timer.start();
			algorithm();
			// timer.stop();
			long totalTime = System.nanoTime() - this.startTime;
			this.solution.setDuration(totalTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Test si falla algo.");
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
		System.out.println(
				"Running timer - Max : " + (this.executionObject.maxTimeForSolution / 1000000000) + " segundos.");

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
	// ArrayList<TreeNode> line = new ArrayList<TreeNode>();
	// ArrayDeque<TreeNode> queue = new ArrayDeque<TreeNode>();
	LinkedList<TreeNode> list = new LinkedList<TreeNode>();

	public TreeNode next() {
		/*
		 * if (line.size() == 0) return null;
		 */
		// TreeNode ret = this.line.get(0);
		// this.line.remove(0);
		// return ret;
		// return queue.poll();
		return list.poll();
	}

	public void addToLine(TreeNode node) {
		// this.line.add(node);
		list.add(node);
	}
}