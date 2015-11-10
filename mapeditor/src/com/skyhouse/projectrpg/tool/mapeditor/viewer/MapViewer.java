package com.skyhouse.projectrpg.tool.mapeditor.viewer;

import java.awt.Color;
import java.awt.Cursor;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.skyhouse.projectrpg.data.MapData;
import com.skyhouse.projectrpg.data.StructureData;
import com.skyhouse.projectrpg.graphics.viewports.GameplayViewport;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.tool.mapeditor.MapEditor;

public class MapViewer extends ApplicationAdapter {
	
	private Map map;
	private World world;
	private SpriteBatch batch;
	private ShapeRenderer renderer;
	private GameplayViewport view;
	private ScreenViewport bgview;
	private Sprite background;
	
	private boolean isMoveCamera;
	
	private boolean setReloadMap = false;
	
	private final String mapFileName = "L01";
	
	/** text status */
	private String mapSizeStatus;
	private String positionStatus;
	
	@Override
	public void create() {
		world = new World(new Vector2(0, -10f), true);
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		view = new GameplayViewport(16f);
		bgview = new ScreenViewport();
		
		loadMap();
		
		MapEditor.viewercanvas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		Gdx.input.setInputProcessor(new InputAdapter() {
			
			private Vector2 position;
			private float cx, cy;
			
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				Vector2 worldposition = view.unproject(new Vector2(screenX, screenY));
				
				if(button == Input.Buttons.MIDDLE) {
					position = new Vector2(screenX, screenY);
					cx = view.getCamera().position.x;
					cy = view.getCamera().position.y;
					isMoveCamera = true;
					MapEditor.viewercanvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				}
				
				if(button == Input.Buttons.LEFT) {
					if(!MapEditor.isAnyToolSelected()) {
						String name = getObjectNameAt(worldposition.x, worldposition.y);
						if(!name.equals("") && !MapEditor.isSelectObject) {
							MapEditor.selectName = name;
							MapEditor.isSelectObject = true;
							MapEditor.window.getStatusBar().updateWorkStatus(MapEditor.selectName + " Selected!");
						} else if(!name.equals("") && MapEditor.isSelectObject) {
							if(name.equals(MapEditor.selectName)) {
								MapEditor.isSelectObject = false;
								MapEditor.window.getStatusBar().updateWorkStatus("Nothing!");
							} else {
								MapEditor.selectName = name;
								MapEditor.isSelectObject = true;
								MapEditor.window.getStatusBar().updateWorkStatus(MapEditor.selectName + " Selected!");
							}
						} else {
							MapEditor.isSelectObject = false;
							MapEditor.window.getStatusBar().updateWorkStatus("Nothing!");
						}
					}
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
					MapEditor.viewercanvas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				return true;
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
		bgview.update(width, height, true);
		
		background.setSize(bgview.getWorldHeight() * (map.getBackground().getWidth()/map.getBackground().getHeight()), bgview.getWorldHeight());
		background.setPosition(0, 0);
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
		
		// Draw bg
		bgview.apply();
		batch.setProjectionMatrix(bgview.getCamera().combined);
		
		batch.begin();
			background.draw(batch);
		batch.end();
		
		// Draw map
		view.apply();
		batch.setProjectionMatrix(view.getCamera().combined);
		renderer.setProjectionMatrix(view.getCamera().combined);
		
		batch.begin();
			map.draw(batch);
		batch.end();
		
		// Draw selected
		if(MapEditor.isSelectObject) {
			Gdx.gl.glEnable(GL11.GL_BLEND);
			Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			renderer.begin(ShapeType.Filled);
				renderer.setColor(0, 0.25f, 0, 0.45f);
				renderer.rect(MapEditor.selectX, MapEditor.selectY, MapEditor.selectWidth, -MapEditor.selectHeight);
			renderer.end();
			Gdx.gl.glDisable(GL11.GL_BLEND);
		}
		
		if(!isMoveCamera) {
			Vector2 mousetoworld = view.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
			float worldGridX = (float)Math.floor(mousetoworld.x);
			float worldGridY = (float)Math.floor(mousetoworld.y);
			positionStatus = String.format("Position : (%.2f, %.2f)", worldGridX, worldGridY);
			
			Gdx.gl.glEnable(GL11.GL_BLEND);
			Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			renderer.begin(ShapeType.Filled);
			renderer.setColor(0, 0, 0, 0.45f);
			renderer.rect(worldGridX, worldGridY, 1f, 1f);
			renderer.end();
			Gdx.gl.glDisable(GL11.GL_BLEND);
		}
		
		// Draw Grid
		Gdx.gl.glEnable(GL11.GL_BLEND);
		Gdx.gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		renderer.begin(ShapeType.Line);
			renderer.setColor(0, 0, 0, 0.25f);
			// Vertical
			for(int i = 0; i <= map.getData().mapWidth * 2; i++) {
				renderer.line(i, map.getData().mapHeight, i, -map.getData().mapHeight);
			}
			// Horizontal
			for(int i = 0; i <= map.getData().mapHeight; i++) {
				renderer.line(0, i, map.getData().mapWidth * 2, i);
			}
			for(int i = -map.getData().mapHeight; i < 0; i++) {
				renderer.line(0, i, map.getData().mapWidth * 2, i);
			}
		renderer.end();
		renderer.begin(ShapeType.Filled);
		renderer.setColor(0, 0, 0, 1f);
			renderer.circle(0, 0, 0.1f, 100);
		renderer.end();
		Gdx.gl.glDisable(GL11.GL_BLEND);
		
		MapEditor.window.getStatusBar().updateOverall(mapSizeStatus + " | " + positionStatus);
	}
	
	private void clearMap() {
		map.dispose();
	}
	
	private void loadMap() {
		String absolutePath = Gdx.files.getLocalStoragePath().replace("mapeditor\\", "").replace("\\", "/");
		MapData data = new MapData(Gdx.files.absolute(absolutePath + "core/assets/mapdata/" + mapFileName + ".map"));
		
		map = new Map(world, data);
		background = new Sprite(map.getBackground());
		background.setSize(bgview.getWorldHeight() * (map.getBackground().getWidth()/map.getBackground().getHeight()), bgview.getWorldHeight());
		background.setPosition(0, 0);
		background.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		mapSizeStatus = String.format("Map size : %dx%d", data.mapWidth, data.mapHeight);
		MapEditor.window.getStatusBar().updateWorkStatus("Map Loaded!", Color.BLACK, Color.GREEN);
	}
	
	public void reloadMap() {
		setReloadMap = true;
	}
	
	public Map getMap() {
		return map;
	}

	private String getObjectNameAt(float x, float y) {
		MapData data = map.getData();
		for(Entry<String, StructureData> entry : data.structures.entrySet()) {
			StructureData v = entry.getValue();
			if(x > v.x && x < v.x + v.width && y < v.y && y > v.y - v.height) {
				MapEditor.selectX = v.x;
				MapEditor.selectY = v.y;
				MapEditor.selectWidth = v.width;
				MapEditor.selectHeight = v.height;
				return entry.getKey();
			}
		}
		return "";
	}
	
}
