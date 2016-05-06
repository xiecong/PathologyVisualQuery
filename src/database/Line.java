package database;

import java.util.ArrayList;

public class Line {
	public ArrayList<Point> line = new ArrayList<Point>();
	Vecteur v;

	public void setVector() {
		// TODO Auto-generated method stub
		if (line.size() >= 2) {
			v = new Vecteur(line.get(line.size() - 1).x - line.get(0).x,
					line.get(line.size() - 1).y - line.get(0).y);
		} else {
			v = new Vecteur(0, 0);
		}
	}
}
