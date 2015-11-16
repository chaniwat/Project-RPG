package com.skyhouse.projectrpg.data;

/**
 * Input data. Use in gameplay, send to server to process. Control by controller or keyboard.
 * @author Meranote
 */
public class InputData extends Data {

	public boolean up = false, 
				  				 left = false, 
				  				 right = false,
				  				 down = false,
				  				 jump = false, 
				  				 dash = false,
				  				 skillA = false, 
				  				 skillB = false,
				  				 atkA = false,
				  				 atkB = false,
				  				 heal = false,
				  				 action = false;
	public float xAxisValue = 0f,
						  yAxisValue = 0f;
	
	/**
	 * Reset the input data.
	 */
	public void reset() {
		up = false;
		left = false;
		right = false;
		down = false;
		jump = false; 
		dash = false;
		skillA = false;
		skillB = false;
		atkA = false;
		atkB = false;
		heal = false;
		action = false;
		xAxisValue = 0f;
		yAxisValue = 0f;
	}
	
}
