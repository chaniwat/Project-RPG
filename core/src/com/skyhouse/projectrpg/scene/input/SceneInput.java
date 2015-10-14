package com.skyhouse.projectrpg.scene.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;


public class SceneInput {
	
	InputAdapter input;
	ControllerAdapter controller;
	 
	public SceneInput() {
		this.input = null;
		this.controller = null;
	}
	
	public SceneInput(InputProcessor inputlistener, ControllerListener controllerlistener) {
		this.input = (InputAdapter) inputlistener;
		this.controller = (ControllerAdapter) controllerlistener;
		
		Controllers.addListener(this.controller);
	}
	
	public void setInputProcessor(InputProcessor inputlistener) {
		this.input = (InputAdapter) inputlistener;
	}
	
	public void setControllerProcessor(ControllerListener controllerlistener) {
		this.controller = (ControllerAdapter) controllerlistener;
	}
	
	public InputAdapter getInputProcessor() {
		return input;
	}
	
	public ControllerAdapter getControllerProcessor() {
		return controller;
	}
	
	public void use() {
		Gdx.input.setInputProcessor(this.input);
		Controllers.clearListeners();
		if(controller != null) Controllers.addListener(this.controller);
	}
	
}
