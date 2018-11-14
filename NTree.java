import java.util.*;

public class NTree<E> {

	protected class Node {
		E data;
		Node parent;
		List<Node> children;

		protected Node(E data) {
			this.data = data;
			this.children = new ArrayList<Node>();
		}

		protected void addChild(Node c) { children.add(c); }
		public boolean equals(Node rhs) {
			return this.data.equals(rhs.data);
		}
	}

	protected Node root;

	public NTree() {}

	public NTree(List<E> values, List<Integer> parents) throws Exception {
		if (values.size() != parents.size()) throw new Exception();
		Map<E, Node> m = new TreeMap<>();
		for (int i = 0; i < values.size(); i++) {
			Node nd = new Node(values.get(i));
			m.put(values.get(i), nd);
			if (parents.get(i) >= 0) {		// -1 signals root
				nd.parent = m.get(values.get(parents.get(i)));
				nd.parent.addChild(nd);
			}
			else root = nd;
		} 
	}

	public boolean equals(NTree<E> rhs) {
		return equals(root, rhs.root);
	}

	protected boolean equals(Node lhs, Node rhs) {
		if (lhs == null || rhs == null) return lhs == rhs;
		if (!lhs.equals(rhs) || lhs.parent != rhs.parent) return false;
		for (int i = 0; i < lhs.children.size(); i++) {
			if (!equals(lhs.children.get(i), rhs.children.get(i))) return false;
		}
		return true;
	}

	public void serialize(String fname) {}

	public void deserialize(String fname) {}

	public static void main(String [] args) {
		try {
		List<String> food = Arrays.asList("Food", "Plant", "Animal", "Roots", "Leaves", "Fruits", "Fish", "Mammals", "Birds", "Potatoes", "Carrots", "Lettuce", "Cabbage", "Apples", "Pears", "Plums", "Oranges", "Salmon", "Tuna", "Beef", "Lamb", "Chicken", "Duck", "Wild", "Farm", "GrannySmith", "Gala");
		List<Integer> foodparents = Arrays.asList(-1, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 4, 4, 5, 5, 5, 5, 6, 6, 7, 7, 8, 8, 17, 17, 13, 13);
		NTree<String> foodtree = new NTree(food, foodparents);

		foodtree.serialize("foodtree.out");
		NTree<String> foodtree2 = new NTree<>();
		foodtree2.deserialize("foodtree.out");

		System.out.println(foodtree.equals(foodtree2));

		List<Integer> intvalues = Arrays.asList(9, 6, 5, 4, 2, 10, 7, 1, 3, 8, 11, 12, 13, 14);
		List<Integer> intparents = Arrays.asList( -1, 0, 1, 1, 1, 2, 2, 2, 3, 3, 8, 8, 8, 8);
		NTree<Integer> inttree = new NTree<>(intvalues, intparents);

		NTree<Integer> inttree2 = new NTree<>();
		inttree.serialize("inttree.out");
		inttree2.deserialize("inttree.out");
		
		System.out.println(inttree.equals(inttree2));
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
