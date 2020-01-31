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
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class GLWindow implements Runnable {
	private ShaderProgram shaderProgram;
	
	private long window;
	private int vsync;
	private int width;
	private int height;
	private String title;
	
	private float[] vertices;
	private FloatBuffer verticesBuffer;
	private int vaoID;
	private int vboID;
	
	
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
		
		
		this.vertices = new float[] {
				0.0f, 0.5f, 0.0f,
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f
		};
		this.verticesBuffer = MemoryUtil.memAllocFloat(this.vertices.length);
		this.verticesBuffer.put(this.vertices).flip();
		this.vaoID = glGenVertexArrays();
		glBindVertexArray(this.vaoID);
		this.vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
		glBufferData(GL_ARRAY_BUFFER, this.verticesBuffer, GL_STATIC_DRAW);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		if(this.verticesBuffer != null) MemoryUtil.memFree(this.verticesBuffer);
		
		
		
		
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
				this.render();
				last =  System.nanoTime();
			}
		}
		System.out.println("loop ended");
		
		this.terminateWindow();
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		this.shaderProgram.bind();
		glBindVertexArray(this.vaoID);
		glDrawArrays(GL_TRIANGLES, 0, 3);
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
