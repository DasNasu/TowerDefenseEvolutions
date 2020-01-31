package dev.bitbite.towerdefenseevolutions;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

public class GLWindow implements Runnable {
	private  ShaderProgram shaderProgram;
	
	private long window;
	private int vsync;
	private int width;
	private int height;
	private String title;
	
	
	public GLWindow(int vsync, int width, int height, String title) {
		this.vsync = vsync;
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	public void createWindow() {
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwDefaultWindowHints();
		this.window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if(this.window == NULL) throw new RuntimeException("Failed to create window");
		try(MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			glfwGetWindowSize(this.window, pWidth, pHeight);
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(this.window, (vidmode.width()-pWidth.get(0))/2, (vidmode.height()-pHeight.get(0))/2);
		}
		glfwMakeContextCurrent(this.window);
		try {
			this.shaderProgram = new ShaderProgram();
			//this.shaderProgram.createVertexShader(Utils.loadResource("/shader/vertex.vs"));
			//this.shaderProgram.createFragmentShader(Utils.loadResource("/shader/fragment.fs"));
			//this.shaderProgram.link();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		glfwSwapInterval(this.vsync);
		glfwShowWindow(this.window);
		
		
		
		glfwSetKeyCallback(this.window, (window, key, scancode, action, mods) -> {
			TowerDefenseEvolutions.getInputHandler().handleKeyboardInput(key);
		});
		glfwSetMouseButtonCallback(this.window, (window, button, action, mods) -> {
			TowerDefenseEvolutions.getInputHandler().handleMouseInput(button);
		});
		glfwSetCursorPosCallback(this.window, (window, xpos, ypos) -> {
			TowerDefenseEvolutions.getInputHandler().handleMouseMovement(xpos, ypos);
		});
		glfwSetScrollCallback(this.window, (window, xoffset, yoffset) -> {
			TowerDefenseEvolutions.getInputHandler().handleMouseScroll(xoffset, yoffset);
		});
		glfwSetFramebufferSizeCallback(this.window, (window, width, height) -> {
			System.out.println("new width: "+width+", new height: "+height);
		});
		glfwMakeContextCurrent(NULL);
	}
	
	public void terminateWindow() {
		glfwFreeCallbacks(this.window);
		glfwDestroyWindow(this.window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	@Override
	public void run() {
		float frameTime = 1000000000f/60f;
		float last = 0;
		float now = System.nanoTime();
		glfwMakeContextCurrent(this.window);
		GL.createCapabilities();
		glClearColor(0, 0, 0, 0);
		while(!glfwWindowShouldClose(this.window)) {
			now = System.nanoTime();
			if((last+frameTime) <= now) {
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				glfwSwapBuffers(this.window);
				last =  System.nanoTime();
			}
		}
		System.out.println("loop ended");
		
		this.terminateWindow();
	}
	
	public long getWindow() {
		return this.window;
	}
	
	public ShaderProgram getShaderProgram() {
		return this.shaderProgram;
	}
}
