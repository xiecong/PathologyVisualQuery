package database;

import java.util.ArrayList;

import cluster.Cluster;
import cluster.NewickNode;

//data structure for storing the sketch and result set
public class ShapeData {
	ShapePolygon sketch = new ShapePolygon();
	ArrayList<ShapePolygon> shapes = new ArrayList<ShapePolygon>();
	NewickNode root;
	int clusterNum = 4;
	public int selectedCluster = -1;
	public ArrayList<NewickNode> clusters = new ArrayList<NewickNode>();
	Query query = new Query(this);
	// boolean isSketch = false;
	boolean sketching = false;
	boolean newSketch = true;
	boolean searching = false;

	public boolean getNewSketch() {
		return newSketch;
	}
	public void setClusterNum(int k){
		this.clusterNum = k;
	}

	public void setTree(String graph) {
		root = Cluster.parse(graph);
		Cluster.getData(root, shapes);
		System.out.println(root.indexList.size());
		for (int i = 0; i < root.averageData.size(); i++) {
			System.out.print(", " + root.averageData.get(i));
		}
		System.out.println();
		for (int i = 0; i < shapes.get(0).turningList.size(); i++) {
			System.out.print(", " + shapes.get(0).turningList.get(i));
		}
		System.out.println();
	}

	public void getClusterAverage() {
		System.out.println(clusterNum);
		clusters.add(root);
		for (int i = 1; i < clusterNum; i++) {
			int split = -1;
			double val = -1;
			for (int j = 0; j < clusters.size(); j++) {
				if (val < clusters.get(j).value) {
					split = j;
					val = clusters.get(j).value;
				}
			}
			for (int j = 0; j < clusters.get(split).numChildren(); j++) {
				clusters.add(clusters.get(split).getChild(j));
			}
			clusters.remove(split);
		}
	}

	public void setSketch(ArrayList<Point> polygonList) {
		String shapeString = "";
		for (int i = 0; i < polygonList.size(); i++) {
			shapeString += (polygonList.get(i).x * 10+300) + " " + (polygonList.get(i).y * 10+200);
			if (i != polygonList.size() - 1) {
				shapeString += ", ";
			}
		}
		sketch = new ShapePolygon(shapeString);
	}

	public void setSketching() {
		this.sketching = true;
		this.newSketch = false;
	}

	public void finishSketch() {
		this.sketching = false;
	}

	public boolean getSketchStatus() {
		return this.sketching;
	}

	public boolean getSearchStatus() {
		return searching;
	}

	public void matchShape() {
		searching = true;
		query.connection();
		query.matchShape(sketch.turningList);
		query.close();
		System.out.println(shapes.size());
		searching = false;
	}

	public ArrayList<ShapePolygon> getResult() {
		return shapes;
	}

	public void clearShapes() {
		shapes.clear();
	}

	public void clearSketch() {
		this.newSketch = true;
		sketch.cx = 0;
		sketch.cy = 0;
		sketch.perimeter = 0;
		sketch.polygonList.clear();
		sketch.sampleList.clear();
		sketch.angleList.clear();
		sketch.turningList.clear();
	}

	public void addShape(String shape) {
		shapes.add(new ShapePolygon(shape));
	}

	public ShapePolygon getSketch() {
		return sketch;
	}

}
