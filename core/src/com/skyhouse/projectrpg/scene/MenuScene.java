package com.skyhouse.projectrpg.scene;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.input.listener.MenuControllerListener;
import com.skyhouse.projectrpg.input.listener.MenuInputListener;
import com.skyhouse.projectrpg.scene.menu.tab.CharacterTab;
import com.skyhouse.projectrpg.scene.menu.tab.OptionTab;
import com.skyhouse.projectrpg.scene.menu.tab.PartyQuestTab;
import com.skyhouse.projectrpg.scene.menu.tab.SkillTab;
import com.skyhouse.projectrpg.scene.menu.tab.Tab;

/**
 * Menu scene of gameplay.<br>
 * <b>Collection of view such as Inventory, Skill, option, etc.</b>
 * @author Meranote
 */
public class MenuScene extends Scene {
	
	public static enum TabSet {
		MENU,
		NPC,
		FINISH
	}
	
	private static final float FADETIME = 0.2f;
	private float accumulatorFadeTime;
	private boolean fadeInFlag, fadeOutFlag;
	
	private float screenwidth, screenheight;
	
	private AssetManager assetmanager = ProjectRPG.client.assetmanager;
	private BitmapFont font = ProjectRPG.client.graphic.font.spritefont;
	private BitmapFont fontbold = ProjectRPG.client.graphic.font.spritefontbold;
	private GlyphLayout layout = ProjectRPG.client.graphic.font.layout;
	
	private HashMap<Class<? extends Tab>, Tab> tabLists;
	private TabSet currentSet = TabSet.MENU;
	
	// Menu Tab
	private CharacterTab charactertab;
	private SkillTab skilltab;
	private PartyQuestTab partyquesttab;
	private OptionTab optiontab;
	
	private float menuWidthOffset;
	
	/** Base on height = 720. */
	private float resolutionHeightScale;
	
	private Sprite cursor;
	private boolean isCursorDisable = false;
	private float cursorBaseSpeed = 5f;
	private float cursorMoveX = 0f, cursorMoveY = 0f;
	
	// Menu Set
	private Date dNow;
	private SimpleDateFormat dateFormat, timeFormat, hourFormat;
	
	private Sprite backgroundMenu;
	
	// Bar
	private Sprite timeSprite;
	private Rectangle timeRect, tabRect;
	// Tab bar
	private Sprite upArrow, downArrow, characterIcon, skillIcon, partyIcon, optionIcon;
	private float[] arrowPositionSet = {0.16f, 0.403f, 0.64f, 0.882f};
	private int currentMenuTab = 0;
	
	/**
	 * Construct a new menu scene.
	 */
	public MenuScene() {
		addViewport(new ScreenViewport());
		tabLists = new HashMap<Class<? extends Tab>, Tab>();
		
		// Build Cursor
		cursor = new Sprite(assetmanager.get("texture/ui/menuscene/cursor.png", Texture.class));
		
		// Build Bar
		dNow = new Date();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		timeFormat = new SimpleDateFormat("HH:mm:ss");
		hourFormat = new SimpleDateFormat("HH");
		
		timeRect = new Rectangle();
		tabRect = new Rectangle();
		
		backgroundMenu = new Sprite(assetmanager.get("texture/ui/menuscene/bg.png", Texture.class));
		timeSprite = new Sprite(assetmanager.get("texture/ui/menuscene/bar/timebar/sun.png", Texture.class));
		
		// Tab Bar
		upArrow = new Sprite(assetmanager.get("texture/ui/menuscene/bar/tabbar/arrow.png", Texture.class));
		downArrow = new Sprite(assetmanager.get("texture/ui/menuscene/bar/tabbar/arrow.png", Texture.class));
		characterIcon = new Sprite(assetmanager.get("texture/ui/menuscene/bar/tabbar/character.png", Texture.class));
		skillIcon = new Sprite(assetmanager.get("texture/ui/menuscene/bar/tabbar/skill.png", Texture.class));
		partyIcon = new Sprite(assetmanager.get("texture/ui/menuscene/bar/tabbar/group.png", Texture.class));
		optionIcon = new Sprite(assetmanager.get("texture/ui/menuscene/bar/tabbar/settings.png", Texture.class));
		
		downArrow.setRotation(180f);
		
		// Construct and settings tab
		charactertab = new CharacterTab();
		skilltab = new SkillTab();
		partyquesttab = new PartyQuestTab();
		optiontab = new OptionTab();
		
		// Add all tab to the list
		tabLists.put(charactertab.getClass(), charactertab);
		tabLists.put(skilltab.getClass(), skilltab);
		tabLists.put(partyquesttab.getClass(), partyquesttab);
		tabLists.put(optiontab.getClass(), optiontab);
	}
	
	@Override
	public void change() {
		ProjectRPG.client.inputmanager.setInputProcessor(MenuInputListener.class);
		ProjectRPG.client.inputmanager.setControllerProcessor(MenuControllerListener.class);
		ProjectRPG.client.gamemanager.getInputData().reset();
		
		currentMenuTab = 0;
		accumulatorFadeTime = FADETIME;
		fadeOutFlag = true;
		fadeInFlag = false;
	}
	
	@Override
	public void resize(float width, float height) {
		screenwidth = getViewport(ScreenViewport.class).getWorldWidth();
		screenheight = getViewport(ScreenViewport.class).getWorldHeight();
		resolutionHeightScale =  screenheight / 720f;
		
		menuWidthOffset = screenwidth * currentMenuTab;
		
		cursor.setSize((cursor.getTexture().getWidth() * 0.7f) * resolutionHeightScale, (cursor.getTexture().getHeight() * 0.7f) * resolutionHeightScale);
		cursor.setOrigin(cursor.getWidth() / 2f, cursor.getHeight() / 2f);
		cursor.setCenter(screenwidth / 2f, screenheight / 2f);
	}

	@Override
	public void update(float deltatime) {
		dNow.setTime(System.currentTimeMillis());
	
		cursorBaseSpeed = 8f * resolutionHeightScale;
		
		cursor.rotate(1.5f);
		cursor.translate(cursorBaseSpeed * cursorMoveX, cursorBaseSpeed * cursorMoveY);
		
		// Update specific set
		if(currentSet.equals(TabSet.MENU)) {
			
			if(cursor.getY() > screenheight - (70f * resolutionHeightScale + cursor.getHeight())) {
				cursor.setPosition(cursor.getX(), screenheight - (70f * resolutionHeightScale + cursor.getHeight()));
			} else if (cursor.getY() < 0f) {
				cursor.setPosition(cursor.getX(), 0f);
			}
			
			if(cursor.getX() < 0f) {
				cursor.setPosition(0f, cursor.getY());
			} else if(cursor.getX() > screenwidth - cursor.getWidth()) {
				cursor.setPosition(screenwidth - cursor.getWidth(), cursor.getY());
			}
			updateMenuSet(deltatime);
		}
		
		// Update current set
		for(Tab tab : tabLists.values()) {
			tab.updateTabSize(screenwidth, screenheight);
			if(tab.getTabSetType().equals(currentSet)) {
				tab.update(deltatime, resolutionHeightScale);
			}
		}
		
		if(fadeOutFlag) {
			if(accumulatorFadeTime > 0f) {
				accumulatorFadeTime -= deltatime;
			} else {
				accumulatorFadeTime = 0f;
				fadeOutFlag = false;
			}
		} else if(fadeInFlag) {
			if(accumulatorFadeTime < FADETIME) {
				accumulatorFadeTime += deltatime;
			} else {
				accumulatorFadeTime = FADETIME;
				ProjectRPG.client.scenemanager.setUseScene(GameScene.class);
			}
		}
		
	}

	private void updateMenuSet(float deltatime) {
		// Update Tab positon
		for(Tab tab : tabLists.values()) {
			if(tab.getTabSetType().equals(currentSet)) {
				tab.updateTabPosition(((screenwidth / 2f) - cursor.getX()) * 0.14f, ((screenheight / 2f) - cursor.getY()) * 0.14f);
			}
		}
		
		if(menuWidthOffset < (screenwidth + (200f * resolutionHeightScale)) * currentMenuTab) {
			menuWidthOffset += ((screenwidth + (200f * resolutionHeightScale)) * deltatime) / 0.25f;
			if(menuWidthOffset >= (screenwidth + (200f * resolutionHeightScale)) * currentMenuTab) menuWidthOffset = (screenwidth + (200f * resolutionHeightScale)) * currentMenuTab;
		} else if(menuWidthOffset > (screenwidth + (200f * resolutionHeightScale)) * currentMenuTab) {
			menuWidthOffset -= ((screenwidth + (200f * resolutionHeightScale)) * deltatime) / 0.25f;
			if(menuWidthOffset <= (screenwidth + (200f * resolutionHeightScale)) * currentMenuTab) menuWidthOffset = (screenwidth + (200f * resolutionHeightScale)) * currentMenuTab;
		}
		
		charactertab.updateTabPosition(charactertab.getTabX() - (menuWidthOffset), charactertab.getTabY());
		skilltab.updateTabPosition((skilltab.getTabX() + (screenwidth + (200f * resolutionHeightScale))) - menuWidthOffset, skilltab.getTabY());
		partyquesttab.updateTabPosition((partyquesttab.getTabX() + (screenwidth + (200f * resolutionHeightScale)) * 2) - menuWidthOffset, partyquesttab.getTabY());
		optiontab.updateTabPosition((optiontab.getTabX() + (screenwidth + (200f * resolutionHeightScale)) * 3) - menuWidthOffset, optiontab.getTabY());
		
		// Update BG
		backgroundMenu.setSize(screenheight * ((float)backgroundMenu.getTexture().getWidth() / (float)backgroundMenu.getTexture().getHeight()), screenheight);
		if(backgroundMenu.getWidth() < screenwidth) {
			backgroundMenu.setSize(screenwidth, screenwidth * ((float)backgroundMenu.getTexture().getHeight() / (float)backgroundMenu.getTexture().getWidth()));
		}
		backgroundMenu.setPosition(getViewport(ScreenViewport.class).getCamera().position.x - (backgroundMenu.getWidth() / 2f),  0);
		
		// Update time sprite
		int currentTime = Integer.parseInt(hourFormat.format(dNow));
		if(currentTime >= 6 && currentTime < 18) {
			timeSprite.setTexture(assetmanager.get("texture/ui/menuscene/bar/tabbar/character.png", Texture.class));
		} else {
			timeSprite.setTexture(assetmanager.get("texture/ui/menuscene/bar/timebar/moon.png", Texture.class));
		}
		timeSprite.setSize(timeSprite.getTexture().getWidth() * 0.26f * resolutionHeightScale, timeSprite.getTexture().getHeight() * 0.26f * resolutionHeightScale);
		timeSprite.setPosition(11f * resolutionHeightScale, screenheight - ((timeSprite.getHeight() + (11f * resolutionHeightScale))));
		
		// Update bg bar
		timeRect.x = 0;
		timeRect.y = screenheight - (70f * resolutionHeightScale);
		timeRect.width = 195f * resolutionHeightScale;
		timeRect.height = 70f * resolutionHeightScale;
		
		tabRect.x = timeRect.width;
		tabRect.y = timeRect.y;
		tabRect.width = screenwidth - timeRect.width;
		tabRect.height = timeRect.height;
		
		// Update tab icon
		characterIcon.setSize(characterIcon.getTexture().getWidth() * 0.2f * resolutionHeightScale, characterIcon.getTexture().getHeight() * 0.2f * resolutionHeightScale);
		skillIcon.setSize(skillIcon.getTexture().getWidth() * 0.24f * resolutionHeightScale, skillIcon.getTexture().getHeight() * 0.24f * resolutionHeightScale);
		partyIcon.setSize(partyIcon.getTexture().getWidth() * 0.24f * resolutionHeightScale, partyIcon.getTexture().getHeight() * 0.24f * resolutionHeightScale);
		optionIcon.setSize(optionIcon.getTexture().getWidth() * 0.24f * resolutionHeightScale, optionIcon.getTexture().getHeight() * 0.24f * resolutionHeightScale);
		
		upArrow.setSize(upArrow.getTexture().getWidth() * 0.23f * resolutionHeightScale, upArrow.getTexture().getHeight() * 0.23f * resolutionHeightScale);
		upArrow.setPosition(tabRect.x + (tabRect.width * arrowPositionSet[currentMenuTab]), screenheight - tabRect.height);
		downArrow.setSize(downArrow.getTexture().getWidth() * 0.23f * resolutionHeightScale, downArrow.getTexture().getHeight() * 0.23f * resolutionHeightScale);
		downArrow.setOrigin(downArrow.getWidth() / 2f, downArrow.getHeight() / 2f);
		downArrow.setPosition(tabRect.x + (tabRect.width * arrowPositionSet[currentMenuTab]), screenheight - downArrow.getHeight());
	}

	@Override
	public void draw(float deltatime) {
		useViewport(ScreenViewport.class);
		
		// Draw specific set
		if(currentSet.equals(TabSet.MENU)) {
			drawMenuSet();
		}
		
		// Draw current tab set
		batch.begin();
			for(Tab tab : tabLists.values()) {
				if(tab.getTabSetType().equals(currentSet)) {
					tab.draw(batch, resolutionHeightScale);
				}
			}
		batch.end();

		// Draw Cursor
		if(!isCursorDisable) {
			batch.begin();
				cursor.draw(batch);
			batch.end();
		}
		
		// Overlay
		ProjectRPG.client.graphic.enableGLAlphaBlend();
		renderer.begin(ShapeType.Filled);
			if((accumulatorFadeTime / FADETIME) > 0f) {
				renderer.setColor(0, 0, 0, accumulatorFadeTime / FADETIME);
				renderer.rect(0, 0, screenwidth, screenheight);
			}
		renderer.end();
		ProjectRPG.client.graphic.disableGLAlphaBlend();
	}
	
	private float layout_x, layout_y;
	
	private void drawMenuSet() {
		// Draw background
		batch.begin();
			backgroundMenu.draw(batch);
		batch.end();
		
		ProjectRPG.client.graphic.enableGLAlphaBlend();
		renderer.begin(ShapeType.Filled);
			renderer.setColor(0, 0, 0, 0.9f);
			renderer.rect(timeRect.x, timeRect.y, timeRect.width, timeRect.height);
			renderer.setColor(0, 0, 0, 0.7f);
			renderer.rect(tabRect.x, tabRect.y, tabRect.width, tabRect.height);
		renderer.end();
		ProjectRPG.client.graphic.disableGLAlphaBlend();
		
		batch.begin();
			timeSprite.draw(batch);
			
			batch.setShader(ProjectRPG.client.graphic.font.shader);
				font.getData().setScale(0.74f * resolutionHeightScale);
				layout.setText(font, timeFormat.format(dNow));
				font.draw(batch, layout, 70f * resolutionHeightScale, screenheight - (14f * resolutionHeightScale));
				font.getData().setScale(0.6f * resolutionHeightScale);
				layout.setText(font, dateFormat.format(dNow));
				font.draw(batch, layout, 71f * resolutionHeightScale, screenheight - (43f * resolutionHeightScale));
				fontbold.getData().setScale(0.76f * resolutionHeightScale);
				
				layout.setText(fontbold, "[WHITE]CHARACTER");
				layout_x = tabRect.x + (tabRect.width * 0.18f) - (layout.width / 2f);
				layout_y = (screenheight - (tabRect.height / 2f)) + (layout.height / 2f);
				fontbold.draw(batch, layout, layout_x, layout_y);
				characterIcon.setPosition(layout_x - characterIcon.getWidth() - (8f * resolutionHeightScale), screenheight - (tabRect.height / 2f) - (characterIcon.getHeight() / 2f));
				
				layout.setText(fontbold, "[WHITE]SKILL");
				layout_x = tabRect.x + (tabRect.width * 0.41f) - (layout.width / 2f);
				layout_y = (screenheight - (tabRect.height / 2f)) + (layout.height / 2f);
				fontbold.draw(batch, layout, layout_x, layout_y);
				skillIcon.setPosition(layout_x - skillIcon.getWidth() - (2f * resolutionHeightScale), screenheight - (tabRect.height / 2f) - (skillIcon.getHeight() / 2f));
				
				layout.setText(fontbold, "[WHITE]PARTY&QUEST");
				layout_x = tabRect.x + (tabRect.width * 0.654f) - (layout.width / 2f);
				layout_y = (screenheight - (tabRect.height / 2f)) + (layout.height / 2f);
				fontbold.draw(batch, layout, layout_x, layout_y);
				partyIcon.setPosition(layout_x - partyIcon.getWidth() - (8f * resolutionHeightScale), screenheight - (tabRect.height / 2f) - (partyIcon.getHeight() / 2f));
				
				layout.setText(fontbold, "[WHITE]OPTION");
				layout_x = tabRect.x + (tabRect.width * 0.895f) - (layout.width / 2f);
				layout_y = (screenheight - (tabRect.height / 2f)) + (layout.height / 2f);
				fontbold.draw(batch, layout, layout_x, layout_y);
				optionIcon.setPosition(layout_x - optionIcon.getWidth() - (4f * resolutionHeightScale), screenheight - (tabRect.height / 2f) - (optionIcon.getHeight() / 2f));
			batch.setShader(null);
			
			characterIcon.draw(batch);
			skillIcon.draw(batch);
			partyIcon.draw(batch);
			optionIcon.draw(batch);
			upArrow.draw(batch);
			downArrow.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	public void setTabSet(TabSet set) {
		currentSet = set;
	}
	
	public TabSet getTabSet() {
		return currentSet;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Tab> T getMenuTab(Class<T> tabClass) {
		T o = (T)tabLists.get(tabClass);
		return o;
	}
	
	public void changeToGameScene() {
		fadeInFlag = true;
	}
	
	public void setCursorEnable(boolean flag) {
		isCursorDisable = flag;
	}
	
	public void checkCursorHit() {
		if(currentSet.equals(TabSet.MENU)) {
			switch(currentMenuTab) {
				case 0:
					// TODO check hit object charactertab
					break;
				case 1:
					// TODO check hit object skilltab
					break;
				case 2:
					// TODO check hit object party&quest
					break;
				case 3:
					// TODO check hit object option
					break;
			}
		}
	}
	
	public void checkCursorOver() {
		if(currentSet.equals(TabSet.MENU)) {
			switch(currentMenuTab) {
				case 0:
					// TODO check over object charactertab
					break;
				case 1:
					// TODO check over object skilltab
					break;
				case 2:
					// TODO check over object party&quest
					break;
				case 3:
					// TODO check over object option
					break;
			}
		}
	}
	
	/**
	 * @param speed 0.0f - 1.0f
	 */
	public void moveCursorX(float speed) {
		cursorMoveX = speed;
	}
	
	/**
	 * @param speed 0.0f - 1.0f
	 */
	public void moveCursorY(float speed) {
		cursorMoveY = speed;
	}
	
	public void nextTab() {
		if(currentSet.equals(TabSet.MENU)) {
			if(currentMenuTab < 3f) {
				currentMenuTab++;
			}
		}
	}
	
	public void prevTab() {
		if(currentSet.equals(TabSet.MENU)) {
			if(currentMenuTab > 0f) {
				currentMenuTab--;
			}
		}
	}
	
	public void setActionState(boolean flag) {
		
	}
	
	public void setCancelState(boolean flag) {
		
	}
	
	public void setUseState(boolean flag) {
		
	}

}
