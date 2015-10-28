package com.skyhouse.projectrpg.input.listener;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;
import com.skyhouse.projectrpg.data.InputData;

/**
 * Gameplay controller listener.
 * @author Meranote
 */
public class GameplayControllerListener extends ControllerAdapter {

	/**
	 * Default button.
	 * axisIndex : left-axis : 3
	 * axisIndex : right-axis : _
	 * button : heal : 0
	 * button : jump : 2
	 * button : skillA : 7
	 * button : skillB : 6
	 */
	
	private InputData inputData;
	
	/**
	 * Construct a new {@link GameplayControllerListener}.
	 * @param inputData
	 */
	public GameplayControllerListener(InputData inputData) {
		this.inputData = inputData;
	}
	
	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		switch(buttonIndex) {
			/*case 0:
				inputData.healPressed = true;
				break;*/
			case 2:
				inputData.jumpPressed = true;
				break;
			/*case 6:
				inputData.skillBPressed = true;
				break;
			case 7:
				inputData.skillAPressed = true;
				break;*/
		}
		return false;
	}
	
	@Override
	public boolean buttonUp(Controller controller, int buttonIndex) {
		switch(buttonIndex) {
			/*case 0:
				inputData.healPressed = false;
				break;*/
			case 2:
				inputData.jumpPressed = false;
				break;
			/*case 6:
				inputData.skillBPressed = false;
				break;
			case 7:
				inputData.skillAPressed = false;
				break;*/
		}
		return false;
	}
	
	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {
		switch(axisIndex) {
			case 3:
				if(value > 0.01) {
					inputData.leftPressed = false;
					inputData.rightPressed = true;
					inputData.xAxisValue = value;
				} else if(value < -0.01) {
					inputData.leftPressed = true;
					inputData.rightPressed = false;
					inputData.xAxisValue = (float) Math.pow(value, 2);
				} else {
					inputData.rightPressed = false;
					inputData.leftPressed = false;
					inputData.xAxisValue = 0;
				}
		}
		return false;
	}
	
	@Override
	public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
		//Gdx.app.log(ProjectRPG.TITLE, "Controller:" + controller.getName() + " | POV: " + povIndex + " | value: " + value);
		return true;
	}
	
}
