package shapesketch;

import database.ShapeData;
import processing.core.PApplet;

public class ClusterCanvas extends PApplet {
	ShapeData data;

	public ClusterCanvas(ShapeData data) {
		this.data = data;
	}

	public void setup() {
	}

	public void draw() {
		background(255);
		noFill();
		rect(0, 0, width - 1, height - 1);
	}
}
