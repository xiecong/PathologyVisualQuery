package density;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import database.Point;
import database.Query;
import database.ShapePolygon;

public class DensityData {
	ArrayList<String> ids = new ArrayList<String>();
	ArrayList<Point> pList = new ArrayList<Point>();
	int xWidth = 1106;
	int yWidth = 487;
	int[] tiles = new int[xWidth * yWidth];
	int maxDensity = 0;
	ArrayList<Point> emptyWindowList = new ArrayList<Point>();
	ArrayList<ShapePolygon> sList = new ArrayList<ShapePolygon>();

	double[] windows = { 33.0, 179.0, 39.0, 329.0, 60.0, 379.0, 61.0, 338.0, 65.0, 380.0, 88.0, 382.0, 92.0, 287.0,
			107.0, 422.0, 113.0, 406.0, 114.0, 114.0, 123.0, 289.0, 124.0, 172.0, 128.0, 231.0, 131.0, 165.0, 135.0,
			247.0, 138.0, 197.0, 150.0, 353.0, 153.0, 197.0, 157.0, 211.0, 172.0, 209.0, 188.0, 243.0, 191.0, 192.0,
			192.0, 156.0, 192.0, 171.0, 192.0, 199.0, 193.0, 188.0, 193.0, 250.0, 195.0, 224.0, 195.0, 231.0, 195.0,
			304.0, 201.0, 178.0, 206.0, 207.0, 210.0, 216.0, 224.0, 243.0, 225.0, 186.0, 237.0, 112.0, 242.0, 71.0,
			253.0, 79.0, 253.0, 136.0, 275.0, 378.0, 281.0, 376.0, 284.0, 304.0, 292.0, 355.0, 308.0, 399.0, 309.0,
			299.0, 311.0, 313.0, 315.0, 335.0, 317.0, 329.0, 317.0, 397.0, 322.0, 391.0, 334.0, 307.0, 336.0, 324.0,
			340.0, 212.0, 345.0, 211.0, 350.0, 212.0, 350.0, 286.0, 355.0, 282.0, 355.0, 374.0, 356.0, 172.0, 358.0,
			267.0, 358.0, 321.0, 360.0, 208.0, 360.0, 292.0, 360.0, 384.0, 362.0, 319.0, 364.0, 128.0, 364.0, 189.0,
			365.0, 203.0, 365.0, 254.0, 366.0, 133.0, 367.0, 277.0, 369.0, 201.0, 371.0, 140.0, 371.0, 308.0, 373.0,
			214.0, 374.0, 298.0, 376.0, 272.0, 377.0, 265.0, 384.0, 215.0, 385.0, 374.0, 388.0, 269.0, 395.0, 218.0,
			396.0, 187.0, 397.0, 349.0, 400.0, 194.0, 405.0, 195.0, 406.0, 214.0, 411.0, 193.0, 431.0, 283.0, 436.0,
			281.0, 442.0, 265.0, 445.0, 208.0, 445.0, 291.0, 446.0, 296.0, 448.0, 149.0, 450.0, 198.0, 451.0, 208.0,
			454.0, 144.0, 462.0, 260.0, 465.0, 229.0, 471.0, 218.0, 472.0, 196.0, 474.0, 274.0, 486.0, 260.0, 490.0,
			252.0, 494.0, 247.0, 498.0, 217.0, 502.0, 339.0, 631.0, 372.0, 660.0, 418.0, 681.0, 393.0, 683.0, 91.0,
			684.0, 174.0, 696.0, 388.0, 697.0, 163.0, 706.0, 354.0, 707.0, 180.0, 715.0, 271.0, 718.0, 70.0, 718.0,
			377.0, 723.0, 167.0, 732.0, 311.0, 734.0, 74.0, 734.0, 305.0, 750.0, 249.0, 757.0, 199.0, 758.0, 234.0,
			760.0, 191.0, 761.0, 230.0, 761.0, 252.0, 768.0, 181.0, 768.0, 187.0, 770.0, 207.0, 777.0, 216.0, 793.0,
			121.0, 793.0, 200.0, 795.0, 127.0, 797.0, 114.0, 815.0, 407.0, 817.0, 77.0, 822.0, 374.0, 823.0, 395.0,
			823.0, 437.0, 825.0, 423.0, 826.0, 416.0, 830.0, 92.0, 830.0, 412.0, 831.0, 439.0, 832.0, 342.0, 832.0,
			383.0, 833.0, 73.0, 840.0, 134.0, 843.0, 419.0, 844.0, 360.0, 850.0, 402.0, 851.0, 144.0, 852.0, 395.0,
			853.0, 122.0, 853.0, 430.0, 854.0, 386.0, 855.0, 292.0, 861.0, 146.0, 861.0, 335.0, 868.0, 143.0, 868.0,
			388.0, 869.0, 304.0, 872.0, 291.0, 875.0, 344.0, 877.0, 456.0, 879.0, 384.0, 880.0, 374.0, 881.0, 391.0,
			881.0, 417.0, 886.0, 392.0, 886.0, 414.0, 888.0, 310.0, 889.0, 365.0, 901.0, 395.0, 901.0, 402.0, 903.0,
			390.0, 905.0, 204.0, 906.0, 198.0, 908.0, 222.0, 912.0, 196.0, 912.0, 303.0, 913.0, 385.0, 919.0, 192.0,
			919.0, 204.0, 919.0, 332.0, 920.0, 303.0, 921.0, 208.0, 923.0, 212.0, 923.0, 330.0, 927.0, 184.0, 928.0,
			326.0, 930.0, 303.0, 933.0, 198.0, 936.0, 138.0, 936.0, 213.0, 943.0, 151.0, 944.0, 292.0, 945.0, 349.0,
			945.0, 374.0, 945.0, 386.0, 947.0, 201.0, 954.0, 230.0, 956.0, 245.0, 957.0, 362.0, 958.0, 125.0, 958.0,
			198.0, 963.0, 244.0, 968.0, 302.0, 976.0, 301.0, 977.0, 208.0, 982.0, 205.0, 984.0, 165.0, 988.0, 292.0,
			991.0, 220.0, 999.0, 299.0, 1001.0, 238.0, 1002.0, 159.0, 1003.0, 287.0, 1011.0, 307.0, 1015.0, 222.0,
			1020.0, 223.0, 1022.0, 162.0, 1022.0, 212.0, 1026.0, 298.0, 1032.0, 244.0, 1036.0, 278.0, 1040.0, 234.0,
			1040.0, 291.0, 1045.0, 213.0, 1046.0, 261.0 };

	public void generateCentroid() {
		Query q = new Query();
		q.connection();
		q.centerQuery();
		q.close();
	}

	public void readCentroid() {

		try {
			BufferedReader br = new BufferedReader(new FileReader("data/centroid.txt"));
			String line;// = br.readLine();
			int count = 0;
			while (true) {
				count++;
				line = br.readLine();
				if (line == null) {
					break;
				}

				String[] lineStr = line.split(",");
				// System.out.println(lineStr[0]+" "+lineStr[1]);
				ids.add(lineStr[0]);
				Point p = new Point(Double.parseDouble(lineStr[1]), Double.parseDouble(lineStr[2]));
				pList.add(p);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void findMinMax() {

		double minx = Double.MAX_VALUE;
		double maxx = -Double.MAX_VALUE;
		double miny = Double.MAX_VALUE;
		double maxy = -Double.MAX_VALUE;
		for (int i = 0; i < pList.size(); i++) {
			Point p = pList.get(i);
			if (minx > p.x) {
				minx = p.x;
			}
			if (miny > p.y) {
				miny = p.y;
			}
			if (maxx < p.x) {
				maxx = p.x;
			}
			if (maxy < p.y) {
				maxy = p.y;
			}
		}
		System.out.println(minx + " " + miny + " " + maxx + " " + maxy);
	}

	public void initTiles() {

		for (int i = 0; i < pList.size(); i++) {
			Point p = pList.get(i);
			int xIndex = (int) (p.x / 100);
			int yIndex = (int) (p.y / 100);
			tiles[xIndex * yWidth + yIndex]++;
		}
	}

	public static void main(String[] args) {
		// dt.findMinMax();
		DensityData dt = new DensityData();
		dt.generateCentroid();
	}

	public ArrayList<ShapePolygon> getWindowShapes(int x, int y, int windowSize) {
		readCentroid();
		x += 50;
		y += 50;
		windowSize += 50;
		HashSet<String> markup_ids = new HashSet<String>();
		sList.clear();
		for (int i = 0; i < pList.size(); i++) {
			double px = pList.get(i).x;
			double py = pList.get(i).y;
			if (Math.abs(px - x) < windowSize && Math.abs(py - y) < windowSize) {
				markup_ids.add(ids.get(i));
			}
		}
		Query q = new Query();
		q.connection();
		sList = q.idsQuery(markup_ids);
		q.close();
		return sList;
	}

	public void getEmptyAreaList() {

		readCentroid();
		initTiles();
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
		System.out.println(emptyWindowList.size());
		for (int i = 0; i < emptyWindowList.size(); i++) {
			System.out.println(emptyWindowList.get(i).x + "," + emptyWindowList.get(i).y + ",");
		}
	}
}
