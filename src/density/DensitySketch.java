package density;

import database.DensityData;
import processing.core.PApplet;

public class DensitySketch extends PApplet {
	int rectSize = 50;
	DensityData densityData = new DensityData();
	int sketchSize = 0;
	int sketchRadius = 1;
	double[] increment = { 0.5, 1, 0.5, 1, 1.5, 1, 0.5, 1, 0.5 };

	public DensitySketch(DensityData densityData) {
		this.densityData = densityData;
	}

	public void setup() {
		sketchSize = densityData.getSketchSize();
		size(sketchSize * rectSize + 2, sketchSize * rectSize + 2);
	}

	public void draw() {
		noStroke();
		for (int i = 0; i < sketchSize; i++) {
			for (int j = 0; j < sketchSize; j++) {
				if (densityData.getSketch()[i * densityData.getSketchSize() + j] < 255) {
					fill(255 - (float) densityData.getSketch()[i * densityData.getSketchSize() + j]);
				} else {
					fill(0);
				}
				rect(i * rectSize, j * rectSize, rectSize, rectSize);
			}
		}
	}

	public void mouseDragged() {
		if (mouseX < rectSize * sketchSize && mouseY < rectSize * sketchSize) {
			int x = mouseX / rectSize;
			int y = mouseY / rectSize;
			for (int i = -sketchRadius; i <= sketchRadius; i++) {
				if ((x + i) >= 0 && (x + i) < sketchSize) {
					for (int j = -sketchRadius; j < sketchRadius; j++) {
						if ((y + j) >= 0 && (y + j) < sketchSize) {
							int index = (x + i) * sketchSize + (y + j);
							int index2 = (i + sketchRadius) * (sketchRadius * 2 + 1) + (j + sketchRadius);
							densityData.getSketch()[index] += this.increment[index2];
						}
					}
				}
			}
		}
	}

}
