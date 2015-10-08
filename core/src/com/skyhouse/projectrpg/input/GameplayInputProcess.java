package com.skyhouse.projectrpg.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.skyhouse.projectrpg.entities.Character;

public class GameplayInputProcess extends InputAdapter {
	
	Character character;
	
	public  GameplayInputProcess(Character maincharacter) {
		this.character = maincharacter;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
			case Keys.LEFT:
				character.inputstate.left_flag = false;
				if(character.inputstate.right_flag == false) character.inputstate.x_value = 0;
				break;
			case Keys.RIGHT:
				character.inputstate.right_flag = false;
				if(character.inputstate.left_flag == false) character.inputstate.x_value = 0;
				break;
			case Keys.UP:
				character.inputstate.up_flag = false;
				break;
			case Keys.CONTROL_LEFT:
				character.inputstate.jump_flag = false;
				break;
		}
		return true;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.LEFT:
				character.inputstate.left_flag = true;
				character.inputstate.x_value = 1;
				break;
			case Keys.RIGHT:
				character.inputstate.right_flag = true;
				character.inputstate.x_value = 1;
				break;
			case Keys.UP:
				character.inputstate.up_flag = true;
				break;
			case Keys.CONTROL_LEFT:
				character.inputstate.jump_flag = true;
				break;
		}
		return true;
	}	
	
}
