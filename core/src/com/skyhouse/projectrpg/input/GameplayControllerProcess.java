package com.skyhouse.projectrpg.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.entity.Character;
import com.skyhouse.projectrpg.data.CharacterData;

public class GameplayControllerProcess extends ControllerAdapter {

	CharacterData characterdata;
	
	public GameplayControllerProcess(Character maincharacter) {
		this.characterdata = maincharacter.getData();
	}
	
	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		switch(buttonIndex) {
			/*case 0:
				characterdata.inputstate.qh_flag = true;
				break;*/
			case 2:
				characterdata.inputstate.jumpPressed = true;
				break;
			/*case 6:
				characterdata.inputstate.s2_flag = true;
				break;
			case 7:
				characterdata.inputstate.s1_flag = true;
				break;*/
		}
		return false;
	}
	
	@Override
	public boolean buttonUp(Controller controller, int buttonIndex) {
		switch(buttonIndex) {
			/*case 0:
				characterdata.inputstate.qh_flag = false;
				break;*/
			case 2:
				characterdata.inputstate.jumpPressed = false;
				break;
			/*case 6:
				characterdata.inputstate.s2_flag = false;
				break;
			case 7:
				characterdata.inputstate.s1_flag = false;
				break;*/
		}
		return false;
	}
	
	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {
		switch(axisIndex) {
			case 3:
				if(value > 0.01) {
					characterdata.inputstate.leftPressed = false;
					characterdata.inputstate.rightPressed = true;
					characterdata.inputstate.xAxisValue = value;
				} else if(value < -0.01) {
					characterdata.inputstate.leftPressed = true;
					characterdata.inputstate.rightPressed = false;
					characterdata.inputstate.xAxisValue = (float) Math.pow(value, 2);
				} else {
					characterdata.inputstate.rightPressed = false;
					characterdata.inputstate.leftPressed = false;
					characterdata.inputstate.xAxisValue = 0;
				}
		}
		return false;
	}
	
	@Override
	public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
		Gdx.app.log(ProjectRPG.TITLE, "Controller:" + controller.getName() + " | POV: " + povIndex + " | value: " + value);
		return true;
	}
	
}
