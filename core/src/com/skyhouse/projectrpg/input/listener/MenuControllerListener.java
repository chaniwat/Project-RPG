package com.skyhouse.projectrpg.input.listener;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.input.InputMapper;
import com.skyhouse.projectrpg.scene.MenuScene;

/**
 * Menu scene controller listener.
 * @author Meranote
 */
public class MenuControllerListener extends ControllerAdapter {
	
	@Override
	public boolean buttonUp(Controller controller, int buttonIndex) {
		MenuScene scene = ProjectRPG.client.scenemanager.getScene(MenuScene.class);
		switch(buttonIndex) {
			case InputMapper.controller.L1:
				scene.prevTab();
				break;
			case InputMapper.controller.R1:
				scene.nextTab();
				break;
			case InputMapper.controller.START:
				scene.changeToGameScene();
				break;
			case InputMapper.controller.B:
				scene.setActionState(false);
				break;
			case InputMapper.controller.A:
				scene.setUseState(false);
				break;
			case InputMapper.controller.X:
				scene.setCancelState(false);
				break;
		}
		return false;
	}
	
	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		MenuScene scene = ProjectRPG.client.scenemanager.getScene(MenuScene.class);
		switch(buttonIndex) {
			case InputMapper.controller.B:
				scene.setActionState(true);
				break;
			case InputMapper.controller.A:
				scene.setUseState(true);
				break;
			case InputMapper.controller.X:
				scene.setCancelState(true);
				break;
		}
		return false;
	}
	
	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {
		MenuScene scene = ProjectRPG.client.scenemanager.getScene(MenuScene.class);
		float cValue = 0f;
		switch(axisIndex) {
			case InputMapper.controller.LEFTXAXIS:
				cValue = InputMapper.controller.INVERTLEFTXAXIS ? -value : value;
				if(cValue > 0.01) {
					scene.moveCursorX(cValue);
				} else if(cValue < 0.01) {
					scene.moveCursorX(cValue);
				} else {
					scene.moveCursorX(0f);
				}
				break;
			case InputMapper.controller.LEFTYAXIS:
				cValue = InputMapper.controller.INVERTLEFTYAXIS ? -value : value;
				if(cValue > 0.01) {
					scene.moveCursorY(cValue);
				} else if(cValue < 0.01) {
					scene.moveCursorY(cValue);
				} else {
					scene.moveCursorY(0f);
				}
				break;
		}
		return false;
	}

}
