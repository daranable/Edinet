package net.maltera.daranable.edinet.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.google.common.collect.Maps;

public class Repository {
	private Map<Integer, SoftReference< Course >> 
			courses = Maps.newHashMap();
	
	private Map<Integer, SoftReference< Assignment >> 
			assignments = Maps.newHashMap();
	
	private Connection database;
	
	public Repository()
	throws IOException, SQLException {
		
		database = DriverManager.getConnection( 
				"jdbc:hsqldb:file:" + getDataDir().getPath(), "SA", "" );
	}
	
	private static File getDataDir() 
	throws FileNotFoundException {
		File homeDir;
		
		if ( System.getProperty( "os.name" ).contains( "Windows" ) ) {
			homeDir = new File( System.getenv( "APPDATA" ) );
			if ( !homeDir.isDirectory() )
				throw new FileNotFoundException( 
						"user's Application Data directory "
						+ " does not exist or is not a directory" );
			
			homeDir = new File( homeDir, "Edinet" );
		} else {
			homeDir = new File( System.getProperty( "user.home" ) );
			if ( !homeDir.isDirectory() )
				throw new FileNotFoundException( "user's home directory"
						+ " does not exist or is not a directory" );
			
			homeDir = new File( homeDir, ".edinet" );
		}
		
		homeDir.mkdir();
		
		return homeDir;
	}
	
	public Connection getDbConnection() {
		return database;
	}
	
	private PreparedStatement stmtCourseById;
	
	public Course getCourse( int id ) 
	throws SQLException, IOException {
		SoftReference<Course> cached = courses.get( id );
		if (null != cached && null != cached.get())
			return cached.get();
		
		if (null == stmtCourseById) {
			stmtCourseById = database.prepareStatement(
					"SELECT course.*\n" +
					"FROM courses AS course\n" +
					"WHERE course.id = ?" );
		}
		
		stmtCourseById.setInt( 0, id );
		ResultSet result = stmtCourseById.executeQuery();
		if (!result.next()) return null;
		
		Course inst = Course.load( this, result );
		courses.put( id, new SoftReference<Course>( inst ) );
		return inst;
	}
}
