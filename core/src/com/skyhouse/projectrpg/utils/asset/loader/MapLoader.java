package com.skyhouse.projectrpg.utils.asset.loader;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.skyhouse.projectrpg.data.MapData;
import com.skyhouse.projectrpg.map.Map;

public class MapLoader extends AsynchronousAssetLoader<Map, MapLoader.MapLoaderParameter> {

	MapData data;
	
	public MapLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, MapLoaderParameter parameter) {
		
	}

	@Override
	public Map loadSync(AssetManager manager, String fileName, FileHandle file, MapLoaderParameter parameter) {
		return new Map(parameter.world, data, manager.get(data.mapTexturePackPath, TextureAtlas.class), manager.get(data.mapBackgroundPath, Texture.class));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, MapLoaderParameter parameter) {
		Array<AssetDescriptor> dependecy = new Array<AssetDescriptor>();
		
		data = new MapData(file);		
		dependecy.add(new AssetDescriptor(data.mapTexturePackPath, TextureAtlas.class));
		dependecy.add(new AssetDescriptor(data.mapBackgroundPath, Texture.class));
		
		return dependecy;
	}
	
	public static class MapLoaderParameter extends AssetLoaderParameters<Map> {
		public World world = null;
	}
	
}
