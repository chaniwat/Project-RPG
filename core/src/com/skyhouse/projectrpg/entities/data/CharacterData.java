package com.skyhouse.projectrpg.entities.data;

public class CharacterData {
	
	public enum CharacterActionState {
		IDLE,
		WALK,
		JUMP,
		FALL,
		ACTION,
		DEATH
	}
	
	public static class CharacterInputState {
		
		public boolean up_flag, left_flag, right_flag, jump_flag;
		public float x_value;
		public boolean s1_flag, s2_flag;
		
		public CharacterInputState() {
			up_flag = false;
			left_flag = false;
			right_flag = false;
			jump_flag = false;
			x_value = 0;
		}
		
	}
	
	private int id;
	public CharacterInputState inputstate;
	public CharacterActionState actionstate;
	private float position_x, position_y;
	private boolean flipXflag = false;
	
	public CharacterData() {}
	public CharacterData(CharacterData data) {
		this.id = data.id;
		this.position_x = data.position_x;
		this.position_y = data.position_y;
		this.actionstate = data.actionstate;
		this.inputstate = data.inputstate;
		this.flipXflag = data.flipXflag;
	}
	public CharacterData(int id, float position_x, float position_y, CharacterActionState state) {
		this.id = id;
		this.position_x = position_x;
		this.position_y = position_y;
		this.actionstate = state;
		this.inputstate = new CharacterInputState();
	}
	
	public int getID() {
		return this.id;
	}
	
	public float getPositionX() {
		return this.position_x;
	}
	
	public float getPositionY() {
		return this.position_y - 0.5f;
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
		setPosition(this.position_x, position_y);
	}
}