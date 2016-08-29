package com.datalex.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Operations {
	
	FileReaderWriter frw = new FileReaderWriter();
	
	MongoClient mongo = new MongoClient("localhost", 27017);
	MongoDatabase database = mongo.getDatabase("people");
	MongoCollection<Document> collection = database.getCollection("allThePeople");
	List<Document> iterable = collection.find().into(new ArrayList<Document>());
	
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	public void displayOptions() throws Exception{
		System.out.println("Choose your next operation: \n1 - View all records\n2 - add a record \n3 - edit a record \n4 - delete a record \n5 - write to file \n6 - exit");
		String answer = reader.readLine();
		
		try{
			switch(Integer.parseInt(answer)){
			case 1: viewPeople(); break;
			case 2: addPerson(); break;
			case 3: editPerson(); break;
			case 4: deletePerson(); break;
			case 5: writeFile(); break;
			case 6: System.exit(0);
			default: System.out.println("Invalid entry. Please enter again"); displayOptions(); break;
			}
		} catch(NumberFormatException e){
			displayOptions();
		}
	}
	
	private static final String[] fields = {"firstName", "lastName", "streetAddress", "city", 
			"birthDate", "contacts"};
	
	public void addPerson(){
		
	}

	public void editPerson(){
		
	}
	
	public void deletePerson() throws IOException{
		System.out.println("enter oid to delete: ");
		String deleteQuery = reader.readLine();
		
		collection.findOneAndDelete(Filters.eq("oid", deleteQuery));
	}
	
	/**
	 * Iterates through the existing records in the database and then 
	 * displays the user options
	 * @throws Exception
	 */
	public void viewPeople() throws Exception{
		
		for(Document doc : iterable){
			System.out.println(doc.toJson());
		}
		
		displayOptions();
	}
	
	public void writeFile(){
		
	}
}
