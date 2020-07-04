package dev.bitbite.illustratio;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.glClearColor;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

public class Illustratio {
	private static Renderer renderer;
	private static InputHandler inputHandler;
	public static Model model;
	
	
	public Illustratio(int vsync, String windowTitle, int width, int height) {
		GLFWErrorCallback.createPrint(System.err).set();
		if(!glfwInit()) throw new IllegalStateException("Could not initialize GLFW");
		Illustratio.renderer = new Renderer(vsync, new GLWindow(width, height, windowTitle), 60f, 0.01f, 1000f);
		Illustratio.inputHandler = new InputHandler();
		Illustratio.model = new Model(new ObjectMesh(new float[] {
			//top left front 0
			-0.5f, 0.5f, 0.5f,
			//bottom left front 1
			-0.5f, -0.5f, 0.5f,
			//bottom right front 2
			0.5f, -0.5f, 0.5f,
			//top right front 3
			0.5f, 0.5f, 0.5f,
			//top left back 4
			-0.5f, 0.5f, -0.5f,
			//bottom left back 5
			-0.5f, -0.5f, -0.5f,
			//bottom right back 6
			0.5f, -0.5f, -0.5f,
			//top right -back 7
			0.5f, 0.5f, -0.5f
		}, new float[] {
			0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,
            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            // For text coords in top face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,
            // For text coords in right face
            0.0f, 0.0f,
            0.0f, 0.5f,
            // For text coords in left face
            0.5f, 0.0f,
            0.5f, 0.5f,
            // For text coords in bottom face
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f
		}, new int[] {
			//front
			0, 1, 2, 2, 3, 0,
			//left
			0, 1, 5, 5, 4, 0,
			//bottom
			1, 2, 5, 5, 2, 6,
			//right
			3, 2, 6, 6, 7, 3,
			//top
			0, 3, 7, 7, 4, 0,
			//back
			4, 5, 6, 6, 7, 4
		}, new Texture("cube_texture")));
		this.run();
	}
	
	public void run() {
		float frameTime = 1000000000f/60f;
		float last = 0;
		float now = System.nanoTime();
		long window = Illustratio.renderer.getGLWindow().getWindow();
		GL.createCapabilities();
		glClearColor(0, 0, 0, 0);
		float xRotation = Illustratio.model.getRotation().x;
		float yRotation = Illustratio.model.getRotation().y;
		float zRotation = Illustratio.model.getRotation().z;
		Illustratio.model.setPosition(0, 0, -2);
		while(!glfwWindowShouldClose(window)) {
			now = System.nanoTime();
			if((last+frameTime) <= now) {
				/*
				 * gamelayer.update()
				 * guilayer.update()
				 * 
				 * 
				 * 
				 * layer.update() {
				 * 		update meshes, positions, etc
				 * 		push to renderer
				 * }
				 */
				if(xRotation == 360) xRotation = 0;
				if(yRotation == 360) yRotation = 0;
				if(zRotation == 360) zRotation = 0;
				xRotation += 0.5f;
				yRotation += 0.5f;
				zRotation += 0.5f;
				Illustratio.model.setRotation(xRotation, yRotation, zRotation);
				
				Illustratio.renderer.render(new Model[] {Illustratio.model});
				last = System.nanoTime();
			}
			glfwPollEvents();
		}
		System.out.println("loop ended");
		
		Illustratio.renderer.getGLWindow().terminateWindow();
	}
	
	public static InputHandler getInputHandler() {
		return Illustratio.inputHandler;
	}
	
	public static Renderer getRenderer() {
		return Illustratio.renderer;
	}
	
	public static void main(String args[]) {
		new Illustratio(0, "TowerDefense: Evolutions", 1440, 800);
	}
}
