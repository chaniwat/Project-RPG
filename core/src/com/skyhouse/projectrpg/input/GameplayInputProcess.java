package com.skyhouse.projectrpg.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.skyhouse.projectrpg.objects.Character;

public class GameplayInputProcess extends InputAdapter {
	
	Character playercharacter;
	
	public GameplayInputProcess(Character playercharacter) {
		this.playercharacter = playercharacter;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
			case Keys.LEFT:
				if(Gdx.input.isKeyPressed(Keys.RIGHT)) playercharacter.walkRight();
				else playercharacter.stopWalk();
				break;
			case Keys.RIGHT:
				if(Gdx.input.isKeyPressed(Keys.LEFT)) playercharacter.walkLeft();
				else playercharacter.stopWalk();
				break;
		}
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.LEFT:
				playercharacter.walkLeft();
				break;
			case Keys.RIGHT:
				playercharacter.walkRight();
				break;
			case Keys.UP:
				playercharacter.jump();
				break;
			case Keys.HOME:
				playercharacter.remove();
				Gdx.input.setInputProcessor(null);
				break;
		}
		return true;
	}	
	
}
