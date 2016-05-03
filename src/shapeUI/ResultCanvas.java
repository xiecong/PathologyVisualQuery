package shapeUI;

import java.awt.Dimension;
import java.util.ArrayList;

import database.ShapeData;
import database.ShapePolygon;
import processing.core.PApplet;

//canvas for returned result
public class ResultCanvas extends PApplet {
	ShapeData data;
	int size = 60;
	int canvasWidth = 200;
	float handleX;
	float handleY;
	float handleW = 20;
	float handleH = 15;
	boolean isDraggable = false;
	int handleFill = 150;
	int windowWidth;
	float translateY = 0;
	private int canvasHeight;

	public ResultCanvas(ShapeData data, Dimension dimension) {
		this.canvasWidth = dimension.width;
		this.canvasHeight = dimension.height;
		this.data = data;
	}

	public void setup() {
		windowWidth = canvasWidth - 22;
		handleX = canvasWidth - 21;
		handleY = 0;// handleH / 2;
	}

	public void drawPolygon(ShapePolygon s) {
		// rect(-canvasWidth / 2, -0.5f * wHeight, canvasWidth, wHeight);
		if (s != null) {
			for (int i = 0; i < s.getSampleList().size() - 1; i++) {
				float x1 = (float) s.getSampleList().get(i).x;
				float y1 = (float) s.getSampleList().get(i).y;
				float x2 = (float) s.getSampleList().get(i + 1).x;
				float y2 = (float) s.getSampleList().get(i + 1).y;
				line(x1, y1, x2, y2);
			}
		}
	}

	public void draw() {
		background(255);
		// window decorations
		stroke(0);
		noFill();
		rect(3, 0, canvasWidth - 1, height - 1);
		fill(0);
		text("query result", 20, 20);
		fill(200);
		rect(windowWidth, 0, canvasWidth - 1, height - 1);

		fill(handleFill);
		rect(handleX, handleY, handleW, handleH);
		if (isDraggable && mouseY > handleH / 2 && mouseY < height - handleH / 2) {
			handleY = mouseY - handleH / 2;
		}
		pushMatrix();
		translate(10, 10);
		popMatrix();
		pushMatrix();
		if (size * data.getResult().size() / 9 > this.canvasHeight) {
			this.translateY = 10 - (handleY) * (size * data.getResult().size() - canvasHeight) / canvasHeight;
		} else {
			this.translateY = 10;
		}

		translate(0, this.translateY);
		// System.out.println(data.getResult().size());
		if (data.selectedCluster == -1) {
			for (int i = 0; i < data.getResult().size(); i++) {
				pushMatrix();
				int x = i % 9, y = i / 9;
				translate((x + 0.5f) * size, (y + 0.5f) * size);
				ShapePolygon s = data.getResult().get(i);
				drawPolygon(s);
				popMatrix();
			}
		} else {
			ArrayList<Integer> indexList = data.clusters.get(data.selectedCluster).indexList;
			for (int i = 0; i < indexList.size(); i++) {
				pushMatrix();
				int x = i % 9, y = i / 9;
				translate((x + 0.5f) * size, (y + 0.5f) * size);
				ShapePolygon s = data.getResult().get(indexList.get(i));
				drawPolygon(s);
				popMatrix();
			}
		}
		popMatrix();
	}

	public void mousePressed() {
		if (mouseX > handleX && mouseX < handleX + handleW && mouseY > handleY && mouseY < handleY + handleH) {
			isDraggable = true;
			handleFill = color(100, 200, 255);
		} else {
			int indexX = (int) (mouseX / size);
			int indexY = (int) (mouseY - this.translateY) / size;
			int index = indexY * 9 + indexX;
			if (data.selectedCluster == -1) {
				data.setSketch(data.getResult().get(index).getSampleList());
			} else {
				int index2 = data.clusters.get(data.selectedCluster).indexList.get(index);
				data.setSketch(data.getResult().get(index2).getSampleList());
			}
		}
	}

	public void mouseReleased() {
		isDraggable = false;
		handleFill = 150;
	}

	public void mouseMoved() {
		if (mouseX > handleX && mouseX < handleX + handleW && mouseY > handleY && mouseY < handleY + handleH) {
			cursor(HAND);
		} else {
			cursor(ARROW);
		}
	}
}
