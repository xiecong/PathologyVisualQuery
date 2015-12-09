package density;

import processing.core.PApplet;

import java.util.ArrayList;

import database.DensityData;
import database.Point;
import database.ShapePolygon;

public class DetailCanvas extends PApplet {
	DensityData dt = new DensityData();
	int x, y, windowSize;
	ArrayList<ShapePolygon> sList;

	public void setup() {
		int index = 6;
		x = (int) dt.getEmptyWindowList().get(index).x * 100;
		y = (int) dt.getEmptyWindowList().get(index).y * 100;
		windowSize = 500;
		sList = dt.getShapesinWindow(x, y, windowSize);
		size(windowSize * 2 + 100, windowSize * 2 + 100);
		System.out.println(sList.size());
	}

	public void draw() {
		translate(-x + windowSize, -y + windowSize);
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
