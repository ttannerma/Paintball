package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;


public class PaintBall extends ApplicationAdapter {

	//Stuff commented out used for the color change effect.

	SpriteBatch batch;
	//Texture img;
	OrthographicCamera camera;
	Color randomColor;
	//TextureData texData;
	//Pixmap map;
	Player player;
	paintPuddles paintPuddles;
	boolean blueColor;
	boolean redColor;
	boolean purpleColor;
	boolean upLeftCollision;
	boolean downLeftCollision;
	boolean upRightCollision;
	boolean downRightCollision;
	int timer = 100;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 400f, 200f);
		randomColor = new Color(0.2f, 0.5f, 0.3f, 1.0f);
		player = new Player(32 * 3,32 * 14);
		paintPuddles = new paintPuddles();
		player.setOriginCenter();
		//texData = img.getTextureData();
		//texData.prepare();
        blueColor = false;
        redColor = false;
        purpleColor = false;
        tiledMap = new TmxMapLoader().load("paintball_map_new.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
	}

	@Override
	public void render () {
        player.rotate(180f);
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.begin();

        redColor = checkRedPaintCollision(redColor);
        blueColor = checkBluePaintCollision(blueColor);

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

		checkRedPaintCollision(redColor);

		if(redColor) {
			clearRedGate();
		}

		checkBluePaintCollision(blueColor);

		if(blueColor) {
			clearBlueGate();
		}

		if(redColor && blueColor) {
			clearPurpleGate();
		}

		batch.end();

		player.render(batch);

        camera.position.x = player.getX(player.playerXpos);
        camera.position.y = player.getY(player.playerYpos);
        camera.update();

        //paintPuddles.render(batch);
		timer--;
	}

	private void clearPurpleGate() {

		// Gets purple gate texture layer.
		TiledMapTileLayer cell = (TiledMapTileLayer)tiledMap.getLayers().get("purple_gate");

		// Sets texture to null.
		cell.setCell(7, 2, null);
		cell.setCell(7, 3, null);
	}

	private void clearRedGate() {

		// Gets red gate texture layer.
		TiledMapTileLayer cell = (TiledMapTileLayer)tiledMap.getLayers().get("red_gate");

		// Sets texture to null.
		cell.setCell(2, 9, null);
		cell.setCell(3, 9, null);
	}

	private void clearBlueGate() {

		// Gets blue gate texture layer.
		TiledMapTileLayer cell = (TiledMapTileLayer)tiledMap.getLayers().get("blue_gate");

		// Sets texture to null.
		cell.setCell(14, 2, null);
		cell.setCell(14, 3, null);
	}


	public boolean checkRedPaintCollision(boolean redColor) {

		// Gets red paint rectangle layer.
		MapLayer collisionObjectLayer = tiledMap.getLayers().get("red_puddle_object");

		// All the objects of the layer.
		MapObjects mapObjects = collisionObjectLayer.getObjects();

		//Collects all rectangles in an array.
		Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

		// Loop through all rectangles.
		for(RectangleMapObject rectangleObject : rectangleObjects) {
			com.badlogic.gdx.math.Rectangle rectangle = rectangleObject.getRectangle();

			if(player.playerRectangle.overlaps(rectangle)) {
				redColor = true;
			}
		}

		return redColor;
	}

	public boolean checkBluePaintCollision(boolean blueColor) {

		// Gets blue paint rectangle layer.
		MapLayer collisionObjectLayer = tiledMap.getLayers().get("blue_puddle_object");

		// All the objects of the layer.
		MapObjects mapObjects = collisionObjectLayer.getObjects();

		//Collects all rectangles in an array.
		Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

		// Loop through all rectangles.
		for(RectangleMapObject rectangleObject : rectangleObjects) {
			com.badlogic.gdx.math.Rectangle rectangle = rectangleObject.getRectangle();

			if(player.playerRectangle.overlaps(rectangle)) {
				blueColor = true;
			}
		}

		return blueColor;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
		//texData.disposePixmap();
		//map.dispose();
		player.dispose();
		paintPuddles.dispose();

    }
}
