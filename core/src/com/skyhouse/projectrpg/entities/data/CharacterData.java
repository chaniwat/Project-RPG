package com.skyhouse.projectrpg.entities.data;

import java.util.HashMap;

public class CharacterData {
	
	public enum CharacterActionState {
		IDLE,
		WALK,
		JUMP,
		FALL,
		ACTION,
		DEATH
	}
	
	@SuppressWarnings("serial")
	public static class CharacterInputState extends  HashMap<Integer, Boolean> {
		public CharacterInputState() {
			super();
		}
		
		@Override
		public Boolean get(Object key) {
			return super.getOrDefault(key, false);
		}
	}
	
	private int id;
	public CharacterActionState actionstate;
	public CharacterInputState inputstate;
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