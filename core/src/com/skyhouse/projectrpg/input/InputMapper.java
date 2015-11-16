package com.skyhouse.projectrpg.input;

import com.badlogic.gdx.Input.Keys;

public class InputMapper {

	private InputMapper() { }
	
	public static class keyboard {
		
		public static final int LEFT = Keys.LEFT,
				RIGHT = Keys.RIGHT,
				UP = Keys.UP,
				DOWN = Keys.DOWN,
				A = Keys.SPACE,
				B = Keys.CONTROL_LEFT,
				X = Keys.SHIFT_LEFT,
				Y = Keys.A,
				L1 = Keys.Z,
				L2 = Keys.S,
				R1 = Keys.X,
				R2 = Keys.C,
				START = Keys.ENTER;
		
	}
	
	public static class controller {
		
		public static final int LEFTXAXIS = 3,
			LEFTYAXIS = 2,
			A = 2,
			B = 1,
			X = 3,
			Y = 0,
			L1 = 4,
			L2 = 6,
			R1 = 5,
			R2 = 7,
			START = 9;
		public static final boolean INVERTLEFTXAXIS = false,
				INVERTLEFTYAXIS = true;
	}

}
