package com.skyhouse.projectrpg.entities.data;

public class StructureData {
	
	private float position_x, position_y;
	private float width, height;
	
	public StructureData() { }
	public StructureData(StructureData data) {
		this.position_x = data.position_x;
		this.position_y = data.position_y;
		this.width = data.width;
		this.height = data.height;
	}
	public StructureData(float position_x, float position_y, float width, float height) {
		this.position_x = position_x;
		this.position_y = position_y;
		this.width = width;
		this.height = height;
	}
	
	public float getPositionX() {
		return position_x;
	}
	
	public float getPositionY() {
		return position_y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
}
