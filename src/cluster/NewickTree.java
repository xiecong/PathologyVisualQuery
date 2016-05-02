package cluster;

import java.util.Vector;

public class NewickTree {

	public static Node parse(String s) {
		int x = s.lastIndexOf(':');
		return build(s, new Node(Double.parseDouble(s.substring(x + 1))), 0, x);
	}

	// this is the parsing code
	public static Node build(String s, Node parent, int from, int to) {
		if (s.charAt(from) != '(') {
			parent.setName(s.substring(from, to));
			return parent;
		}

		int b = 0; // bracket counter
		int colon = 0; // colon marker
		int x = from; // position marker

		for (int i = from; i < to; i++) {
			char c = s.charAt(i);

			if (c == '(')
				b++;
			else if (c == ')')
				b--;
			else if (c == ':')
				colon = i;

			if (b == 0 || b == 1 && c == ',') {
				parent.addChild(build(
						s,
						new Node(Double.parseDouble(s.substring(colon + 1, i))),
						x + 1, colon));
				x = i;
			}
		}

		return parent;
	}

	// -------------------- end of parsing code ------------------

	public static class Node {
		public int index;
		//private String name = null;
		private double value = 0;

		private Vector<Node> childVec = new Vector<Node>();

		public Node(double d) {
			value = d;
		}

		public void setName(String s) {
			//name = s;
			index = (int) Double.parseDouble(s);
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			toString(this, sb);
			return sb.toString();
		}

		public int numChildren() {
			return childVec.size();
		}

		public void addChild(Node n) {
			childVec.add(n);
		}

		public Node getChild(int i) {
			return (Node) childVec.get(i);
		}

		// toString method provided for testing purposes
		public static void toString(Node n, StringBuffer sb) {
			if (n.numChildren() == 0) {
				sb.append(n.index);
				sb.append(":");
				sb.append(n.value);
			} else {
				sb.append("(");
				toString(n.getChild(0), sb);
				for (int i = 1; i < n.numChildren(); i++) {
					sb.append(",");
					toString(n.getChild(i), sb);
				}
				sb.append("):");
				sb.append(n.value);
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(parse("1:1"));
		System.out.println(parse("((0.0:1.1735,38.0:1.1735):1.87246,(((5.0:1.03283,37.0:1.03283):1.36773,6.0:1.36773):1.41731,(7.0:1.31801,11.0:1.31801):1.41731):1.87246):2.05499"));
	}
}