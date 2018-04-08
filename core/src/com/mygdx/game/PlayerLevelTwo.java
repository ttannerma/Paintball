
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

/**
 * Contains implementation of the player class for level two.
 * Created by Teemu on 2/23/2018.
 */
public class PlayerLevelTwo extends Sprite {

    private Texture texture, originalTexture;
    public Rectangle playerRectangle;
    private TextureRegion[][] playerRegion;
    private TextureRegion[] rollingAnimation;
    private TextureRegion currentFrame;
    private SpriteBatch batch;
    private Animation<TextureRegion> rolling;

    private CollisionDetection colDetection;
    private boolean colorChanged = false;
    TiledMap tiledMap;

    int i = 0;
    float x;
    float y;
    float playerYpos;
    float playerXpos;
    boolean upLeftCollision;
    boolean downLeftCollision;
    boolean upRightCollision;
    boolean downRightCollision;
    boolean redColor;
    boolean blueColor;
    boolean secondRedColor;
    boolean whiteColor;
    boolean cyanColor;
    boolean blackColor;
    boolean brownColor;
    boolean yellowColor;
    boolean orangeColor;
    boolean secondWhiteColor;
    boolean pinkColor;
    float lastXVelocity = 0;
    float lastYVelocity = 0;
    String collision;

    float accelY;
    float accelZ;

    public PlayerLevelTwo (float x, float y, TiledMap tiledMap) {
        setTexture(new Texture(Gdx.files.internal("sketch_ball.png")));
        setupTextureRegion();
        setX(x);
        setY(y);
        this.tiledMap = tiledMap;
        redColor = false;
        blueColor = false;
        secondRedColor = false;
        whiteColor = false;
        cyanColor = false;
        blackColor = false;
        brownColor = false;
        orangeColor = false;
        yellowColor = false;
        secondWhiteColor = false;
        pinkColor = false;
        collision = "walls";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setupTextureRegion() {
        playerRegion = TextureRegion.split(getTexture(), getTexture().getWidth() / 1, getTexture().getHeight());
        rollingAnimation = convertTo1D(playerRegion);
        rolling = new Animation<TextureRegion>(1 / 60f, rollingAnimation);
        playerRectangle = new Rectangle(x, y, rolling.getKeyFrame(0).getRegionWidth() / 10, getTexture().getHeight() / 10);
        currentFrame = rolling.getKeyFrames()[0];
    }

    private TextureRegion[] convertTo1D(TextureRegion[][] region) {
        TextureRegion[] animation = new TextureRegion[region.length * region[0].length];
        int index = 0;
        for(int i = 0; i < region.length; i++) {
            for(int j = 0; j < region[i].length; j++) {
                animation[index] = region[i][j];
            }
        }

        return animation;
    }

    public void render(SpriteBatch batch) {

        redColor = getRed(redColor);
        blueColor = getBlue(blueColor);

        checkSecondRedPuddleCollision();

        float posThreshold = 2;
        float negThreshold = -2;
        float speed = 80 * Gdx.graphics.getDeltaTime();

        accelY = Gdx.input.getAccelerometerY();
        accelZ = Gdx.input.getAccelerometerZ();


        // y = oikea vasen
        // z = eteen taakse
        if(accelY > posThreshold) {
            getMyCorners(getX(playerXpos) + speed, getY(playerYpos), collision);
            if(upRightCollision && upLeftCollision) {
                if(!checkPurpleGateCollision() && !checkSecondRedGateCollision()) {
                    x += speed;
                } else {
                    x += (-1 * speed);
                }
            }
        }

        if(accelY < negThreshold) {
            getMyCorners(getX(playerXpos) - speed, getY(playerYpos), collision);
            if(downLeftCollision && upLeftCollision) {
                if(!checkRedGateCollision()) {
                    x += (-1 * speed);
                } else {
                    x += speed;
                }
            }
        }

        if(accelZ > posThreshold) {
            getMyCorners(getX(playerXpos), getY(playerYpos) + speed, collision);
            if(upLeftCollision && upRightCollision) {
                if(!checkSecondRedGateCollision() && !checkCyanGateCollision() && !checkBrownGateCollision() && !checkOrangeGateCollision() && !checkPinkGateCollision()) {
                    y += speed;
                }
            } else {
                y += (-1 * speed);
            }
        }

        if(accelZ < negThreshold) {
            getMyCorners(getX(playerXpos), getY(playerYpos) - speed, collision);
            if(downLeftCollision && downRightCollision && !checkSecondWhiteGateCollision()) {
                y += (-1 * speed);
            } else {
                y += speed;
            }
        }

        playerRectangle.setPosition(x, y);
        //Gdx.app.log("TAG", "x: " + Float.toString(x) + " y: " + Float.toString(y));

        currentFrame = rolling.getKeyFrames()[0];
        //animationFrame--;

        batch.begin();
        batch.draw(currentFrame, playerRectangle.x, playerRectangle.y,
                playerRectangle.getHeight() /2,
                playerRectangle.getWidth() /2,
                playerRectangle.width, playerRectangle.height, 1, 1, -x);

        batch.end();
        playerXpos = playerRectangle.x;
        playerYpos = playerRectangle.y;

    }

    private boolean checkPinkGateCollision() {

        if(pinkColor) {
            return false;
        }
        // Gets red gate rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("pink_gate_obj");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            com.badlogic.gdx.math.Rectangle rectangle = rectangleObject.getRectangle();

            if(playerRectangle.overlaps(rectangle)) {
                Gdx.app.log("PINK GATE", "HIT");
                return true;
            }
        }

        return false;
    }

    private boolean checkSecondWhiteGateCollision() {

        if(secondWhiteColor) {
            return false;
        }
        // Gets red gate rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("white_gate_obj");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            com.badlogic.gdx.math.Rectangle rectangle = rectangleObject.getRectangle();

            if(playerRectangle.overlaps(rectangle)) {
                Gdx.app.log("WHITE GATE", "HIT");
                return true;
            }
        }

        return false;
    }

    private boolean checkOrangeGateCollision() {

        if(orangeColor) {
            return false;
        }
        // Gets red gate rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("orange_gate_obj");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            com.badlogic.gdx.math.Rectangle rectangle = rectangleObject.getRectangle();

            if(playerRectangle.overlaps(rectangle)) {
                Gdx.app.log("ORANGE GATE", "HIT");
                return true;
            }
        }

        return false;
    }

    private boolean checkBrownGateCollision() {

        if(brownColor) {
            return false;
        }
        // Gets red gate rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("brown_gate_obj");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            com.badlogic.gdx.math.Rectangle rectangle = rectangleObject.getRectangle();

            if(playerRectangle.overlaps(rectangle)) {
                Gdx.app.log("BROWN GATE", "HIT");
                return true;
            }
        }

        return false;
    }

    private boolean checkCyanGateCollision() {

        if(cyanColor) {
            return false;
        }
        // Gets red gate rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("cyan_gate_obj");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            com.badlogic.gdx.math.Rectangle rectangle = rectangleObject.getRectangle();

            if(playerRectangle.overlaps(rectangle)) {
                Gdx.app.log("CYAN GATE", "HIT");
                return true;
            }
        }

        return false;
    }

    private boolean checkSecondRedGateCollision() {

        if(secondRedColor) {
            return false;
        }
        // Gets red gate rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("second_red_gate_obj");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            com.badlogic.gdx.math.Rectangle rectangle = rectangleObject.getRectangle();

            if(playerRectangle.overlaps(rectangle)) {
                Gdx.app.log("RED GATE", "HIT");
                return true;
            }
        }

        return false;
    }

    private boolean checkRedGateCollision() {

        if(redColor && !blueColor) {
            return false;
        }
        // Gets red gate rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("red_gate_object");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            com.badlogic.gdx.math.Rectangle rectangle = rectangleObject.getRectangle();

            if(playerRectangle.overlaps(rectangle)) {
                Gdx.app.log("RED GATE", "HIT");
                return true;
            }
        }

        return false;
    }

    public void checkSecondRedPuddleCollision() {

        // Gets worlds wall rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("second_red_puddle_obj");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();

            if(playerRectangle.overlaps(rectangle)) {
                secondRedColor = true;
            }
        }
    }

    private boolean checkPurpleGateCollision() {

        if(redColor && blueColor) {
            return false;
        }
        // Gets red gate rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("purple_gate_object");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            com.badlogic.gdx.math.Rectangle rectangle = rectangleObject.getRectangle();

            if(playerRectangle.overlaps(rectangle)) {
                Gdx.app.log("PURPLE GATE", "HIT");
                return true;
            }
        }

        return false;
    }

    public void getMyCorners(float pX, float pY, String collision) {

        float downYpos = pY;
        float upYpos = rolling.getKeyFrame(0).getRegionHeight() / 10 / 2 + downYpos;
        float leftXpos = pX;
        float rightXpos = rolling.getKeyFrame(0).getRegionWidth() / 10 / 2 + leftXpos;

        upLeftCollision = isFree(leftXpos, upYpos, collision);
        downLeftCollision = isFree(leftXpos, downYpos, collision);
        upRightCollision = isFree(rightXpos, upYpos, collision);
        downRightCollision = isFree(rightXpos, downYpos, collision);

        Gdx.app.log("COLLSION!", "hit" + upLeftCollision + downLeftCollision + downRightCollision + upRightCollision);
    }

    private boolean isFree(float x, float y, String collision) {

        int indexXround = (int)(x / 32);
        int indexYround = (int)(y / 32);

        TiledMapTileLayer wallCells = (TiledMapTileLayer) tiledMap.getLayers().get(collision);

        if(wallCells.getCell(indexXround, indexYround) != null) {
            return false;

        } else {

            return true;
        }
    }

    public void setSecondRedColor(boolean redColored) {
        secondRedColor = redColored;
    }
    public void setCyan(boolean cyanColored){
        cyanColor = cyanColored;
    }
    public void setPink(boolean pinkColored) {
        pinkColor = pinkColored;
    }
    public void setRed(boolean redColored) {
        redColor = redColored;
    }
    public void setBlue(boolean blueColored) {
        blueColor = blueColored;
    }
    public void setWhite(boolean whiteColored) {
        whiteColor = whiteColored;
    }
    public void setSecondWhite(boolean secondWhiteColored) {
        secondWhiteColor = secondWhiteColored;
    }
    public void setBrown(boolean brownColored) {
        brownColor = brownColored;
    }
    public void setOrange(boolean orangeColored) {
        orangeColor = orangeColored;
    }
    public void setBlack(boolean blackColored) {
        blackColor = blackColored;
    }
    public void setYellow(boolean yellowColored) {
        yellowColor = yellowColored;
    }

    public boolean getBlue(boolean blueColor) {
        return blueColor;
    }

    public boolean getRed(boolean redColor) {
        return redColor;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX(float playerXpos) {
        return playerXpos + (rolling.getKeyFrame(0).getRegionWidth() / 10 / 2);
    }
    public float getY(float playerYpos) {
        return playerYpos + (rolling.getKeyFrame(0).getRegionHeight() / 10 / 2);
    }

    public boolean isColorChanged() {
        return colorChanged;
    }

    public void setColorChanged(boolean colorChanged) {
        this.colorChanged = colorChanged;
    }

    public void dispose() {

    }
}