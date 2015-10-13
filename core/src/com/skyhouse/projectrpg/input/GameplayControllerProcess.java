package com.skyhouse.projectrpg.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.entities.Character;

public class GameplayControllerProcess extends ControllerAdapter {

	Character character;
	
	public  GameplayControllerProcess(Character maincharacter) {
		this.character = maincharacter;
	}
	
	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		switch(buttonIndex) {
			case 2:
				character.inputstate.jump_flag = true;
		}
		return true;
	}
	
	@Override
	public boolean buttonUp(Controller controller, int buttonIndex) {
		switch(buttonIndex) {
			case 2:
				character.inputstate.jump_flag = false;
		}
		return super.buttonUp(controller, buttonIndex);
	}
	
	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {
		switch(axisIndex) {
			case 3:
				if(value > 0.01) {
					character.inputstate.left_flag = false;
					character.inputstate.right_flag = true;
					character.inputstate.x_value = value;
				} else if(value < -0.01) {
					character.inputstate.left_flag = true;
					character.inputstate.right_flag = false;
					character.inputstate.x_value = (float) Math.pow(value, 2);
				} else {
					character.inputstate.right_flag = false;
					character.inputstate.left_flag = false;
					character.inputstate.x_value = 0;
				}
		}
		return super.axisMoved(controller, axisIndex, value);
	}
	
	@Override
	public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
		Gdx.app.log(ProjectRPG.TITLE, "Controller:" + controller.getName() + " | POV: " + povIndex + " | value: " + value);
		return true;
	}
	
}
