package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DensityData {
	int xWidth = 1106;
	int yWidth = 487;
	int[] tiles = new int[xWidth * yWidth];
	int maxDensity = 0;
	ArrayList<Point> emptyWindowList = new ArrayList<Point>();
	ArrayList<ShapePolygon> sList = new ArrayList<ShapePolygon>();

	public DensityData() {
		this.initTilesFromFile();
		this.getEmptyAreaList();
	}

	public int getWidth() {
		return this.xWidth;
	}

	public int getHeight() {
		return this.yWidth;
	}

	public int[] getTiles() {
		return this.tiles;
	}

	public ArrayList<Point> getEmptyWindowList() {
		return this.emptyWindowList;
	}

	public void initTilesFromDataSet() {
		Query q = new Query(this);
		q.connection();
		q.centerQuery();
		q.close();
		for (int i = 0; i < tiles.length; i++) {
			System.out.print(tiles[i] + ",");
		}
	}

	public void initTilesFromFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/densityField.txt"));
			String line;// = br.readLine();
			while (true) {
				line = br.readLine();
				if (line == null) {
					break;
				}

				String[] lineStr = line.split(",");
				for (int i = 0; i < lineStr.length; i++) {
					this.tiles[i] = Integer.parseInt(lineStr[i].trim());
				}

			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// dt.findMinMax();
		DensityData dt = new DensityData();
	}

	public ArrayList<ShapePolygon> getShapesinWindow(int x, int y, int windowSize) {
		x += 50;
		y += 50;
		windowSize += 50;
		Query q = new Query();
		q.connection();
		sList = q.windowQuery(x - windowSize, x + windowSize, y - windowSize, y + windowSize);
		q.close();
		return sList;
	}

	public void getEmptyAreaList() {
		// initTilesFromDataSet();
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i] > maxDensity) {
				maxDensity = tiles[i];
			}
		}
		int threshold = 5;
		for (int x = 20; x < xWidth - 20; x++) {
			for (int y = 20; y < yWidth - 20; y++) {
				boolean found = true;
				int iRadius = 1;
				int oRadius = 5;
				int iSum = 0;
				int oSum = 0;
				double avgRadius = 0;
				int count = 0;
				for (int i = -oRadius; i <= oRadius; i++) {
					for (int j = -oRadius; j <= oRadius; j++) {
						double r = Math.sqrt(i * i + j * j);
						if (r < oRadius) {
							int thisx = x + i;
							int thisy = y + i;
							int index = thisx * yWidth + thisy;
							avgRadius += r * tiles[index];
							count += tiles[index];
							oSum += tiles[index];
							if (r < iRadius) {
								iSum += tiles[index];
							}
							if (iSum > threshold) {
								found = false;
								break;
							}
						}
					}
					if (found == false) {
						break;
					}
				}
				if (found && oSum > 500 && avgRadius / count > 3.4) {
					boolean tooNear = false;
					for (int i = 0; i < emptyWindowList.size(); i++) {
						Point p = emptyWindowList.get(i);
						if ((Math.abs(p.x - x) + Math.abs(p.y - y)) < 6) {
							tooNear = true;
							break;
						}
					}
					if (!tooNear) {
						emptyWindowList.add(new Point(x, y));
					}
				}
			}
		}
		/*
		 * System.out.println(emptyWindowList.size()); for (int i = 0; i <
		 * emptyWindowList.size(); i++) {
		 * System.out.println(emptyWindowList.get(i).x + "," +
		 * emptyWindowList.get(i).y + ","); }
		 */
	}
}
