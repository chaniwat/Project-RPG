package com.skyhouse.projectrpg.tool.mapeditor;

import java.awt.Cursor;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.skyhouse.projectrpg.data.MapData;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.map.Map;

public class MapViewer extends ApplicationAdapter {
	
	private Map map;
	private World world;
	private SpriteBatch batch;
	private ShapeRenderer renderer;
	private GameplayViewport view;
//	private Sprite background;
	
	private boolean isMoveCamera;
	
	private boolean setReloadMap = false;
	
	private final String mapFileName = "L02";
	
	@Override
	public void create() {
		world = new World(new Vector2(0, -10f), true);
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		view = new GameplayViewport(16f);	
		
		loadMap();
		
		MapEditorLauncher.mapView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		Gdx.input.setInputProcessor(new InputAdapter() {
			
			private Vector2 position;
			private float cx, cy;
			
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				if(button == Input.Buttons.MIDDLE) {
					position = new Vector2(screenX, screenY);
					cx = view.getCamera().position.x;
					cy = view.getCamera().position.y;
					isMoveCamera = true;
					MapEditorLauncher.mapView.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				}
				return true;
			}
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				if(isMoveCamera) {
					view.getCamera().position.x = cx + (16f * (position.x - screenX) / Gdx.graphics.getWidth()) * view.getViewScale();
					view.getCamera().position.y = cy - (16f * (position.y - screenY) / Gdx.graphics.getHeight()) * view.getViewScale();
					view.getCamera().update();
				}
				return true;
			}
			
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				if(button == Input.Buttons.MIDDLE) {
					isMoveCamera = false;
					MapEditorLauncher.mapView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				return true;
			}
			
			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				// TODO Auto-generated method stub
				return super.mouseMoved(screenX, screenY);
			}
			
			@Override 
			public boolean scrolled(int amount) {
				float newViewScale = view.getViewScale() + (0.05f*amount);
				if(newViewScale > 0.45f) {
					view.setViewScale(newViewScale);
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
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		// Update
		if(setReloadMap) {
			clearMap();
			loadMap();
			setReloadMap = false;
		}
		
		// Draw map
		view.apply();
		batch.setProjectionMatrix(view.getCamera().combined);
		renderer.setProjectionMatrix(view.getCamera().combined);
		
		batch.begin();
//			background.draw(batch);
			map.draw(batch);
		batch.end();
		
		if(!isMoveCamera) {
			Vector2 mousetoworld = view.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
			float worldGridX = (float)Math.floor(mousetoworld.x);
			float worldGridY = (float)Math.floor(mousetoworld.y);
			MapEditorLauncher.updatePositionStatus(worldGridX, worldGridY);
			
			Gdx.gl.glEnable(GL11.GL_BLEND);
			Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			renderer.begin(ShapeType.Filled);
			renderer.setColor(0, 0, 0, 0.45f);
			renderer.rect(worldGridX, worldGridY, 1f, 1f);
			renderer.end();
			Gdx.gl.glDisable(GL11.GL_BLEND);
		}
	}
	
	private void clearMap() {
		map.dispose();
	}
	
	private void loadMap() {
		String absolutePath = Gdx.files.getLocalStoragePath().replace("desktop\\", "").replace("\\", "/");
		MapData data = new MapData(Gdx.files.absolute(absolutePath + "core/assets/mapdata/" + mapFileName + ".map"));
		
		map = new Map(world, data);
//		background = new Sprite(map.getBackground());
//		background.setSize(17 * (map.getBackground().getWidth()/map.getBackground().getHeight()), 17);
//		background.setPosition(-20, -2);
//		background.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		MapEditorLauncher.updateMapSizeStatus(data.mapWidth, data.mapHeight);
	}
	
	public void reloadMap() {
		setReloadMap = true;
	}

}
