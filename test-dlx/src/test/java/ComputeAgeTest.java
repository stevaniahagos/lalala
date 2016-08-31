import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.datalex.model.Person;

public class ComputeAgeTest {
	@Test
	public void computeAgeCorrect(){
		try{
			Person person  = new Person();
			person.setAge("07/29/1994");
			int age = person.getAge(); 
			Assert.assertEquals(22, age);			
		} catch(ParseException e){
			System.err.println(e.getMessage());
		}
	}

	@Test
	public void computeAgeLeapYear(){
		try{
			Person person = new Person();
			person.setAge("02/29/2012");
			int age = person.getAge();
			Assert.assertEquals(4, age);
		} catch(ParseException e){
			System.err.println(e.getMessage());
		}
	}
}
