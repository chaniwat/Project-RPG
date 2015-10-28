package com.skyhouse.projectrpg.manager;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.map.Map;
import com.skyhouse.projectrpg.utils.asset.loader.MapLoader.MapLoaderParameter;

/**
 * Manage map. Contain the collection of {@link Map}.
 * @author Meranote
 */
public class MapManager extends Manager {

	private AssetManager assetmanager = ProjectRPG.Client.assetmanager;
	private ArrayList<String> loadingMapPath = new ArrayList<String>();
	private ArrayList<String> finishLoadMapPath = new ArrayList<String>();
	private HashMap<String, Map> maps = new HashMap<String, Map>();
	
	@Override
	public void update(float deltaTime) {
		for(String path : loadingMapPath) {
			if(assetmanager.isLoaded(path)) {
				Map m = assetmanager.get(path, Map.class);
				if(maps.containsKey(m.getData().name)) {
					throw new GdxRuntimeException("duplicate map name found. exit.");
				} else {
					maps.put(m.getData().name, m);					
				}
				finishLoadMapPath.add(path);
			}
		}
		while(!finishLoadMapPath.isEmpty()) loadingMapPath.remove(finishLoadMapPath.remove(0));
	}
	
	/**
	 * Add new map .
	 */
	public void addMap(String pathToMap, World world) {
		if(assetmanager.isLoaded(pathToMap)) {
			Gdx.app.log(ProjectRPG.TITLE, "already load this map.");
			return;
		}
		if(loadingMapPath.contains(pathToMap)) {
			Gdx.app.log(ProjectRPG.TITLE, "this map is in loading queue.");
			return;
		}
		MapLoaderParameter params = new MapLoaderParameter();
		params.world = world;
		assetmanager.load(pathToMap, Map.class, params);
		loadingMapPath.add(pathToMap);
	}
	
	/**
	 * Get the map by given map name.
	 * @return {@link Map}
	 */
	public Map getMap(String name) {
		if(!maps.containsKey(name)) {
			assetmanager.finishLoadingAsset(loadingMapPath.get(loadingMapPath.size() - 1));
			loadingMapPath.clear();
		}
		if(!maps.containsKey(name)) throw new GdxRuntimeException("No given map added.");
		return maps.get(name);
	}
	
	/**
	 * Return the collection of {@link Map}. <br>
	 * <b>Key is represent to map name</b>
	 * @return {@link HashMap}<{@link String}, {@link Map}>
	 */
	public HashMap<String, Map> getAllMap() {
		if(!loadingMapPath.isEmpty()) {
			assetmanager.finishLoadingAsset(loadingMapPath.get(loadingMapPath.size() - 1));
			loadingMapPath.clear();
		}
		return maps;
	}
	
	/**
	 * Remove the map by given map name.
	 * @param name
	 */
	public void removeMap(String name) {
		maps.get(name).dispose();
	}

	@Override
	public void dispose() {
		if(!loadingMapPath.isEmpty()) {
			assetmanager.finishLoadingAsset(loadingMapPath.get(loadingMapPath.size() - 1));
			loadingMapPath.clear();
		}
		maps.clear();
	}
	
}
