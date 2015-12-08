package density;

import processing.core.PApplet;

public class OveviewCanvas extends PApplet {
	DensityData dt;

	public OveviewCanvas(DensityData dt){
		this.dt = dt;
	}

	public void setup() {
		size(dt.xWidth, dt.yWidth);
	}

	public void draw() {
		background(0);
		for (int i = 0; i < dt.tiles.length; i++) {
			int x = i / dt.yWidth;
			int y = i % dt.yWidth;
			noStroke();
			fill(255 - dt.tiles[i] * 255 / 36.0f);
			rect(x, y, 1, 1);
		}
		stroke(255, 0, 0);
		noFill();
		for (int i = 0; i < dt.emptyWindowList.size(); i++) {
			ellipse((int) dt.emptyWindowList.get(i).x, (int) dt.emptyWindowList.get(i).y, 12, 12);
		}
	}
}
