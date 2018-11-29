import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.IOException;

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

	public void serialize(String fname) {
		FileWriter file = new FileWriter(new File(fname), true);

		Queue<Node> q = new LinkedList<>();
		q.offer(root);

		while (!q.isEmpty()) {
			// fetch node in queue
			Node node = q.poll();
			Node tnd = node;
			// use tracker node to print node and its parents if it has any
			file.print(tnd.data + " ");
			while(tnd.parent != null) {
				tnd = tnd.parent;
				file.print(tnd.data + " ");
			}
			System.out.println();
			for (int i = 0; i < node.children.size(); i++) {
				q.offer(node.children.get(i));
			}

		}
		file.close();
	}

	public void deserialize(String fname) throws IOException{
		Scanner input = new Scanner(new File(fname));

		// grab first string and set it as root
		String root = input.nextLine();


		// create name and number lists
		List<E> names = new ArrayList<>();
		names.add(root);
		List<Integer> parents = new ArrayList<>();
		parents.add(-1);

		String currentLine;
		E lastString, parent;
		String[] lineArr;
		int parentIndex;
		int nodeNumber = 1;

		// java version of objects
		// this will contain strings with their corresponding index
		Map<E, Integer> map = new TreeMap<>();
		map.put(root, 0);

		// read whole file
		while (input.hasNext()) {
			// read line by line
			currentLine = input.nextLine();
			lineArr = currentLine.split(" ");
			
			// add last string of input array to name list
			lastString = lineArr[lineArr.length-1];
			names.add(lastString);
			// add new nodes parent index to parent list
			
			// so what is new nodes parent
			parent = lineArr[lineArr.length -2];
			parentIndex = map.get(parent);

			// finally add to parent list
			// System.out.printf("parent %s\tparentIndex %d\t", parent, parentIndex);
			parents.add(parentIndex);

			// add new node to parent map
			// System.out.printf("lastString %s\tnodeNumber %d\n", lastString, nodeNumber);
			map.put(lastString, nodeNumber++);

		}
		input.close();

		if (names.size() != parents.size()) throw new Exception();
		Map<E, Node> m = new TreeMap<>();
		for (int i = 0; i < names.size(); i++) {
			Node nd = new Node(names.get(i));
			m.put(names.get(i), nd);
			if (parents.get(i) >= 0) {		// -1 signals root
				nd.parent = m.get(names.get(parents.get(i)));
				nd.parent.addChild(nd);
			}
			else root = nd;
		} 

	}

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

		NTree<Integer> intt.;;;;;;;;;;;;;;;;;;;;;;;; ree2 = new NTree<>();
		inttree.serialize("inttree.out");
		inttree2.deserialize("inttree.out");
		
		System.out.println(inttree.equals(inttree2));
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
