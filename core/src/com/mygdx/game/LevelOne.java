package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Teemu on 23.3.2018.
 */

public class LevelOne extends ApplicationAdapter implements Screen {

    PaintBall host;
    SpriteBatch batch;
    Rectangle rectangle;
    OrthographicCamera camera;
    Color puddleCol;
    TextureData texData;
    Pixmap map;
    Player player;
    boolean blueColorChanged;
    boolean redColorChanged;
    boolean purpleColorChanged;
    boolean upLeftCollision;
    boolean downLeftCollision;
    boolean upRightCollision;
    boolean downRightCollision;
    boolean mapFinished;
    int timer = 100;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    CollisionDetection collisionDetection;
    Rectangle playerRectangle;

    public LevelOne(final PaintBall host) {

        tiledMap = new TmxMapLoader().load("paintball_map_new.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        player = new Player(32 * 4,32 * 14, tiledMapRenderer, tiledMap);
        collisionDetection = new CollisionDetection(tiledMapRenderer, tiledMap);
        batch = host.getBatch();
        this.host = host;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400f, 200f);
        puddleCol = new Color(0.2f, 0.5f, 0.3f, 1.0f);
        player.setOriginCenter();
        //texData.prepare();
        blueColorChanged = false;
        redColorChanged = false;
        purpleColorChanged = false;
        mapFinished = false;
        playerRectangle = player.playerRectangle;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        playerRectangle = player.playerRectangle;
        player.rotate(180f);
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.begin();

        // Check for the reset point collision. Returns true if player hits reset point.
        if (checkResetPointCollision()) {
            // reset color
        }

        redColorChanged = checkPaintCollision(redColorChanged, "red_puddle_object");
        setColorOfPlayer(redColorChanged,new Color(5f,0f,0f,1f), "red_gate");
        blueColorChanged = checkPaintCollision(blueColorChanged, "blue_puddle_object");
        setColorOfPlayer(blueColorChanged,new Color(5f,0f,0f,1f), "blue_gate");
        setColorOfPlayer(blueColorChanged, redColorChanged,new Color(5f,0f,5f,1f), "purple_gate");


        checkWallCollision();
        //checkPaintCollision(redColorChanged, "red_puddle_object");

        batch.end();

        player.render(batch);

        camera.position.x = player.getX(player.playerXpos);
        camera.position.y = player.getY(player.playerYpos);
        camera.update();

        timer--;

        mapFinished = collisionDetection.checkGoalCollision(playerRectangle);
        if(mapFinished) {
            MapFinished mapFinished = new MapFinished(host);
            host.setScreen(mapFinished);
        }

    }

    public void setPuddleCol(Color puddleCol) {
        this.puddleCol = puddleCol;
    }

    public Color getPuddleCol() {
        return puddleCol;
    }

    private void setColorOfPlayer(boolean colorBoolean1, Color color, String path) {
        if(colorBoolean1) {
            setPuddleCol(color);
            clearGate(path);
        }
        if(player.isColorChanged()) {
            changeColor(player.getOriginalTexture(), getPuddleCol());
            player.setupTextureRegion();
            //player.setColorChanged(false);
        }
    }

    private void setColorOfPlayer(boolean colorBoolean1, boolean colorBoolean2, Color color, String path) {
        if(colorBoolean1 && colorBoolean2) {
            setPuddleCol(color);
            clearGate(path);
        }
        if(player.isColorChanged()) {
            changeColor(player.getOriginalTexture(), getPuddleCol());
            player.setupTextureRegion();
            //player.setColorChanged(false);
        }
    }

    private void clearGate(String path) {

        // Gets the gates texture layer.
        TiledMapTileLayer cell = (TiledMapTileLayer)tiledMap.getLayers().get(path);

        // Sets texture to null.
        if(path.equals("purple_gate")) {
            cell.setCell(7, 2, null);
            cell.setCell(7, 3, null);

        } else if(path.equals("red_gate")) {
            cell.setCell(2, 9, null);
            cell.setCell(3, 9, null);
            boolean redColorSet = true;
            player.setRed(redColorSet);

        } else if(path.equals("blue_gate")) {
            cell.setCell(8, 2, null);
            cell.setCell(8, 3, null);
            boolean blueColorSet = true;
            player.setBlue(blueColorSet);
        }
    }


    int colorTimer = 0;
    public boolean checkPaintCollision(boolean color, String path) {
        colorTimer--;
        if(player.isColorChanged() == false && color == true) {
            return color;
        }
        // Gets paint puddles rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get(path);

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            rectangle = rectangleObject.getRectangle();

            if(player.playerRectangle.overlaps(rectangle)) {
                color = true;
                if(player.isColorChanged() == false && colorTimer <= 0) {
                    colorTimer = 500;
                    player.setColorChanged(true);
                } else
                    player.setColorChanged(false);
            }
        }

        return color;
    }

    public boolean checkResetPointCollision() {

        // Gets reset point rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("reset_point_object");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();

            if(player.playerRectangle.overlaps(rectangle)) {
                Gdx.app.log("RESET", "RESET");
                return true;
            }
        }

        return false;
    }

    public void checkWallCollision() {

        // Gets worlds wall rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("map_walls_object");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();

            if(player.playerRectangle.overlaps(rectangle)) {
                Gdx.app.log("TAG", "WALL HIT");
            }
        }
    }

    /**
     * This method is causing the lag from the color swap.
     */
    Color tempColor = new Color (1,1,1,1);
    public void changeColor(Texture texture, Color color2) {

        if(color2 != tempColor) {
            if (!texture.getTextureData().isPrepared()) {
                texture.getTextureData().prepare();
            }
            map = player.getTexture().getTextureData().consumePixmap();
            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    Color color = new Color(map.getPixel(x, y));
                    if (color != null) {
                        if (color2.r + color.r <= 1) {
                            color.r = (color2.r + 0.2f) + (color.r - 0.2f);
                        } else color.r = 1;

                        if (color2.b + color.b <= 1) {
                            color.b = (color2.b + 0.2f) + (color.b - 0.2f);
                        } else color.b = 1;

                        color.g = color2.g + color.g;
                    }

                    map.setColor(color);
                    map.fillRectangle(x, y, 1, 1);
                    tempColor = color2;
                }
            }
        }

        tempColor = color2;
        player.setTexture(new Texture(map));
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        player.dispose();
        map.dispose();
        batch.dispose();
    }
}
