package com.skyhouse.projectrpg.server.system;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.CharacterData;

public class PlayerManagementSystem {

	private HashMap<Integer, Integer> clientPlayerConnectionIDMapper, playerClientConnectionIDMapper;
	private HashMap<Integer, String> clientInstanceMapper;
	private HashMap<Integer, CharacterData> playerDataMapper;
	
	public PlayerManagementSystem() {
		clientPlayerConnectionIDMapper = new HashMap<Integer, Integer>();
		playerClientConnectionIDMapper = new HashMap<Integer, Integer>();
		clientInstanceMapper = new HashMap<Integer, String>();
		playerDataMapper = new HashMap<Integer, CharacterData>();
	}
	
	public void addClientPlayer(int connectionID, int uid) {
		clientPlayerConnectionIDMapper.put(connectionID, uid);
		playerClientConnectionIDMapper.put(uid, connectionID);
	}
	
	public void removeClientPlayer(int connectionID) {
		int uid = clientPlayerConnectionIDMapper.remove(connectionID);
		clientInstanceMapper.remove(connectionID);
		playerClientConnectionIDMapper.remove(uid);
		playerDataMapper.remove(uid);
	}
	
	public CharacterData prepareConnectPlayer(int uid, CharacterData data, String instance) {
		Vector2 spawningpoint = ProjectRPG.server.instances.get(instance).getSpawningPoint();
		data.x = spawningpoint.x;
		data.y = spawningpoint.y;
		clientInstanceMapper.put(getConnectionIDOfUID(uid), instance);
		playerDataMapper.put(uid, data);
		return data;
	}
	
	public void disconnectPlayer(int connectionID) {
		ProjectRPG.server.instances.get(getClientCurrentInstance(connectionID)).removeCharacter(getUID(connectionID));
	}
	
	public void playerReady(int connectionID) {
		String playerinstance = getClientCurrentInstance(connectionID);
		ProjectRPG.server.instances.get(playerinstance).addCharacter(getUID(connectionID), playerDataMapper.get(getUID(connectionID)));
	}
	
	public void updateCharacterData(int uid, CharacterData data) {
		playerDataMapper.put(uid, data);
	}
	
	public CharacterData getCharacterData(int uid) {
		return playerDataMapper.get(uid);
	}
	
	public String getClientCurrentInstance(int connectionID) {
		return clientInstanceMapper.get(connectionID);
	}

	public boolean isUserLogin(int uid) {
		return playerClientConnectionIDMapper.containsKey(uid);
	}
	
	public int getConnectionIDOfUID(int uid) {
		return playerClientConnectionIDMapper.get(uid);
	}
	
	public boolean containUID(int uid) {
		return playerClientConnectionIDMapper.containsKey(uid);
	}
	
	public boolean containConnectionID(int connectionID) {
		return clientPlayerConnectionIDMapper.containsKey(connectionID);
	}
	
	public int getUID(int connectionID) {
		return clientPlayerConnectionIDMapper.get(connectionID);
	}
	
}
