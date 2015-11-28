package de.unihannover.swp2015.robots2.visual.resource;

public enum ResConst {
	
	ATLAS_PATH("assets/tex/"),
	ATLAS_NAME("superTexture"),
	
	DEFAULT_WALL_H("default_wall_v"),
	DEFAULT_WALL_V("default_wall_v"),
	DEFAULT_FIELD("default_field"),
	DEFAULT_RES_0("default_res_0"),
	DEFAULT_RES_1("default_res_0"),
	DEFAULT_RES_2("default_res_0"),
	DEFAULT_RES_3("default_res_0"),
	DEFAULT_RES_4("default_res_0"),
	DEFAULT_RES_5("default_res_0"),
	DEFAULT_RES_6("default_res_0"),
	DEFAULT_RES_7("default_res_0"),
	DEFAULT_RES_8("default_res_0"),
	DEFAULT_RES_9("default_res_0"),
	DEFAULT_RES_10("default_res_0"),
	DEFAULT_ROBO_NORTH("default_robo_north"),
	DEFAULT_ROBO_SOUTH("default_robo_south"),
	DEFAULT_ROBO_WEST("default_robo_west"),
	DEFAULT_ROBO_EAST("default_robo_east");
	
	private final String name;
	
	private ResConst(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
