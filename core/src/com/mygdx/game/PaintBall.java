package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Set;

import sun.applet.Main;


public class PaintBall extends Game{


	public SpriteBatch batch;
	OrthographicCamera camera;
	MainMenuScreen MainMenuScreen;
	SettingsScreen settingsScreen;
	SplashScreen splashScreen;
	Settings settings;

	float moveRight;
	float moveLeft;
	float moveUp;
	float moveDown;
	float zeroPointX;
	float zeroPointY;
	float zeroPointZ;
	boolean language;
	String mainMenu;


	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();
		MainMenuScreen = new MainMenuScreen(this);
		settingsScreen = new SettingsScreen(this, 0.5f);
		splashScreen = new SplashScreen(this);
		settings = Settings.getInstance();
		setScreen(splashScreen);
	}

	@Override
	public void render () {
		super.render();
	}

	public void updateSettings() {
		settings = Settings.getInstance();
		language = settings.getBoolean("language", GameData.DEFAULT_LANGUAGE);

		Gdx.app.log("PaintBall", "update settings");
	}

	public void updateControls() {
		settings = Settings.getInstance();
		moveRight = settings.getFloat("sensitivityRight", GameData.DEFAULT_SENSITIVITY_RIGHT);
		moveLeft = settings.getFloat("sensitivityLeft", GameData.DEFAULT_SENSITIVITY_LEFT);
		moveUp = settings.getFloat("sensitivityUp", GameData.DEFAULT_SENSITIVITY_UP);
		moveDown = settings.getFloat("sensitivityDown", GameData.DEFAULT_SENSITIVITY_DOWN);
		zeroPointX = settings.getFloat("zeroPointX", GameData.DEFAULT_ZERO_POINT_X);
		zeroPointY = settings.getFloat("zeroPointY", GameData.DEFAULT_ZERO_POINT_Y);
		zeroPointZ = settings.getFloat("zeroPointZ", GameData.DEFAULT_ZERO_POINT_Z);
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	@Override
	public void dispose () {

		batch.dispose();
	}
}