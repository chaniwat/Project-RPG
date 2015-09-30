package com.skyhouse.projectrpg.input;

import com.badlogic.gdx.InputAdapter;
import com.skyhouse.projectrpg.entities.Character;

public class GameplayInputProcess extends InputAdapter {
	
	Character maincharacter;
	
	public  GameplayInputProcess(Character maincharacter) {
		this.maincharacter = maincharacter;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		/*
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
		*/
		maincharacter.inputstate.put(keycode, false);
		return true;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		/*
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
		*/
		maincharacter.inputstate.put(keycode, true);
		return true;
	}	
	
}
