package com.skyhouse.projectrpg.server.system;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.CharacterData;

public class PlayerManagementSystem {

	private HashMap<Integer, Integer> clientPlayerConnectionIDMapper;
	
	public PlayerManagementSystem() {
		clientPlayerConnectionIDMapper = new HashMap<Integer, Integer>();
	}
	
	public void addClientPlayer(int connectionID, int uid) {
		clientPlayerConnectionIDMapper.put(connectionID, uid);
	}
	
	/**
	 */
	public CharacterData connectPlayer(int uid, CharacterData data) {
		Vector2 spawningpoint = ProjectRPG.server.townInstance.getSpawningPoint();
		data.x = spawningpoint.x;
		data.y = spawningpoint.y;
		ProjectRPG.server.townInstance.addCharacter(uid, data);
		return data;
	}

	public boolean isUserLogin(int uid) {
		return clientPlayerConnectionIDMapper.containsValue(uid);
	}
	
}
