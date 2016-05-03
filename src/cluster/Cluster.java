package cluster;

import java.util.ArrayList;

import weka.clusterers.HierarchicalClusterer;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import database.ShapePolygon;

public class Cluster {

	public String shapeClustering(int attrNum, ArrayList<ShapePolygon> shapes) {
		HierarchicalClusterer clusterer = new HierarchicalClusterer();
		try {
			clusterer.setOptions(new String[] { "-L", "COMPLETE" });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//clusterer.setDebug(true);
		clusterer.setNumClusters(1);
		clusterer.setDistanceFunction(new CustomizeDistance());
		clusterer.setDistanceIsBranchLength(true);

		// Build dataset
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		for (int i = 0; i < attrNum + 1; i++) {
			attributes.add(new Attribute("a" + i));
		}
		Instances data = new Instances("Shape", attributes, attrNum + 1);

		// Add data
		for (int i = 0; i < shapes.size(); i++) {
			double[] tArray = new double[attrNum + 1];
			for (int j = 0; j < attrNum; j++) {
				tArray[j] = shapes.get(i).getTurningList().get(j);
			}
			tArray[attrNum] = i;
			data.add(new DenseInstance(1.0, tArray));
		}

		String graph = null;
		// Cluster network
		try {
			clusterer.buildClusterer(data);
			graph = clusterer.graph();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return graph;
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
		if (node.numChildren() == 0) {
			node.indexList.add(node.index);
			for (int i = 0; i < shapes.get(node.index).getTurningList().size(); i++) {
				double t = shapes.get(node.index).getTurningList().get(i);
				node.averageData.add(t);
			}
		} else {
			for (int i = 0; i < tSize; i++) {
				node.averageData.add(0.0);
			}
			for (int i = 0; i < node.numChildren(); i++) {
				NewickNode child = node.getChild(i);
				getData(child, shapes);
				ArrayList<Integer> childList = child.indexList;
				for (int j = 0; j < childList.size(); j++) {
					node.indexList.add(childList.get(j));
				}
				for (int j = 0; j < tSize; j++) {
					double t = node.averageData.get(j);
					node.averageData.set(j, t + child.averageData.get(j) * childList.size());
				}
			}

			for (int j = 0; j < tSize; j++) {
				double t = node.averageData.get(j);
				node.averageData.set(j, t / node.indexList.size());
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(parse("1:1"));
		System.out.println(parse(
				"((0.0:1.1735,38.0:1.1735):1.87246,(((5.0:1.03283,37.0:1.03283):1.36773,6.0:1.36773):1.41731,(7.0:1.31801,11.0:1.31801):1.41731):1.87246):2.05499"));
	}
}
