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
			//back
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,0.5f,-0.5f,
			//front
			-0.5f,0.5f,0.5f,	
			-0.5f,-0.5f,0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,
			//right
			0.5f,0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,
			//left
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			-0.5f,-0.5f,0.5f,	
			-0.5f,0.5f,0.5f,
			//top
			-0.5f,0.5f,0.5f,
			-0.5f,0.5f,-0.5f,
			0.5f,0.5f,-0.5f,
			0.5f,0.5f,0.5f,
			//bottom
			-0.5f, -0.5f, 0.5f,
			-0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,0.5f
		}, new float[] {
			0,0,
			0,1,
			1,1,
			1,0,			
			0,0,
			0,1,
			1,1,
			1,0,			
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0
		}, new int[] {
			3, 1, 0, 2, 1, 3,	
			4,5,7,7,5,6,
			11, 9, 8, 10, 9, 11,
			12,13,15,15,13,14,	
			19, 17, 16, 18, 17, 19,
			20,21,23,23,21,22
		}, new Texture("cube")));
		this.run();
	}
	
	public void run() {
		float frameTime = 1000000000f/60f;
		float last = 0;
		float now = System.nanoTime();
		long window = Illustratio.renderer.getGLWindow().getWindow();
		GL.createCapabilities();
		glClearColor(0, 0, 0, 0);
		Illustratio.model.setPosition(0, 0, -5);
		long bounceCounter = 0;
		boolean bounced = false;
		float xRotation = Illustratio.model.getRotation().x;
		float yRotation = Illustratio.model.getRotation().y;
		float zRotation = Illustratio.model.getRotation().z;
		float xPosition = Illustratio.model.getPosition().x;
		float yPosition = Illustratio.model.getPosition().y;
		float zPosition = Illustratio.model.getPosition().z;
		float xMoveSpeed = 0.02f;
		float yMoveSpeed = 0.02f;
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
				if(xPosition >= 4) {
					xMoveSpeed *= -1;
					bounceCounter++;
					bounced = true;
					
				}
				if(xPosition <= -4) {
					xMoveSpeed *= -1;
					bounceCounter++;
					bounced = true;
				}
				if(yPosition >= 2) {
					yMoveSpeed *= -1;
					bounceCounter++;
					bounced = true;
				}
				if(yPosition <= -2) {
					yMoveSpeed *= -1;
					bounceCounter++;
					bounced = true;
				}
				xRotation += 0.25f;
				yRotation += 0.25f;
				zRotation += 0.1f;
				xPosition += xMoveSpeed;
				yPosition += yMoveSpeed;
				Illustratio.model.setRotation(xRotation, yRotation, zRotation);
				Illustratio.model.setPosition(xPosition, yPosition, -5);
				
				Illustratio.renderer.render(new Model[] {Illustratio.model});
				last = System.nanoTime();
			}
			if(bounced) {
				System.out.println(bounceCounter);
				bounced = false;
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
