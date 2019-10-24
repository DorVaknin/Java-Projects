package edu.cg.models.Car;

import java.util.LinkedList;
import java.util.List;

import com.jogamp.opengl.*;

import edu.cg.models.IRenderable;

/**
 * A F1 Racing Car.
 *
 */
public class F1Car implements IRenderable {


	public void render(GL2 gl) {
		// TODO: Render the whole car. 
		//       Here You should only render the three parts: back, center and front.
		gl.glPushMatrix();
	    gl.glEnable(gl.GL_COLOR_MATERIAL);
		gl.glPushMatrix();
		new Center().render(gl);
		gl.glPopMatrix();
		gl.glPushMatrix();
	    gl.glTranslated(Specification.F_FRONT_LENGTH/2 + Specification.C_BASE_LENGTH/2, 0, 0);
	    new Front().render(gl);
	    gl.glPopMatrix();

	    //below we are trying to move back half the depth of the center, and then half the depth of the back
	     
		gl.glPushMatrix();
	     
	    gl.glTranslated(-(Specification.C_BASE_DEPTH/2 + (Specification.B_BASE_LENGTH)/2) , 0.0, 0.0);
	    new Back().render(gl);
	    gl.glPopMatrix();
	    gl.glDisable(gl.GL_COLOR_MATERIAL);
		gl.glPopMatrix();


	}

	@Override
	public String toString() {
		return "F1Car";
	}

	
	public void init(GL2 gl) {

	}
	@Override
	public void destroy(GL2 gl) {
		// TODO Auto-generated method stub
		
	}
	// TODO: Put your implementation from previous exercise.
	// 		 * We deleted all components source files (Back.java, Center.java...), so put your components implementation as well.
	//       * You also need to setup material properties for different
	//       * components of the car.
}
