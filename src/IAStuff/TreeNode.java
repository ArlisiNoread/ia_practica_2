package IAStuff;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TreeNode implements Comparable<TreeNode> {
	public TreeNode father;
	public int i;
	public int j;
	TreeNode up, right, down, left;
	public int cost;
	public double heuristic;

	public TreeNode() {
		super();
	}
	
	/*
	 * Constructor con opción para agregar el padre directamente
	 * */
	public TreeNode(TreeNode father) {
		this.father = father;
	}
	
	/*
	 * Regresa un true si las coordenadas (i, j)
	 * mandadas como parámetros son iguales a las 
	 * del nodo padre de este nodo, en caso contrario
	 * regresa false. 
	 * */
	public boolean isThisCoordFather(int i, int j) {
		if(this.father == null) return false;
		return (this.father.i == i && this.father.j == j);  
	}



	public TreeNode setCoords(int i, int j) {
		this.i = i;
		this.j = j;
		return this;
	}

	public TreeNode setCoords(int i, int j, int cost, double heuristic) {
		this.i = i;
		this.j = j;
		this.cost = cost;
		this.heuristic = heuristic;
		return this;
	}
	
	/*
	 * Regresa la suma del costo acumulador más la heurística de este nodo.
	 * */
	public double getCostPlusHeuristic() {
		return this.heuristic + this.cost;
	}

	/*
	 * Regresa un caracter que indica la dirección tomada por el nodo
	 * anterior para llegar a este nodo donde:
	 * 	'x' : si es raiz,
	 * 	'u' : si es arriba,
	 *  'r' : si es derecha,
	 *  'l' : si es izquierda,
	 *  '0' : en teoría nunca debe enviar este caracter.
	 * */
	public char getDirectionToThis() {
		if (father == null)
			return 'x';
		if (father.up == this)
			return 'u';
		if (father.right == this)
			return 'r';
		if (father.down == this)
			return 'd';
		if (father.left == this)
			return 'l';
		return '0';
	}
	
	/*
	 * Imprime el path desde el nodo raíz hasta este nodo
	 * en una presentación bonita.
	 * */
	public void printPath() {
		ArrayList<TreeNode> list = new ArrayList<TreeNode>();

		TreeNode actualNode = this;
		while (actualNode != null) {
			list.add(actualNode);
			actualNode = actualNode.father;
		}
		
		System.out.println("Número de acciones: " + list.size());
		System.out.println("Tipo de acción: (x = raíz)-(u = arriba)-(r = derecha)-(d = abajo)-(l = izquierda)");
		System.out.println("Interpretación: ->(tipo_acción):[coord_i, coord_j]");

		int breakCnt = 0;
		for (int x = list.size() - 1; x >= 0; x--) {
			if(breakCnt >= 5) {
				System.out.print("...\n");
				breakCnt = 0;
			}
			actualNode = list.get(x);
			System.out.print("->(" + actualNode.getDirectionToThis() + "):[" + actualNode.i + "," + actualNode.j + "]");
			breakCnt++;
		}
		System.out.print("\n");
	}

	/*
	 * Imprime el path desde el nodo raíz hasta este nodo
	 * en una presentación bonita donde se incluye el costo acumulado
	 * y la heurística.
	 * */
	public void printPathWithCostAndHeuristic() {
		ArrayList<TreeNode> list = new ArrayList<TreeNode>();

		TreeNode actualNode = this;
		while (actualNode != null) {
			list.add(actualNode);
			actualNode = actualNode.father;
		}
		
		System.out.println("Número de acciones: " + list.size());
		System.out.println("Tipo de acción: (x = raíz)-(u = arriba)-(r = derecha)-(d = abajo)-(l = izquierda)");
		System.out.println("Interpretación: ->(tipo_acción, costo_acumulado, heurística):[coord_i, coord_j]");

		int breakCnt = 0;
		for (int x = list.size() - 1; x >= 0; x--) {
			if(breakCnt >= 4) {
				System.out.print("...\n");
				breakCnt = 0;
			}
			actualNode = list.get(x);
			System.out.print("->(" + actualNode.getDirectionToThis() + "," + actualNode.cost + "," + new DecimalFormat("#.####").format(actualNode.heuristic) + "):["
					+ actualNode.i + "," + actualNode.j + "]");
			breakCnt++;
		}
		System.out.print("\n");
	}

	/*
	 * Override del método compareTo() de la interfáz Comparable.
	 * Nos permite implementar la lista de prioridad rápidamente.
	 * */
	@Override
	public int compareTo(TreeNode node) {
		if (this.getCostPlusHeuristic() == node.getCostPlusHeuristic()) {
			return 0;
		} else if (this.getCostPlusHeuristic() > node.getCostPlusHeuristic()) {
			return 1;
		} else {
			return -1;
		}

	}

}
