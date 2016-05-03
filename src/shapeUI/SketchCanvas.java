package shapeUI;

import java.awt.Dimension;
import java.util.ArrayList;

import database.ShapeData;
import database.Point;
import database.ShapePolygon;
import processing.core.PApplet;
//canvas for sketch input
//start point is different!
public class SketchCanvas extends PApplet {
	ShapeData data;
	// ShapePolygon sketch;
	float radius = 10;
	int selectId = -1;

	public SketchCanvas(ShapeData data, Dimension dimension) {
		// this.sketch = data.getSketch();
		this.data = data;
	}

	public void setup() {
	}

	public void drawSketch() {
		ArrayList<Point> sketchList = data.getSketch().getPolygonList();

		for (int i = 0; i < sketchList.size() - 1; i++) {
			line((float) (sketchList.get(i).x), (float) (sketchList.get(i).y), (float) (sketchList.get(i + 1).x),
					(float) (sketchList.get(i + 1).y));
		}
		if (!data.getSketchStatus()) {
			for (int i = 0; i < sketchList.size() - 1; i++) {
				fill(255);
				ellipse((float) (sketchList.get(i).x), (float) (sketchList.get(i).y), radius, radius);
			}
		}
	}

	public void draw() {
		background(255);
		fill(0);
		text("Sketch",20,20);
		noFill();
		rect(0, 0, width - 1, height - 1);
		drawSketch();
	}

	public void mousePressed() {
		if (data.getNewSketch()) {
			// data.clearSketch();
			data.setSketching();
		} else {
			for (int i = 0; i < data.getSketch().getPolygonList().size(); i++) {
				float x = (float) (mouseX - data.getSketch().getPolygonList().get(i).x);
				float y = (float) (mouseY - data.getSketch().getPolygonList().get(i).y);
				if (x * x + y * y < radius * radius) {
					selectId = i;
					break;
				}
			}
			// see if it click on a sample node
		}
	}

	public void mouseDragged() {
		if (data.getSketchStatus()) {
			data.getSketch().addPoint(new Point(mouseX, mouseY));
		} else {
			if (selectId != -1) {
				data.getSketch().getPolygonList().get(selectId).setPosition(mouseX, mouseY);
			}
			// see if it is dragging a sample point
		}
	}

	public void mouseReleased() {
		ArrayList<Point> sketchList = data.getSketch().getPolygonList();
		if (data.getSketchStatus()) {
			data.finishSketch();
			data.getSketch().addPoint(new Point(sketchList.get(0).x, sketchList.get(0).y));
			data.getSketch().setFeaturesWithPolygon();
			data.getSketch().setPolygonFromSample();
		} else {
			// see if it dragged a sample point
			selectId = -1;
			data.getSketch().setFeaturesWithPolygon();
			data.getSketch().setPolygonFromSample();
		}
	}
}
