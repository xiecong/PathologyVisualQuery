package database;

import java.util.ArrayList;
//class for computing the features
public class ShapeFeatures {

	public static double[] centroid(ArrayList<Point> pointList) {
		// double area = 0;
		double[] fs = new double[3];
		for (int i = 0; i < pointList.size() - 1; i++) {
			Point p1 = pointList.get(i);
			Point p2 = pointList.get(i + 1);
			fs[0] += (p1.x * p2.y) - (p1.y * p2.x);
		}
		fs[0] /= 2;
		fs[1] = 0;
		fs[2] = 0;
		for (int i = 0; i < pointList.size() - 1; i++) {
			Point p1 = pointList.get(i);
			Point p2 = pointList.get(i + 1);
			fs[1] += (p1.x + p2.x) * (p1.x * p2.y - p1.y * p2.x);
			fs[2] += (p1.y + p2.y) * (p1.x * p2.y - p1.y * p2.x);
		}
		fs[1] = fs[1] / 6 / fs[0];
		fs[2] = fs[2] / 6 / fs[0];
		// System.out.println(fs[0] + ";" + fs[1] + ";" + fs[2]);
		return fs;
	}

	public static double perimeter(ArrayList<Point> pointList) {
		double p = 0;
		for (int i = 0; i < pointList.size() - 1; i++) {
			Point p1 = pointList.get(i);
			Point p2 = pointList.get(i + 1);
			p += Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y)
					* (p1.y - p2.y));
		}
		return p;
	}

	public static double[] axis(ArrayList<Point> pointList) {
		double[] axises = new double[2];
		// double majorLength = 0;
		Point e1 = new Point(0, 0);
		Point e2 = new Point(0, 0);
		for (int i = 0; i < pointList.size(); i++) {
			Point p1 = pointList.get(i);
			for (int j = i + 1; j < pointList.size(); j++) {
				Point p2 = pointList.get(j);
				double thisDistance = Math.sqrt((p1.x - p2.x) * (p1.x - p2.x)
						+ (p1.y - p2.y) * (p1.y - p2.y));
				if (axises[0] < thisDistance) {
					axises[0] = thisDistance;
					e1 = new Point(p1.x, p1.y);
					e2 = new Point(p2.x, p2.y);
				}
			}
		}
		// System.out.println(e1.x + " " + e1.y + " " + e2.x + " " + e2.y);
		double a = -(e1.y - e2.y);
		double b = e1.x - e2.x;

		// ax1 + by1 = c1
		double minc = a * pointList.get(0).x + b * pointList.get(0).y;
		double maxc = a * pointList.get(0).x + b * pointList.get(0).y;
		for (int i = 0; i < pointList.size(); i++) {
			double thisc = a * pointList.get(i).x + b * pointList.get(i).y;
			if (minc > thisc) {
				minc = thisc;
			}
			if (maxc < thisc) {
				maxc = thisc;
			}
		}
		axises[1] = Math.abs((maxc - minc) / Math.sqrt(a * a + b * b));

		// System.out.println(axises[0] + " " + axises[1]);
		return axises;
	}

	public static void construct(String polygon) {
		ArrayList<Point> pointList = new ArrayList<Point>();

		String[] points = polygon.split(", ");
		for (int i = 0; i < points.length; i++) {
			String x = points[i].split(" ")[0];
			String y = points[i].split(" ")[1];
			pointList.add(new Point(Integer.parseInt(x), Integer.parseInt(y)));
		}
		ShapeFeatures.centroid(pointList);
		ShapeFeatures.axis(pointList);
	}

}
