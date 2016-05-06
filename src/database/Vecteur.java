package database;

public class Vecteur {
	public double x = 0;
	public double y = 0;

	public Vecteur(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void add(double x, double y){
		this.x += x;
		this.y += y;
	}

	public static double angle(Vecteur v1, Vecteur v2) {
		double l1 = Math.sqrt(v1.x * v1.x + v1.y * v1.y);
		double l2 = Math.sqrt(v2.x * v2.x + v2.y * v2.y);
		double cos = (v1.x * v2.x + v1.y * v2.y) / l1 / l2;
		double angle = Math.acos(cos);
		if (cos >= 1) {
			return 0;
		}
		double sin = v1.x * v2.y - v1.y * v2.x;
		if (sin > 0) {
			return angle;
		} else {
			return -angle;
		}
	}
}
