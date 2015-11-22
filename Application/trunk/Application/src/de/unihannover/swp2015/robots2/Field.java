package de.unihannover.swp2015.robots2;

import java.util.Set;
import java.lang.IllegalArgumentException;

public class Field {
	private double growthRate;
	private int resources;
	private Set <CardinalDirection> passableDirections;
	private CardinalDirection startDirection; // transform into optional in java8
	
	/**
	 * Constructor for field from initial values.
	 * 
	 * @param growthRate Growth rate of resource on this field.
	 * @param initialResources Initial resources on this field.
	 * @param passableDirections Directions a robot can move to from this field.
	 * @param startPosition This is one of the cardinal directions if this is a startPosition.
	 */
	Field(double growthRate, int initialResources, String passableDirections, char startingDirection)
	{
		this.growthRate = growthRate;
		this.resources = initialResources;
		
		setPassableDirections(passableDirections);
		setStartDirection(startingDirection);		
	}
	
	/**
	 * Delegating constructor to Field(4 params). Rest will be defaults. 
	 * 
	 * @param growthRate Growth rate of resource on this field.
	 * @param initialResources Initial resources on this field.
	 * @param passableDirections Directions a robot can move to from this field.
	 */
	Field(double growthRate, int initialResources, String passableDirections)
	{
		this(growthRate, initialResources, passableDirections, 'i'); // 'i' is a little dirty, but will keep the code slim
	}
	
	/**
	 * Delegating constructor to Field(3 params). Rest will be defaults.
	 * 
	 * @param growthRate Growth rate of resource on this field.
	 * @param initialResources Initial resources on this field.
	 */
	Field(double growthRate, int initialResources)
	{
		this(growthRate, initialResources, "");
	}
	
	/**
	 * Delegating constructor to Field(2 params). Rest will be defaults.
	 * 
	 * @param growthRate Growth rate of resource on this field.
	 */
	Field(double growthRate)
	{
		this(growthRate, 10);
	}
	
	/**
	 * Delegating constructor to Field(1 params). Rest will be defaults.
	 */
	Field()
	{
		this(0.);
	}
	
	/**
	 * Sets the directions a robot can go to from here.
	 * 
	 * @param directions A string containing any of nesw standing for the four cardinal directions.
	 */
	public void setPassableDirections(String directions) {
		if (directions.contains("n"))
			this.passableDirections.add(CardinalDirection.NORTH);
		else if (directions.contains("e"))
			this.passableDirections.add(CardinalDirection.EAST);
		else if (directions.contains("s"))
			this.passableDirections.add(CardinalDirection.SOUTH);
		else if (directions.contains("w"))
			this.passableDirections.add(CardinalDirection.WEST);
		// else nothing
	}
	
	/**
	 * Sets this field to a start position and assigns the start direction.
	 * @param direction The direction the robot has to face on start. (n, e, s, w)
	 */
	public void setStartDirection(char direction) {
		if (direction == 'n')
			this.startDirection = CardinalDirection.NORTH;
		else if (direction == 'e')
			this.startDirection = CardinalDirection.EAST;
		else if (direction == 's')
			this.startDirection = CardinalDirection.SOUTH;
		else if (direction == 'w')
			this.startDirection = CardinalDirection.WEST;
		else
			this.startDirection = null;
	}
	
	/** 
	 * Is this a possible start position?
	 * @return Is this a start position?
	 */
	public boolean isStartPosition() {
		if (startDirection != null)
			return true;
		return false;
	}
	
	/**
	 * Getter for growth rate (of resource)
	 * @return resource growth rate.
	 */
	public double getGrowthRate() {
		return growthRate;
	}

	/**
	 * Setter the growth rate on this field.
	 * @param growthRate Field growth rate.
	 */
	public void setGrowthRate(double growthRate) {
		this.growthRate = growthRate;
	}

	/**
	 * Getter for resource amount on this field.
	 * @return current resources.
	 */
	public int getResources() {
		return resources;
	}

	/**
	 * Sets the current resources on this field.
	 * @param resources Amount of resources on the field.
	 */
	public void setResources(int resources) {
		this.resources = resources;
	}

	/**
	 * Returns the start direction for this field, or null if no start position.
	 * @return Starting direction or null.
	 */
	public CardinalDirection getStartPosition() {
		return startDirection;
	}

	/**
	 * Gives this field a starting direction. Robots can therefore start from here from now on.
	 * @param startDirection
	 */
	public void setStartDirection(CardinalDirection startDirection) {
		this.startDirection = startDirection;
	}

	/**
	 * Getter for passableDirections (Anti Walls).
	 * 
	 * @return A set of directions a roboter can got to from this position.
	 */
	public Set<CardinalDirection> getPassableDirections() {
		return passableDirections;
	}	
}
