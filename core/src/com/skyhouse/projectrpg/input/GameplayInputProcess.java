package com.skyhouse.projectrpg.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.entity.Character;

public class GameplayInputProcess extends InputAdapter {
	
	CharacterData characterdata;
	
	public  GameplayInputProcess(Character maincharacter) {
		this.characterdata = maincharacter.getData();
	}
	
	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
			case Keys.LEFT:
				characterdata.inputstate.leftPressed = false;
				if(characterdata.inputstate.rightPressed == false) characterdata.inputstate.xAxisValue = 0;
				break;
			case Keys.RIGHT:
				characterdata.inputstate.rightPressed = false;
				if(characterdata.inputstate.leftPressed == false) characterdata.inputstate.xAxisValue = 0;
				break;
			case Keys.UP:
				characterdata.inputstate.upPressed = false;
				break;
			case Keys.CONTROL_LEFT:
				characterdata.inputstate.jumpPressed = false;
				break;
			case Keys.ESCAPE:
				Gdx.app.exit();
				break;
		}
		return true;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.LEFT:
				characterdata.inputstate.leftPressed = true;
				characterdata.inputstate.xAxisValue = 1;
				break;
			case Keys.RIGHT:
				characterdata.inputstate.rightPressed = true;
				characterdata.inputstate.xAxisValue = 1;
				break;
			case Keys.UP:
				characterdata.inputstate.upPressed = true;
				break;
			case Keys.CONTROL_LEFT:
				characterdata.inputstate.jumpPressed = true;
				break;
		}
		return true;
	}	
	
}
