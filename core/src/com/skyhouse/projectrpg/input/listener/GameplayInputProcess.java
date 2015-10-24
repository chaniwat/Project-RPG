package com.skyhouse.projectrpg.input.listener;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.data.InputData;
import com.skyhouse.projectrpg.entity.Character;

public class GameplayInputProcess extends InputAdapter {
	
	private InputData inputData;
	
	public  GameplayInputProcess(InputData inputData) {
		this.inputData = inputData;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
			case Keys.LEFT:
				inputData.leftPressed = false;
				if(inputData.rightPressed == false) inputData.xAxisValue = 0;
				break;
			case Keys.RIGHT:
				inputData.rightPressed = false;
				if(inputData.leftPressed == false) inputData.xAxisValue = 0;
				break;
			case Keys.UP:
				inputData.upPressed = false;
				break;
			case Keys.CONTROL_LEFT:
				inputData.jumpPressed = false;
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
				inputData.leftPressed = true;
				inputData.xAxisValue = 1;
				break;
			case Keys.RIGHT:
				inputData.rightPressed = true;
				inputData.xAxisValue = 1;
				break;
			case Keys.UP:
				inputData.upPressed = true;
				break;
			case Keys.C:
				inputData.jumpPressed = true;
				break;
		}
		return true;
	}	
	
}
