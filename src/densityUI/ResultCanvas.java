package densityUI;

import java.awt.Dimension;

import database.DensityData;
import database.Line;
import database.Point;
import processing.core.PApplet;

public class ResultCanvas extends PApplet {
	DensityData dt;
	int canvasWidth, canvasHeight;

	public ResultCanvas(DensityData dt, Dimension dim) {
		this.canvasWidth = dim.width;
		this.canvasHeight = dim.height;
		this.dt = dt;
	}

	int wHeight = 130;
	float handleX;
	float handleY;
	float handleW = 20;
	float handleH = 15;
	boolean isDraggable = false;
	int handleFill = 150;
	int windowWidth;
	float translateY = 0;

	public void setup() {
		windowWidth = canvasWidth - 22;
		handleX = canvasWidth - 21;
		handleY = 0;// handleH / 2;

	}

	public void drawPolygon(int index) {
		Point p = dt.getSketchList().get(index);
		pushMatrix();
		translate(40, (index) * wHeight);

		noStroke();
		for (int i = 0; i < dt.getSketchSize(); i++) {
			for (int j = 0; j < dt.getSketchSize(); j++) {
				int position = ((int) p.x + i) * dt.getHeight()
						+ ((int) p.y + j);
				fill((255 - (float) dt.getTiles()[position] * 255 / 36));
				rect(i * 10, j * 10, 10, 10);
			}
		}
		popMatrix();
	}

	public void draw() {
		background(255);
		// window decorations
		stroke(0);
		fill(255);
		rect(3, 0, canvasWidth - 1, height - 1);
		fill(200);
		rect(windowWidth, 0, canvasWidth - 1, height - 1);

		fill(handleFill);
		rect(handleX, handleY, handleW, handleH);
		if (isDraggable && mouseY > handleH / 2
				&& mouseY < height - handleH / 2) {
			handleY = mouseY - handleH / 2;
		}
		pushMatrix();
		translate(10, 10);
		popMatrix();
		pushMatrix();
		if (wHeight * dt.getSketchList().size() > 800) {
			this.translateY = -handleY
					* (wHeight * dt.getSketchList().size() - 800) / 800;
		} else {
			this.translateY = 0;
		}

		translate(0, this.translateY);
		// System.out.println(data.getResult().size());
		for (int i = 0; i < dt.getSketchList().size(); i++) {
			drawPolygon(i);
		}
		popMatrix();
	}

	public void mousePressed() {
		if (mouseX > handleX && mouseX < handleX + handleW && mouseY > handleY
				&& mouseY < handleY + handleH) {
			isDraggable = true;
			handleFill = color(100, 200, 255);
		} else {
			int index = (int) (mouseY - this.translateY) / wHeight;
			System.out.println(index);
			dt.setSelectedSketch(index);
		}
	}

	public void mouseReleased() {
		isDraggable = false;
		handleFill = 150;
	}

	public void mouseMoved() {
		if (mouseX > handleX && mouseX < handleX + handleW && mouseY > handleY
				&& mouseY < handleY + handleH) {
			cursor(HAND);
		} else {
			cursor(ARROW);
		}
	}
}
