package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import sun.applet.Main;


public class PaintBall extends Game{

	public SpriteBatch batch;
	OrthographicCamera camera;
	MainMenuScreen MainMenuScreen;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();
		MainMenuScreen = new MainMenuScreen(this);
		setScreen(MainMenuScreen);
	}

	@Override
	public void render () {
		super.render();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	@Override
	public void dispose () {

		batch.dispose();
	}
}