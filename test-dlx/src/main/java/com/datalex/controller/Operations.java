package com.datalex.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.datalex.model.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * Functions:
 *  -- CRUD operations
 *  -- write existing mongoDb data into a new file
 *  -- logic for parsing the strings found in initial csv file to add to database
 */
public class Operations {
	
	 MongoClient mongo = new MongoClient("localhost", 27017);
	 MongoDatabase database = mongo.getDatabase("people");
	 MongoCollection<Document> collection = database.getCollection("allThePeople");
	
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	ObjectMapper mapper = new ObjectMapper();
	
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String COMMA_DELIMITER = ",";
	private static final String SEMI_COLON_DELIMITER = "; ";
	private static final String[] fields = {"firstName", "lastName", "streetAddress", "city", 
			"birthDate", "contacts"};
	private static final String[] fileFields = {"id", "firstName", "lastName", "streetAddress", "city", 
			"birthDate", "age", "contacts"};
	
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
			System.out.println("Invalid entry. Please enter again");
			displayOptions();
		}
	}
	
	public void addPerson() throws Exception{
		Person person = new Person();
		System.out.println("Enter your first name: ");
		person.setFirstName(reader.readLine());
		System.out.println("Enter your last name: ");
		person.setLastName(reader.readLine());
		System.out.println("Enter your address: ");
		person.setStreetAddress(reader.readLine());
		System.out.println("Enter your city: ");
		person.setCity(reader.readLine());
		System.out.println("Enter your birthdate: (Format: mm/dd/yyyy)");
		person.setBirthDate(reader.readLine());
		person.setAge(person.getBirthDate());
		System.out.println("Enter contact info: <Format: \"TYPE: CONTENT\">\n (for multiple records, please separate with a semi colon \":\")"
				+ "\n please make sure all contact types are unique to avoid overwriting your contact information");
		person.setContactInfos(reader.readLine());
		
		Document personToAdd = Document.parse(mapper.writeValueAsString(person));
		collection.insertOne(personToAdd);
		
		displayOptions();
	}

	public void editPerson() throws Exception{
	    System.out.println("Enter document _id to update: ");
	    String updateId = reader.readLine();
	    Document update = collection.find(Filters.eq("_id", new ObjectId(updateId))).first();
	    System.out.println("For your reference, here is the document's existing data: \n" + update.toJson());
	    System.out.println("Please choose what field to update: \n1 - first name \n2 - last name \n3 - street address"
	    		+ "\n4 - city \n5 - birthday \n6 - contact information");
	    String updateField = reader.readLine();
	    System.out.println("Enter your new value: ");
	    String updatedValue = reader.readLine();
	    Person person = mapper.readValue(update.toJson(), Person.class);
	    
	    Document updatedPerson = editOptions(person, updateField, updatedValue);
	    collection.findOneAndReplace(Filters.eq("_id", new ObjectId(updateId)), updatedPerson);
	    
		displayOptions();
	}
	
	public Document editOptions(Person person, String updateField, String updatedValue) throws Exception{
		try{
			switch(Integer.parseInt(updateField)){
		    case 1: person.setFirstName(updatedValue); break;
		    case 2: person.setLastName(updatedValue); break;
		    case 3: person.setStreetAddress(updatedValue); break;
		    case 4: person.setCity(updatedValue); break;
		    case 5: person.setBirthDate(updatedValue); person.setAge(person.getBirthDate()); break;
		    case 6: person.setContactInfos(updatedValue); break;
		    default: System.out.println("Invalid entry. please enter again");
		    }
		} catch(NumberFormatException e){
			System.err.println("Invalid entry. Please enter again");
			editOptions(person, updateField, updatedValue);
		}
		
		Document updatedPerson  = Document.parse(mapper.writeValueAsString(person));
		return updatedPerson;
	}
	
	public void deletePerson() throws Exception{
		System.out.println("enter oid to delete: ");
		String deleteQuery = reader.readLine();
		
		collection.findOneAndDelete(Filters.eq("_id", new ObjectId(deleteQuery)));
		displayOptions();
	}
	
	/**
	 * Iterates through the existing records in the database and then 
	 * displays the user options
	 * @throws Exception
	 */
	public void viewPeople() throws Exception{
		
		List<Document> iterable = collection.find().into(new ArrayList<Document>());
		
		for(Document doc : iterable){
			System.out.println(doc.toJson());
		}
		
		displayOptions();
	}
	
	public void writeFile() throws Exception{
		List<Document> iterable = collection.find().into(new ArrayList<Document>());
		
		File file = new File("src/main/resources/com/datalex/new/data/Persons.csv");
		if(!file.exists()){
			file.getAbsoluteFile().getParentFile().mkdirs();
			file.createNewFile();
		}

		System.out.println("FILE PATH: " + file.getAbsolutePath());
		
		
		FileWriter writer = new FileWriter(file.getAbsolutePath());
		writer.append(Arrays.toString(fileFields).substring(1, Arrays.toString(fileFields).length() - 1).concat(NEW_LINE_SEPARATOR));
		
		for(Document doc : iterable){
			doc.keySet().forEach(key -> {
				//removing foreign characters from key (first and last element of fileFields contain square brackets)
				try {
					//code logic for parsing the "contacts" mongoDb sub-document
					if(key.equals("contactInfos")){
						Document contactDoc = (Document) doc.get(key);
						contactDoc.keySet().forEach(contactKey -> {
							try {
								writer.append(contactKey + ": " + String.valueOf(contactDoc.get(contactKey)).concat(SEMI_COLON_DELIMITER));
							} catch (Exception e) {System.out.println(e.getMessage());}
						});
					} else{						
						writer.append(String.valueOf(doc.get(key)).concat(COMMA_DELIMITER));
					}
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			});
			
			writer.append(NEW_LINE_SEPARATOR);
		}
		
		try{
			writer.close();
		}catch(Exception e){
			
		}
		displayOptions();
	}
	
	/**
	 * converts passed Document into a Person object and adds into the database
	 * @param person
	 */
	public void parsePerson(Document doc) throws Exception{
		
		Person person = new Person();
		person.setFirstName(doc.getString("firstName"));
		person.setLastName(doc.getString("lastName"));
		person.setStreetAddress(doc.getString("streetAddress"));
		person.setCity(doc.getString("city"));
		person.setBirthDate(doc.getString("birthDate"));
		person.setAge(person.getBirthDate());
		person.setContactInfos(doc.getString("contacts"));
		
		collection.insertOne(Document.parse(mapper.writeValueAsString(person)));
	}
	
	/**
	 * Reads the data found in initial csv file and writes into database
	 * if collection does not have any existing records as of start time
	 * @param fileName
	 * @return String of file contents
	 */
	public void readFile(String fileName){ 
		
		if(collection.count() == 0){
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(fileName).getFile());
			try{
				BufferedReader br = new BufferedReader(new FileReader(file));
				
				/*
				 * skips the first line of the .csv file so that the header values will not be added to the database
				 * suppressed unused warning because the header String's only purpose is to skip over the file's first line
				 */
				@SuppressWarnings("unused")
				String header = br.readLine();
				
				//iterates through the rest of the file to add to the database
				String line = br.readLine();
				while(null != line){
					writeToDb(line);
					line = br.readLine();
				}
				
				br.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		System.out.println("No init. Collection contains documents");
	}
	

	
	/**
	 * Converts the data from .csv file into a Person object and saves it into the database
	 * @param line
	 * @throws Exception 
	 */
	private void writeToDb(String line) throws Exception{
		String[] contents = line.split(COMMA_DELIMITER);
		Document toAdd = new Document();
		for(String field : fields){
			for(String content : contents){
				if(ArrayUtils.indexOf(fields, field) == ArrayUtils.indexOf(contents, content))
					toAdd.put(field, content);
			}
		}
		parsePerson(toAdd);
	}
}
