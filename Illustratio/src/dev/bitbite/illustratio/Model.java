package dev.bitbite.illustratio;

import org.joml.Vector3f;

public class Model {
	private final ObjectMesh mesh;
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	
	public Model(ObjectMesh mesh) {
		this.mesh = mesh;
		this.position = new Vector3f(0, 0, 0);
		this.rotation = new Vector3f(0, 0, 0);
		this.scale = 1;
	}
	
	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}
	
	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public Vector3f getRotation() {
		return this.rotation;
	}
	
	public float getScale() {
		return this.scale;
	}
	
	public ObjectMesh getMesh() {
		return this.mesh;
	}
}
