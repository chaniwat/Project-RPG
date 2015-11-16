package com.skyhouse.projectrpg.input.listener;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.InputData;
import com.skyhouse.projectrpg.input.InputMapper;

/**
 * Gameplay controller listener.
 * @author Meranote
 */
public class GameplayControllerListener extends ControllerAdapter {

	
	private InputData inputData;
	
	/**
	 * Construct a new {@link GameplayControllerListener}.
	 * @param inputData
	 */
	public GameplayControllerListener(InputData inputData) {
		this.inputData = inputData;
	}
	
	@Override
	public boolean buttonUp(Controller controller, int buttonIndex) {
		switch(buttonIndex) {
			case InputMapper.controller.R1:
				inputData.atkA = false;
				break;
			case InputMapper.controller.L1:
				inputData.atkB = false;
				break;
			case InputMapper.controller.R2:
				inputData.skillA = false;
				break;
			case InputMapper.controller.L2:
				inputData.skillB = false;
				break;
			case InputMapper.controller.B:
				inputData.action = false;
				break;
			case InputMapper.controller.Y:
				inputData.heal = false;
				break;
			case InputMapper.controller.A:
				inputData.jump = false;
				break;
			case InputMapper.controller.X:
				inputData.dash = false;
				break;
		}
		return false;
	}
	
	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		switch(buttonIndex) {
			case InputMapper.controller.R1:
				inputData.atkA = true;
				break;
			case InputMapper.controller.L1:
				inputData.atkB = true;
				break;
			case InputMapper.controller.R2:
				inputData.skillA = true;
				break;
			case InputMapper.controller.L2:
				inputData.skillB = true;
				break;
			case InputMapper.controller.B:
				inputData.action = true;
				break;
			case InputMapper.controller.Y:
				inputData.heal = true;
				break;
			case InputMapper.controller.START:
				ProjectRPG.client.gamemanager.changeToMenuScene();
				break;
			case InputMapper.controller.A:
				inputData.jump = true;
				break;
			case InputMapper.controller.X:
				inputData.dash = true;
				break;
		}
		return false;
	}
	
	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {
		float cValue = (float) Math.pow(value, 2);
		switch(axisIndex) {
			case InputMapper.controller.LEFTXAXIS:
				if(cValue > 0.01) {
					if(value > 0) {
						inputData.left = false;
						inputData.right = true;
					} else if(value < 0) {
						inputData.left = true;
						inputData.right = false;
					}
					if(InputMapper.controller.INVERTLEFTXAXIS) {
						inputData.left = !inputData.left;
						inputData.right = !inputData.right;
					}
					inputData.xAxisValue = cValue;
				} else {
					inputData.right = false;
					inputData.left = false;
					inputData.xAxisValue = 0f;
				}
				break;
			case InputMapper.controller.LEFTYAXIS:
				if(cValue > 0.01) {
					if(value > 0) {
						inputData.up = false;
						inputData.down = true;
					} else if(value < 0) {
						inputData.up = true;
						inputData.down = false;
					}
					if(InputMapper.controller.INVERTLEFTYAXIS) {
						inputData.up = !inputData.up;
						inputData.down = !inputData.down;
					}
					inputData.yAxisValue = cValue;
				} else {
					inputData.up = false;
					inputData.down = false;
					inputData.yAxisValue = 0f;
				}
				break;
		}
		return false;
	}
	
}
