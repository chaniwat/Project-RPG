package com.skyhouse.projectrpg.tool.mapviewer;

import java.awt.Cursor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.skyhouse.projectrpg.data.MapData;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.map.Map;

public class MapViewer extends ApplicationAdapter {
	
	private Map map;
	private World world;
	private SpriteBatch batch;
	private GameplayViewport view;
	private Sprite background;
	
	@Override
	public void create() {
		world = new World(new Vector2(0, -10f), true);
		batch = new SpriteBatch();
		view = new GameplayViewport(16f);
		MapData data = new MapData(Gdx.files.internal("mapdata/L01.map"));
		
		map = new Map(world, data);
		background = new Sprite(map.getBackground());
		background.setSize(17 * (map.getBackground().getWidth()/map.getBackground().getHeight()), 17);
		background.setPosition(-20, -2);
		background.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);	
		
		Gdx.input.setInputProcessor(new InputAdapter() {
			
			private boolean isMoveCameraDown;
			private Vector2 position;
			private float cx, cy;
			
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				if(button == Input.Buttons.RIGHT) {
					position = new Vector2(screenX, screenY);
					cx = view.getCamera().position.x;
					cy = view.getCamera().position.y;
					isMoveCameraDown = true;
				}
				return true;
			}
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				if(isMoveCameraDown) {
					view.getCamera().position.x = cx + (16f * (position.x - screenX) / Gdx.graphics.getWidth());
					view.getCamera().position.y = cy - (16f * (position.y - screenY) / Gdx.graphics.getHeight());
					view.getCamera().update();			
				}
				return true;
			}
			
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				if(button == Input.Buttons.RIGHT) {
					isMoveCameraDown = false;
				}
				return true;
			}
			
		});
	}
	
	@Override
	public void resize(int width, int height) {
		view.update(width, height);
	}
	
	@Override
	public void render() {
		// Clear buffer
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		// Draw map
		view.apply();
		batch.setProjectionMatrix(view.getCamera().combined);
		
		batch.begin();
			background.draw(batch);
			map.draw(batch);
		batch.end();
	}

}
