package com.datalex.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.datalex.model.Person;


/* *
 * Functions: 
 *   Reads specified file and writes into database
 *   Writes into a new file if/when changes have been made to the existing data
 * */
public class FileReaderWriter {
	
	private static final Log log = LogFactory.getLog(FileReaderWriter.class);
	
	public static void main(String[] args) throws Exception{
		Person me = new Person();
		log.info("MY AGE IS: " + me.computeAge("08/29/1996"));
	}
}
