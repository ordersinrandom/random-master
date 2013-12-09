package com.jbp.randommaster.draft.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

public class TestMongo1 {

	public static void main(String[] args) {

		try {
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			mongoClient.setWriteConcern(WriteConcern.ACKNOWLEDGED);

			DB db = mongoClient.getDB("TestMongo1");
			DBCollection coll = db.getCollection("TestCollection");

			int index = (new Random()).nextInt(10000);

			for (int i = index; i <= index + 10000; i++) {
				Date now = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				String entryText = "Entry " + i + " " + df.format(now);

				BasicDBObject doc = new BasicDBObject("entry_name", entryText).append("written_time", now);
				coll.insert(doc);
				
				if (i%500==0)
					System.out.println("Written item: "+entryText);
				
			}


			mongoClient.close();

			System.out.println("Data written");

		} catch (Exception e1) {
			e1.printStackTrace(System.err);
		}
	}

}
