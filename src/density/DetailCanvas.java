package density;

import processing.core.PApplet;

import java.util.ArrayList;

import database.DensityData;
import database.Point;
import database.ShapePolygon;

public class DetailCanvas extends PApplet {
	DensityData dt;
	int x = 3800, y = 23000;

	public DetailCanvas(DensityData dt) {
		this.dt = dt;
	}

	public void setup() {
		/*
		 * int index = 6; x = (int) dt.getEmptyWindowList().get(index).x * 100;
		 * y = (int) dt.getEmptyWindowList().get(index).y * 100; windowSize =
		 * 500;
		 * 
		 * sList = dt.getShapesinWindow(x - windowSize, x + windowSize + 100, y
		 * - windowSize, y + windowSize + 100); size(windowSize * 2 + 100,
		 * windowSize * 2 + 100); System.out.println(sList.size());
		 */
		size(500, 500);
		// sList = dt.getShapesinWindow(2800,3900,17400,18500);
	}

	public void draw() {
		background(255);
		// translate(-x + windowSize, -y + windowSize);
		Point p = dt.getSelectedWindow();
		if (p != null) {
			ArrayList<ShapePolygon> sList = dt.getShapeList();
			translate(-(float) p.x * 100, -(float) p.y * 100);
			for (int i = 0; i < sList.size(); i++) {
				ArrayList<Point> pList = sList.get(i).getPolygonList();
				for (int j = 1; j < pList.size(); j++) {
					Point p1 = pList.get(j - 1);
					Point p2 = pList.get(j);
					line((float) p1.x, (float) p1.y, (float) p2.x, (float) p2.y);
				}
			}
		}
	}
}
