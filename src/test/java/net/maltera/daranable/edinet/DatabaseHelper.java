package net.maltera.daranable.edinet;

import java.io.File;
import java.util.Properties;
import java.util.Random;

public class DatabaseHelper {
	private final Properties vanillaProps;
	private final Properties standardProps;
	
	private final File testDir;
	
	public DatabaseHelper() {
		Random rand = new Random();
		testDir = new File( System.getProperty( "user.dir" ), "edinet_test_" 
				+ String.format( "%010d", rand.nextInt( Integer.MAX_VALUE ) ) );
		testDir.mkdirs();
		
		vanillaProps = System.getProperties();
		
		standardProps = (Properties) vanillaProps.clone();
		standardProps.setProperty( "os.name", "Testing" );
		standardProps.setProperty( "user.home", testDir.getAbsolutePath() );
	}
	
	public void prepare() {
		System.setProperties( standardProps );
	}
	
	private static void deleteRecursive (File target) {
		for (File file : target.listFiles()) {
			if (file.isDirectory())
				deleteRecursive( file );
			else file.delete();
		}
		
		target.delete();
	}
	
	public void cleanup() {
		System.setProperties( vanillaProps );

		deleteRecursive( testDir );
	}
}
