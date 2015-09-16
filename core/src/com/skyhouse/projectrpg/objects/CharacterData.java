package com.skyhouse.projectrpg.objects;

public class CharacterData {
	
	public enum CharacterState {
		IDLE,
		WALK,
		JUMP,
		FALL,
		ACTION,
		DEATH
	}
	
	private int id;
	private CharacterState state;
	private float position_x, position_y;
	private boolean flipXflag;
	
	public CharacterData() {}
	public CharacterData(int id, float position_x, float position_y, CharacterState CharacterState) {
		this.id = id;
		this.position_x = position_x;
		this.position_y = position_y;
		this.state = CharacterState;
	}
	
	public int getID() {
		return this.id;
	}
	
	public float getPositionX() {
		return this.position_x;
	}
	
	public float getPositionY() {
		return this.position_y - 1;
	}
	
	public CharacterState getState() {
		return this.state;
	}
	
	public boolean isFlipX() {
		return flipXflag;
	}
	
	public void setFilpX(boolean flag) {
		this.flipXflag = flag;
	}
	
	public void setPosition(float position_x, float position_y) {
		this.position_x = position_x;
		this.position_y = position_y;
	}
	
	public void setPositionX(float position_x) {
		setPosition(position_x, this.position_y);
	}
	
	public void setPositonY(float position_y) {
		setPosition(this.position_x, position_y + 1);
	}
	
	public void setState(CharacterState state) {
		this.state = state;
	}
}