package net.maltera.daranable.edinet.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Term 
extends TermReference {
	private String name;
	private Date start, finish;
	private boolean name_dirty, start_dirty, finish_dirty;
	
	private Term( int year, int serial ) {
		super( year, serial );
	}
	
	public static Term load( Repository repo, ResultSet result ) 
	throws SQLException {
		final Term inst = new Term( result.getInt( "year" ), 
				result.getInt( "serial" ) );
		
		inst.name = result.getString( "name" );
		
		inst.start = result.getDate( "start" );
		inst.finish = result.getDate( "finish" );
		
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
		return (Date) finish.clone();
	}
	
	public void setEndDate( Date end ) {
		if ( this.finish.equals( end ) ) return;
		
		this.finish = (Date) end.clone();
		this.finish_dirty = true;
	}
}
