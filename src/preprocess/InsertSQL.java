package preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//insert data into database
public class InsertSQL {

	public static void readData() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"data/features.txt"));
			String line;// = br.readLine();
			while (true) {
				line = br.readLine();
				if (line == null) {
					break;
				}

				String[] lineStr = line
						.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				for (int i = 0; i < lineStr.length; i++) {
					System.out.println(lineStr[i].trim() + "\t DOUBLE,");
				}

			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				System.out.println("import from \"" + fileEntry.getPath()
						+ "\" of del insert into features;");
			}
		}
	}

	public static void write(final File folder) {
		try {
			File f = new File("data/insert.sql");
			if (f.exists()) {
				System.out.println("exists");
			} else {
				if (f.createNewFile()) {
					System.out.println("new");
				} else {
					System.out.println("failed");
				}
			}
			BufferedWriter output = new BufferedWriter(new FileWriter(f));

			output.write("TRUNCATE TABLE markup IMMEDIATE;\n");

			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					listFilesForFolder(fileEntry);
				} else {
					if (!fileEntry.getName().equals(".DS_Store")) {
						output.write("import from \"" + fileEntry.getPath()
								+ "\" of del insert into markup;\n");
						System.out.println("import from \"" + fileEntry.getPath()
								+ "\" of del insert into markup;");
					}
				}
			}

			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// Test.getOneMargin();
			InsertSQL.write(new File("D:/Workspace/PathologyVisualQuery/data"));
	}
}
