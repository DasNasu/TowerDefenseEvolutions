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
			-0.5f,  0.5f, -1.5f,
	        -0.5f, -0.5f, -1.5f,
	         0.5f, -0.5f, -1.5f,
	         0.5f,  0.5f, -1.5f
		}, new float[] {
			0.5f, 0.0f, 0.0f,
		    0.0f, 0.5f, 0.0f,
		    0.0f, 0.0f, 0.5f,
		    0.0f, 0.5f, 0.5f
		}, new int[] {
			0, 1, 3, 3, 1, 2
		}));
		this.run();
	}
	
	public void run() {
		float frameTime = 1000000000f/60f;
		float last = 0;
		float now = System.nanoTime();
		long window = Illustratio.renderer.getGLWindow().getWindow();
		GL.createCapabilities();
		glClearColor(0, 0, 0, 0);
		float degrees = 0;
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
				if(degrees == 360) degrees = 0;
				Illustratio.model.setRotation(0, 0, degrees);
				
				Illustratio.renderer.render(new Model[] {Illustratio.model});
				last = System.nanoTime();
				degrees++;
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
