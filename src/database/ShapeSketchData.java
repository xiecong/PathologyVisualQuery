package database;

import java.util.ArrayList;
//data structure for storing the sketch and result set
public class ShapeSketchData {
	ShapePolygon sketch = new ShapePolygon();
	ArrayList<ShapePolygon> shapes = new ArrayList<ShapePolygon>();
	Query query = new Query(this);
	// boolean isSketch = false;
	boolean sketching = false;
	boolean newSketch = true;
	boolean searching = false;

	public boolean getNewSketch() {
		return newSketch;
	}

	public void setSketch(ArrayList<Point> polygonList) {
		String shapeString = "";
		for (int i = 0; i < polygonList.size(); i++) {
			shapeString += (polygonList.get(i).x * 10 + 400) + " " + (polygonList.get(i).y * 10 + 400);
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
