package com.skyhouse.projectrpg.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.skyhouse.projectrpg.objects.Character;
import com.skyhouse.projectrpg.objects.CharacterData.CharacterState;

public class GameplayInputProcess extends InputAdapter {
	
	Character playercharacter;
	
	public  GameplayInputProcess(Character playercharacter) {
		this.playercharacter = playercharacter;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
			case Keys.LEFT:
				if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
					playercharacter.setFilpX(false);
				}
				else playercharacter.setState(CharacterState.IDLE);
				break;
			case Keys.RIGHT:
				if(Gdx.input.isKeyPressed(Keys.LEFT)) {
					playercharacter.setFilpX(true);
				}
				else playercharacter.setState(CharacterState.IDLE);
				break;
		}
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.LEFT:
				playercharacter.setState(CharacterState.WALK);
				playercharacter.setFilpX(true);
				break;
			case Keys.RIGHT:
				playercharacter.setState(CharacterState.WALK);
				playercharacter.setFilpX(false);
				break;
			case Keys.UP:
				playercharacter.setState(CharacterState.JUMP);;
				break;
		}
		return true;
	}	
	
}
