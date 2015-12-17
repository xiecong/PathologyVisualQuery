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
	int sketchSize = 10;
	double[] sketch = new double[sketchSize * sketchSize];
	ArrayList<Point> SketchResultList = new ArrayList<Point>();
	int selectedSketch = -1;

	public ArrayList<Point> getSketchList() {
		return SketchResultList;
	}

	public void clearSketch() {
		for (int i = 0; i < sketch.length; i++) {
			sketch[i] = 0;
		}
	}

	public void setSelectedSketch(int index) {
		this.selectedSketch = -1;
		if (index >= 0 && index < SketchResultList.size()) {
			Point p = SketchResultList.get(index);
			getShapesinWindow((int) p.x * 100, (int) p.x * 100 + 1000, (int) p.y * 100, (int) p.y * 100 + 1000);
			this.selectedSketch = index;
		}
	}

	public Point getSelectedWindow() {
		if (this.selectedSketch >= 0 && this.selectedSketch < SketchResultList.size()) {
			//System.out.println("not null");
			return SketchResultList.get(this.selectedSketch);
		} else {
			return null;
		}
	}

	public ArrayList<ShapePolygon> getShapeList() {
		return sList;
	}

	public void search() {
		SketchResultList.clear();
		double threshold = 15;
		double sketchMax = 0;
		double sketchMin = Integer.MAX_VALUE;
		for (int x = 0; x < sketchSize; x++) {
			for (int y = 0; y < sketchSize; y++) {
				int index = x * sketchSize + y;
				if (sketchMax < this.sketch[index]) {
					sketchMax = this.sketch[index];
				}
				if (sketchMin > this.sketch[index]) {
					sketchMin = this.sketch[index];
				}
			}
		}
		sketchMax += 1;
		double sketchRange = sketchMax - sketchMin;
		System.out.println("sketch intensity " + sketchMin + "," + sketchMax);
		int count = 0;
		for (int x = 0; x < xWidth - sketchSize; x++) {
			for (int y = 0; y < yWidth - sketchSize; y++) {
				int max = 0;
				int min = Integer.MAX_VALUE;
				for (int i = 0; i < sketchSize; i++) {
					for (int j = 0; j < sketchSize; j++) {
						int index = (x + i) * yWidth + (y + j);
						if (max < this.tiles[index]) {
							max = this.tiles[index];
						}
						if (min > this.tiles[index]) {
							min = this.tiles[index];
						}
					}
				}
				max++;
				int range = max - min;
				double diff = 0;
				for (int i = 0; i < sketchSize; i++) {
					for (int j = 0; j < sketchSize; j++) {

						int index = (x + i) * yWidth + (y + j);
						int index2 = i * sketchSize + j;
						diff += Math.abs(1.0 * (this.tiles[index] - min) / range
								- (this.sketch[index2] - sketchMin) / sketchRange);
					}
				}
				if (diff < threshold) {
					SketchResultList.add(new Point(x, y));
					System.out.println(x * 100 + "," + (x + 10) * 100 + "," + y * 100 + "," + (y + 10) * 100);
					count++;
				}
			}
		}
		System.out.println(count + " results");
	}

	public int getSketchSize() {
		return sketchSize;
	}

	public double[] getSketch() {
		return this.sketch;
	}

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

	public ArrayList<ShapePolygon> getShapesinWindow(int x1, int x2, int y1, int y2) {
		Query q = new Query();
		q.connection();
		sList = q.windowQuery(x1, x2, y1, y2);
		System.out.println(sList.size());
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
