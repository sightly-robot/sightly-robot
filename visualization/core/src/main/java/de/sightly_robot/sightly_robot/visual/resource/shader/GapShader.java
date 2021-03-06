package de.sightly_robot.sightly_robot.visual.resource.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Convenience class which just initializes a {@link ShaderProgram} with the
 * gap-vertex and gap-fragment shader.
 * 
 * @author Rico Schrage
 */
public class GapShader extends ShaderProgram {

	/**
	 * Constructs a {@link ShaderProgram} with the gap vertex and fragment
	 * shader.
	 */
	public GapShader() {
		super(Gdx.files.internal("assets/shader/gap.vertex"), Gdx.files
				.internal("assets/shader/gap.fragment"));
	}

}
