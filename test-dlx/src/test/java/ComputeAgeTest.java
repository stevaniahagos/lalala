import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.datalex.model.Person;

public class ComputeAgeTest {
	
	@Test
	public void computeAgeCorrect() throws ParseException{
		Person person  = new Person();
		
		person.setAge("06/15/1995");
		int age = person.getAge(); 
		
		Assert.assertEquals(21, age);
	}

}
