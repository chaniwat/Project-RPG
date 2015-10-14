package com.skyhouse.projectrpg.map;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class MapLoader extends SynchronousAssetLoader<Map, MapLoader.MapLoaderParameter> {
	
	public MapLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public Map load(AssetManager assetManager, String fileName, FileHandle file, MapLoaderParameter parameter) {
		try {
			return new Map(file, parameter.assetmanager);
		} catch (IOException e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, MapLoaderParameter parameter) {
		Array<AssetDescriptor> dependency = new Array<AssetDescriptor>();
		
		XmlReader reader = new XmlReader();
		Element root = null;
		try {
			root = reader.parse(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Element head = root.getChildByName("head");
		String pathtotexture = "textures/tilemap/" + head.getChildByName("texture").getText() + ".pack";
		String pathtobg = "textures/background/" + head.getChildByName("background").getChildByName("url").getText() + ".png";
		
		dependency.add(new AssetDescriptor(pathtotexture, TextureAtlas.class));
		dependency.add(new AssetDescriptor(pathtobg, Texture.class));
		
		return dependency;
	}

	public static class MapLoaderParameter extends AssetLoaderParameters<Map> {
		AssetManager assetmanager;
		
		public MapLoaderParameter(AssetManager assetmanager) {
			this.assetmanager = assetmanager;
		}
	}
	
}
