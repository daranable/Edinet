package net.maltera.daranable.edinet.model.sort;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public abstract class RecordFactory<E>
implements Iterable<E> {
	private PreparedStatement statement;
	
	protected abstract PreparedStatement prepareStatement()
	throws SQLException;
	
	protected abstract E transform( ResultSet result )
	throws SQLException;
	
	public Iterator<E> iterator() {
		try {
			return new RecordIterator();
		} catch (SQLException caught) {
			throw new RuntimeException( caught );
		}
	}
	
	private class RecordIterator
	implements Iterator<E> {
		private ResultSet result;
		private E current;
		private RuntimeException exception;
		
		public RecordIterator()
		throws SQLException {
			if (null == statement)
				statement = prepareStatement();
			result = statement.executeQuery();
			transformNext();
		}
		
		private void transformNext()
		throws SQLException {
			if (null == result) return;
			
			if (!result.next()) {
				current = null;
				result.close();
				result = null;
			}
			
			current = transform( result );
		}

		public boolean hasNext() {
			return null != current;
		}

		public E next() {
			if (null != exception) throw exception;
			
			E ret = current;
			
			try {
				transformNext();
			} catch (SQLException caught) {
				exception = new RuntimeException( caught );
			}
			
			return ret;
		}

		public void remove() {
			throw new UnsupportedOperationException(
					"this iterator does not support removal" );
		}
	}
}
