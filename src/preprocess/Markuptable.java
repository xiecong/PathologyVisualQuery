package preprocess;

import database.Query;

public class Markuptable {
	public static void main(String[] args) {
		Query q = new Query();
		q.connection();
		q.export();
		q.close();
	}
}