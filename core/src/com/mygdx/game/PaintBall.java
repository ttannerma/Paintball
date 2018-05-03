package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Set;

import sun.applet.Main;


public class PaintBall extends Game{

	String language = "";

	public SpriteBatch batch;
	OrthographicCamera camera;
	MainMenuScreen MainMenuScreen;
	SettingsScreen settingsScreen;
	Settings settings;

	float moveRight;
	float moveLeft;
	float moveUp;
	float moveDown;
	float zeroPointX;
	float zeroPointY;
	float zeroPointZ;
	float timerSides;
	float timerUp;
	float timerDown;
	boolean horizontalAxis;
	boolean soundEffectsOn;
	float hysteresisRight;
	float hysteresisLeft;
	float hysteresisUp;
	float hysteresisDown;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();
		MainMenuScreen = new MainMenuScreen(this, true);
		settingsScreen = new SettingsScreen(this);
		settings = Settings.getInstance();
		setScreen(MainMenuScreen);
	}

	@Override
	public void render () {
		super.render();
	}

	public void updateSettings() {
		settings = Settings.getInstance();
		soundEffectsOn = settings.getBoolean("soundEffects", GameData.DEFAULT_SOUND_EFFECTS);
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
		horizontalAxis = settings.getBoolean("horizontalAxis", GameData.DEFAULT_HORIZONTAL_AXIS);
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	@Override
	public void dispose () {

		batch.dispose();
	}
}