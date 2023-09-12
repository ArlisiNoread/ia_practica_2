package IAStuff;

import java.util.ArrayList;

public class TreeNode {
	public TreeNode father;
	public int i;
	public int j;
	TreeNode up, right, down, left;

	public TreeNode() {
		
	}
	
	public TreeNode(TreeNode father) {
		this.father = father;
	}
	
	public TreeNode setCoords(int i, int j) {
		this.i = i;
		this.j = j;
		return this;
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

		for (int x = list.size() - 1; x >= 0; x--) {
			actualNode = list.get(x);
			System.out.print("->(" + actualNode.getDirectionToThis() + "):[" + actualNode.i + "," + actualNode.j + "]");
		}
		System.out.print("\n");
	}

}
