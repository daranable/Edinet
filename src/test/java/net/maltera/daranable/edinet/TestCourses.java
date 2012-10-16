package net.maltera.daranable.edinet;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Date;

import net.maltera.daranable.edinet.model.Course;
import net.maltera.daranable.edinet.model.Database;
import net.maltera.daranable.edinet.model.Repository;
import net.maltera.daranable.edinet.model.Term;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestCourses {
	private static DatabaseHelper dbHelper;
	
	@BeforeClass
	public static void setupClass() {
		dbHelper = new DatabaseHelper();
	}
	
	@Before
	public void setup() {
		dbHelper.prepare();
	}
	
	@After
	public void cleanup() {
		dbHelper.cleanup();
	}
	
	@Test
	public void testNewCourse() 
	throws Exception {
		Repository repo = new Repository();
		Database database = repo.getDatabase();
		Date startDate = new Date();
		
		// Create the new Term to use with my course.
		Term term = Term.create( repo );
		
		// Set it's default values
		term.setYear( 2012 );
		term.setName( "Fall" );
		term.setStartDate( startDate );
		
		// Push its values to the db
		term.commit();
		
		Color color = new Color( 0, 255, 0 );
		
		// Create a new course.
		Course course = Course.create( repo );
		
		course.setTermReference( term );
		course.setTeacher( "Ceto" );
		course.setName( "Underwater Basket Weaving 101" );
		course.setAbbreviation( "UBW-101" );
		course.setColor( color );
		course.setNotes( "Basic information on making baskets under water." );
		
		course.commit();
		
		int courseId = course.getId();
		
		// Get rid of the reference
		course = null;
		
		// Pull it from database to check it committed.
		course = repo.getCourse( courseId );
		
		assertTrue( "course is null", null != course );
		
		assertTrue( "Term does not match", 
				course.getTermRef().equals( term ) );
		assertEquals( "Ceto", course.getTeacher() );
		assertEquals( "Underwater Basket Weaving 101", course.getName() );
		assertEquals( "UBW-101", course.getAbbreviation() );
		assertTrue( "Color does not match", 
				color.equals( course.getColor() ) );
		assertEquals( "Basic information on making baskets under water.",
				course.getNotes() );
		
		
		database.shutdown();
	}
	
	@Test
	public void testUpdateCourse() 
	throws Exception {
		Repository repo = new Repository();
		Database database = repo.getDatabase();
		Date startDate = new Date();
		
		// Create the new Term to use with my course.
		Term term = Term.create( repo );
		
		// Set it's default values
		term.setYear( 2012 );
		term.setName( "Fall" );
		term.setStartDate( startDate );
		
		// Push its values to the db
		term.commit();
		
		Color color = new Color( 0, 255, 0 );
		
		// Create a new course.
		Course course = Course.create( repo );
		
		course.setTermReference( term );
		course.setTeacher( "Ceto" );
		course.setName( "Underwater Basket Weaving 101" );
		course.setAbbreviation( "UBW-101" );
		course.setColor( color );
		course.setNotes( "Basic information on making baskets under water." );
		
		course.commit();
		
		course.setTeacher( "Poseidon" );
		course.setName( "Underwater Basket Weaving 201 - In Space" );
		course.setAbbreviation( "UBW-201" );
		course.setColor( color );
		course.setNotes( "Intermediate information on making baskets under " +
				"water in space." );
		
		course.commit();
		
		int courseId = course.getId();
		
		// Get rid of the reference
		course = null;
		
		// Pull it from database to check it committed.
		course = repo.getCourse( courseId );
		
		assertTrue( "course is null", null != course );
		
		assertTrue( "Term does not match", 
				course.getTermRef().equals( term ) );
		assertEquals( "Poseidon", course.getTeacher() );
		assertEquals( "Underwater Basket Weaving 201 - In Space", 
				course.getName() );
		assertEquals( "UBW-201", course.getAbbreviation() );
		assertTrue( "Color does not match", 
				color.equals( course.getColor() ) );
		assertEquals( "Intermediate information on making baskets under " +
				"water in space.", course.getNotes() );
		
		database.shutdown();
	}
}
