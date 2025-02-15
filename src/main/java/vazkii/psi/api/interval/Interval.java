package vazkii.psi.api.interval;

public interface Interval<T>  {
	
	/**
	 * Returns a new interval that covers this interval and the one given as argument.
	 */
	Interval<T> combine(Interval<T> with);
	
	boolean isValid();
	
}
