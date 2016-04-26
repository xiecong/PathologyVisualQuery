package density;

import database.DensityData;
import processing.core.PApplet;

public class OveviewCanvas extends PApplet {
	DensityData dt = new DensityData();

	public OveviewCanvas() {
	}

	public void setup() {
		size(dt.getWidth(), dt.getHeight());
		System.out.println(dt.getWidth()+", "+dt.getHeight());
	}

	public void draw() {
		background(0);
		for (int i = 0; i < dt.getTiles().length; i++) {
			int x = i / dt.getHeight();
			int y = i % dt.getHeight();
			noStroke();
			fill(255 - dt.getTiles()[i] * 255 / 36.0f);
			rect(x, y, 1, 1);
		}
		stroke(255, 0, 0);
		noFill();
		for (int i = 0; i < dt.getEmptyWindowList().size(); i++) {
			//ellipse((int) dt.getEmptyWindowList().get(i).x, (int) dt.getEmptyWindowList().get(i).y, 12, 12);
		}
	}
}
