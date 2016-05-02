package database;

import java.util.ArrayList;
//data structure for polygon
//polygonList: original polygon
//sampleList, 30 control points around the perimeter
//angleList, the difference of each edge compare to the previous edge
//turning age, the difference of each edge to the first edge
//cx, cy: center
public class ShapePolygon {
	final int segNum = 30;
	ArrayList<Point> polygonList = new ArrayList<Point>();
	ArrayList<Point> sampleList = new ArrayList<Point>();
	ArrayList<Double> angleList = new ArrayList<Double>();
	ArrayList<Double> turningList = new ArrayList<Double>();
	String id;
	double cx = 0;
	double cy = 0;
	double perimeter;

	public void setIDString(String id) {
		this.id = id;
	}

	public ArrayList<Point> getPolygonList() {
		return polygonList;
	}

	public ArrayList<Point> getSampleList() {
		return this.sampleList;
	}

	public ArrayList<Double> getAngleList() {
		return this.angleList;
	}

	public ArrayList<Double> getTurningList() {
		return this.turningList;
	}

	public double getcx() {
		return this.cx;
	}

	public double getcy() {
		return this.cy;
	}

	public ShapePolygon() {

	}

	public void setPolygonFromSample() {
		this.polygonList.clear();
		for (int i = 0; i < this.sampleList.size(); i++) {
			this.polygonList.add(new Point(this.sampleList.get(i).x + cx, this.sampleList.get(i).y + cy));
		}
	}

	public ShapePolygon(ArrayList<Point> polygonList) {
		for (int i = 0; i < polygonList.size(); i++) {
			this.polygonList.add(new Point(polygonList.get(i).x, polygonList.get(i).y));
		}
		setFeaturesWithPolygon();
	}

	public ShapePolygon(String polygon) {
		String[] points = polygon.split(", ");

		for (int i = 0; i < points.length; i++) {
			String[] coordinates = points[i].split(" ");
			String x = coordinates[0];
			String y = coordinates[1];
			polygonList.add(new Point(Double.parseDouble(x), Double.parseDouble(y)));

		}
		setFeaturesWithPolygon();
	}

	public void addPoint(Point p) {
		polygonList.add(p);
	}

	public void setFeaturesWithPolygon() {
		sampleList.clear();
		angleList.clear();
		turningList.clear();
		double[] fs = ShapeFeatures.centroid(polygonList);
		cx = fs[1];
		cy = fs[2];
		perimeter = ShapeFeatures.perimeter(polygonList);
		this.calculateTurningList();
	}

	private void calculateTurningList() {
		if(polygonList.size()==0){
			return;
		}
		double[] perimeterArray = new double[polygonList.size()];
		perimeterArray[0] = 0;
		for (int i = 1; i < this.polygonList.size(); i++) {
			Point p1 = polygonList.get(i - 1);
			Point p2 = polygonList.get(i);
			perimeterArray[i] = perimeterArray[i - 1]
					+ Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
		}
		double maxValue = 0;
		int maxIndex = 0;
		int j = 0;
		ArrayList<Point> tempsampleList = new ArrayList<Point>();
		for (int i = 0; i < segNum; i++) {
			double thisPosition = i * this.perimeter / segNum;
			for (; j < perimeterArray.length; j++) {
				if (thisPosition >= perimeterArray[j] && thisPosition < perimeterArray[j + 1]) {
					double rate = (thisPosition - perimeterArray[j]) / (perimeterArray[j + 1] - perimeterArray[j]);
					Point p1 = polygonList.get(j);
					Point p2 = polygonList.get(j + 1);
					double x = (1 - rate) * (p1.x - cx) + (rate) * (p2.x - cx);
					double y = (1 - rate) * (p1.y - cy) + (rate) * (p2.y - cy);
					double square = x * x + y * y;
					if (maxValue < square) {
						maxValue = square;
						maxIndex = i;
					}
					tempsampleList.add(new Point(x, y));
					break;
				}
			}
		}
		for (int i = maxIndex; i < tempsampleList.size(); i++) {
			sampleList.add(new Point(tempsampleList.get(i).x, tempsampleList.get(i).y));
		}
		for (int i = 0; i < maxIndex + 1; i++) {
			sampleList.add(new Point(tempsampleList.get(i).x, tempsampleList.get(i).y));
		}

		Point pzero = sampleList.get(0);
		Point pfirst = sampleList.get(1);
		Point pmax = sampleList.get(sampleList.size() - 2);
		double angle = Vecteur.angle(new Vecteur(pzero.x - pmax.x, pzero.y - pmax.y),
				new Vecteur(pfirst.x - pzero.x, pfirst.y - pzero.y));
		angleList.add(angle);
		turningList.add(angle);

		// System.out.println(0 + " " + angleList.get(0) + " " +
		// turningList.get(0));
		for (int i = 1; i < sampleList.size() - 1; i++) {
			Point p1 = sampleList.get(i - 1);
			Point p2 = sampleList.get(i);
			Point p3 = sampleList.get(i + 1);
			Vecteur v1 = new Vecteur(p2.x - p1.x, p2.y - p1.y);
			Vecteur v2 = new Vecteur(p3.x - p2.x, p3.y - p2.y);
			angleList.add(Vecteur.angle(v1, v2));
			turningList.add(angleList.get(i) + turningList.get(i - 1));
			// System.out.println(i + " " + angleList.get(i) + " " +
			// turningList.get(i));
		}
	}

	public static double turningDistantceL1(ArrayList<Double> tList1, ArrayList<Double> tList2) {
		double dis1 = 0;
		double dis2 = 0;
		for (int i = 0; i < tList1.size(); i++) {
			dis1 += Math.abs(tList1.get(i) - tList2.get(i));
		}
		for (int i = 0; i < tList1.size(); i++) {
			dis2 += Math.abs(tList1.get(i) + tList2.get(i));
		}
		if (dis1 < dis2) {
			return dis1;
		} else {
			return dis2;
		}
	}

}
