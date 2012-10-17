package net.maltera.daranable.edinet.model.sort;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.TreeSet;

public class RecordSet<E>
extends TreeSet<E> {
	private static final long serialVersionUID = -353440433715868950L;
	
	private final RecordFactory<? extends E> factory;
	
	public RecordSet( RecordFactory<? extends E> factory ) {
		if (null == factory)
			throw new NullPointerException( "factory may not be null" );
		this.factory = factory;
	}
	
	public RecordSet(
			RecordFactory<? extends E> factory,
			Comparator<? super E> comparator ) {
		super( comparator );
		
		if (null == factory)
			throw new NullPointerException( "factory may not be null" );
		this.factory = factory;
	}
	
	public void load()
	throws SQLException {
		this.clear();
		for (E entry : factory) {
			this.add( entry );
		}
	}
}
