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
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.util.dialog.OptionDialogListener;
import com.kotcrab.vis.ui.util.dialog.DialogUtils.OptionDialogType;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextButton.VisTextButtonStyle;
import com.kotcrab.vis.ui.widget.VisTextField.VisTextFieldStyle;
import com.skyhouse.projectrpg.ProjectRPG;

/**
 * Character creator scene. <br>
 * <b>Create new character.</b>
 * @author Meranote
 */
public class CharacterCreatorScene extends Scene {

	/**
	 * GUI Container for home (or login) scene.
	 * @author Meranote
	 */
	private class GUICharacterCreatorContainer extends Stage {
		public GUICharacterCreatorContainer(Viewport viewport, Batch batch) {
			super(viewport, batch);
		}
	}
	private GUICharacterCreatorContainer guicontainer;
	private VisTable root;
	private AssetManager assetmanager = ProjectRPG.client.assetmanager;
	
	/**
	 * Construct a new character creator scene.
	 */
	public CharacterCreatorScene() {
		addViewport(new ScreenViewport());
		guicontainer = new GUICharacterCreatorContainer(getViewport(ScreenViewport.class), batch);
		ProjectRPG.client.inputmanager.addInputProcessor(guicontainer);
		
		root = new VisTable(true);
		
		LabelStyle labelstyle = new LabelStyle(assetmanager.get("font/Roboto-Regular.ttf", BitmapFont.class), Color.WHITE);		
		VisLabel loginLabel = new VisLabel("ใส่ชื่อของท่านครับ", labelstyle);
		root.add(loginLabel).padBottom(10f);
		root.row();
		
		TextFieldStyle templatetextfieldstyle = new VisTextField().getStyle();
		VisTextFieldStyle textfieldstyle = new VisTextFieldStyle(assetmanager.get("font/Roboto-Regular.ttf", BitmapFont.class), templatetextfieldstyle.fontColor, templatetextfieldstyle.cursor, templatetextfieldstyle.selection, templatetextfieldstyle.background);
		
		final VisTextField nameTextInput = new VisTextField("", textfieldstyle);
		root.add(nameTextInput);
		root.row();
		
		TextButtonStyle templatebuttonstyle = new VisTextButton("").getStyle();
		VisTextButtonStyle buttonstyle = new VisTextButtonStyle(templatebuttonstyle.up, templatebuttonstyle.down, templatebuttonstyle.checked, assetmanager.get("font/Roboto-Regular.ttf", BitmapFont.class));
		
		VisTextButton loginButton = new VisTextButton("สร้างตัวละคร", buttonstyle);
		root.add(loginButton).colspan(2).padTop(10f);
		
		loginButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				DialogUtils.showOptionDialog(guicontainer, "Confirm", "Are you sure to use " + nameTextInput.getText() + " ?", OptionDialogType.YES_NO, new OptionDialogListener() {
					@Override
					public void yes() {
						// TODO send name to server to create a new character. 
					}
					@Override
					public void no() { return; }
					@Override
					public void cancel() { }
				});
			}
		});
		
		root.pack();
		guicontainer.addActor(root);
	}

	@Override
	public void change() {
		ProjectRPG.client.inputmanager.setInputProcessor(GUICharacterCreatorContainer.class);
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
