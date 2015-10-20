package com.skyhouse.projectrpg.scene.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;

/**
 * Input processor used with {@link Scene}.
 * @author Meranote
 */
public class SceneInput {
	
	private InputProcessor input;
	private ControllerListener controller;
	 
	/** Construct a new SceneInput. */
	public SceneInput() {	}
	/** 
	 * Construct a new SceneInput with {@link InputAdapter} and {@link ControllerAdapter}.
	 * @param inputlistener
	 * @param controllerlistener 
	 */
	public SceneInput(InputProcessor inputlistener, ControllerListener controllerlistener) {
		this.input = inputlistener;
		this.controller = controllerlistener;
		
		Controllers.addListener(this.controller);
	}
	
	/** Set input listener to a given {@link InputAdapter}, <b>null</b> for disabled. */
	public void setInputProcessor(InputAdapter inputlistener) {
		this.input = (InputAdapter) inputlistener;
	}
	
	/** Set input listener to a given {@link ControllerAdapter}, <b>null</b> for disabled. */
	public void setControllerProcessor(ControllerAdapter controllerlistener) {
		this.controller = (ControllerAdapter) controllerlistener;
	}
	
	/** 
	 * Get the current input listener.
	 * @return {@link InputAdapter}
	 */
	public InputAdapter getInputProcessor() {
		return (InputAdapter)input;
	}
	
	/**
	 * Get the current controller listener.
	 * @return {@link ControllerAdapter}
	 */
	public ControllerAdapter getControllerProcessor() {
		return (ControllerAdapter)controller;
	}
	
	/** Set the listener to this input listener. */
	public void use() {
		Gdx.input.setInputProcessor(this.input);
		Controllers.clearListeners();
		if(controller != null) Controllers.addListener(this.controller);
	}
	
}
