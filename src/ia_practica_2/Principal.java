package ia_practica_2;

import IAStuff.AlgorithmManager;
import IAStuff.IASolution;
import MazeStuff.Maze;
import MazeStuff.MazeManager;

public class Principal {

	public static void main(String[] args) throws Exception {

		Maze maze = new MazeManager().createMaze(32, 0.3);
		
		System.out.println(maze);
		AlgorithmManager algorithmManager = new AlgorithmManager();
		
		IASolution solution4 = algorithmManager.solveByDeepSearch(maze, 100000000);
		System.out.println(solution4.getAlgorithm()+ " - Tiempo: " + (solution4.getDuration() / 1000000000.0) + " segundos.");
		solution4.getSolution().printPath();
		maze.printWithSolution(solution4);
		solution4=null;
		
		
		IASolution solution2 = algorithmManager.solveByAStarSearch(maze, 100000000, "manhatanUP");
		System.out.println(solution2.getAlgorithm()+ " - Tiempo: " + (solution2.getDuration() / 1000000000.0) + " segundos.");
		solution2.getSolution().printPathWithCostAndHeuristic();
		maze.printWithSolution(solution2);
		solution2=null;
		
		IASolution solution3 = algorithmManager.solveByAStarSearch(maze, 100000000, "euclidianDistanceUpRight");
		System.out.println(solution3.getAlgorithm()+ " - Tiempo: " + (solution3.getDuration() / 1000000000.0) + " segundos.");
		solution3.getSolution().printPathWithCostAndHeuristic();
		maze.printWithSolution(solution3);
		solution3=null;
		
		IASolution solution = algorithmManager.solveBySimpleWidthSearch(maze, 100000000);
		System.out.println(solution.getAlgorithm()+ " - Tiempo: " + (solution.getDuration() / 1000000000.0) + " segundos.");
		solution.getSolution().printPath();
		maze.printWithSolution(solution);
		solution=null;

	}

}
