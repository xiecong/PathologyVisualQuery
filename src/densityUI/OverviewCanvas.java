package densityUI;

import java.awt.Dimension;

import database.DensityData;
import database.Point;
import processing.core.PApplet;

public class OverviewCanvas extends PApplet {
	DensityData densityData;
	int canvasWidth;
	int canvasHeight;
	int[] tiles;
	float ratio;

	public OverviewCanvas(DensityData dt, Dimension ocDim) {
		// TODO Auto-generated constructor stub
		this.densityData = dt;
		this.canvasWidth = ocDim.width;
		this.canvasHeight = ocDim.height;
	}

	public void setup() {
		size(canvasWidth, canvasHeight);
		noFill();
		ratio = 1.0f * densityData.xWidth / canvasWidth;
		tiles = new int[canvasWidth * canvasHeight];
		for (int i = 0; i < densityData.xWidth; i++) {
			for (int j = 0; j < densityData.yWidth; j++) {
				double x = (i / ratio);
				double y = (j / ratio);
				int xPos = (int) x;
				int yPos = (int) y;
				double xratio = x - (int) x;
				double yratio = y - (int) y;
				if (xPos < canvasWidth && yPos < canvasHeight) {
					tiles[xPos * canvasHeight + yPos] += (1 - xratio) * (1 - yratio)
							* densityData.tiles[i * densityData.yWidth + j];
				}
				if (xPos < canvasWidth && (yPos + 1) < canvasHeight) {
					tiles[xPos * canvasHeight + (yPos + 1)] += (1 - xratio) * (yratio)
							* densityData.tiles[i * densityData.yWidth + j];
				}
				if ((xPos + 1) < canvasWidth && yPos < canvasHeight) {
					tiles[(xPos + 1) * canvasHeight + yPos] += (xratio) * (1 - yratio)
							* densityData.tiles[i * densityData.yWidth + j];
				}
				if ((xPos + 1) < canvasWidth && (yPos + 1) < canvasHeight) {
					tiles[(xPos + 1) * canvasHeight + (yPos + 1)] += (xratio) * (yratio)
							* densityData.tiles[i * densityData.yWidth + j];
				}
			}
		}

		int max = 0;
		for (int i = 0; i < canvasWidth; i++) {
			for (int j = 0; j < canvasHeight; j++) {
				if (tiles[i * canvasHeight + j] > max) {
					max = tiles[i * canvasHeight + j];
				}
			}
		}
		for (int i = 0; i < canvasWidth; i++) {
			for (int j = 0; j < canvasHeight; j++) {
				tiles[i * canvasHeight + j] = 255 - 255 * tiles[i * canvasHeight + j] / max;
			}
		}
	}

	public void draw() {
		for (int i = 0; i < canvasWidth; i++) {
			for (int j = 0; j < canvasHeight; j++) {
				stroke(tiles[i * canvasHeight + j]);
				point(i, j);
			}
		}
		if (densityData.selectedSketch != -1) {
			Point p = densityData.getSketchList().get(densityData.selectedSketch);
			stroke(255, 0, 0);
			rect((float) (p.x - 1) / ratio, (float) (p.y - 1) / ratio, 12 / ratio, 12 / ratio);
		}
	}

}
