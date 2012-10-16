package net.maltera.daranable.edinet.model;

import java.awt.Color;
import java.io.IOException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.common.io.CharStreams;

public class Course {
	private int id;
	private String teacher, notes, name, abbreviation; 
	private TermReference termRef;
	private boolean teacher_dirty, notes_dirty, name_dirty, abbreviation_dirty; 
	private boolean color_dirty, termRef_dirty;
	private Color color;
	private Repository repo;
	private boolean in_database;
	
	private Course( int id ) {
		this.id = id;
	}
	
	public static Course load( Repository repo, ResultSet result ) 
	throws SQLException, IOException {
		final Course inst = new Course( result.getInt( "id" ) );
		
		
		inst.termRef = new TermReference( result.getInt( "year" ),
				result.getInt( "term" ) );
		
		inst.name = result.getString( "name" );
		inst.abbreviation = result.getString( "abbreviation" );
		inst.teacher = result.getString( "teacher" );
		
		Clob notes = result.getClob( "notes" );
		inst.notes = CharStreams.toString( notes.getCharacterStream() );
		notes.free();
		
		int colorInt = result.getInt( "color" );
		inst.color = new Color( colorInt );
		
		inst.in_database = true;
		
		inst.repo = repo;
		
		return inst;
	}
	
	public static Course create( Repository repo ) {
		final Course inst = new Course( -1 );
		
		inst.in_database = false;
		inst.repo = repo;
		
		return inst;
	}
	
	public int getId() {
		return id;
	}
	
	public TermReference getTermRef() {
		return termRef;
	}
	
	public void setTermReference( TermReference termRef ) {
		if ( null == this.termRef ? 
				null == termRef : this.termRef.equals( termRef ) )
					return;
		
		this.termRef = termRef;
		this.termRef_dirty = true;
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
	
	public String getAbbreviation() {
		return abbreviation;
	}
	
	public void setAbbreviation( String abbreviation ) {
		if ( this.abbreviation == abbreviation ) return;
		
		this.abbreviation = abbreviation;
		this.abbreviation_dirty = true;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor( Color color ) {
		if ( this.color == color ) return;
		
		this.color = color;
		this.color_dirty = true;
	}
	
	private PreparedStatement stmtInsert;
	
	private void insert() 
	throws SQLException {
		if ( null == termRef ) {
			throw new IllegalStateException(
					"termRef must be set for new records" );
		}
		
		if ( null == abbreviation | "".equals( abbreviation ) ) {
			throw new IllegalStateException(
					"abbreviation must be set for new records" );
		}
		
		Connection connection = this.repo.getDatabase().getConnection();
		
		if ( null == this.stmtInsert ) {
			this.stmtInsert = connection.prepareStatement( 
					"INSERT INTO courses ( \n" +
					"    year, term, teacher, name, \n" +
					"    color, notes, abbreviation \n" +
					") VALUES ( ?, ?, ?, ?, ?, ?, ? )",
					Statement.RETURN_GENERATED_KEYS );
		}
		
		Clob clob = connection.createClob();
		clob.setString( 1, this.notes );
		
		stmtInsert.setInt( 1, this.termRef.getYear() );
		stmtInsert.setInt( 2, this.termRef.getSerial() );
		stmtInsert.setString( 3, this.teacher );
		stmtInsert.setString( 4, this.name );
		stmtInsert.setInt( 5, this.color.getRGB() );
		stmtInsert.setClob( 6, clob );
		stmtInsert.setString( 7, this.abbreviation );
		
		stmtInsert.executeUpdate();
		
		ResultSet result = stmtInsert.getGeneratedKeys();
		
		if ( !result.next() ) return;
		
		this.id = result.getInt( "id" );
		this.in_database = true;
		this.termRef_dirty = false;
		this.teacher_dirty = false;
		this.notes_dirty = false;
		this.name_dirty = false;
		this.color_dirty = false;
		this.abbreviation_dirty = false;
	}
	
	public void commit() 
	throws SQLException {
		if (!in_database) {
			insert();
			return;
		}
		
		if ( !termRef_dirty && !teacher_dirty && !notes_dirty && !name_dirty
				&& !notes_dirty && !color_dirty && !abbreviation_dirty ) return;
		
		Connection connection = repo.getDatabase().getConnection();
		PreparedStatement stmt;
		
		stmt = connection.prepareStatement( 
				"UPDATE courses \n" +
				"SET year = ?, term = ?, teacher = ?, name = ?, \n" +
				"abbreviation = ?, color = ?" +
				( this.notes_dirty ? ", notes = ? \n" : " \n" )+
				"WHERE id = ?" );
		
		stmt.setInt( 1, termRef.getYear() );
		stmt.setInt( 2, termRef.getSerial() );
		stmt.setString( 3, teacher );
		stmt.setString( 4, name );
		stmt.setString( 5, abbreviation );
		stmt.setInt( 6, color.getRGB() );
		if ( notes_dirty ) {
			Clob clob = connection.createClob();
			clob.setString( 1, notes );
			
			stmt.setClob( 7, clob );
			stmt.setInt( 8, id );
		} else {
			stmt.setInt( 7, id );
		}
		
		stmt.executeUpdate();
	}
}
