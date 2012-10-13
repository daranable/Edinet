package net.maltera.daranable.edinet;

import static org.junit.Assert.*;

import java.util.Date;

import net.maltera.daranable.edinet.model.Database;
import net.maltera.daranable.edinet.model.Repository;
import net.maltera.daranable.edinet.model.Term;
import net.maltera.daranable.edinet.model.TermReference;

import org.junit.*;

public class TestTerms {
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
	public void testNewTerm() 
	throws Exception {
		Repository repo = new Repository();
		Database database = repo.getDatabase();
		Date startDate = new Date();
		
		// Create the new Term
		Term term = Term.create( repo );
		
		// Set it's default values
		term.setYear( 2012 );
		term.setName( "Fall" );
		term.setStartDate( startDate );
		
		// Push its values to the db
		term.commit();
		
		// Store the terms serial so we can check it's database values.
		int serial = term.getSerial();
		
		// get rid of our reference.
		term = null;
		TermReference termRef = new TermReference( 2012, serial );
		
		term = repo.getTerm( termRef );
		
		assertTrue( "term is null", null != term );
		
		assertTrue( "date was not stored correctly", 
				!term.getStartDate().equals( startDate ) );
		
		assertEquals( "name", "Fall", term.getName() );
		
		database.shutdown();
	}
	
	@Test
	public void testUpdateTerm()
	throws Exception {
		Repository repo = new Repository();
		Database database = repo.getDatabase();
		
		Term term = Term.create( repo );
		
		term.setYear( 2012 );
		term.setName( "Fall" );
		term.setStartDate( new Date() );
		
		term.commit();
		
		term.setName( "Spring" );
		term.commit();
		
		term = null;
		
		TermReference termRef = new TermReference( 2012, 1 );
		term = repo.getTerm( termRef );
		
		assertEquals( "name", "Spring", term.getName() );
		
		database.shutdown();
	}
}
