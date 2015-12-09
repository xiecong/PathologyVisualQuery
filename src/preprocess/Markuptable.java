package preprocess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import database.Query;

public class Markuptable {
	public static void main(String[] args) {
		Query q = new Query();
		q.connection();
		q.queryTest2();
		q.close();
	}
}