package shapesketch;

import java.awt.Dimension;

import cluster.NewickNode;
import database.ShapeData;
import processing.core.PApplet;

public class ClusterCanvas extends PApplet {
	ShapeData data;
	int canvasWidth;

	public ClusterCanvas(ShapeData data, Dimension dimension) {
		this.data = data;
		canvasWidth = dimension.width;
	}

	public void setup() {
	}

	public void draw() {
		background(255);
		noFill();
		rect(0, 0, width - 1, height - 1);
		for (int i = 0; i < data.clusters.size(); i++) {
			drawAverage(data.clusters.get(i));
		}
	}

	private void drawAverage(NewickNode newickNode) {
		// TODO Auto-generated method stub

	}
}
