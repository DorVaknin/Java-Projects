package edu.cg.models.Car;

import com.jogamp.opengl.GL2;

import edu.cg.models.IRenderable;
import edu.cg.models.SkewedBox;

public class Center implements IRenderable {
	// TODO: The following elements are used to build the center of the body.
	// bodyBase is the black base of the center.
	private SkewedBox bodyBase = new SkewedBox(Specification.C_BASE_LENGTH, Specification.C_BASE_HEIGHT, Specification.C_BASE_HEIGHT, Specification.C_BASE_DEPTH, Specification.C_BASE_DEPTH);
	// backSeatBox is the back seat of the center.
	private SkewedBox backSeatBox = new SkewedBox(Specification.C_BACK_LENGTH,Specification.C_BACK_HEIGHT_1,Specification.C_BACK_HEIGHT_2,Specification.C_BACK_DEPTH,Specification.C_BACK_DEPTH);
	// frontBox is used to render both the 'front' and 'back' parts of the centers 
	// Look at the provided example with wireframe mode to better visualize where these elements should be placed.
	private SkewedBox frontBox = new SkewedBox(Specification.C_FRONT_LENGTH,Specification.C_FRONT_HEIGHT_1,Specification.C_FRONT_HEIGHT_2,Specification.C_FRONT_DEPTH_1,Specification.C_FRONT_DEPTH_2);
	// sideBox is used to render both the right-side and left-side of the center of the body.
	private SkewedBox sideBox = new SkewedBox(Specification.C_SIDE_LENGTH,Specification.C_SIDE_HEIGHT_1,Specification.C_SIDE_HEIGHT_2,Specification.C_SIDE_DEPTH_1,Specification.C_SIDE_DEPTH_2);
	
	
	public void render(GL2 gl) {
		// TODO: Render the center of the car.
		Materials.SetBlackMetalMaterial(gl);
		gl.glPushMatrix();
		gl.glTranslated(0.0, 0.0, 0.0);
		bodyBase.render(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		Materials.SetBlackMetalMaterial(gl);
		gl.glTranslated(-1*Specification.C_BACK_LENGTH/4.0, 0.0, 0.0);
		backSeatBox.render(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		Materials.SetRedMetalMaterial(gl);
		gl.glTranslated((Specification.C_FRONT_LENGTH/2)-1*(Specification.C_BASE_LENGTH/2),0,0);
		gl.glRotated(180.0, 0.0, 1.0, 0.0);
		frontBox.render(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslated(Specification.C_BACK_LENGTH,0,0);
		frontBox.render(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslated(0.0,Specification.C_BASE_HEIGHT,Specification.C_BASE_LENGTH/2.0);
		gl.glRotated(90.0, 0.0, 1.0, 0.0);
		sideBox.render(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslated(0.0,Specification.C_BASE_HEIGHT,Specification.C_BASE_LENGTH/(-2.0));
		gl.glRotated(270.0, 0.0, 1.0, 0.0);
		sideBox.render(gl);
		gl.glPopMatrix();

	}

	
	public void init(GL2 gl) {
	}


	@Override
	public void destroy(GL2 gl) {
		// TODO Auto-generated method stub
		
	}
}
