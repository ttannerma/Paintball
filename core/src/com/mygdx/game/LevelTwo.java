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
 * Created by Teemu on 3.4.2018.
 */

public class LevelTwo extends ApplicationAdapter implements Screen {

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    PaintBall host;
    SpriteBatch batch;
    OrthographicCamera camera;
    PlayerLevelTwo player;
    Rectangle rectangle;

    boolean redColorChanged;
    boolean blueColorChanged;
    boolean purpleColorChanged;
    boolean secondRedColorChanged;
    boolean redColor;
    boolean blueColor;
    String puddleCol;

    public LevelTwo(final PaintBall host) {

        batch = host.getBatch();
        this.host = host;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400f, 200f);
        tiledMap = new TmxMapLoader().load("SecondLevel.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        player = new PlayerLevelTwo(32 * 21, 32 * 8, tiledMap);

        blueColorChanged = false;
        redColorChanged = false;
        purpleColorChanged = false;
        redColor = false;
        blueColor = false;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.begin();


        camera.position.x = player.getX(player.playerXpos);
        camera.position.y = player.getY(player.playerYpos);
        camera.update();

        redColorChanged = checkPaintCollision(redColorChanged, "red_puddle_object");
        blueColorChanged = checkPaintCollision(blueColorChanged, "blue_puddle_object");
        secondRedColorChanged = checkPaintCollision(secondRedColorChanged, "second_red_puddle_obj");

        setColorOfPlayer(redColorChanged, blueColorChanged, secondRedColorChanged);

        if(checkResetCollision()) {
            changeColor("null");
            player.setupTextureRegion();
            player.setRed(false);
            player.setBlue(false);
            player.setSecondRedColor(false);
        }

        batch.end();

        player.render(batch);



    }
    public void setPuddleCol(String puddleCol) {

        this.puddleCol = puddleCol;
    }

    public String getPuddleCol() {

        return puddleCol;
    }

    private void setColorOfPlayer(boolean red, boolean blue, boolean secondRed) {
        if(red && !blue) {
            setPuddleCol("red");
            clearGate("red_gate");
            player.setRed(true);
        }
        if(blue && !red) {
            setPuddleCol("blue");
            clearGate("blue_gate");
            player.setRed(true);
        }
        if(red && blue) {
            setPuddleCol("purple");
            clearGate("purple_gate");
            clearGate("blue_gate");
        }
        if(secondRed) {
            setPuddleCol("secondRed");
            clearGate("red_gate_second");
        }
        if(player.isColorChanged()) {
            changeColor(getPuddleCol());
            player.setupTextureRegion();
        }
    }

    private void clearGate(String path) {

        // Gets the gates texture layer.
        TiledMapTileLayer cell = (TiledMapTileLayer)tiledMap.getLayers().get(path);

        // Sets texture to null.
        if(path.equals("purple_gate")) {
            cell.setCell(23, 7, null);
            cell.setCell(23, 8, null);
            cell.setCell(23, 9, null);

        } else if(path.equals("red_gate")) {
            cell.setCell(16, 7, null);
            cell.setCell(16, 8, null);
            cell.setCell(16, 9, null);
            boolean redColorSet = true;
            player.setRed(redColorSet);

        } else if(path.equals("blue_gate")) {
            cell.setCell(8, 2, null);
            cell.setCell(8, 3, null);
            boolean blueColorSet = true;
            player.setBlue(blueColorSet);
        } else if(path.equals("red_gate_second")) {
            cell.setCell(39, 7, null);
            cell.setCell(39, 8, null);
            cell.setCell(36, 10, null);
            cell.setCell(37, 10, null);
            player.setSecondRedColor(true);
        }
    }

    public boolean checkPaintCollision(boolean color, String path) {
        if(player.isColorChanged() == false && color == true) {
            return color;
        }
        // Gets paint puddles rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get(path);

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        // Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            rectangle = rectangleObject.getRectangle();

            if(player.playerRectangle.overlaps(rectangle)) {
                color = true;
                if(player.isColorChanged() == false ) {
                    player.setColorChanged(true);
                } else
                    player.setColorChanged(false);
            }
        }

        return color;
    }

    public void changeColor(String color) {

        if (color.equals("red") || color.equals("secondRed")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_red.png")));
        } else if (color.equals("blue")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_blue.png")));
        } else if (color.equals("purple")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_purple.png")));
        } else if (color.equals("green")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_green.png")));
        } else if (color.equals("null")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball.png")));
            redColorChanged = false;
            blueColorChanged = false;
        }
    }

    public boolean checkResetCollision() {

        // Gets worlds wall rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("reset_point_object");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();

            if(player.playerRectangle.overlaps(rectangle)) {
                return true;
            }
        }

        return false;
    }

    public void setRed(boolean redColored) {
        redColor = redColored;
    }
    public void setBlue(boolean blueColored) {
        blueColor = blueColored;
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

        batch.dispose();
    }
}

