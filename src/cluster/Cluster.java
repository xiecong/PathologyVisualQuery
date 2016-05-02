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
	
}
