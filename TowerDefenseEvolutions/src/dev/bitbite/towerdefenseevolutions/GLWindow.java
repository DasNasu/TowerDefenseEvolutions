package dev.bitbite.towerdefenseevolutions;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryStack;

public class GLWindow implements Runnable {
	private ShaderProgram shaderProgram;
	
	private long window;
	private int vsync;
	private int width;
	private int height;
	private String title;
	
	private ObjectMesh mesh;
	
	
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
		GL.createCapabilities();
		GLUtil.setupDebugMessageCallback();
		try {
			this.shaderProgram = new ShaderProgram();
			this.shaderProgram.createVertexShader(Utils.loadResource("vertex.vs"));
			this.shaderProgram.createFragmentShader(Utils.loadResource("fragment.fs"));
			this.shaderProgram.link();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		this.mesh = new ObjectMesh(new float[] {
				-0.5f,  0.5f, 0.0f,
		        -0.5f, -0.5f, 0.0f,
		         0.5f, -0.5f, 0.0f,
		         0.5f,  0.5f, 0.0f
		}, new float[] {
				0.5f, 0.0f, 0.0f,
			    0.0f, 0.5f, 0.0f,
			    0.0f, 0.0f, 0.5f,
			    0.0f, 0.5f, 0.5f
		}, new int[] {
				0, 1, 3, 3, 1, 2
		});
		
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
				this.render(this.mesh);
				last = System.nanoTime();
			}
		}
		System.out.println("loop ended");
		
		this.terminateWindow();
	}
	
	private void render(ObjectMesh mesh) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		this.shaderProgram.bind();
		glBindVertexArray(mesh.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		this.shaderProgram.unbind();
		glfwSwapBuffers(this.window);
	}
	
	public long getWindow() {
		return this.window;
	}
	
	public ShaderProgram getShaderProgram() {
		return this.shaderProgram;
	}
}
