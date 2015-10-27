package com.skyhouse.projectrpg.manager;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.skyhouse.projectrpg.ProjectRPG;

/**
 * Manage the input listener and data.
 * @author Meranote
 */
public class InputManager extends Manager {

	private HashMap<Class<? extends InputProcessor>, InputAdapter> inputs;
	private HashMap<Class<? extends ControllerListener>, ControllerAdapter> controllers;
	private InputAdapter currentInput;
	private ControllerAdapter currentController;
	
	private InputAdapter nullInput = new InputAdapter();
	private ControllerAdapter nullController = new ControllerAdapter();
	
	private boolean isChanged = false;
	
	/**
	 * Construct a new InputManager with no listener setup.
	 */
	public InputManager() {
		this(null, null);
	}
	/** 
	 * Construct a new InputManager with {@link InputAdapter} and {@link ControllerAdapter}.
	 * @param inputListener
	 * @param controllerListener 
	 */
	public InputManager(InputAdapter inputListener, ControllerAdapter controllerListener) { 
		inputs = new HashMap<Class<? extends InputProcessor>, InputAdapter>();
		controllers = new HashMap<Class<? extends ControllerListener>, ControllerAdapter>();
		
		if(inputListener != null) {
			inputs.put(inputListener.getClass(), inputListener);
			currentInput = inputListener;
		} else {
			currentInput = nullInput;
		}
		if(controllerListener != null) {
			controllers.put(controllerListener.getClass(), controllerListener);
			currentController = controllerListener;
		} else {
			currentController = nullController;			
		}
		
		Gdx.input.setInputProcessor(currentInput);
		Controllers.addListener(currentController);
	}
	
	/**
	 * Add input listener.
	 * @param inputListener extend class of {@link InputAdapter}
	 */
	public void addInputProcessor(InputAdapter inputListener) {
		if(inputs.containsKey(inputListener.getClass())) {
			Gdx.app.log(ProjectRPG.TITLE, "Already add this input listener class.");
			return;
		}
		inputs.put(inputListener.getClass(), inputListener);
	}
	
	/**
	 * Add controller listener.
	 * @param controllerListener extend class of {@link ControllerAdapter}
	 */
	public void addControllerProcessor(ControllerAdapter controllerListener) {
		if(controllers.containsKey(controllerListener.getClass())) {
			Gdx.app.log(ProjectRPG.TITLE, "Already add this controller listener class.");
			return;
		}
		controllers.put(controllerListener.getClass(), controllerListener);
	}
	
	/** Set input listener to a given extend class of {@link InputAdapter}, <b>null</b> for disabled. */
	public void setInputProcessor(Class<? extends InputAdapter> inputListenerClass) {
		if(inputListenerClass == null) {
			currentInput = nullInput;
			isChanged = true;
			return;
		}
		currentInput = inputs.get(inputListenerClass);
		if(currentInput == null) throw new GdxRuntimeException("No input listener added. Add first.");
		isChanged = true;
	}
	
	/** Set controller listener to a given extend class of {@link ControllerAdapter}, <b>null</b> for disabled. */
	public void setControllerProcessor(Class<? extends ControllerAdapter> controllerListenerClass) {
		if(controllerListenerClass == null) {
			currentController = null;
			isChanged = true;
			return;
		}
		currentController = controllers.get(controllerListenerClass);
		if(currentController == null) throw new GdxRuntimeException("No contoller listener added. Add first.");
		isChanged = true;
	}
	
	/** 
	 * Get the current input listener.
	 * @return {@link InputAdapter}
	 */
	public InputAdapter getInputProcessor() {
		return currentInput;
	}
	
	/**
	 * Get the current controller listener.
	 * @return {@link ControllerAdapter}
	 */
	public ControllerAdapter getControllerProcessor() {
		return currentController;
	}
	
	@Override
	public void update(float deltaTime) {
		if(isChanged) {
			if(currentInput != null) {			
				Gdx.input.setInputProcessor(currentInput);
			} else {
				Gdx.input.setInputProcessor(nullInput);
			}
			Controllers.clearListeners();
			if(currentController != null) Controllers.addListener(this.currentController);
		}
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
