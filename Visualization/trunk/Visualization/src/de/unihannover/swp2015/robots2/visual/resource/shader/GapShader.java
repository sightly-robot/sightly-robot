package de.unihannover.swp2015.robots2.visual.resource.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Convenience class, just initializes a {@link ShaderProgram} with the
 * gap-vertex and gap-fragment shader.
 * 
 * @author Rico Schrage
 */
public class GapShader extends ShaderProgram {

	public GapShader() {
		super(Gdx.files.internal("assets/shader/gap.vertex"), Gdx.files.internal("assets/shader/gap.fragment"));
	}

}
