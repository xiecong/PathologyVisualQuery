package cluster;

import java.util.ArrayList;

import database.ShapePolygon;

public class NewickNode {
	public int index;
	public double value = 0;
	private ArrayList<NewickNode> childVec = new ArrayList<NewickNode>();
	//ShapePolygon shape;
	public ArrayList<Integer> indexList = new ArrayList<Integer>();
	public ArrayList<Double> averageData = new ArrayList<Double>();

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

}