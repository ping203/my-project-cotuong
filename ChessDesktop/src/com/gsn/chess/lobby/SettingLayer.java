package com.gsn.chess.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.gsn.chess.asset.ChessSound;
import com.gsn.chess.asset.ChessTexture;
import com.gsn.chess.game.ChessSetting;
import com.gsn.chess.play.PlayScreen;
import com.gsn.engine.layout.GsnTableLayout;
import com.gsn.engine.myplay.GsnLayer;

public class SettingLayer extends GsnLayer implements ClickListener{
	Image bg;
	ImageButton soundBtn;
	ImageButton exitBtn;	
	private String tag = "Setting Layer";
	
	public SettingLayer(float width, float height) {
		super(width, height);
		bg = new Image(ChessTexture.greyBG);
		bg.width = width;
		bg.height = height;
		
		
		exitBtn = new ImageButton(ChessTexture.exitBtn, ChessTexture.exitBtnDown);
		soundBtn = new ImageButton(ChessTexture.soundBtn, ChessTexture.soundBtnDown, ChessTexture.soundBtnOff);		
		
		GsnTableLayout table = new GsnTableLayout(0, 0, width, height);
		table.newRow(true, 1f);
		table.add(0.5f).putCenter(soundBtn);
		table.add(0.5f).putCenter(exitBtn);
		
		addActor(bg);
		addActor(exitBtn);
		addActor(soundBtn);
		
		exitBtn.setClickListener(this);
		soundBtn.setClickListener(this);
		loadSetting();
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (!super.touchDown(x, y, pointer, button)){
			Gdx.app.log(tag, "click ra ngoai");
			if (parent instanceof LobbyScreen)
				((LobbyScreen)parent).hideSettingLayer();
			if (parent instanceof PlayScreen)
				((PlayScreen)parent).hideSettingLayer();
		}
		return super.touchUp(x, y, pointer, button);
	}

	@Override
	public void click(Actor actor, float x, float y) {
		Gdx.app.log(tag , "click ");		
		if (actor == exitBtn){
			if (parent instanceof LobbyScreen)
				((LobbyScreen)parent).showExitDlg();
			if (parent instanceof PlayScreen)
				((PlayScreen)parent).showQuitDialog();
		} else if (actor == soundBtn){
			ChessSetting.enableSound = !soundBtn.isChecked();
			Gdx.app.log(tag, " enable sound : " + ChessSetting.enableSound);
		}
		ChessSound.playSoundClick();
	}

	public void loadSetting() {		
		soundBtn.setChecked(!ChessSetting.enableSound);
	}
}
