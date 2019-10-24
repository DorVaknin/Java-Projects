package edu.cg.models;

import java.io.File;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import edu.cg.algebra.Vec;

public class SkewedBox implements IRenderable {
	// TODO: Add you implementation from previous exercise.
	//       * Note you may want to enable textures here to render
	//         the wooden boxes.
	private double length, height1, height2, depth1, depth2;
	private int is_texture;
	private Texture wooden_box_texture;

	public SkewedBox() {
		length = .1;
		height1 = .2;
		height2 = .1;
		depth1 = .2;
		depth2 = .1;
	};

	public SkewedBox(double length, double h1, double h2, double d1, double d2) {
		this.length = length;
		this.height1 = h1;
		this.height2 = h2;
		this.depth1 = d1;
		this.depth2 = d2;
	}
	
	public SkewedBox(double size_of_box, int is_texture) {
		this.length = size_of_box;
		this.height1 = size_of_box;
		this.height2 = size_of_box;
		this.depth1 = size_of_box;
		this.depth2 = size_of_box;
		this.is_texture = is_texture;
	}


	public void render(GL2 gl) {
		if(this.is_texture == 1) {
			gl.glEnable(gl.GL_TEXTURE_2D);
	        this.wooden_box_texture.bind(gl);
	        gl.glTexEnvi(gl.GL_TEXTURE_ENV, gl.GL_TEXTURE_ENV_MODE, gl.GL_MODULATE);
	        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR_MIPMAP_LINEAR);
	        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
	        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAX_LOD, 1);
		}

		gl.glNormal3d(-1.0, 0.0, 0.0);
        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glTexCoord2d(0.0, 0.0);
        gl.glVertex3d(-this.length / 2.0, 0.0, this.depth1 / 2.0);
        gl.glTexCoord2d(0.0, 1.0);
        gl.glVertex3d(-this.length / 2.0, this.height1, this.depth1 / 2.0);
        gl.glTexCoord2d(1.0, 1.0);
        gl.glVertex3d(-this.length / 2.0, this.height1, -this.depth1 / 2.0);
        gl.glTexCoord2d(1.0, 0.0);
        gl.glVertex3d(-this.length / 2.0, 0.0, -this.depth1 / 2.0);
        gl.glEnd();
        
        gl.glNormal3d(0.0, -1.0, 0.0);
        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glTexCoord2d(0.0, 0.0);
        gl.glVertex3d(-this.length / 2.0, 0.0, -this.depth1 / 2.0);
        gl.glTexCoord2d(0.0, 1.0);
        gl.glVertex3d(-this.length / 2.0, 0.0, this.depth1 / 2.0);
        gl.glTexCoord2d(1.0, 1.0);
        gl.glVertex3d(this.length / 2.0, 0.0, -this.depth2 / 2.0);
        gl.glTexCoord2d(1.0, 0.0);
        gl.glVertex3d(this.length / 2.0, 0.0, this.depth2 / 2.0);
        gl.glEnd();
        
        gl.glNormal3d(1.0, 0.0, 0.0);
        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glTexCoord2d(0.0, 0.0);
        gl.glVertex3d(this.length / 2.0, 0.0, -this.depth2 / 2.0);
        gl.glTexCoord2d(0.0, 1.0);
        gl.glVertex3d(this.length / 2.0, this.height2, -this.depth2 / 2.0);
        gl.glTexCoord2d(1.0, 1.0);
        gl.glVertex3d(this.length / 2.0, this.height2, this.depth2 / 2.0);
        gl.glTexCoord2d(1.0, 0.0);
        gl.glVertex3d(this.length / 2.0, 0.0, this.depth2 / 2.0);
        gl.glEnd();
        
        gl.glNormal3d(0.0, 1.0, 0.0);
        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glTexCoord2d(0.0, 0.0);
        gl.glVertex3d(this.length / 2.0, this.height2, this.depth2 / 2.0);
        gl.glTexCoord2d(0.0, 1.0);
        gl.glVertex3d(this.length / 2.0, this.height2, -this.depth2 / 2.0);
        gl.glTexCoord2d(1.0, 1.0);
        gl.glVertex3d(-this.length / 2.0, this.height1, -this.depth1 / 2.0);
        gl.glTexCoord2d(1.0, 0.0);
        gl.glVertex3d(-this.length / 2.0, this.height1, this.depth1 / 2.0);
        gl.glEnd();
        
        gl.glNormal3d(0.0, 0.0, -1.0);
        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glTexCoord2d(0.0, 0.0);
        gl.glVertex3d(-this.length / 2.0, this.height1, -this.depth1 / 2.0);
        gl.glTexCoord2d(0.0, 1.0);
        gl.glVertex3d(this.length / 2.0, this.height2, -this.depth2 / 2.0);
        gl.glTexCoord2d(1.0, 1.0);
        gl.glVertex3d(this.length / 2.0, 0.0, -this.depth2 / 2.0);
        gl.glTexCoord2d(1.0, 0.0);
        gl.glVertex3d(-this.length / 2.0, 0.0, -this.depth1 / 2.0);
        gl.glEnd();
        
        gl.glNormal3d(0.0, 0.0, 1.0);
        gl.glBegin(GL2ES3.GL_QUADS);
        gl.glTexCoord2d(0.0, 0.0);
        gl.glVertex3d(-this.length / 2.0, 0.0, this.depth1 / 2.0);
        gl.glTexCoord2d(0.0, 1.0);
        gl.glVertex3d(this.length / 2.0, 0.0, this.depth2 / 2.0);
        gl.glTexCoord2d(1.0, 1.0);
        gl.glVertex3d(this.length / 2.0, this.height2, this.depth2 / 2.0);
        gl.glTexCoord2d(1.0, 0.0);
        gl.glVertex3d(-this.length / 2.0, this.height1, this.depth1 / 2.0);
        gl.glEnd();
        gl.glDisable(gl.GL_TEXTURE_2D);
	}

	
	public void init(GL2 gl) {
		File f_wooden_box_texture = new File("Textures/WoodBoxTexture.jpg");
		if (this.is_texture == 1) {
            try {//system call
                this.wooden_box_texture = TextureIO.newTexture(f_wooden_box_texture, true);
            }
            catch (Exception err) {
                System.err.print("Cannot read texture");
            }
        }
	}
	
	
	@Override
	public String toString() {
		return "SkewedBox";
	}
	public void destroy(GL2 gl) {
		if (this.is_texture == 1) {
			wooden_box_texture.destroy(gl);
			wooden_box_texture = null;
        }
	}

}

