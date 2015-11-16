package com.skyhouse.projectrpg.input.listener;

import com.badlogic.gdx.InputAdapter;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.InputData;
import com.skyhouse.projectrpg.input.InputMapper.keyboard;

/**
 * Gameplay keyboard listener.
 * @author Meranote
 */
public class GameplayInputListener extends InputAdapter {
	
	private InputData inputData;
	
	/**
	 * Construct a new {@link GameplayInputListener}.
	 * @param inputData
	 */
	public GameplayInputListener(InputData inputData) {
		this.inputData = inputData;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
			case keyboard.R1:
				inputData.atkA = false;
				break;
			case keyboard.L1:
				inputData.atkB = false;
				break;
			case keyboard.R2:
				inputData.skillA = false;
				break;
			case keyboard.L2:
				inputData.skillB = false;
				break;
			case keyboard.B:
				inputData.action = false;
				break;
			case keyboard.Y:
				inputData.heal = false;
				break;
			case keyboard.LEFT:
				inputData.left = false;
				if(inputData.right == false) inputData.xAxisValue = 0;
				break;
			case keyboard.RIGHT:
				inputData.right = false;
				if(inputData.left == false) inputData.xAxisValue = 0;
				break;
			case keyboard.UP:
				inputData.up = false;
				if(inputData.down == false) inputData.yAxisValue = 0;
				break;
			case keyboard.DOWN:
				inputData.down = false;
				if(inputData.up == false) inputData.yAxisValue = 0;
				break;
			case keyboard.A:
				inputData.jump = false;
				break;
			case keyboard.X:
				inputData.dash = false;
				break;
		}
		
		return true;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case keyboard.R1:
				inputData.atkA = true;
				break;
			case keyboard.L1:
				inputData.atkB = true;
				break;
			case keyboard.R2:
				inputData.skillA = true;
				break;
			case keyboard.L2:
				inputData.skillB = true;
				break;
			case keyboard.B:
				inputData.action = true;
				break;
			case keyboard.Y:
				inputData.heal = true;
				break;
			case keyboard.START:
				ProjectRPG.client.gamemanager.changeToMenuScene();
				break;
			case keyboard.LEFT:
				inputData.left = true;
				inputData.xAxisValue = 1;
				break;
			case keyboard.RIGHT:
				inputData.right = true;
				inputData.xAxisValue = 1;
				break;
			case keyboard.UP:
				inputData.up = true;
				inputData.yAxisValue = 1;
				break;
			case keyboard.DOWN:
				inputData.down = true;
				inputData.yAxisValue = 1;
				break;
			case keyboard.A:
				inputData.jump = true;
				break;
			case keyboard.X:
				inputData.dash = true;
				break;
		}
		
		return true;
	}
	
}
