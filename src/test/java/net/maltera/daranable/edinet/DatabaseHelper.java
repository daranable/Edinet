package net.maltera.daranable.edinet;

import java.io.File;
import java.util.Properties;
import java.util.Random;

public class DatabaseHelper {
	private final Properties vanillaProps;
	private final Random rand = new Random();
	
	private File testDir;
	
	public DatabaseHelper() {
		vanillaProps = System.getProperties();
	}
	
	public void prepare() {
		testDir = new File( System.getProperty( "user.dir" ), "edinet_test_" 
				+ String.format( "%010d", rand.nextInt( Integer.MAX_VALUE ) ) );
		testDir.mkdirs();
		
		Properties props = (Properties) vanillaProps.clone();
		props.setProperty( "os.name", "Testing" );
		props.setProperty( "user.home", testDir.getAbsolutePath() );
		
		System.setProperties( props );
	}
	
	private static void deleteRecursive (File target) {
		File[] contents = target.listFiles();
		if (null != contents) {
			for (File file : contents) {
				if (file.isDirectory())
					deleteRecursive( file );
				else file.delete();
			}
		}
		
		target.delete();
	}
	
	public void cleanup() {
		System.setProperties( vanillaProps );

		deleteRecursive( testDir );
	}
}
