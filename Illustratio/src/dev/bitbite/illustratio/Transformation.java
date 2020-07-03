package dev.bitbite.illustratio;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
	private Matrix4f projectionMatrix;
	private final Matrix4f worldMatrix;
	
	public Transformation() {
		this.projectionMatrix = new Matrix4f();
		this.worldMatrix = new Matrix4f();
	}
	
	public Matrix4f getProjectionMatrix(float fieldOfView, float width, float height, float zNear, float zFar) {
		this.projectionMatrix.identity();
		this.projectionMatrix.perspective(fieldOfView, ((float) (width/height)), zNear, zFar);
		return this.projectionMatrix;
	}
	
	public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
		this.worldMatrix.identity().translate(offset).rotateX((float)Math.toRadians(rotation.x)).rotateY((float)Math.toRadians(rotation.y)).rotateZ((float)Math.toRadians(rotation.z)).scale(scale);
		return this.worldMatrix;
	}
}
