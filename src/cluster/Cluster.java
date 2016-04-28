package cluster;

import java.util.ArrayList;

import weka.clusterers.HierarchicalClusterer;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import database.ShapePolygon;

public class Cluster {

	public HierarchicalClusterer clusterer;
	Instances data;

	/**
	 * @throws Exception
	 */
	public void clustering(int attrNum, ArrayList<ShapePolygon> shapes) {
		// Instantiate clusterer
		clusterer = new HierarchicalClusterer();
		try {
			clusterer.setOptions(new String[] { "-L", "COMPLETE" });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clusterer.setDebug(true);
		clusterer.setNumClusters(2);
		clusterer.setDistanceFunction(new EuclideanDistance());
		clusterer.setDistanceIsBranchLength(true);

		// Build dataset
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		for (int i = 0; i < attrNum; i++) {
			attributes.add(new Attribute("" + i));
		}
		data = new Instances("Weka test", attributes, attrNum);

		// Add data
		for (int i = 0; i < shapes.size(); i++) {
			double[] tArray = new double[attrNum];
			for (int j = 0; j < attrNum; j++) {
				tArray[j] = shapes.get(i).getTurningList().get(j);
			}
			data.add(new DenseInstance(1.0, tArray));
		}

		// Cluster network
		try {
			clusterer.buildClusterer(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Print normal
		clusterer.setPrintNewick(false);
		try {
			System.out.println(clusterer.graph());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Print Newick
		clusterer.setPrintNewick(true);
		try {
			System.out.println(clusterer.graph());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
