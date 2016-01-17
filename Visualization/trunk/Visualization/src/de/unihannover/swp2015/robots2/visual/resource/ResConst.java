package de.unihannover.swp2015.robots2.visual.resource;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

/**
 * All available keys for resources in {@link IResourceHandler}. 
 * The names of the enums are the same as the filenames of the packed textures.
 * 
 * @author Rico Schrage
 */
public enum ResConst {
	
	ATLAS_PATH("assets/theme/", ResType.PATH),
	ATLAS_NAME("theme", ResType.PATH),
	DEFAULT_THEME("default", ResType.PATH),
	
	DEFAULT_WALL("default_wall", ResType.TEX),
	DEFAULT_ONEWAY("default_oneway", ResType.TEX, TextureFilter.Linear),
	DEFAULT_FIELD("default_field", ResType.TEX),
	DEFAULT_FIELD_1("default_field_one", ResType.TEX),
	DEFAULT_FIELD_2("default_field_two", ResType.TEX),
	DEFAULT_FIELD_2_CURVE("default_field_2_c", ResType.TEX),
	DEFAULT_FIELD_3("default_field_three", ResType.TEX),
	DEFAULT_FIELD_4("default_field_four", ResType.TEX),
	DEFAULT_RES_1("default_res_one", ResType.TEX),
	DEFAULT_RES_2("default_res_two", ResType.TEX),
	DEFAULT_RES_3("default_res_three", ResType.TEX),
	DEFAULT_RES_4("default_res_four", ResType.TEX),
	DEFAULT_RES_5("default_res_five", ResType.TEX),
	DEFAULT_RES_6("default_res_six", ResType.TEX),
	DEFAULT_RES_7("default_res_seven", ResType.TEX),
	DEFAULT_RES_8("default_res_eight", ResType.TEX),
	DEFAULT_RES_9("default_res_nine", ResType.TEX),
	DEFAULT_RES_10("default_res_ten", ResType.TEX),
	DEFAULT_ROBO("default_robo", ResType.TEX),
	DEFAULT_BUBBLE("default_bubble", ResType.TEX),
	DEFAULT_STARTPOS("default_startpos", ResType.TEX, TextureFilter.Linear),
	DEFAULT_WARNING("default_warning", ResType.TEX),
	DEFAULT_CONNECTION("default_connection", ResType.TEX),

	DEFAULT_FONT("default_font", ResType.FONT);
	
	/** Name of the resource, should be equal to the real file */
	private final String name;
	/** {@link ResType} */
	private final ResType type;
	/** Filter of the resource (only relevant for TEX and FONT)*/
	private final TextureFilter filter;
	
	/**
	 * Constructs a resource constant, which describes a resource.
	 * 
	 * @param fileName name of the file, which should be loaded when loading this constant.
	 * @param resType type of resource
	 */
	private ResConst(final String fileName, final ResType resType) {
		this(fileName, resType, TextureFilter.Nearest);
	}
	
	private ResConst(final String fileName, final ResType resType, TextureFilter filter) {
		this.name = fileName;
		this.type = resType;
		this.filter = filter;
	}
	
	/**
	 * @return name of the resource
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return type of the resource
	 */
	public ResType getType() {
		return type;
	}
	
	/**
	 * @return filter of the resource, only relevant when type TEX or FONT, null otherwise.
	 */
	public Texture.TextureFilter getFilter() {
		return filter;
	}
	
	/**
	 * Available resources types.
	 * 
	 * @author Rico Schrage
	 */
	public enum ResType {
		PATH, TEX, FONT, AUDIO;
	}
	
}
