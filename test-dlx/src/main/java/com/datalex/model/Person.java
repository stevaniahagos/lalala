package com.datalex.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Person {
	
	private String firstName;
	private String lastName;
	private String streetAddress;
	private String city;
	private String birthDate;
	private int age;
	private HashMap<String, String> contactInfos;
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getStreetAddress() {
		return streetAddress;
	}
	
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public HashMap<String, String> getContactInfos() {
		return contactInfos;
	}
	
	public void setContactInfos(HashMap<String, String> contactInfos) {
		this.contactInfos = contactInfos;
	}
	
	public int computeAge(String birthDate) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/DD/YYYY");
		Calendar bday = Calendar.getInstance();
		int personAge = 0;
		
		return personAge;
	}

}
