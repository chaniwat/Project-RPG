package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextButton.VisTextButtonStyle;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.VisTextFieldStyle;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.net.packets.InitialRequest;

/**
 * Home scene. <br>
 * <b>First scene that need user to login.</b>
 * @author Meranote
 */
public class HomeScene extends Scene {

	/**
	 * GUI Container for home (or login) scene.
	 * @author Meranote
	 */
	private class GUILoginContainer extends Stage {
		public GUILoginContainer(Viewport viewport, Batch batch) {
			super(viewport, batch);
		}
	}
	private GUILoginContainer guicontainer;
	private VisTable root;
	private AssetManager assetmanager = ProjectRPG.Client.assetmanager;
	
	/**
	 * Construct a new home scene.
	 */
	public HomeScene() {
		addViewport(new ScreenViewport());
		guicontainer = new GUILoginContainer(getViewport(ScreenViewport.class), batch);
		ProjectRPG.Client.inputmanager.addInputProcessor(guicontainer);
		ProjectRPG.Client.inputmanager.setInputProcessor(GUILoginContainer.class);
		
		root = new VisTable(true);
		
		LabelStyle labelstyle = new LabelStyle(assetmanager.get("font/Roboto-Regular.ttf", BitmapFont.class), Color.WHITE);		
		VisLabel loginLabel = new VisLabel("ล๊อกอินอะ รู้จักไหม - -)\"", labelstyle);
		root.add(loginLabel).colspan(2).padBottom(10f);
		root.row();
		
		TextFieldStyle templatetextfieldstyle = new VisTextField().getStyle();
		VisTextFieldStyle textfieldstyle = new VisTextFieldStyle(assetmanager.get("font/Roboto-Regular.ttf", BitmapFont.class), templatetextfieldstyle.fontColor, templatetextfieldstyle.cursor, templatetextfieldstyle.selection, templatetextfieldstyle.background);
		
		final VisTextField userTextInput = new VisTextField("", textfieldstyle);
		root.add(new VisLabel("ID : "));
		root.add(userTextInput);
		root.row();
		
		final VisTextField passTextInput = new VisTextField("", textfieldstyle);
		passTextInput.setPasswordMode(true);
		passTextInput.setPasswordCharacter('*');
		root.add(new VisLabel("Pass : "));
		root.add(passTextInput);
		root.row();
		
		TextButtonStyle templatebuttonstyle = new VisTextButton("").getStyle();
		VisTextButtonStyle buttonstyle = new VisTextButtonStyle(templatebuttonstyle.up, templatebuttonstyle.down, templatebuttonstyle.checked, assetmanager.get("font/Roboto-Regular.ttf", BitmapFont.class));
		
		VisTextButton loginButton = new VisTextButton("เข้าสู่ระบบ", buttonstyle);
		loginButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				InitialRequest request = new InitialRequest();
				request.username = userTextInput.getText();
				request.password = passTextInput.getText();
				ProjectRPG.Client.network.net.sendTCP(request);
			}
			
		});
		root.add(loginButton).colspan(2).padTop(10f);
		
		root.pack();
		guicontainer.addActor(root);
	}
	
	@Override
	public void change() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float deltatime) {
		root.setPosition(
				(getViewport(ScreenViewport.class).getScreenWidth() / 2f) - (root.getWidth() / 2f), 
				(getViewport(ScreenViewport.class).getScreenHeight() / 2f) - (root.getHeight() / 2f));
		guicontainer.act(deltatime);
	}

	@Override
	public void draw(float deltatime) {
		guicontainer.draw();
	}

	@Override
	public void dispose() {
		guicontainer.dispose();
	}

}
