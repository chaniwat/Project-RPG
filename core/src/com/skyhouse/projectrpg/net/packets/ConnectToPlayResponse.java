package com.skyhouse.projectrpg.net.packets;

import com.skyhouse.projectrpg.data.CharacterData;

public class ConnectToPlayResponse {
	
	public int state;
	public String instance;
	public CharacterData data;
	public String mappath;

}
