package shapesketch;

import java.awt.Dimension;

import cluster.NewickNode;
import database.ShapeData;
import processing.core.PApplet;

public class ClusterCanvas extends PApplet {
	ShapeData data;
	int canvasWidth;
	int canvasHeight;

	public ClusterCanvas(ShapeData data, Dimension dimension) {
		this.data = data;
		canvasWidth = dimension.width;
		canvasHeight = dimension.height;
	}

	public void setup() {
	}

	public void draw() {
		background(255);
		noFill();
		rect(0, 0, width - 1, height - 1);
		for (int i = 0; i < data.clusters.size(); i++) {
			pushMatrix();
			translate(canvasWidth * i / data.clusters.size(), 0);
			drawAverage(data.clusters.get(i));
			popMatrix();
		}
	}

	private void drawAverage(NewickNode newickNode) {
		// TODO Auto-generated method stub
		fill(0);
		int l = 5;
		int x = canvasWidth / 2 / data.clusters.size(), y = canvasHeight / 2;
		int x0 = x, y0 = y;
		for (int i = 0; i < newickNode.averageData.size(); i++) {
			double degree = newickNode.averageData.get(i);
			line(x, y, x + l * (float) Math.cos(degree),
					y + l * (float) Math.sin(degree));
			x += l * (float) Math.cos(degree);
			y += l * (float) Math.sin(degree);
		}
		line(x, y, x0, y0);
	}
	public void mousePressed() {
		int index = (int) (mouseX *data.clusters.size() / canvasWidth);
		data.selectedCluster = index;
		System.out.println(data.selectedCluster);
	}
}
