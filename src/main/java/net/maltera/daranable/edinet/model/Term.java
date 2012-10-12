package net.maltera.daranable.edinet.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Term 
extends TermReference {
	private String name;
	private Date start, end;
	private boolean name_dirty, start_dirty, end_dirty;
	
	private Term( int year, int serial ) {
		super( year, serial );
	}
	
	public static Term load( Repository repo, ResultSet result ) 
	throws SQLException {
		final Term inst = new Term( result.getInt( "year" ), 
				result.getInt( "serial" ) );
		
		inst.name = result.getString( "name" );
		
		inst.start = result.getDate( "start" );
		inst.end = result.getDate( "end" );
		
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName( String name ) {
		if ( this.name == name ) return;
		
		this.name = name;
		this.name_dirty = true;
	}
	
	public Date getStartDate() {
		return (Date) start.clone();
	}
	
	public void setStartDate( Date start ) {
		if ( this.start.equals( start ) ) return;
		
		this.start = (Date) start.clone();
		this.start_dirty = true;
	}
	
	public Date getEndDate() {
		return (Date) end.clone();
	}
	
	public void setEndDate( Date end ) {
		if ( this.end.equals( end ) ) return;
		
		this.end = (Date) end.clone();
		this.end_dirty = true;
	}
}
