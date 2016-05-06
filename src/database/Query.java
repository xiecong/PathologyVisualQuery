package database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//do the query of DataBase
public class Query {

	String url = "jdbc:mysql://127.0.0.1:3306/SAMPLE";
	String user = "root";
	String password = "";
	Connection con;
	Statement stmt;
	ShapeData shapeSketchdata;
	DensityData densityData;

	/*
	 * public static void main(String[] args) { Query st = new Query();
	 * st.connection(); // st.query(); st.writetoFile(); st.close(); }
	 */
	public Query(ShapeData data) {
		this.shapeSketchdata = data;
	}
	public Query(DensityData densityData){
		this.densityData = densityData;
	}

	public Query() {
	}

	public void connection() {
		try {
			// Load the driver
			//Class.forName("com.ibm.db2.jcc.DB2Driver");

			// Create the connection using the IBM Data Server Driver for JDBC
			// and SQLJ
			con = DriverManager.getConnection(url, user, password);
			// Commit changes manually
			con.setAutoCommit(false);

			// Create the Statement
			stmt = con.createStatement();
		} catch (Exception e) {
			System.err.println("Could not load JDBC driver");
			System.out.println("Exception: " + e);
			e.printStackTrace();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<ShapePolygon> windowQuery(double x1, double x2, double y1, double y2) {
		ArrayList<ShapePolygon> shapeList = new ArrayList<ShapePolygon>();
		try {
			String query = "select * from markup where cx between " + x1 + " and " + x2 + " and cy between " + y1
					+ " and " + y2;
			System.out.println(query);
			ResultSet rs = stmt.executeQuery(query.toUpperCase());
			while (rs.next()) {
				shapeList.add(new ShapePolygon(rs.getString("polygon")));
			}
			rs.close();

		} catch (SQLException ex) {
			System.err.println("SQLException information");
			while (ex != null) {
				System.err.println("Error msg: " + ex.getMessage());
				System.err.println("SQLSTATE: " + ex.getSQLState());
				System.err.println("Error code: " + ex.getErrorCode());
				ex.printStackTrace();
				ex = ex.getNextException();
			}
		}
		return shapeList;
	}

	public void densityQuery() {
		try {
			String query = "select * from markup";
			ResultSet rs = stmt.executeQuery(query.toUpperCase());
			while (rs.next()) {
				double x = rs.getDouble("cx");
				double y = rs.getDouble("cy");
				int xIndex = (int) (x / 100);
				int yIndex = (int) (y / 100);
				densityData.getTiles()[xIndex * densityData.getHeight() + yIndex]++;
			}
			// Close the ResultSet
			rs.close();

		} catch (SQLException ex) {
			System.err.println("SQLException information");
			while (ex != null) {
				System.err.println("Error msg: " + ex.getMessage());
				System.err.println("SQLSTATE: " + ex.getSQLState());
				System.err.println("Error code: " + ex.getErrorCode());
				ex.printStackTrace();
				ex = ex.getNextException();
			}
		}
	}

	public void matchShape(ArrayList<Double> sketchList) {
		shapeSketchdata.clearShapes();
		try {
			String query = "select * from markup limit 100000";
			ResultSet rs = stmt.executeQuery(query.toUpperCase());
			while (rs.next()) {
				String[] turnings = rs.getString("turnings").trim().split(",");
				ArrayList<Double> tList = new ArrayList<Double>();

				for (int i = 0; i < turnings.length; i++) {
					tList.add(Double.parseDouble(turnings[i]));
				}
				double dis = ShapePolygon.turningDistantceL1(tList, sketchList);
				if (dis < 9) {
					shapeSketchdata.addShape(rs.getString("polygon"));
					// System.out.println(rs.getString("markup_id").trim());
				}
			}
			// Close the ResultSet
			rs.close();

		} catch (SQLException ex) {
			System.err.println("SQLException information");
			while (ex != null) {
				System.err.println("Error msg: " + ex.getMessage());
				System.err.println("SQLSTATE: " + ex.getSQLState());
				System.err.println("Error code: " + ex.getErrorCode());
				ex.printStackTrace();
				ex = ex.getNextException();
			}
		}
	}

	public void export() {
		ArrayList<String> outputList = new ArrayList<String>();
		try {
			String query = "select * from markup";
			ResultSet rs = stmt.executeQuery(query.toUpperCase());
			int count = 0;
			while (rs.next()) {

				ShapePolygon sp = new ShapePolygon(rs.getString("polygon"));
				String resultStr = rs.getString("markup_id").trim() + ",\"" + rs.getString("polygon").trim() + "\",\""
						+ rs.getString("turnings").trim() + "\",\"" + rs.getString("angles").trim() + "\"," + sp.cx
						+ "," + sp.cy;
				outputList.add(resultStr);
				count++;
				if (count % 10000 == 0) {
					write(outputList, "data/markup_" + count + ".csv");
					outputList.clear();
				}
			}
			write(outputList, "data/markup_" + count + ".csv");
			outputList.clear();
			rs.close();

		} catch (SQLException ex) {
			System.err.println("SQLException information");
			while (ex != null) {
				System.err.println("Error msg: " + ex.getMessage());
				System.err.println("SQLSTATE: " + ex.getSQLState());
				System.err.println("Error code: " + ex.getErrorCode());
				ex.printStackTrace();
				ex = ex.getNextException();
			}
		}

	}

	public void close() {

		// Close the Statement
		try {
			stmt.close();

			// Connection must be on a unit-of-work boundary to allow close
			con.commit();

			// Close the connection
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void write(ArrayList<String> outputList, String fileName) {

		BufferedWriter bufferWriter = null;
		try {
			File file = new File(fileName);
			bufferWriter = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < outputList.size(); i++) {
				bufferWriter.write(outputList.get(i) + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferWriter != null)
				try {
					bufferWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	// query from ECCENTRICITY, area, and MINOR_AXIS / MAJOR_AXIS
	public void writetoFile() {
		try {
			// this is the query
			// String query =
			// "select features.markup_id, AREA , PERIMETER , ECCENTRICITY ,
			// CIRCULARITY , MAJOR_AXIS ,MINOR_AXIS , EXTENT_RATIO ,polygon from
			// features, markup where features.markup_id = markup.markup_id
			// order by features.major_axis desc fetch first 10 rows only";
			String query = "select * from markup";

			System.out.println("Query is: \"" + query + "\"");
			// Execute a query and generate a ResultSet instance
			ResultSet rs = stmt.executeQuery(query.toUpperCase());
			System.out.println("Result is:");
			// Print all of the employee numbers to standard output device
			int count = 0;
			ArrayList<String> outputList = new ArrayList<String>();
			while (rs.next()) {
				/*
				 * System.out.println(rs.getString("major_axis") + ";" +
				 * rs.getString("minor_axis") + ";" + rs.getString("polygon"));
				 */
				String output = rs.getString("markup_id").trim() + ",\"" + rs.getString("polygon").trim() + "\",\"";
				ShapePolygon s = new ShapePolygon(rs.getString("polygon"));

				ArrayList<Double> tList = s.turningList;
				for (int i = 0; i < tList.size(); i++) {
					output += tList.get(i);
					if (i != tList.size() - 1) {
						output += ",";
					}
				}
				output += "\",\"";
				ArrayList<Double> aList = s.angleList;
				for (int i = 0; i < aList.size(); i++) {
					output += aList.get(i);
					if (i != aList.size() - 1) {
						output += ",";
					}
				}
				output += "\"\n";
				// System.out.print(output);
				count++;
				outputList.add(output);
				if (count % 100000 == 0) {
					System.out.println(count + "," + output);
					write(outputList, "data/markup_" + count);
					outputList.clear();
				}
			}
			// Close the ResultSet
			write(outputList, "data/markup_" + count);
			outputList.clear();
			rs.close();

		} catch (SQLException ex) {
			System.err.println("SQLException information");
			while (ex != null) {
				System.err.println("Error msg: " + ex.getMessage());
				System.err.println("SQLSTATE: " + ex.getSQLState());
				System.err.println("Error code: " + ex.getErrorCode());
				ex.printStackTrace();
				ex = ex.getNextException(); // For drivers that support chained
											// exceptions
			}
		}
	} // End main
}
