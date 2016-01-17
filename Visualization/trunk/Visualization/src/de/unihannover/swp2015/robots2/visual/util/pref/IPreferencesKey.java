package de.unihannover.swp2015.robots2.visual.util.pref;

/**
 * Marker interface, which marks an enum as applicable.
 * 
 * @author Rico Schrage
 */
public interface IPreferencesKey {

	/**
	 * Returns a string representation of the object, which will be used as key.
	 * 
	 * @return String key
	 */
	public String getKey();
	
	/**
	 * @return default value of the key
	 */
	public Object getDefault();
	
}
