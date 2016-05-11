package densityUI;

import database.DensityData;
import database.Line;
import database.Point;
import database.Vecteur;
import processing.core.PApplet;

public class SketchCanvas extends PApplet {
	int rectSize = 50;
	DensityData densityData = new DensityData();
	int sketchSize = 0;
	int sketchRadius = 1;
	double[] increment = { 0.5, 1, 0.5, 1, 1.5, 1, 0.5, 1, 0.5 };

	public SketchCanvas(DensityData densityData) {
		this.densityData = densityData;
	}

	public void setup() {
		sketchSize = densityData.getSketchSize();
		size(sketchSize * rectSize + 2, sketchSize * rectSize + 2);
	}

	public void draw() {
		noStroke();
		int size = sketchSize;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (densityData.getSketch()[i * densityData.getSketchSize() + j] < 255) {
					fill(255 - (float) densityData.getSketch()[i * densityData.getSketchSize() + j]);
				} else {
					fill(0);
				}
				rect(i * rectSize, j * rectSize, rectSize, rectSize);
			}
		}
		stroke(0);
		Vecteur[] vField = densityData.vectorField;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int index = j * size + i;
				if (vField[index].x != 0 && vField[index].y != 0) {
					line((i + 0.5f) * rectSize, (j + 0.5f) * rectSize,
							(i + 0.5f) * rectSize + (float) vField[index].x * 6,
							(j + 0.5f) * rectSize + (float) vField[index].y * 6);
				}
			}
		}
		int i = densityData.lines.size() - 1;
		if (i >= 0) {
			// for (int i = 0; i < densityData.lines.size(); i++) {
			// System.out.println(i+" "+densityData.lines.get(i).line.size());
			drawLine(densityData.lines.get(i));
		}
		// }
		stroke(0);
		noFill();
		rect(0, 0, 499, 499);
	}

	private void drawLine(Line line) {
		// TODO Auto-generated method stub
		if (line.line.size() > 2) {
			for (int i = 0; i < line.line.size() - 1; i++) {
				float x1 = (float) line.line.get(i).x;
				float y1 = (float) line.line.get(i).y;
				float x2 = (float) line.line.get(i + 1).x;
				float y2 = (float) line.line.get(i + 1).y;
				line(x1, y1, x2, y2);
			}
		}
	}

	public void mouseReleased() {
		if (densityData.sketchType == 1) {
			densityData.lineDragging = false;
			densityData.lines.add(new Line());
			densityData.lines.get(densityData.lineNum).setVector();
			densityData.lineNum++;
			densityData.updateVector();
			/*
			 * for (int i = 0; i < 10; i++) { for (int j = 0; j < 10; j++) {
			 * System.out.print(densityData.vField0[j * 10 + i].x + "," +
			 * densityData.vField0[j * 10 + i].y + " "); } System.out.println();
			 * }
			 */
		}
	}

	public void mouseDragged() {
		if (densityData.sketchType == 0) {
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
		} else {
			densityData.lineDragging = true;
			densityData.lines.get(densityData.lineNum).line.add(new Point(mouseX, mouseY));
		}
	}

}
