package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.net.packets.CreateCharacterRequest;
import com.skyhouse.projectrpg.scene.gui.DialogGUI;

/**
 * Character creator scene. <br>
 * <b>Create new character.</b>
 * @author Meranote
 */
public class CharacterCreatorScene extends Scene implements DialogGUI {

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
	private VisTextField nameTextInput;
	
	/**
	 * Construct a new character creator scene.
	 */
	public CharacterCreatorScene() {
		addViewport(new ScreenViewport());
		guicontainer = new GUICharacterCreatorContainer(getViewport(ScreenViewport.class), batch);
		ProjectRPG.client.inputmanager.addInputProcessor(guicontainer);
		
		prepareGUI();
	}
	
	
	/**
	 * Confirm dialog listener.<br>
	 * Internal use only.
	 * @author Meranote
	 */
	private class ConfirmDialogListener implements OptionDialogListener {

		@Override
		public void yes() { 
			CreateCharacterRequest request = new CreateCharacterRequest();
			request.uid = ProjectRPG.client.gamemanager.getUID();
			request.name = nameTextInput.getText();
			ProjectRPG.client.network.net.sendTCP(request);
		}

		@Override
		public void no() { }

		@Override
		public void cancel() { }
		
	}
	
	private void prepareGUI() {
		root = new VisTable(true);
		
		VisLabel loginLabel = new VisLabel("กำหนดชื่อตัวละคร");
		root.add(loginLabel).padBottom(10f);
		root.row();
		
		final VisTextField nameTextInput = new VisTextField("");
		root.add(nameTextInput);
		root.row();
		
		VisTextButton loginButton = new VisTextButton("สร้างตัวละคร");
		root.add(loginButton).colspan(2).padTop(10f);
		
		loginButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				DialogUtils.showOptionDialog(guicontainer, "Confirm", "Are you sure to use " + nameTextInput.getText() + " ?", OptionDialogType.YES_NO, new ConfirmDialogListener());
			}
		});
		
		root.setBackground("window");
		root.pad(20, 20, 20, 20);
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
	
	@Override
	public void showErrorDialog(String message) {
		DialogUtils.showErrorDialog(guicontainer, message);
	}

}
