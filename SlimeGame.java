package com.jnm.slime;

import static javax.swing.text.StyleConstants.Background;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sun.org.apache.bcel.internal.Const;

import Screens.PlayScreen;

public class SlimeGame extends Game {


	//rendering utils
	public SpriteBatch batch;

	//universal player
//	public Player player;
//
//	//screens
//	public MenuScreen menuScreen;
	public PlayScreen playScreen;
//	public LevelSelectScreen levelSelectScreen;
//	public SettingsScreen settingsScreen;
//
//	//main bg
//	public Background menuBackground;


	@Override
	public void create () {
		batch = new SpriteBatch();

//		player = new Player("player, rm");
//
//		menuScreen = new MenuScreen(this);
//		playScreen = new playScreen(this);
//		levelSelectScreen = new LevelSelectScreen(this);
//		settingsScreen = new SettingsScreen(this);

		this.setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public void dispose() {
//		batch.dispose();
//		super.dispose();

//		rm.dispose();
//		menuScreen.dispose();
//		playScreen.dispose();
//		levelSelectScreen.dispose();
//		settingsScreen.dispose();
	}


}
