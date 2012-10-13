package net.maltera.daranable.edinet.model;

public class TermReference 
implements Comparable<TermReference> {
	protected int year, serial;
	
	public TermReference( int year, int serial ) {
		this.year = year;
		this.serial = serial;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getSerial() {
		return serial;
	}
	
	public boolean equals( Object o ) {
		if (!( o instanceof TermReference )) return false;
		TermReference that = (TermReference) o;
		
		return that.year == this.year && that.serial == this.serial;
	}
	
	public int compareTo( TermReference that ) {
		if (this.year != that.year)
			return this.year < that.year ? -1 : 1;
		
		if (this.serial != that.serial)
			return this.serial < that.serial ? -1 : 1;
		
		return 0;
	}
	
	@Override
	public int hashCode() {
		return (year & 0xFFFF) << 16 | serial & 0xFFFF;
	}
}
