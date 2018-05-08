package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    boolean secondRedColorChanged;
    boolean blueColorChanged;
    boolean secondBlueChanged;
    boolean purpleColorChanged;
    boolean whiteColorChanged;
    boolean cyanColorChanged;
    boolean blackColorChanged;
    boolean secondWhiteChanged;
    boolean redColor;
    boolean blueColor;
    boolean bluePicked;
    float width;
    float height;
    float row_height;
    float col_width;
    float musicVol;
    String puddleCol;
    Stage stage;
    Skin mySkin;
    BitmapFont logo;
    Music music;
    String buttonText;

    public LevelThree(final PaintBall host, float musicVolume, String buttonText) {

        batch = host.getBatch();
        this.host = host;
        this.buttonText = buttonText;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400f, 200f);
        tiledMap = new TmxMapLoader().load("Map_Three.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        player = new PlayerLevelThree(32 * 22, 32 * 8, tiledMap, host);

        musicVol = musicVolume;
        music = Gdx.audio.newMusic(Gdx.files.internal("gameplay.wav"));
        music.play();
        music.setVolume(musicVol);
        music.setLooping(true);


        blueColorChanged = false;
        redColorChanged = false;

        purpleColorChanged = false;
        whiteColorChanged = false;
        cyanColorChanged = false;
        blackColorChanged = false;
        secondWhiteChanged = false;
        secondBlueChanged = false;
        secondRedColorChanged = false;

        redColor = false;
        blueColor = false;

        logo = new BitmapFont(Gdx.files.internal("font.txt"));
        logo.getData().setScale(0.7f, 0.7f);

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        row_height = camera.viewportHeight / 15;
        col_width = camera.viewportWidth / 12;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        int row_height = Gdx.graphics.getWidth() / 12;
        int col_width = Gdx.graphics.getWidth() / 12;

        Button button2 = new TextButton(buttonText,mySkin,"small");
        button2.setSize(col_width * 2, row_height);
        button2.setPosition(width - (width / 4), 0);
        button2.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                MainMenuScreen mainMenuScreen = new MainMenuScreen(host);
                host.setScreen(mainMenuScreen);
                dispose();
                return true;
            }
        });
        stage.addActor(button2);
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
            secondRedColorChanged = false;
            blueColorChanged = false;
            secondBlueChanged = false;
            whiteColorChanged = false;
            secondWhiteChanged = false;
            blackColorChanged = false;

        }

        if(checkGoalCollision()) {
            dispose();
            MapFinished mapFinished = new MapFinished(host);
            host.setScreen(mapFinished);
        }

        batch.end();
        player.render(batch);
        stage.act();
        stage.draw();
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
        if(secondBlue && !secondWhite && !secondRed && !black) {
            setPuddleCol("blue");
            clearGate("blue_gate_two");
            player.setSecondBlueColor(true);
        }
        if(white) {
            setPuddleCol("white");
            player.setWhite(true);
        }
        if(secondBlue && secondWhite) {
            setPuddleCol("cyan");
            player.setCyan(true);
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
        if(secondRed && !secondWhite) {
            setPuddleCol("secondRed");
            clearGate("red_gate_two");
            player.setSecondRedColor(true);
        }
        if(black) {
            setPuddleCol("black");
            clearGate("black_gate_two");
            player.setBlack(true);
        }
        if(black && red && !white && !blue) {
            setPuddleCol("brown");
            clearGate("brown_gate");
            player.setBrown(true);
        }
        if(secondWhite && !secondBlue && !blue && !red) {
            setPuddleCol("white");
            player.setSecondWhite(true);
            clearGate("white_gate_one");
        }
        if(secondWhite && secondRed && !blue) {
            setPuddleCol("pink");
            player.setPink(true);
            clearGate("pink_gate_two");
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
        } else if(path.equals("blue_gate_two")) {
            cell.setCell(23, 25, null);
            cell.setCell(24, 25, null);
            cell.setCell(25, 25, null);
            cell.setCell(26, 25, null);
            player.setSecondBlueColor(true);
        } else if(path.equals("red_gate_two")) {
            cell.setCell(34, 20, null);
            cell.setCell(35, 20, null);
            cell.setCell(36, 20, null);
            cell.setCell(37, 20, null);
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
        } else if(path.equals("pink_gate_two")) {
            cell.setCell(20, 28, null);
            cell.setCell(20, 29, null);
            cell.setCell(20, 30, null);
        } else if(path.equals("blue_gate")) {
            boolean blueColorSet = true;
            player.setBlue(blueColorSet);
        } else if(path.equals("black_gate_two")) {
            cell.setCell(13, 28, null);
            cell.setCell(13, 29, null);
            cell.setCell(13, 30, null);
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
        music.dispose();
        stage.dispose();
        player.dispose();
    }
}
