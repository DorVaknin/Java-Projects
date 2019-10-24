package edu.cg.models;

import java.io.File;
import java.util.LinkedList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import edu.cg.algebra.Point;
import edu.cg.models.Car.Materials;

public class TrackSegment implements IRenderable {
	// TODO: Some constants you can use
	public final static double ASPHALT_TEXTURE_WIDTH = 20.0;
	public final static double ASPHALT_TEXTURE_DEPTH = 10.0;
	public final static double GRASS_TEXTURE_WIDTH = 10.0;
	public final static double GRASS_TEXTURE_DEPTH = 10.0;
	public final static double TRACK_LENGTH = 500.0;
	public final static double BOX_LENGTH = 1.5;
	private LinkedList<Point> boxesLocations; // Store the boxes centroids (center points) here.
	Texture texture_of_grass;
	Texture texture_of_asphalt;
	private SkewedBox wooden_box = new SkewedBox(1.5, 1);
	private double strip_depth = 10;


	// TODO Add additional fields, for example:
	//		- Add wooden box model (you will only need one object which can be rendered many times)
	//      - Add grass and asphalt textures.

	public void setDifficulty(double difficulty) {
		// TODO: Set the difficulty of the track segment. Here you decide what are the boxes locations.
		//		 We provide a simple implementation. You can change it if you want. But if you do decide to use it, then it is your responsibility to understand the logic behind it.
		//       Note: In our implementation, the difficulty is the probability of a box to appear in the scene. 
		//             We divide the scene into rows of boxes and we sample boxes according the difficulty probability.
		difficulty = Math.min(difficulty, 0.95);
		difficulty = Math.max(difficulty, 0.05);
		double numberOfLanes = 4.0;
		double deltaZ = 0.0;
		if (difficulty < 0.25) {
			deltaZ = 100.0;
		} else if (difficulty < 0.5) {
			deltaZ = 75.0;
		} else {
			deltaZ = 50.0;
		}
		boxesLocations = new LinkedList<Point>();
		for (double dz = deltaZ; dz < TRACK_LENGTH - BOX_LENGTH / 2.0; dz += deltaZ) {
			int cnt = 0; // Number of boxes sampled at each row.
			boolean flag = false;
			for (int i = 0; i < 12; i++) {
				double dx = -((double) numberOfLanes / 2.0) * ((ASPHALT_TEXTURE_WIDTH - 2.0) / numberOfLanes) + BOX_LENGTH / 2.0
						+ i * BOX_LENGTH;
				if (Math.random() < difficulty) {
					boxesLocations.add(new Point(dx, BOX_LENGTH / 2.0, -dz));
					cnt += 1;
				} else if (!flag) {// The first time we don't sample a box then we also don't sample the box next to. We want enough space for the car to pass through. 
					i += 1;
					flag = true;
				}
				if (cnt > difficulty * 10) {
					break;
				}
			}
		}
	}

	public TrackSegment(double difficulty) {
		setDifficulty(difficulty);
	}

	@Override
	public void render(GL2 gl) {
        Materials.setWoodenBoxMaterial(gl);

		for (int i = 0; i < boxesLocations.size() ; i++) {
        	Point cur_point = boxesLocations.get(i);
        	gl.glPushMatrix();
            gl.glTranslated(cur_point.x, 0.0, cur_point.z);
            this.wooden_box.render(gl);
            gl.glPopMatrix();
        }
		

		// render asphalt
		Materials.setAsphaltMaterial(gl);
        gl.glPushMatrix();
        this.applyTextures(gl, this.texture_of_asphalt, 20.0, 500.0);
        gl.glPopMatrix();
		
        // render grass
        Materials.setGreenMaterial(gl);
        double grass_offset = 15.0;
        gl.glTranslated(grass_offset , 0.0, 0.0);
        this.applyTextures(gl, this.texture_of_grass, 10.0, 500.0);
        gl.glTranslated(-grass_offset + (-grass_offset), 0.0, 0.0);
        this.applyTextures(gl, this.texture_of_grass, 10.0, 500.0);
        gl.glPopMatrix();
	}

	@Override
	public void init(GL2 gl) {
		this.wooden_box.init(gl);
		File f_asphalt_tex = new File("Textures/RoadTexture.jpg");
		File f_grass_tex = new File("Textures/GrassTexture.jpg");
		try {
			texture_of_asphalt = TextureIO.newTexture(f_asphalt_tex, true);
			texture_of_grass = TextureIO.newTexture(f_grass_tex, true);
		} catch(Exception err) {
			System.err.print("Cannot read the texture");
		}
	}

	
	private void applyTextures(GL2 gl, Texture tex,double total_width, double total_depth) {
		gl.glEnable(gl.GL_TEXTURE_2D);
        tex.bind(gl);
        // blending
        gl.glTexEnvi(gl.GL_TEXTURE_ENV, gl.GL_TEXTURE_ENV_MODE, gl.GL_MODULATE);
        // GL_LINEAR_MIPMAP_LINEAR produces best quality for minification, and GL_LINEAR for magnification.
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
        // level of detail
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAX_LOD, 1);
        gl.glColor3d(1.0, 0.0, 0.0);
        GLU glu = new GLU();
        GLUquadric q = glu.gluNewQuadric();
        gl.glColor3d(1.0, 0.0, 0.0);
        gl.glNormal3d(0.0, 1.0, 0.0);
        gl.glTexCoord2d(0, 0);
        for (double strip_depth_counter = 0.0; strip_depth_counter < 500.0; strip_depth_counter += strip_depth) {
            gl.glBegin(gl.GL_QUADS);
		    gl.glTexCoord2d(0, 0);
		    gl.glVertex3d(-total_width / 2.0, 0.0, 0 - strip_depth_counter);
		    gl.glTexCoord2d(1, 0);
		    gl.glVertex3d(total_width/2, 0.0, 0 - strip_depth_counter);
		    gl.glTexCoord2d(1, 1);
		    gl.glVertex3d(total_width/2, 0.0, -10 - strip_depth_counter);
		    gl.glTexCoord2d(0, 1);
		    gl.glVertex3d(-total_width/2, 0.0, -10 - strip_depth_counter);  
		    gl.glEnd();
        }
        glu.gluDeleteQuadric(q);
        gl.glDisable(gl.GL_TEXTURE_2D);
    }
	
	public void destroy(GL2 gl) {
        this.wooden_box.destroy(gl);
		this.texture_of_asphalt.destroy(gl);
        this.texture_of_grass.destroy(gl);
        wooden_box = null;
        texture_of_asphalt = null;
        texture_of_grass = null;
	}

}
