package de.unihannover.swp2015.robots2.visual.resource;

/**
 * All available keys for resources in {@link IResourceHandler}.
 * 
 * @author Rico Schrage
 */
public enum ResConst {
	
	ATLAS_PATH("assets/texturepacker/"),
	ATLAS_NAME("tiles_default_theme"),
	
	DEFAULT_WALL_N("default_wall_n"),
	DEFAULT_WALL_W("default_wall_w"),
	DEFAULT_WALL_E("default_wall_e"),
	DEFAULT_WALL_S("default_wall_s"),
	DEFAULT_FIELD("default_field"),
	DEFAULT_FIELD_1_N("default_field_1_n"),
	DEFAULT_FIELD_1_E("default_field_1_e"),
	DEFAULT_FIELD_1_S("default_field_1_s"),
	DEFAULT_FIELD_1_W("default_field_1_w"),
	DEFAULT_FIELD_2_N("default_field_2_n"),
	DEFAULT_FIELD_2_E("default_field_2_e"),
	DEFAULT_FIELD_3_N("default_field_3_n"),
	DEFAULT_FIELD_3_E("default_field_3_e"),
	DEFAULT_FIELD_3_S("default_field_3_s"),
	DEFAULT_FIELD_3_W("default_field_3_w"),
	DEFAULT_FIELD_4("default_field_4"),
	DEFAULT_FIELD_C_NE("default_field_c_ne"),
	DEFAULT_FIELD_C_ES("default_field_c_es"),
	DEFAULT_FIELD_C_SW("default_field_c_sw"),
	DEFAULT_FIELD_C_WN("default_field_c_wn"),
	DEFAULT_RES_1("default_res_01"),
	DEFAULT_RES_2("default_res_02"),
	DEFAULT_RES_3("default_res_03"),
	DEFAULT_RES_4("default_res_04"),
	DEFAULT_RES_5("default_res_05"),
	DEFAULT_RES_6("default_res_06"),
	DEFAULT_RES_7("default_res_07"),
	DEFAULT_RES_8("default_res_08"),
	DEFAULT_RES_9("default_res_09"),
	DEFAULT_RES_10("default_res_10"),
	DEFAULT_ROBO_NORTH("default_robo_north"),
	DEFAULT_BUBBLE("default_bubble"),
	DEFAULT_FONT("default_font");
	
	private final String name;
	
	private ResConst(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
