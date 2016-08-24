package com.datalex.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.datalex.model.Person;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


/* *
 * Functions: 
 *   Reads specified file and writes into database
 *   Writes into a new file if/when changes have been made to the existing data
 * */
public class FileReaderWriter {
	
	private static final Log log = LogFactory.getLog(FileReaderWriter.class);
	
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String COMMA_DELIMITER = ",";
	
	MongoClient mongo = new MongoClient("localhost", 27017);
	MongoDatabase database = mongo.getDatabase("people");
	MongoCollection collection = database.getCollection("allThePeople");
	FindIterable iterable = collection.find();
			
	public static void main(String[] args) throws Exception{
		Person me = new Person();
		FileReaderWriter frw = new FileReaderWriter();
		File toRead = new File("classpath:");
		log.info(frw.readFile("initPersons.csv"));
	}
	
	private String readFile(String fileName){
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String content = "";
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuffer sb = new StringBuffer();
			
			/*
			 * skips the first line of the csv file so that the header values will not be added to the database
			 * suppressed unused warning because the header String's only purpose is to skip over the file's first line
			 */
			@SuppressWarnings("unused")
			String header = br.readLine();
			
			//iterates through the rest of the file to add to the database
			String line = br.readLine();
			while(null != line){
				sb.append(line);
				writeToDb(line);
				line = br.readLine();
			}
			content = sb.toString();
			log.info(content);
		} catch(Exception e){
			e.printStackTrace();
		}
		return content;
	}
	
	private void writeToDb(String line){
		Person person = new Person();
		String[] contents = line.split(COMMA_DELIMITER);
		for(String something : contents){
			log.info(something);
		}
	}
}
