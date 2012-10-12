package net.maltera.daranable.edinet.model;

import java.awt.Color;
import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.common.io.CharStreams;

public class Course {
	private final int id;
	private String teacher, notes, name; 
	private int year, term;
	private boolean teacher_dirty, notes_dirty, name_dirty; 
	private boolean year_dirty, term_dirty, color_dirty;
	private Color color;
	private Repository repo;
	
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
		
		int colorInt = result.getInt( "color" );
		inst.color = new Color( colorInt );
		
		inst.repo = repo;
		
		return inst;
	}
	
	public String getTeacher() {
		return teacher;
	}
	
	public void setTeacher( String teacher ) {
		if ( this.teacher == teacher ) return;
		
		this.teacher = teacher;
		this.teacher_dirty = true;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes( String notes ) {
		if ( this.notes == notes ) return;
		
		this.notes = notes;
		this.notes_dirty = true;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName( String name ) {
		if ( this.name == name ) return;
		
		this.name = name;
		this.name_dirty = true;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor( Color color ) {
		if ( this.color == color ) return;
		
		this.color = color;
		this.color_dirty = true;
	}
}
