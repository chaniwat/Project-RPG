package com.skyhouse.projectrpg.objects;

import com.badlogic.gdx.Gdx;
import com.brashmonkey.spriter.Spriter;

public class Actor {
	
	public Actor (String pathtoscml) {
		Spriter.load(Gdx.files.internal(pathtoscml).read(), pathtoscml);
	}

}
