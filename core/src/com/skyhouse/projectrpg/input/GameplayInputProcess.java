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
	
	/*
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		Vector3 worldCoordinates = mainCam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		Gdx.app.log(com.skyhouse.projectrpg.ProjectRpgGame.TITLE, "touch position: ("+worldCoordinates.x+","+worldCoordinates.y+")");
		new Box2DBallTest(PhysicGlobal.getWorld(), worldCoordinates.x, worldCoordinates.y);
		return true;
	}
	*/
	
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		switch(keycode) {
			case Keys.LEFT:
				if(Gdx.input.isKeyPressed(Keys.RIGHT)) playercharacter.walkRight();
				else playercharacter.stopWalk();
				break;
			case Keys.RIGHT:
				if(Gdx.input.isKeyPressed(Keys.LEFT)) playercharacter.walkLeft();
				else playercharacter.stopWalk();
				break;
			case Keys.DOWN:
				playercharacter.stand();
				break;
		}
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
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
			case Keys.DOWN:
				playercharacter.crouch();
				break;
		}
		return true;
	}
	
}
