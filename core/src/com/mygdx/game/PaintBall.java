package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class PaintBall extends ApplicationAdapter {

	//Stuff commented out used for the color change effect.

	SpriteBatch batch;
	//Texture img;
	OrthographicCamera camera;
	Color randomColor;
	//TextureData texData;
	//Pixmap map;
	Player player;
	int timer = 100;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800f, 400f);
		randomColor = new Color(0.2f, 0.5f, 0.3f, 1.0f);
		player = new Player(4,4);
		player.setOriginCenter();
		//texData = img.getTextureData();
		//texData.prepare();
	}

	public void print() {
		Gdx.app.log("TAG", "working");
		//testing
	}

	@Override
	public void render () {
		player.rotate(180f);
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		/*
		if(timer <= 0) {
			timer = 10;

			map = img.getTextureData().consumePixmap();

			for(int y = 0; y < map.getHeight(); y++) {
				for(int x = 0; x < map.getWidth(); x++) {

					map.setColor(randomColor);
					map.fillRectangle(x, y, 1,1);
				}
			}
			img = new Texture(map);
		}
		*/
		testmethod();
		batch.end();
		player.render(batch);
		camera.position.set(player.getX(), player.getY(), 0);
		timer--;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
		//texData.disposePixmap();
		//map.dispose();
		player.dispose();
	}

	public void testmethod() {

	    Gdx.app.log("TAG", "Testings");
    }
}
