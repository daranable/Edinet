package net.maltera.daranable.edinet.model;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Assignment {
	private int id;
	private String title, description;
	private boolean title_dirty, description_dirty;
	private int time_estimate, time_worked, time_remaining;
	private boolean time_estimate_dirty, time_worked_dirty;
	private boolean time_remaining_dirty, course_dirty;
	private Date assigned, due, completed;
	private boolean assigned_dirty, due_dirty, completed_dirty;
	private boolean in_database, estimate_in_database;
	private Course course;
	private Repository repo;
	
	private Assignment( int id ) {
		this.id = id;
	}
	
	public static Assignment load( Repository repo, ResultSet result ) 
	throws SQLException {
		final Assignment inst = new Assignment( result.getInt( "id" ) );
		
		inst.time_estimate = result.getInt( "time_estimate" );
		inst.time_worked = result.getInt( "time_worked" );
		inst.time_remaining = result.getInt( "time_remaining" );
		inst.course = repo.getCourse( result.getInt( "course_id" ) );
		
		inst.assigned = result.getDate( "assigned" );
		inst.due = result.getDate( "due" );
		inst.completed = result.getDate( "completed" );
		
		inst.title = result.getString( "title" );
		
		Clob description = result.getClob( "description" );
		inst.description =
				description.getSubString( 1, (int) description.length() );
		
		inst.in_database = true;
		inst.estimate_in_database = ( inst.time_estimate != 0 );
		
		inst.repo = repo;
		
		return inst;
	}
	
	public static Assignment create( Repository repo ) {
		final Assignment inst = new Assignment( -1 );
		
		inst.in_database = false;
		inst.estimate_in_database = false;
		
		inst.repo = repo;
		
		return inst;
	}
	
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle( String title ) {
		if ( this.title == title ) return;
		
		this.title = title;
		this.title_dirty = true;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription( String description ) {
		if ( this.description == description ) return;
		
		this.description = description;
		this.description_dirty = true;
	}
	
	public int getTimeEstimate() {
		return time_estimate;
	}
	
	public void setTimeEstimate( int estimate ) {
		if ( estimate_in_database ) {
			throw new IllegalStateException( 
					 "you may not change time estimate once it is in the" +
					 " database" );
		}
		
		this.time_estimate = estimate;
		this.time_estimate_dirty = true;
	}
	
	
	public int getTimeWorked() {
		return time_worked;
	}
	
	public void setTimeWorked( int time_worked ) {
		if ( time_worked == this.time_worked ) return;
		
		this.time_worked = time_worked;
		this.time_worked_dirty = true;
	}
	
	public int getTimeRemaining() {
		return time_remaining;
	}
	
	public void setTimeRemaining( int remaining ) {
		if ( remaining == time_remaining ) return;
		
		time_remaining = remaining;
		time_remaining_dirty = true;
	}
	
	public Date getAssignedDate() {
		return assigned;
	}
	
	public void setAssignedDate( Date assigned ) {
		if ( null == this.assigned ? 
				null == assigned : this.assigned.equals( assigned ) )
					return;
		
		this.assigned = assigned;
		this.assigned_dirty = true;
	}
	
	public Date getDueDate() {
		return due;
	}
	
	public void setDueDate( Date due ) {
		if ( null == this.due ? 
				null == due : this.due.equals( due ) )
					return;
		
		this.due = due;
		this.due_dirty = true;
	}
	
	public Date getCompletedDate() {
		return completed;
	}
	
	public void setCompletedDate( Date completed ) {
		if ( null == this.completed ? 
				null == completed : this.completed.equals( completed ) )
					return;
		
		this.completed = completed;
		this.completed_dirty = true;
	}
	
	public Course getCourse() {
		return course;
	}
	
	public void setCourse( Course course ) {
		if ( null == course ) {
			throw new IllegalStateException(
					"course may not be set to null" );
		}
		
		if ( course.equals( this.course ) ) return;
		
		this.course = course;
		this.course_dirty = true;
	}
	
	private PreparedStatement stmtInsert;
	
	private void insert() 
	throws SQLException {
		if (null == title)
			throw new IllegalStateException(
					"name must be set for new records" );
		if (null == course) {
			throw new IllegalStateException(
					"course must be set for new records" );
		}
		
		final Connection connection = repo.getDatabase().getConnection();
		
		if ( null == this.stmtInsert ) {
			this.stmtInsert = connection.prepareStatement( 
					"INSERT INTO assignments ( \n" +
					"    course_id, title, description, time_estimate, \n" +
					"    time_worked, time_remaining, assigned, due, \n" +
					"    completed \n" +
					") VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )",
					Statement.RETURN_GENERATED_KEYS );
		}
		
		Clob clob = connection.createClob();
		if ( null != description ) {
			clob.setString( 1, description );
		}
		
		this.time_remaining = time_estimate - time_worked;
		
		stmtInsert.setInt( 1, course.getId() );
		stmtInsert.setString( 2, title );
		stmtInsert.setClob( 3, clob );
		stmtInsert.setInt( 4, time_estimate );
		stmtInsert.setInt( 5, time_worked );
		stmtInsert.setInt( 6, time_remaining );
		stmtInsert.setDate( 7, ( null == assigned ? null : 
			new java.sql.Date( assigned.getTime() ) ) );
		stmtInsert.setDate( 8, ( null == due ? null : 
			new java.sql.Date( due.getTime() ) ) );
		stmtInsert.setDate( 9, ( null == completed ? null : 
			new java.sql.Date( completed.getTime() ) ) );
		
		stmtInsert.execute();
		
		ResultSet result = stmtInsert.getGeneratedKeys();
		
		if ( !result.next() ) return;
		
		this.id = result.getInt( "id" );
		this.course_dirty = false;
		this.description_dirty = false;
		this.title_dirty = false;
		this.time_estimate_dirty = false;
		this.time_worked_dirty = false;
		this.assigned_dirty = false;
		this.due_dirty = false;
		this.completed_dirty = false;
		
		this.in_database = true;
	}
	
	public void commit() 
	throws SQLException {
		if ( !in_database ) {
			insert();
			return;
		}
		
		if ( !course_dirty && !title_dirty && !description_dirty
				&& !time_estimate_dirty && !time_worked_dirty 
				&& !time_remaining_dirty && !assigned_dirty && !due_dirty
				&& !completed_dirty ) return;
		
		Connection connection = repo.getDatabase().getConnection();
		PreparedStatement stmt;
		
		stmt = connection.prepareStatement( 
				"UPDATE assignments \n" +
				"SET course_id = ?, title = ?, time_estimate = ?, \n" +
				"    time_worked = ?, time_remaining = ?, assigned = ?, \n" +
				"    due = ?, completed = ?" +
				(description_dirty ? ", description = ? \n" : " \n") +
				"WHERE id = ?" );
		
		stmt.setInt( 1, course.getId() );
		stmt.setString( 2, title );
		stmt.setInt( 3, time_estimate );
		stmt.setInt( 4, time_worked );
		stmt.setInt( 5, time_remaining );
		stmt.setDate( 6, ( assigned == null ?
				null : new java.sql.Date( assigned.getTime() ) ) );
		stmt.setDate( 7, ( null == due ? null : 
			new java.sql.Date( due.getTime() ) ) );
		stmt.setDate( 8, ( null == completed ? null : 
			new java.sql.Date( completed.getTime() ) ) );
		
		if ( description_dirty ) {
			Clob clob = connection.createClob();
			clob.setString( 1, description );
			
			stmt.setClob( 9, clob );
			stmt.setInt( 10, id );
		} else {
			stmt.setInt( 9, id );
		}
		
		stmt.execute();
		
		this.course_dirty = false;
		this.description_dirty = false;
		this.title_dirty = false;
		this.time_estimate_dirty = false;
		this.time_worked_dirty = false;
		this.assigned_dirty = false;
		this.due_dirty = false;
		this.completed_dirty = false;
	}
}
