package edu.cg.models.Car;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import edu.cg.models.IRenderable;

public class PairOfWheels implements IRenderable {
	// TODO: Use the wheel field to render the two wheels.
	private final Wheel wheel = new Wheel();
	
	
	public void render(GL2 gl) {
		// TODO: Render the pair of wheels.
	  gl.glPushMatrix();
      gl.glTranslated(0.0, 0.0, -1*Specification.TIRE_DEPTH);
      
      GLU glu = new GLU();
      Materials.SetDarkGreyMetalMaterial(gl);

      GLUquadric q = glu.gluNewQuadric();
      glu.gluCylinder(q, Specification.PAIR_OF_WHEELS_ROD_RADIUS, Specification.PAIR_OF_WHEELS_ROD_RADIUS, Specification.PAIR_OF_WHEELS_ROD_DEPTH, 20, 1);
      gl.glTranslated(0.0, 0.0, -1*Specification.TIRE_DEPTH/2.0);
      this.wheel.render(gl);

//      gl.glTranslated(0.0, 0.0, -1*(Specification.TIRE_RADIUS+Specification.PAIR_OF_WHEELS_ROD_DEPTH));
      gl.glTranslated(0.0, 0.0, Specification.PAIR_OF_WHEELS_ROD_DEPTH + Specification.TIRE_DEPTH);
      gl.glRotated(180.0, 0.0, 1.0, 0.0);
      this.wheel.render(gl);
      gl.glPopMatrix();

      glu.gluDeleteQuadric(q);
			
     
	}

	
	public void init(GL2 gl) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String toString() {
		return "PairOfWheels";
	}


	@Override
	public void destroy(GL2 gl) {
		// TODO Auto-generated method stub
		
	}

}
