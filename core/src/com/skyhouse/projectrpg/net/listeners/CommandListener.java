package com.skyhouse.projectrpg.net.listeners;

import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.skyhouse.projectrpg.ProjectRPG;

public class CommandListener extends Thread {

	private Scanner input;
	private String command;
	private boolean running = true;
	
	public CommandListener() {
		super("CommandListener");
	}
	
	@Override
	public void run() {
		input = new Scanner(System.in);
			
		while(running) {
			command = input.nextLine();			
			postCommand(command);
		}
	}
	
	private void postCommand(String command) {
		Gdx.app.log(ProjectRPG.TITLE, "Executed command -> " + command);
		if(command.equals("quit")) {
			running = false;
			Gdx.app.exit();
		}
	}
	
}
