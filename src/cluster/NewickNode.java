package cluster;

import java.util.ArrayList;

import database.ShapePolygon;

public class NewickNode {
	public int index;
	public double value = 0;
	private ArrayList<NewickNode> childVec = new ArrayList<NewickNode>();
	public ArrayList<Integer> indexList = new ArrayList<Integer>();
	public ArrayList<Double> turningList = new ArrayList<Double>();

	public NewickNode(double d) {
		value = d;
	}

	public void setName(String s) {
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

	public void addChild(NewickNode n) {
		childVec.add(n);
	}

	public NewickNode getChild(int i) {
		return childVec.get(i);
	}

	// toString method provided for testing purposes
	public static void toString(NewickNode n, StringBuffer sb) {
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

	public static NewickNode parse(String s) {
		int x = s.lastIndexOf(':');
		return build(s, new NewickNode(Double.parseDouble(s.substring(x + 1))), 0, x);
	}

	// this is the parsing code
	public static NewickNode build(String s, NewickNode parent, int from, int to) {
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
				parent.addChild(build(s, new NewickNode(Double.parseDouble(s.substring(colon + 1, i))), x + 1, colon));
				x = i;
			}
		}

		return parent;
	}

	public static void getData(NewickNode node, ArrayList<ShapePolygon> shapes) {
		int tSize = shapes.get(0).getTurningList().size();
		if (node.childVec.size() == 0) {
			node.indexList.add(node.index);
			for (int i = 0; i < shapes.get(node.index).getTurningList().size(); i++) {
				double t = shapes.get(node.index).getTurningList().get(i);
				node.turningList.add(t);
			}
		} else {
			for (int i = 0; i < tSize; i++) {
				node.turningList.add(0.0);
			}
			for (int i = 0; i < node.childVec.size(); i++) {
				NewickNode child = node.childVec.get(i);
				getData(child, shapes);
				ArrayList<Integer> childList = child.indexList;
				for (int j = 0; j < childList.size(); j++) {
					node.indexList.add(childList.get(j));
				}
				for (int j = 0; j < tSize; j++) {
					double t = node.turningList.get(j);
					node.turningList.set(j, t + child.turningList.get(j) * childList.size());
				}
			}

			for (int j = 0; j < tSize; j++) {
				double t = node.turningList.get(j);
				node.turningList.set(j, t / node.indexList.size());
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(parse("1:1"));
		System.out.println(parse(
				"((0.0:1.1735,38.0:1.1735):1.87246,(((5.0:1.03283,37.0:1.03283):1.36773,6.0:1.36773):1.41731,(7.0:1.31801,11.0:1.31801):1.41731):1.87246):2.05499"));
	}
}