package IAStuff;

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
	
	public boolean isThisCoordFather(int i, int j) {
		if(this.father == null) return false;
		return (this.father.i == i && this.father.j == j);  
	}

	public TreeNode(TreeNode father) {
		this.father = father;
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

	public double getCostPlusHeuristic() {
		return this.heuristic + this.cost;
	}

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

	public void printPath() {
		ArrayList<TreeNode> list = new ArrayList<TreeNode>();

		TreeNode actualNode = this;
		while (actualNode != null) {
			list.add(actualNode);
			actualNode = actualNode.father;
		}
		
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

	public void printPathWithCostAndHeuristic() {
		ArrayList<TreeNode> list = new ArrayList<TreeNode>();

		TreeNode actualNode = this;
		while (actualNode != null) {
			list.add(actualNode);
			actualNode = actualNode.father;
		}
		
		int breakCnt = 0;
		for (int x = list.size() - 1; x >= 0; x--) {
			if(breakCnt >= 5) {
				System.out.print("...\n");
				breakCnt = 0;
			}
			actualNode = list.get(x);
			System.out.print("->(" + actualNode.getDirectionToThis() + "," + actualNode.cost + "," + actualNode.heuristic + "):["
					+ actualNode.i + "," + actualNode.j + "]");
			breakCnt++;
		}
		System.out.print("\n");
	}

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
