package edu.cg;

import java.awt.Component;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import edu.cg.algebra.Vec;
import edu.cg.models.Track;
import edu.cg.models.TrackSegment;
import edu.cg.models.Car.F1Car;
import edu.cg.models.Car.Specification;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
/**
 * An OpenGL 3D Game.
 *
 */
public class NeedForSpeed implements GLEventListener {
	private GameState gameState = null; // Tracks the car movement and orientation
	private F1Car car = null; // The F1 car we want to render
	private Vec carCameraTranslation = null; // The accumulated translation that should be applied on the car, camera
												// and light sources
	private Track gameTrack = null; // The game track we want to render
	private FPSAnimator ani; // This object is responsible to redraw the model with a constant FPS
	private Component glPanel; // The canvas we draw on.
	private boolean isModelInitialized = false; // Whether model.init() was called.
	private boolean isDayMode = true; // Indicates whether the lighting mode is day/night.
	// TODO: add fields as you want. For example:
	// - Car initial position (should be fixed).
	// - Camera initial position (should be fixed)

	public NeedForSpeed(Component glPanel) {
		this.glPanel = glPanel;
		gameState = new GameState();
		gameTrack = new Track();
		carCameraTranslation = new Vec(0.0);
		car = new F1Car();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		if (!isModelInitialized) {
			initModel(gl);
		}
		if (isDayMode) {
			// TODO: Setup background color when day mode is on (You can choose differnt color)
			gl.glClearColor(0.52f, 0.824f, 1.0f, 1.0f);
		} else {
			// TODO: Setup background color when night mode is on (You can choose differnt color)
			gl.glClearColor(0.0f, 0.0f, 0.32f, 1.0f);
		}
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		// TODO: This is the flow in which we render the scene. You can use different flow.
		// Step (1) You should update the accumulated translation that needs to be
		// applied on the car, camera and light sources.
		updateCarCameraTranslation(gl);
		// Step (2) Position the camera and setup its orientation
		setupCamera(gl);
		// Step (3) setup the lighting.
		setupLights(gl);
		// Step (4) render the car.
		renderCar(gl);
		// Step (5) render the track.
		renderTrack(gl);
	}

	private void updateCarCameraTranslation(GL2 gl) {
		Vec next_trans = this.gameState.getNextTranslation();
		Vec temp = this.carCameraTranslation.add(next_trans);
        this.carCameraTranslation = temp;        
        double bounding_asphalt = Math.min(Math.max((double) this.carCameraTranslation.x, -8.5), 8.5);
        this.carCameraTranslation.x = (float) (bounding_asphalt);
        // if the translation reaches beyond the depth of a segment, we make make it relative to the segment depth
        if ((double) Math.abs(this.carCameraTranslation.z) >= 504.0) {
        	double relative_position = (double) Math.abs(this.carCameraTranslation.z) % 500.0;
            this.carCameraTranslation.z = -((float) relative_position);
            this.gameTrack.changeTrack(gl);
        }
	}

	private void setupCamera(GL2 gl) {
        GLU glu = new GLU();
        glu.gluLookAt((double)this.carCameraTranslation.x, 2 + 
        		(double)this.carCameraTranslation.y, 2 + 
        		(double)this.carCameraTranslation.z, 
        		(double)this.carCameraTranslation.x, 1.4 + 
        		(double)this.carCameraTranslation.y, -5.0 + 
        		(double)this.carCameraTranslation.z, 0.0, 0.7, -0.3);
        }


	private void setupLights(GL2 gl) {
		if (isDayMode) {
			// TODO Setup day lighting.
			// * Remember: switch-off any light sources that were used in night mode
			int disable_light = gl.GL_LIGHT1;
			int enable_light = gl.GL_LIGHT0;
			
			gl.glDisable(disable_light);
			
			float[] sun_color = new float[]{1, 1, 1, 1};		
	        Vec sun_normalized = new Vec(0.0, 1.0, 1.0).normalize();        
	        float[] sun_position = new float[]{sun_normalized.x, sun_normalized.y, sun_normalized.z, 0};
	        float[] ambient_arr = new float[]{(float) 0.1, (float) 0.1, (float) 0.1, (float) 0.1};
	        
	        gl.glLightfv(enable_light, gl.GL_SPECULAR, sun_color, 0);
	        gl.glLightfv(enable_light, gl.GL_DIFFUSE, sun_color, 0);
	        gl.glLightfv(enable_light, gl.GL_AMBIENT, ambient_arr, 0);
	        
	        gl.glLightfv(enable_light, gl.GL_POSITION, sun_position, 0);
	        gl.glEnable(enable_light);
		
		} else {
			// TODO Setup night lighting.
			// * Remember: switch-off any light sources that are used in day mode
			// * Remember: spotlight sources also move with the camera.
			
			// moon
			
			float[] moon_model = new float[]{0.15f, (float) 0.15, (float) 0.18, 1};
			gl.glLightModelfv(gl.GL_LIGHT_MODEL_AMBIENT, moon_model, 0);
            
			float[] location_1 = new float[]{(float) 0 + this.carCameraTranslation.x, 
					(float) 8 + this.carCameraTranslation.y, 
					(float) this.carCameraTranslation.z, 
					(float) 1};
			            
			// spotlight
			
			float[] color_1 = new float[]{(float) 0.85, (float) 0.85, (float) 0.85, (float) 1};
	        gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, location_1, 0);
	        // TODO: enlarge spotlight angle
	        gl.glLightf(gl.GL_LIGHT0, gl.GL_SPOT_CUTOFF, 75.0f);
	        gl.glLightfv(gl.GL_LIGHT0, gl.GL_SPOT_DIRECTION, new float[]{0.0f, -1.0f, 0.0f}, 0);
	        gl.glLightfv(gl.GL_LIGHT0, gl.GL_SPECULAR, color_1, 0);
	        gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE, color_1, 0);
	        gl.glEnable(gl.GL_LIGHT0);
			
            float[] location_2 = new float[]{(float) this.carCameraTranslation.x, 
            		(float) 8 + this.carCameraTranslation.y, 
            		(float) -15 + this.carCameraTranslation.z, 
            		(float) 1};
            
			float[] color_2 = new float[]{0.85f, 0.85f, 0.85f, 1.0f};
			
	        gl.glLightfv(gl.GL_LIGHT1, gl.GL_POSITION, location_2, 0);
	        gl.glLightf(gl.GL_LIGHT1, gl.GL_SPOT_CUTOFF, 75.0f);
	        gl.glLightfv(gl.GL_LIGHT1, gl.GL_SPOT_DIRECTION, new float[]{0.0f, -1.0f, 0.0f}, 0);
	        gl.glLightfv(gl.GL_LIGHT1, gl.GL_SPECULAR, color_2, 0);
	        gl.glLightfv(gl.GL_LIGHT1, gl.GL_DIFFUSE, color_2, 0);
	        gl.glEnable(gl.GL_LIGHT1);
		}

	}

	private void renderTrack(GL2 gl) {
		gl.glPushMatrix();
        this.gameTrack.render(gl);
        gl.glPopMatrix();
	}

	private void renderCar(GL2 gl) {
		double rotation_of_car = this.gameState.getCarRotation();
        gl.glPushMatrix();
        gl.glTranslated(this.carCameraTranslation.x, 0.3 + 
        		(double)this.carCameraTranslation.y, -5.5 + 
        		(double)this.carCameraTranslation.z);//TODO
        gl.glRotated(-1.0*(rotation_of_car), 0.0, 1.0, 0.0);
        gl.glScaled(3.0, 3.0, 3.0);//TODO
        gl.glRotated(90.0, 0.0, 0.5, 0.0);//TODO
        this.car.render(gl);
        gl.glPopMatrix();
	}

	public GameState getGameState() {
		return gameState;
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO: Destroy all models.
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		// Initialize display callback timer
		ani = new FPSAnimator(30, true);
		ani.add(drawable);
		glPanel.repaint();

		initModel(gl);
		ani.start();
	}

	public void initModel(GL2 gl) {
		gl.glCullFace(GL2.GL_BACK);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_NORMALIZE);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_SMOOTH);
		car.init(gl);
		gameTrack.init(gl);
		isModelInitialized = true;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLU();
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        double z_near = 1.7;
        double z_far = 500; 
        double ar = width / (double)height;
        glu.gluPerspective(57, ar, z_near, z_far);
	}

	/**
	 * Start redrawing the scene with 30 FPS
	 */
	public void startAnimation() {
		if (!ani.isAnimating())
			ani.start();
	}

	/**
	 * Stop redrawing the scene with 30 FPS
	 */
	public void stopAnimation() {
		if (ani.isAnimating())
			ani.stop();
	}

	public void toggleNightMode() {
		isDayMode = !isDayMode;
	}

}