package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
 * Created by Teemu on 19.4.2018.
 */

public class LevelThree extends ApplicationAdapter implements Screen {

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    PaintBall host;
    SpriteBatch batch;
    OrthographicCamera camera;
    PlayerLevelThree player;

    Rectangle rectangle;

    boolean redColorChanged;
    boolean blueColorChanged;
    boolean purpleColorChanged;
    boolean secondRedColorChanged;
    boolean whiteColorChanged;
    boolean cyanColorChanged;
    boolean blackColorChanged;
    boolean secondWhiteChanged;
    boolean secondBlueChanged;
    boolean redColor;
    boolean blueColor;
    boolean bluePicked;
    String puddleCol;



    public LevelThree(final PaintBall host) {

        batch = host.getBatch();
        this.host = host;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400f, 200f);
        tiledMap = new TmxMapLoader().load("Map_Three.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        player = new PlayerLevelThree(32 * 22, 32 * 8, tiledMap);

        blueColorChanged = false;
        redColorChanged = false;
        purpleColorChanged = false;
        whiteColorChanged = false;
        cyanColorChanged = false;
        blackColorChanged = false;
        secondWhiteChanged = false;
        secondBlueChanged = false;

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

        redColorChanged = checkPaintCollision(redColorChanged, "red_paint_one_object");
        blueColorChanged = checkPaintCollision(blueColorChanged, "blue_paint_one_object");
        secondRedColorChanged = checkPaintCollision(secondRedColorChanged, "red_paint_two_object");
        whiteColorChanged = checkPaintCollision(whiteColorChanged, "white_paint_one_object");
        blackColorChanged = checkPaintCollision(blackColorChanged, "black_paint_one_object");
        secondWhiteChanged = checkPaintCollision(secondWhiteChanged, "white_paint_two_object");
        secondBlueChanged = checkPaintCollision(secondBlueChanged, "blue_paint_two_object");


        setColorOfPlayer(redColorChanged, blueColorChanged, secondRedColorChanged, whiteColorChanged, blackColorChanged, secondWhiteChanged, secondBlueChanged);


        if(checkResetCollision()) {
            changeColor("null");
            player.setupTextureRegion();
            player.setRed(false);
            player.setBlue(false);
            player.setSecondBlueColor(false);
            player.setSecondRedColor(false);
            player.setWhite(false);
            player.setSecondWhite(false);
            player.setBlack(false);


            redColorChanged = false;
            blueColorChanged = false;
            secondBlueChanged = false;
            secondRedColorChanged = false;
            whiteColorChanged = false;
            secondWhiteChanged = false;
            blackColorChanged = false;

        }

        if(checkGoalCollision()) {
            MapFinished mapFinished = new MapFinished(host);
            host.setScreen(mapFinished);
        }

        batch.end();

        player.render(batch);
    }

    private boolean checkGoalCollision() {

        // Gets red gate rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("finish_object");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            com.badlogic.gdx.math.Rectangle rectangle = rectangleObject.getRectangle();

            if(player.playerRectangle.overlaps(rectangle)) {
                return true;
            }
        }

        return false;
    }

    public void setPuddleCol(String puddleCol) {

        this.puddleCol = puddleCol;
    }

    public String getPuddleCol() {

        return puddleCol;
    }

    private void setColorOfPlayer(boolean red, boolean blue, boolean secondRed, boolean white, boolean black, boolean secondWhite, boolean secondBlue) {
        if(red && !blue && !white && !black) {
            setPuddleCol("red");
            clearGate("red_gate_one");
            player.setRed(true);
        }
        if(blue && !red && !white && !black) {
            setPuddleCol("blue");
            clearGate("blue_gate");
            player.setBlue(true);
        }
        if(secondBlue && !white && !red && !black) {
            setPuddleCol("blue");
            player.setSecondBlueColor(true);
        }

        if(white) {
            setPuddleCol("white");
            player.setWhite(true);
        }

        if(blue && white) {
            setPuddleCol("cyan");
            clearGate("lightblue_gate_one");
            player.setCyan(true);
        }
        if(red && blue && !white && !black) {
            setPuddleCol("purple");
            clearGate("purple_gate");
            clearGate("blue_gate");
        }
        if(secondRed && !blue) {
            setPuddleCol("secondRed");
            clearGate("red_gate_second");
            player.setSecondRedColor(true);
        }

        if(black && !blue && !red && !white) {
            setPuddleCol("black");
            player.setBlack(true);
        }
        if(black && red && !white && !blue) {
            setPuddleCol("brown");
            clearGate("brown_gate");
            player.setBrown(true);
        }
        if(secondWhite && !blue && !red) {
            setPuddleCol("white");
            player.setSecondWhite(true);
            clearGate("white_gate_one");
        }
        if(secondWhite && red && !blue) {
            setPuddleCol("pink");
            player.setPink(true);
            clearGate("pink_gate");
        }

        if(player.isColorChanged()) {
            changeColor(getPuddleCol());
            player.setupTextureRegion();
        }
        Gdx.app.log("Colors: ", "red: " + red + "blue: " +  blue + "second Red: " + secondRed + "white: " + white + "black: " + black + "secondWhite :" + secondWhite + "secondblue" + secondBlue);
    }

    private void clearGate(String path) {

        // Gets the gates texture layer.
        TiledMapTileLayer cell = (TiledMapTileLayer)tiledMap.getLayers().get(path);

        // Sets texture to null.

        if(path.equals("red_gate_one")) {
            cell.setCell(17, 13, null);
            cell.setCell(18, 13, null);
            cell.setCell(19, 13, null);
            cell.setCell(20, 13, null);
            boolean redColorSet = true;
            player.setRed(redColorSet);
        } else if(path.equals("secondBlue")) {
            player.setSecondBlueColor(true);
        } else if(path.equals("red_gate_second")) {
            cell.setCell(39, 10, null);
            cell.setCell(36, 14, null);
            cell.setCell(37, 14, null);
            player.setSecondRedColor(true);
        } else if(path.equals("lightblue_gate_one")) {
            cell.setCell(21, 15, null);
            cell.setCell(21, 16, null);
            cell.setCell(21, 17, null);
            cell.setCell(21, 18, null);
            player.setCyan(true);
        } else if(path.equals("brown_gate")) {
            cell.setCell(36, 30, null);
            cell.setCell(37, 30, null);
        } else if(path.equals("orange_gate")) {
            cell.setCell(36, 31, null);
            cell.setCell(37, 31, null);
        } else if(path.equals("white_gate_one")) {
            cell.setCell(29, 28, null);
            cell.setCell(29, 29, null);
            cell.setCell(29, 30, null);
        } else if(path.equals("pink_gate")) {
            cell.setCell(33, 43, null);
        } else if(path.equals("blue_gate")) {
            boolean blueColorSet = true;
            player.setBlue(blueColorSet);
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

        if(color.equals("null")) {

        }
        if (color.equals("red") || color.equals("secondRed")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_red.png")));
        } else if (color.equals("blue")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_blue.png")));
        } else if (color.equals("purple")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_purple.png")));
        } else if (color.equals("green")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_green.png")));
        } else if (color.equals("white")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_white.png")));
        } else if (color.equals("cyan")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_cyan.png")));
        } else if (color.equals("brown")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_brown.png")));
        } else if (color.equals("black")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_black.png")));
        } else if (color.equals("yellow")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_yellow.png")));
        } else if (color.equals("orange")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_orange.png")));
        } else if(color.equals("pink")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_pink.png")));
        } else if (color.equals("null")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball.png")));
            redColorChanged = false;
            blueColorChanged = false;
            secondRedColorChanged = false;
            whiteColorChanged = false;
            blackColorChanged = false;
            secondBlueChanged = false;
        }
    }

    public boolean checkResetCollision() {

        // Gets worlds wall rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("reset_object");

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

    }
}
