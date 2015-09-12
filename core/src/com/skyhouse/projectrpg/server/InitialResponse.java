package com.skyhouse.projectrpg.server;

import java.util.ArrayList;

import com.skyhouse.projectrpg.objects.CharacterData;

public class InitialResponse {
	
	public ArrayList<CharacterData> charactersData;
	public int clientid;
	
	public InitialResponse() {
		charactersData = new ArrayList<CharacterData>();
	}

}
