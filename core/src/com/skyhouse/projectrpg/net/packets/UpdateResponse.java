package com.skyhouse.projectrpg.net.packets;

import java.util.HashMap;

import com.skyhouse.projectrpg.data.CharacterData;

public class UpdateResponse {

	public String currentInstance;
	public HashMap<Integer, CharacterData> data;
	
}
