package de.unihannover.swp2015.robots2.visual.resource;

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
	
	DEFAULT_WALL_N("default_wall_n", ResType.TEX),
	DEFAULT_WALL_W("default_wall_w", ResType.TEX),
	DEFAULT_WALL_E("default_wall_e", ResType.TEX),
	DEFAULT_WALL_S("default_wall_s", ResType.TEX),
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
	DEFAULT_STARTPOS("default_startpos", ResType.TEX),
	
	DEFAULT_FONT("default_font", ResType.FONT),
	DEFAULT_FONT_BIG("default_font_big", ResType.FONT),
	DEFAULT_FONT_TITLE("default_font_title", ResType.FONT);
	
	private final String name;
	private final ResType type;
	
	private ResConst(final String name, final ResType type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public ResType getType() {
		return type;
	}
	
	public static enum ResType {
		PATH, TEX, FONT, AUDIO;
	}
	
}
