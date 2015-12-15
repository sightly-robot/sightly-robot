package de.unihannover.swp2015.robots2.utils;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * "Polyfill" for missing Java8 optional.
 * Almost compliant with Java8 Optional class.
 * 
 * not supported: ifPresent, filter, map, flatMap, orElseGet, orElseThrow
 *   
 * @author Tim Ebbeke
 */
public class Optional <T> {
	private final T value;
	
	private static final Optional<?> EMPTY = new Optional<>();
	
	/**
	 * Constructor for private EMPTY member. 
	 * Complies with Java8.
	 */
	private Optional() {
		this.value = null;
	}
	
	/**
	 * Creates a new optional with a value.
	 * @param value non-null value of optional.
	 */
	private Optional(T value) {
		this.value = Objects.requireNonNull(value);
	}
	
	/**
	 * Returns an empty Optional instance.
	 * @return An empty Optional instance.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Optional <T> empty() {
		return (Optional<T>) EMPTY;
	}
	
	/**
	 * Creates a new Optional with a value.
	 * 
	 * @param value The value to hold.
	 * @throws java.lang.NullPointerException if value is null
	 * @return An Optional with the value.
	 */
	public static <T> Optional <T> of(T value) {
		return new Optional<>(value);
	}
	
	/**
	 * Creates an Optional class from a value.
	 * The value can be null and if so, empty() is returned.
	 * 
	 * @param value A value to hold (can be null)
	 * @return T An Optional with a present value if the specified value is non-null, otherwise will return empty()
	 */
	public static <T> Optional <T> ofNullable(T value) {
		if (value == null)
			return empty();
		return of(value);
	}
	
	/**
	 * If a value is present in this Optional, returns the value, otherwise throws NoSuchElementException.
	 * @throws NoSuchElementException - if there is no value present
	 * @return the non-null value held by this Optional
	 * @see isPresent
	 */
	public T get() {
		if (value == null) {
			throw new NoSuchElementException("No value present");
		}
		return value;
	}
	
	/**
	 * Return true if there is a value present, otherwise false. 
	 * @return true if there is a value present, otherwise false
	 */
	public boolean isPresent() {
		return value != null;
	}
	
	/**
	 * Return the value if present, otherwise return other. 
	 * @param other the value to be returned if there is no value present, may be null
	 * @return the value, if present, otherwise other
	 */
	public T orElse(T other) {
		return value != null ? value : other;
	}	
	
	/**
	 * Indicates whether some other object is "equal to" this Optional. The other object is considered equal if:
	 *
     *	- it is also an Optional and;
     *	- both instances have no value present or;
     *	- the present values are "equal to" each other via equals(). 
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (!(obj instanceof Optional)) {
			return false;
		}
		
		Optional <?> other = (Optional <?>) obj;
		
		return Objects.equals(value, other.value);
	}
	
	/**
	 * Returns the hash code value of the present value, if any, or 0 (zero) if no value is present. 
	 * @return hash code value of the present value or 0 if no value is present.
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
	
	/**
	 * Returns a non-empty string representation of this Optional suitable for debugging. 
	 * The exact presentation format is unspecified and may vary between implementations and versions.
	 * @return The string representation of this instance. 
	 */
	@Override
	public String toString() {
		if (value != null)
			return String.format("Optional[%s]", value);
		else
			return "Optional.empty";
	}
}
