package com.skyhouse.projectrpg.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.skyhouse.projectrpg.entities.Character;
import com.skyhouse.projectrpg.entities.data.CharacterData.CharacterState;

public class GameplayInputProcess extends InputAdapter {
	
	Character maincharacter;
	
	public  GameplayInputProcess(Character maincharacter) {
		this.maincharacter = maincharacter;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
			case Keys.LEFT:
				if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
					maincharacter.setFilpX(false);
				}
				else maincharacter.setState(CharacterState.IDLE);
				break;
			case Keys.RIGHT:
				if(Gdx.input.isKeyPressed(Keys.LEFT)) {
					maincharacter.setFilpX(true);
				}
				else maincharacter.setState(CharacterState.IDLE);
				break;
		}
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.LEFT:
				maincharacter.setState(CharacterState.WALK);
				maincharacter.setFilpX(true);
				break;
			case Keys.RIGHT:
				maincharacter.setState(CharacterState.WALK);
				maincharacter.setFilpX(false);
				break;
			case Keys.UP:
				maincharacter.setState(CharacterState.JUMP);;
				break;
		}
		return true;
	}	
	
}
