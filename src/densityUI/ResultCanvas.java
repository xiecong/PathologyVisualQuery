package densityUI;

import java.awt.Dimension;

import database.DensityData;
import database.Line;
import database.Point;
import processing.core.PApplet;

public class ResultCanvas extends PApplet {
	DensityData dt;
	int canvasWidth, canvasHeight;
	int wHeight = 150;
	float handleX;
	float handleY;
	float handleW = 15;
	float handleH = 20;
	boolean isDraggable = false;
	int handleFill = 150;
	int windowWidth;
	float translateY = 0;

	public ResultCanvas(DensityData dt, Dimension dim) {
		this.canvasWidth = dim.width;
		this.canvasHeight = dim.height;
		wHeight = canvasHeight - 50;
		this.dt = dt;
		windowWidth = canvasHeight - 22;
		handleX = 0;
		handleY = canvasHeight - 21;// handleH / 2;
	}

	public void setup() {
		size(canvasWidth, canvasHeight);
	}

	public void drawHeatMap(int index) {
		Point p = dt.getSketchList().get(index);
		pushMatrix();
		translate((index) * wHeight, 40);

		noStroke();
		for (int i = 0; i < dt.getSketchSize(); i++) {
			for (int j = 0; j < dt.getSketchSize(); j++) {
				int position = ((int) p.x + i) * dt.getHeight() + ((int) p.y + j);
				fill((255 - (float) dt.getTiles()[position] * 255 / 36));
				rect(i * 10, j * 10, 10, 10);
			}
		}
		stroke(0);
		noFill();
		rect(0, 0, 100, 100);

		popMatrix();
	}

	public void draw() {
		background(255);
		// window decorations
		stroke(0);
		fill(255);
		rect(0, 0, canvasWidth - 1, canvasHeight - 1);
		fill(200);
		rect(0, windowWidth, canvasWidth - 1, canvasHeight - 1);

		fill(handleFill);
		rect(handleX, handleY, handleW, handleH);
		if (isDraggable && mouseX > handleH / 2 && mouseX < canvasWidth - handleH / 2) {
			handleX = mouseX - handleH / 2;
		}

		pushMatrix();
		if (wHeight * dt.getSketchList().size() > canvasWidth) {
			this.translateY = 10 - handleX * (wHeight * dt.getSketchList().size() - canvasWidth) / canvasWidth;
		} else {
			this.translateY = 10;
		}
		translate(this.translateY, 0);
		for (int i = 0; i < dt.getSketchList().size(); i++) {
			drawHeatMap(i);
		}
		popMatrix();
	}

	public void mousePressed() {
		if (mouseX > handleX && mouseX < handleX + handleW && mouseY > handleY && mouseY < handleY + handleH) {
			isDraggable = true;
			handleFill = color(100, 200, 255);
		} else {
			int index = (int) (mouseX - this.translateY) / wHeight;
			System.out.println(index);
			dt.setSelectedSketch(index);
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
