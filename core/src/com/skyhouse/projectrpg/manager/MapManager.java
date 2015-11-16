package com.skyhouse.projectrpg.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.box2d.World;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.utils.asset.loader.MapLoader.MapLoaderParameter;

/**
 * Manage map.
 * @author Meranote
 */
public class MapManager extends Manager {

	private AssetManager assetmanager = ProjectRPG.client.assetmanager;
	
	private String pathtomap = "";
	private Map map;
	private boolean isMapReady = false;
	
	@Override
	public void update(float deltaTime) {
		if(assetmanager.isLoaded(pathtomap) && isMapReady == false) {
			map = assetmanager.get(pathtomap, Map.class);
			ProjectRPG.client.entitymanager.updateNpc(map.getData().npcData);
			isMapReady = true;
		}
	}
	
	@Override
	public void dispose() {
		if(isMapReady) {
			assetmanager.unload(pathtomap);
		}
	}

	/**
	 * Change map.
	 */
	public void changeMap(String pathtomap, World world) {
		if(this.pathtomap != "") {
			assetmanager.unload(this.pathtomap);
		}
		isMapReady = false;
		this.pathtomap = pathtomap;
		MapLoaderParameter params = new MapLoaderParameter();
		params.world = world;
		assetmanager.load(pathtomap, Map.class, params);
	}
	
	/**
	 * Get the map by given map name.
	 * @return {@link Map}
	 */
	public Map getCurrentMap() {
		if(isMapReady) return map;
		else return null;
	}
	
	/**
	 * Check the map that was loaded or not.
	 * @return
	 */
	public boolean isMapReady() {
		return isMapReady;
	}
	
}
