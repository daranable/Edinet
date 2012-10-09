package net.maltera.daranable.edinet.model;

import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.common.io.CharStreams;

public class Course {
	private final int id;
	private String teacher, notes, name; 
	private int year, term;
	
	private Course( int id ) {
		this.id = id;
	}
	
	public static Course load( Repository repo, ResultSet result ) 
	throws SQLException, IOException {
		final Course inst = new Course( result.getInt( "id" ) );

		inst.year = result.getInt( "year" );
		inst.term = result.getInt( "term" );
		
		inst.name = result.getString( "name" );
		inst.teacher = result.getString( "teacher" );
		
		Clob notes = result.getClob( "notes" );
		inst.notes = CharStreams.toString( notes.getCharacterStream() );
		notes.free();
		
		return inst;
	}

}
