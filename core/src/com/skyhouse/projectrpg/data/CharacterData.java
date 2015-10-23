package com.skyhouse.projectrpg.data;

public class CharacterData extends Data {
	
	public static enum CharacterActionState {
		IDLE, WALK, JUMP, FALL, ACTION, DEATH
	}
	
	public class CharacterInputState {
		
		public boolean upPressed = false, 
					   leftPressed = false, 
					   rightPressed = false, 
					   jumpPressed = false, 
					   s1Pressed = false, 
					   s2Pressed = false, 
					   qhPressed = false;
		public float xAxisValue = 0f;
		
	}
	
	public int id;
	public float x, y;
	public boolean flipX = false;
	public CharacterInputState inputstate;
	public CharacterActionState actionstate = CharacterActionState.IDLE;
	
	public CharacterData() {
		this.inputstate = new CharacterInputState();
	}
}