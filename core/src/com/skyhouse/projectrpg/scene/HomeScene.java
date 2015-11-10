package com.skyhouse.projectrpg.scene;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.net.packets.LoginRequest;
import com.skyhouse.projectrpg.scene.gui.DialogGUI;

/**
 * Home scene. <br>
 * <b>First scene that need user to login.</b>
 * @author Meranote
 */
public class HomeScene extends Scene implements DialogGUI {

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
	private VisTextField userTextInput;
	private VisTextField passTextInput;
	
	private float screenwidth, screenheight;
	
	private BitmapFont font;
	private GlyphLayout layout = ProjectRPG.client.graphic.font.layout;
	
	private boolean isFirstChange = true;
	private static final float MINOVERLAYTIME = 2f;
	private float accumulatorOverlayTime;
	
	/**
	 * Construct a new home scene.
	 */
	public HomeScene() {
		addViewport(new ScreenViewport());
		guicontainer = new GUILoginContainer(getViewport(ScreenViewport.class), batch);
		ProjectRPG.client.inputmanager.addInputProcessor(guicontainer);

		font = ProjectRPG.client.graphic.font.regular;
		layout = new GlyphLayout();
		
		accumulatorOverlayTime = MINOVERLAYTIME;
		
		prepareGUI();
	}
	
	/**
	 * Text field listener.<br>
	 * Internal use only.
	 * @author Meranote
	 */
	private class TextFieldListener extends InputListener {
		@Override
		public boolean keyDown(InputEvent event, int keycode) {
			if(keycode == Keys.ENTER) {
				doLogin(userTextInput.getText(), passTextInput.getText());
			}
			return true;
		}
	}
	
	private void prepareGUI() {
		root = new VisTable(true);
		
		VisLabel loginLabel = new VisLabel("เข้าสู่ระบบ");
		root.add(loginLabel).colspan(2).padBottom(10f);
		root.row();
		
		userTextInput = new VisTextField("");
		root.add(new VisLabel("ID : "));
		root.add(userTextInput);
		root.row();
		
		passTextInput = new VisTextField("");
		passTextInput.setPasswordMode(true);
		passTextInput.setPasswordCharacter('*');
		root.add(new VisLabel("Pass : "));
		root.add(passTextInput);
		root.row();
		
		VisTextButton loginButton = new VisTextButton("เข้าสู่ระบบ");
		root.add(loginButton).colspan(2).padTop(10f);
		
		TextFieldListener inputListener = new TextFieldListener();
		userTextInput.addListener(inputListener);
		passTextInput.addListener(inputListener);
		loginButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				doLogin(userTextInput.getText(), passTextInput.getText());
			}
		});
		
		root.setBackground("window");
		root.pad(20, 20, 20, 20);
		root.pack();
		
		guicontainer.addActor(root);
	}
	
	private void doLogin(String username, String password) {
		LoginRequest request = new LoginRequest();
		request.username = username;
		request.password = password;
		ProjectRPG.client.network.net.sendTCP(request);
	}
	
	@Override
	public void showErrorDialog(String message) {
		DialogUtils.showErrorDialog(guicontainer, message);
	}
	
	@Override
	public void change() {
		if(isFirstChange) {
			ProjectRPG.client.scenemanager.deleteScene(StartScene.class);
			isFirstChange = false;
		}
		ProjectRPG.client.inputmanager.setInputProcessor(GUILoginContainer.class);
	}

	@Override
	public void update(float deltatime) {
		screenwidth = getViewport(ScreenViewport.class).getWorldWidth();
		screenheight = getViewport(ScreenViewport.class).getWorldHeight();
		
		updateGUI(deltatime);
		
		if(accumulatorOverlayTime > 0f) {
			accumulatorOverlayTime -= deltatime;
		} else accumulatorOverlayTime = 0f;
	}
	
	private void updateGUI(float deltatime) {
		root.setPosition((screenwidth / 2f) - (root.getWidth() / 2f), ((screenheight / 2f) - (root.getHeight() / 2f)));
		guicontainer.act(deltatime);
	}

	@Override
	public void draw(float deltatime) {
		guicontainer.draw();
		
		batch.begin();
			layout.setText(font, "[WHITE]" + ProjectRPG.TITLE + ": " + "Version = " + ProjectRPG.VERSION);
			font.draw(batch, layout, screenwidth - layout.width - 20, layout.height + 20);
		batch.end();
		
		// Overlay
		ProjectRPG.client.graphic.enableGLAlphaBlend();
		renderer.begin(ShapeType.Filled);
			renderer.setColor(0, 0, 0, accumulatorOverlayTime / MINOVERLAYTIME);
			renderer.rect(0, 0, screenwidth, screenheight);
		renderer.end();
		ProjectRPG.client.graphic.disableGLAlphaBlend();
		
	}

	@Override
	public void dispose() {
		guicontainer.dispose();
	}

}
