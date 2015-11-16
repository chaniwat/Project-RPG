package com.skyhouse.projectrpg.input.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.input.InputMapper.keyboard;
import com.skyhouse.projectrpg.scene.MenuScene;
import com.skyhouse.projectrpg.scene.MenuScene.TabSet;

/**
 * Menu scene keyboard listener.
 * @author Meranote
 */
public class MenuInputListener extends InputAdapter {
	
	@Override
	public boolean keyUp(int keycode) {
		MenuScene scene = ProjectRPG.client.scenemanager.getScene(MenuScene.class);
		if(scene.getTabSet().equals(TabSet.MENU)) {
			switch (keycode) {
				case keyboard.L1:
					scene.prevTab();
					break;
				case keyboard.R1:
					scene.nextTab();
					break;
				case keyboard.START:
					scene.changeToGameScene();
					break;
				case keyboard.UP:
					if(Gdx.input.isKeyPressed(keyboard.DOWN)) scene.moveCursorY(-1f);
					else scene.moveCursorY(0);
					break;
				case keyboard.DOWN:
					if(Gdx.input.isKeyPressed(keyboard.UP)) scene.moveCursorY(1f);
					else scene.moveCursorY(0);
					break;
				case keyboard.LEFT:
					if(Gdx.input.isKeyPressed(keyboard.RIGHT)) scene.moveCursorX(1f);
					else scene.moveCursorX(0);
					break;
				case keyboard.RIGHT:
					if(Gdx.input.isKeyPressed(keyboard.LEFT)) scene.moveCursorX(-1f);
					else scene.moveCursorX(0);
					break;
				case keyboard.B:
					scene.setActionState(false);
					break;
				case keyboard.A:
					scene.setUseState(false);
					break;
				case keyboard.X:
					scene.setCancelState(false);
					break;
			}
		}
		return true;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		MenuScene scene = ProjectRPG.client.scenemanager.getScene(MenuScene.class);
		if(scene.getTabSet().equals(TabSet.MENU)) {
			switch (keycode) {
				case keyboard.UP:
					scene.moveCursorY(1f);
					break;
				case keyboard.DOWN:
					scene.moveCursorY(-1f);
					break;
				case keyboard.LEFT:
					scene.moveCursorX(-1f);
					break;
				case keyboard.RIGHT:
					scene.moveCursorX(1f);
					break;
				case keyboard.B:
					scene.setActionState(true);
					break;
				case keyboard.A:
					scene.setUseState(true);
					break;
				case keyboard.X:
					scene.setCancelState(true);
					break;
			}
		}
		return true;
	}
	
}
