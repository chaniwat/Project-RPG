package com.skyhouse.projectrpg.scene.menu.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.text.WordUtils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.skyhouse.projectrpg.ProjectRPG;
import com.skyhouse.projectrpg.data.CharacterData;
import com.skyhouse.projectrpg.data.ActorData.ActionState;
import com.skyhouse.projectrpg.entity.Character;
import com.skyhouse.projectrpg.graphics.ItemSlot;
import com.skyhouse.projectrpg.scene.MenuScene.TabSet;
import com.skyhouse.projectrpg.spriter.SpriterPlayer;
import com.skyhouse.projectrpg.utils.JobType;

public class CharacterTab extends Tab {
	
	public static enum EquipSlot {
		WEAPON,
		OFFHAND,
		HELMET,
		ARMOR,
		PANT,
		GLOVE,
		BOOT,
		EARRING,
		NECKLACE,
		RING,
		POTION;
		
		public static EquipSlot getValue(int index) {
			switch (index) {
				case 0:
					return WEAPON;
				case 1:
					return OFFHAND;
				case 2:
					return HELMET;
				case 3:
					return ARMOR;
				case 4:
					return PANT;
				case 5:
					return GLOVE;
				case 6:
					return BOOT;
				case 7:
					return EARRING;
				case 8:
					return NECKLACE;
				case 9:
					return RING;
				case 10:
					return POTION;
			}
			return null;
		}
		
		public static int getValue(EquipSlot slot) {
			switch(slot) {
				case WEAPON:
					return 0;
				case OFFHAND:
					return 1;
				case HELMET:
					return 2;
				case ARMOR:
					return 3;
				case PANT:
					return 4;
				case GLOVE:
					return 5;
				case BOOT:
					return 6;
				case EARRING:
					return 7;
				case NECKLACE:
					return 8;
				case RING:
					return 9;
				case POTION:
					return 10;
			}
			return -1;
		}
	}
	
	private AssetManager assetmanager = ProjectRPG.client.assetmanager;
	private BitmapFont font = ProjectRPG.client.graphic.font.spritefont;
	private BitmapFont fontbold = ProjectRPG.client.graphic.font.spritefontbold;
	private GlyphLayout layout = ProjectRPG.client.graphic.font.layout;
	
	private Character playercharacter;
	
	private HashMap<EquipSlot, ItemSlot> equipSlots;
	private ArrayList<ItemSlot> itemSlots;
	private Sprite nextPageArrow, prevPageArrow;
	
	private int maxPage = 2;
	private int currentPage = 0;
	
	private int damage = 0,
						 defense = 0,
						 level = 1,
						 job = 1;
	
	public CharacterTab() {
		super(TabSet.MENU);
		
		equipSlots = new HashMap<CharacterTab.EquipSlot, ItemSlot>();
		
		for(EquipSlot slot : EquipSlot.values()) {
			equipSlots.put(slot, new ItemSlot());
		}
		
		itemSlots = new ArrayList<ItemSlot>();
		for(int i = 0; i < 30 * maxPage; i++) {
			itemSlots.add(new ItemSlot());
		}
		
		nextPageArrow = new Sprite(assetmanager.get("texture/ui/menuscene/charactertab/arrow.png", Texture.class));
		prevPageArrow = new Sprite(assetmanager.get("texture/ui/menuscene/charactertab/arrow_disable.png", Texture.class));
	}
	
	@Override
	public void update(float deltatime, float resolutionHeightScale) {
		playercharacter.setPosition((width / 2f) + x, ((height / 2f) + (-2f * resolutionHeightScale)) + y);
		playercharacter.setScale(0.72f * resolutionHeightScale);
		playercharacter.update(deltatime);
		
		for(Entry<EquipSlot, ItemSlot> entry : equipSlots.entrySet()) {
			entry.getValue().updateSize(resolutionHeightScale);
			float translateX = 0f, translateY = 0f;
			switch(entry.getKey()) {
				case WEAPON:
					translateX = -145f;
					translateY = 125f;
					break;
				case OFFHAND:
					translateX = -230f;
					translateY = 125f;
					break;
				case ARMOR:
					translateX = 90f;
					translateY = 45f;
					break;
				case BOOT:
					translateX = 175f;
					translateY = -35f;
					break;
				case EARRING:
					translateX = -145f;
					translateY = 45f;
					break;
				case GLOVE:
					translateX = 175f;
					translateY = 45f;
					break;
				case HELMET:
					translateX = 90f;
					translateY = 125f;
					break;
				case NECKLACE:
					translateX = -230f;
					translateY = 45f;
					break;
				case PANT:
					translateX = 90f;
					translateY = -35f;
					break;
				case RING:
					translateX = -145f;
					translateY = -35f;
					break;
				case POTION:
					translateX = -230f;
					translateY = -35f;
					break;
			}
			entry.getValue().setPosition((width / 2f) + translateX * resolutionHeightScale + x, (height / 2f) + translateY * resolutionHeightScale + y);
		}
		
		nextPageArrow.setSize((nextPageArrow.getTexture().getWidth() * 0.6354f) * resolutionHeightScale, (nextPageArrow.getTexture().getHeight() * 0.6354f) * resolutionHeightScale);
		nextPageArrow.setPosition((width / 2f) + 410f * resolutionHeightScale + x, (height / 2f) - 263f * resolutionHeightScale + y);
		
		prevPageArrow.setSize((prevPageArrow.getTexture().getWidth() * 0.6354f) * resolutionHeightScale, (prevPageArrow.getTexture().getHeight() * 0.6354f) * resolutionHeightScale);
		prevPageArrow.setOrigin(prevPageArrow.getWidth() / 2f, prevPageArrow.getHeight() / 2f);
		prevPageArrow.setRotation(180f);
		prevPageArrow.setPosition((width / 2f) - 538f * resolutionHeightScale + x, (height / 2f) - 263f * resolutionHeightScale + y);

		for(ItemSlot slot : itemSlots) {
			slot.updateSize(resolutionHeightScale);
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float resolutionHeightScale) {
		playercharacter.draw(batch);
		
		for(ItemSlot slot : equipSlots.values()) {
			slot.draw(batch);
		}
		
		batch.setShader(ProjectRPG.client.graphic.font.shader);
		
			fontbold.getData().setScale(1f * resolutionHeightScale);
			layout.setText(fontbold, "[BLACK]Tester001");
			fontbold.draw(batch, layout, (width / 2f) - (layout.width / 2f) + x, height - (110f * resolutionHeightScale) + y);
			
			font.getData().setScale(0.5f * resolutionHeightScale);
			for(Entry<EquipSlot, ItemSlot> slot : equipSlots.entrySet()) {
				layout.setText(font, "[BLACK]"+WordUtils.capitalize(slot.getKey().toString().toLowerCase()));
				font.draw(batch, layout, slot.getValue().getX() + (slot.getValue().getWidth() / 2f) - (layout.width / 2f), slot.getValue().getY() + slot.getValue().getHeight() + (16.8f * resolutionHeightScale));
			}
			
			fontbold.getData().setScale(0.72f * resolutionHeightScale);
			layout.setText(fontbold, "[BLACK]ค่าพลังโจมตี");
			fontbold.draw(batch, layout, (width / 2f) - (layout.width / 2f) + (-364f * resolutionHeightScale) + x, (height / 2f) + (210f * resolutionHeightScale) + y);
			layout.setText(fontbold, "[BLACK]ค่าพลังป้องกัน");
			fontbold.draw(batch, layout, (width / 2f) - (layout.width / 2f) + (-369f * resolutionHeightScale) + x, (height / 2f) + (82f * resolutionHeightScale) + y);
			layout.setText(fontbold, "[BLACK]เลเวล");
			fontbold.draw(batch, layout, (width / 2f) - (layout.width / 2f) + (364f * resolutionHeightScale) + x, (height / 2f) + (210f * resolutionHeightScale) + y);
			layout.setText(fontbold, "[BLACK]อาชีพ");
			fontbold.draw(batch, layout, (width / 2f) - (layout.width / 2f) + (363f * resolutionHeightScale) + x, (height / 2f) + (82f * resolutionHeightScale) + y);
			
			fontbold.getData().setScale(1.1f * resolutionHeightScale);
			layout.setText(fontbold, "[BLACK]" + damage);
			fontbold.draw(batch, layout, (width / 2f) - (layout.width / 2f) + (-370f * resolutionHeightScale) + x, (height / 2f) + (153f * resolutionHeightScale) + y);
			layout.setText(fontbold, "[BLACK]" + defense);
			fontbold.draw(batch, layout, (width / 2f) - (layout.width / 2f) + (-370f * resolutionHeightScale) + x, (height / 2f) + (27f * resolutionHeightScale) + y);
			layout.setText(fontbold, "[BLACK]" + level);
			fontbold.draw(batch, layout, (width / 2f) - (layout.width / 2f) + (364f * resolutionHeightScale) + x, (height / 2f) + (153f * resolutionHeightScale) + y);
			layout.setText(fontbold, "[BLACK]" + JobType.getValue(job).getThaiName());
			fontbold.draw(batch, layout, (width / 2f) - (layout.width / 2f) + (363f * resolutionHeightScale) + x, (height / 2f) + (27f * resolutionHeightScale) + y);
			
			fontbold.getData().setScale(0.8f * resolutionHeightScale);
			layout.setText(fontbold, "[BLACK]Inventory");
			fontbold.draw(batch, layout, (width / 2f) - (layout.width / 2f) + (-410f * resolutionHeightScale) + x, (height / 2f) - (35f * resolutionHeightScale) + y);
			
			font.getData().setScale(0.53f * resolutionHeightScale);
			layout.setText(font, "[BLACK]" + (currentPage + 1) + " / 2");
			font.draw(batch, layout, (width / 2f) - (layout.width / 2f) + x, 30f * resolutionHeightScale + y);
		batch.setShader(null);
		
		for(int i = 30 * currentPage; i < 30 + (30 * currentPage); i++) {
			ItemSlot slot = itemSlots.get(i);
			float slotwidth = (width / 2f) + (-440f + (90f * (int)(i % 10))) * resolutionHeightScale + x;
			float slotheight = (height / 2f) + (-145f - (85f * (int)(i / 10))) * resolutionHeightScale + y;
			slot.setPosition(slotwidth, slotheight);
			slot.draw(batch);
		}
		
		nextPageArrow.draw(batch);
		prevPageArrow.draw(batch);
	}
	
	/**
	 * Set character.
	 */
	public void updatePlayerCharacter(CharacterData data) {
		CharacterData equipData = new CharacterData();
		equipData.equip = data.equip;
		if(playercharacter == null) playercharacter = new Character(equipData);
		else {
			playercharacter.updateCharacterByData(equipData);
		}
	}
	
}
