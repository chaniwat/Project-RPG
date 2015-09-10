package com.skyhouse.projectrpg.server;

import java.util.Scanner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.skyhouse.projectrpg.ProjectRPGGame.ProjectRPG;

public class ProjectRPGServer extends ApplicationAdapter {
	
	float timer, nextTimer;
	
	@Override
	public void create() {
		
		timer = 0f;	
		nextTimer = 1f;
		
		new Thread(new Runnable() {
			
			Scanner input;
			
			@Override
			public void run() {
				input = new Scanner(System.in);
				while(true) {
					String command = input.nextLine();	
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							receiveConsole(command);
						}
					});
				}
			}
		}).start();
	}

	@Override
	public void render() {
		timer += Gdx.graphics.getDeltaTime();
		if(timer > nextTimer) {
			Gdx.app.log(ProjectRPG.TITLE, String.format("%.2f", timer));
			nextTimer += 1f;
		}
	}
	
	private void receiveConsole(String command) {
		if(command.equals("test")) {
			Gdx.app.log(ProjectRPG.TITLE, "Checked!");
		}
	}

	@Override
	public void dispose() {
		
	}

}
