package com.skyhouse.projectrpg.data;

/**
 * Input data. Use in gameplay, send to server to process. Control by controller or keyboard.
 * @author Meranote
 */
public class InputData extends Data {

	public boolean upPressed = false, 
				  				 leftPressed = false, 
				  				 rightPressed = false,
				  				 bottomPressed = false,
				  				 jumpPressed = false, 
				  				 skillAPressed = false, 
				  				 skillBPressed = false,
				  				 atkAPressed = false,
				  				 atkBPressed = false,
				  				 healPressed = false,
				  				 swapTrayPressed = false,
				  				 actionPressed = false;
	public float xAxisValue = 0f,
						  yAxisValue = 0f;
	
}
