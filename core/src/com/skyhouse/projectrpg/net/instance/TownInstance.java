package com.skyhouse.projectrpg.net.instance;

import com.badlogic.gdx.Gdx;
import com.skyhouse.projectrpg.data.MapData;

public class TownInstance extends Instance {
	
	public TownInstance() {
		super("Town", new MapData(Gdx.files.internal("data/map/L01.map")));
	}

}
