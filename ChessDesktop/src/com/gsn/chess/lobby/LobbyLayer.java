package com.gsn.chess.lobby;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.gsn.chess.asset.ChessTexture;
import com.gsn.chess.asset.DataProvider;
import com.gsn.chess.game.MyChess;
import com.gsn.chess.packet.PacketFactory;
import com.gsn.chess.play.BubbleChat;
import com.gsn.engine.ActorUtility;
import com.gsn.engine.IDowloader.IImageFactoryListener;
import com.gsn.engine.layout.GsnTableLayout;
import com.gsn.engine.myplay.GsnLayer;
import com.gsn.engine.template.GsnEnableButton;

public class LobbyLayer extends GsnLayer implements ClickListener, IImageFactoryListener {
	GsnEnableButton bet1000;
	GsnEnableButton bet5000;
	GsnEnableButton quickPlayBtn;
	int select = 1000;
	
	ImageButton settingBtn;
	
	Image greyBG;
	Image noticeImg;
	
	String tag = "Lobby Layer";

	File avatarFile = null;
	
	LobbyScreen parent;
	
	public LobbyLayer(LobbyScreen parent, float width, float height) {
		super(width, height);
		this.parent = parent;
		init();
	}
	
	public void init(){
		clear();
		Image bg = new Image(ChessTexture.background);
		bg.width = width;
		bg.height = height;
		addActor(bg);

		bet1000 = new GsnEnableButton(1000, "quick play", ChessTexture.bet1000_1, ChessTexture.bet1000_2, ChessTexture.bet1000_3, ChessTexture.bet1000_4);
		bet5000 = new GsnEnableButton(5000, "quick play", ChessTexture.bet5000_1, ChessTexture.bet5000_2, ChessTexture.bet5000_3, ChessTexture.bet5000_4);
		quickPlayBtn = new GsnEnableButton(1, "", ChessTexture.quickPlayBtn, ChessTexture.quickPlayBtnDown, ChessTexture.quickPlayBtnDisable);
		bet1000.setAndSaveClickListener(this);
		bet1000.setChecked(true);
		bet5000.setAndSaveClickListener(this);
		quickPlayBtn.setAndSaveClickListener(this);
		
		float pad = 20;
		GsnTableLayout table = new GsnTableLayout(0, 0, width, bet1000.height + quickPlayBtn.height + 2 * pad);

		table.newRow(false, quickPlayBtn.height);
		table.add(1f).putCenter(quickPlayBtn);
		table.newRow(false, bet1000.height + 2 * pad);
		table.add(0.5f).putCenter(bet1000);
		table.add(0.5f).putCenter(bet5000);
		
		settingBtn = new ImageButton(ChessTexture.settingBtn, ChessTexture.settingBtnDown);
		ActorUtility.setRatio(settingBtn, 1f, 1f, width, height);
		settingBtn.setClickListener(this);

		Group group = new Group();
		group.width = table.width;
		group.height = table.height;
		group.addActor(bet1000);
		group.addActor(bet5000);
		group.addActor(quickPlayBtn);

		ActorUtility.setCenter(group, width / 2, height / 2);
		addActor(group);		
		addActor(settingBtn);
		
		
		greyBG = new Image(ChessTexture.greyBG);
		greyBG.width = width;
		greyBG.height = height;
		greyBG.setClickListener(this);
		
		noticeImg = new Image(ChessTexture.waitingNotice);
		ActorUtility.setCenter(noticeImg, width / 2, height / 2);
		
		addActor(greyBG);
		addActor(noticeImg);
	}

	
	public void setUserInfo(){
		if (DataProvider.myInfo.gold < 1000 * 2) {
			bet1000.setEnable(false);
			quickPlayBtn.setEnable(false);
		}

		if (DataProvider.myInfo.gold < 5000 * 2)
			bet5000.setEnable(false);
		greyBG.remove();
		noticeImg.remove();
		
		BubbleChat chat = new BubbleChat(ChessTexture.chatMe, 80, ChessTexture.fontMedium, "kakaksf sdfas sdf asf asfa sdfas df sadf asfasd fasf asdfdsafasd fas asdfsdf");
		addActor(chat);
	}	
	
	@Override
	public void click(Actor actor, float x, float y) {
		if (actor instanceof GsnEnableButton) {
			GsnEnableButton button = (GsnEnableButton) actor;
			if (button == bet1000) {
				select = 1000;
				bet1000.setChecked(true);
				bet5000.setChecked(false);
			} else if (button == bet5000) {
				select = 5000;
				bet1000.setChecked(false);
				bet5000.setChecked(true);
			} else if (button == quickPlayBtn) {
				MyChess.game.setPlayScreen();
				MyChess.client.send(PacketFactory.createQuickPlay(select));
			} 
		}
		if (actor == settingBtn){
			Gdx.app.log(tag, "click setting btn");
			parent.showSettingLayer();
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode){
		case Keys.F1:
			//set avatar
						
			break;
		case Keys.F2:
			//set info
			
			break;
		}
		return super.keyDown(keycode);
	}

	@Override
	public void onError(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinishLoading(String id, File outFile) {
		avatarFile = outFile;		
	}
	
	@Override
	public void act(float delta) {
		if (avatarFile != null){
			Gdx.app.log(tag, avatarFile.getAbsolutePath());
			Texture texture = new Texture(new FileHandle(avatarFile));
			Image avatar = new Image(texture);
			addActor(avatar);
			avatarFile = null;
		}
		super.act(delta);
	}
}
