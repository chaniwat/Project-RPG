package com.skyhouse.projectrpg.net.packets;

import com.skyhouse.projectrpg.entities.data.CharacterData;

public class InitialResponse {
	
	public CharacterData data;

	public InitialResponse() {
		data = new CharacterData();
	}
	
}
