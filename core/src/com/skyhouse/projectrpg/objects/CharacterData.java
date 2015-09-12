package com.skyhouse.projectrpg.objects;

public class CharacterData {
	public int id;
	public float x, y;
	public int state;
	
	public CharacterData() {}
	public CharacterData(int id, float x, float y, int state) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.state = state;
	}
}