
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * @author Teemu Tannerma
 * @version 1.6
 * @since 11.3.2018
 *
 * Player class for second level.
 */
public class PlayerLevelTwo extends Sprite {

    private Texture texture;
    public Rectangle playerRectangle;
    private TextureRegion[][] playerRegion;
    private TextureRegion[] rollingAnimation;
    private TextureRegion currentFrame;
    private SpriteBatch batch;
    private Animation<TextureRegion> rolling;
    PaintBall host;


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
    boolean greenColor;
    boolean redUsed;
    boolean secondRedUsed;
    boolean blueUsed;
    boolean purpleUsed;
    boolean up;
    boolean down;
    boolean left;
    boolean right;
    boolean usingGameChair;

    float leftThreshold;
    float rightThreshhold;
    float upThreshold;
    float downThreshold;

    String collision;

    float accelY;
    float accelZ;

    /**
     * Constructor for PlayerLevelTwo.
     * @param x
     * @param y
     * @param tiledMap
     * @param host
     */
    public PlayerLevelTwo (float x, float y, TiledMap tiledMap, PaintBall host) {
        setTexture(new Texture(Gdx.files.internal("sketch_ball.png")));
        setupTextureRegion();
        setX(x);
        setY(y);
        this.host = host;
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
        greenColor = false;
        purpleUsed = false;
        redUsed = false;
        blueUsed = false;
        secondRedUsed = false;
        collision = "walls";

        usingGameChair = host.settings.getBoolean("gameChair", false);
        leftThreshold = host.settings.getFloat("sensitivityLeft", -2f);
        upThreshold = host.settings.getFloat("sensitivityUp", 2f);
        downThreshold = host.settings.getFloat("sensitivityDown", -2f);
        rightThreshhold = host.settings.getFloat("sensitivityRight", 2f);

        if(usingGameChair) {
            leftThreshold = -1f;
            upThreshold = 3.5f;
            rightThreshhold = 1f;
            downThreshold = 1.5f;
        }
    }

    /**
     * Gets current texture for the animation.
     * @return texture.
     */
    @Override
    public Texture getTexture() {
        return texture;
    }

    /**
     * Sets texture for the animation.
     * @param texture
     */
    @Override
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Animates the players movement.
     */
    public void setupTextureRegion() {
        playerRegion = TextureRegion.split(getTexture(), getTexture().getWidth() / 1, getTexture().getHeight());
        rollingAnimation = convertTo1D(playerRegion);
        rolling = new Animation<TextureRegion>(1 / 60f, rollingAnimation);
        playerRectangle = new Rectangle(x, y, rolling.getKeyFrame(0).getRegionWidth() / 10, getTexture().getHeight() / 10);
        currentFrame = rolling.getKeyFrames()[0];
    }

    /**
     * Converts 2d array to 1d.
     * @param region
     * @return 1d array
     */
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

        float speed = 80 * Gdx.graphics.getDeltaTime();

        accelY = Gdx.input.getAccelerometerY();
        accelZ = Gdx.input.getAccelerometerZ();
        up = Gdx.input.isKeyPressed(Input.Keys.UP);
        down = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        left = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        right = Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        if(redColor) {
            redUsed = true;
        }
        if(blueColor) {
            blueUsed = true;
        }
        if(secondRedColor) {
            secondRedUsed = true;
        }
        if(redColor && blueColor) {
            purpleUsed = true;
        }

        if(accelY > rightThreshhold || right) {
            getMyCorners(getX(playerXpos) + speed, getY(playerYpos), collision);
            if(upRightCollision && upLeftCollision) {
                if(!checkPurpleGateCollision() && !checkSecondRedGateCollision()) {
                    x += speed;
                } else {
                    x += (-1 * speed);
                }
            }
        }

        if(accelY < leftThreshold || left) {
            getMyCorners(getX(playerXpos) - speed, getY(playerYpos), collision);
            if(downLeftCollision && upLeftCollision) {
                if(!checkRedGateCollision()) {
                    x += (-1 * speed);
                } else {
                    x += speed;
                }
            }
        }

        if(accelZ > upThreshold || up) {
            getMyCorners(getX(playerXpos), getY(playerYpos) + speed, collision);
            if(upLeftCollision && upRightCollision) {
                if(!checkSecondRedGateCollision() && !checkCyanGateCollision() && !checkBrownGateCollision() && !checkOrangeGateCollision() && !checkPinkGateCollision()) {
                    y += speed;
                }
            } else {
                y += (-1 * speed);
            }
        }

        if(accelZ < downThreshold || down) {
            getMyCorners(getX(playerXpos), getY(playerYpos) - speed, collision);
            if(downLeftCollision && downRightCollision && !checkSecondWhiteGateCollision()) {
                y += (-1 * speed);
            } else {
                y += speed;
            }
        }

        playerRectangle.setPosition(x, y);

        currentFrame = rolling.getKeyFrames()[0];

        batch.begin();
        batch.draw(currentFrame, playerRectangle.x, playerRectangle.y,
                playerRectangle.getHeight() /2,
                playerRectangle.getWidth() /2,
                playerRectangle.width, playerRectangle.height, 1, 1, -x);

        batch.end();
        playerXpos = playerRectangle.x;
        playerYpos = playerRectangle.y;

    }

    /**
     * Checks if player rectangle collides with named object layer.
     * @return true if collision is happening.
     */
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

    /**
     * Checks if player rectangle collides with named object layer.
     * @return true if collision is happening.
     */
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

    /**
     * Checks if player rectangle collides with named object layer.
     * @return true if collision is happening.
     */
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

    /**
     * Checks if player rectangle collides with named object layer.
     * @return true if collision is happening.
     */
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

    /**
     * Checks if player rectangle collides with named object layer.
     * @return true if collision is happening.
     */
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

    /**
     * Checks if player rectangle collides with named object layer.
     * @return true if collision is happening.
     */
    private boolean checkSecondRedGateCollision() {

        if(secondRedColor || secondRedUsed) {
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

    /**
     * Checks if player rectangle collides with named object layer.
     * @return true if collision is happening.
     */
    private boolean checkRedGateCollision() {

        if(redColor || redUsed) {
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

    /**
     * Checks if player rectangle collides with named object layer.
     * @return true if collision is happening.
     */
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

    /**
     * Checks if player rectangle collides with named object layer.
     * @return true if collision is happening.
     */
    private boolean checkPurpleGateCollision() {

        if(redColor && blueColor || purpleUsed) {
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

    /**
     * Checks if players texture's corners collide with tile layers.
     * @param pX
     * @param pY
     * @param collision
     */
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

    /**
     * Checks if given coordinates of players textures collide with tile layer.
     * @param x
     * @param y
     * @param collision
     * @return
     */
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

    /**
     * Sets green color.
     * @param greenColored
     */
    public void setGreen(boolean greenColored) {
        greenColor = greenColored;
    }

    /**
     * Sets second red color.
     * @param redColored
     */
    public void setSecondRedColor(boolean redColored) {
        secondRedColor = redColored;
    }

    /**
     * Sets cyan/light blue color.
     * @param cyanColored
     */
    public void setCyan(boolean cyanColored){
        cyanColor = cyanColored;
    }

    /**
     * Sets pink color.
     * @param pinkColored
     */
    public void setPink(boolean pinkColored) {
        pinkColor = pinkColored;
    }

    /**
     * Sets red color
     * @param redColored
     */
    public void setRed(boolean redColored) {
        redColor = redColored;
    }

    /**
     * Sets blue color.
     * @param blueColored
     */
    public void setBlue(boolean blueColored) {
        blueColor = blueColored;
    }

    /**
     * Sets white color.
     * @param whiteColored
     */
    public void setWhite(boolean whiteColored) {
        whiteColor = whiteColored;
    }

    /**
     * Sets second white color.
     * @param secondWhiteColored
     */
    public void setSecondWhite(boolean secondWhiteColored) {
        secondWhiteColor = secondWhiteColored;
    }

    /**
     * Sets brown color.
     * @param brownColored
     */
    public void setBrown(boolean brownColored) {
        brownColor = brownColored;
    }

    /**
     * Sets orange color.
     * @param orangeColored
     */
    public void setOrange(boolean orangeColored) {
        orangeColor = orangeColored;
    }

    /**
     * Sets black color.
     * @param blackColored
     */
    public void setBlack(boolean blackColored) {
        blackColor = blackColored;
    }

    /**
     * Sets yellow color.
     * @param yellowColored
     */
    public void setYellow(boolean yellowColored) {
        yellowColor = yellowColored;
    }

    /**
     * Gets blue color boolean.
     * @param blueColor
     * @return true or false
     */
    public boolean getBlue(boolean blueColor) {
        return blueColor;
    }

    /**
     * Gets red color boolean
     * @param redColor
     * @return true or false
     */
    public boolean getRed(boolean redColor) {
        return redColor;
    }

    /**
     * Sets x position for player.
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets y position for player.
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Gets x coordinate.
     * @param playerXpos
     * @return x coordinate.
     */
    public float getX(float playerXpos) {
        return playerXpos + (rolling.getKeyFrame(0).getRegionWidth() / 10 / 2);
    }

    /**
     * Gets y coordinate.
     * @param playerYpos
     * @return y coordinate.
     */
    public float getY(float playerYpos) {
        return playerYpos + (rolling.getKeyFrame(0).getRegionHeight() / 10 / 2);
    }

    /**
     * Returns true if color has changed.
     * @return
     */
    public boolean isColorChanged() {
        return colorChanged;
    }

    /**
     * Sets color changed boolean.
     * @param colorChanged
     */
    public void setColorChanged(boolean colorChanged) {
        this.colorChanged = colorChanged;
    }

    public void dispose() {

    }
}