package edu.cg.models.Car;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import edu.cg.models.IRenderable;

public class Wheel implements IRenderable {

	
	public void render(GL2 gl) {
		// TODO: Render the wheel. 
		// The wheel should be in the center relative to its local coordinate system.
		GLU glu = new GLU();
		GLUquadric q = glu.gluNewQuadric();
        Materials.setMaterialTire(gl);
        gl.glPushMatrix();
        gl.glTranslated(0.0, 0.0, -Specification.TIRE_DEPTH/2);
        
        glu.gluCylinder(q, Specification.TIRE_RADIUS, Specification.TIRE_RADIUS, Specification.TIRE_DEPTH, 20, 1);
        gl.glRotated(180.0, 1.0, 0.0, 0.0);
        
        glu.gluDisk(q, Specification.TIRE_RADIUS - 0.01 , Specification.TIRE_RADIUS, 20, 1);
        Materials.setMaterialRims(gl);
        glu.gluDisk(q, 0.0, Specification.TIRE_RADIUS - 0.01, 20, 1);
        gl.glRotated(180.0, 1.0, 0.0, 0.0);
        gl.glTranslated(0.0, 0.0, Specification.TIRE_DEPTH);
        
        glu.gluDisk(q, Specification.TIRE_RADIUS - 0.01 , Specification.TIRE_RADIUS, 20, 1);
        Materials.setMaterialRims(gl);
        glu.gluDisk(q, 0.0, Specification.TIRE_RADIUS - 0.01, 20, 1);
        gl.glRotated(180.0, 1.0, 0.0, 0.0);
        gl.glTranslated(0.0, 0.0, -2*Specification.TIRE_DEPTH);
       
        gl.glPopMatrix();
	}

	
	public void init(GL2 gl) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		return "Wheel";
	}


	@Override
	public void destroy(GL2 gl) {
		// TODO Auto-generated method stub
		
	}

}
