
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
 * Contains implementation of the player and collision detection to walls.
 * Created by sauli on 2/23/2018.
 */
public class Player extends Sprite {

    private Texture texture;
    public Rectangle playerRectangle;
    private TextureRegion[][] playerRegion;
    private TextureRegion[] rollingAnimation;
    private TextureRegion currentFrame;

    private Animation<TextureRegion> rolling;

    private boolean colorChanged = false;
    TiledMap tiledMap;
    PaintBall host;

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
    boolean up;
    boolean down;
    boolean left;
    boolean right;
    boolean usingGameChair;

    float leftThreshold;
    float rightThreshhold;
    float upThreshold;
    float downThreshold;

    float accelY;
    float accelZ;
    String collision;

    public Player (float x, float y, TiledMap tiledMap, PaintBall host) {
        setTexture(new Texture(Gdx.files.internal("sketch_ball.png")));
        setupTextureRegion();
        setX(x);
        setY(y);
        this.host = host;
        this.tiledMap = tiledMap;
        redColor = false;
        blueColor = false;
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

        float speed = 80 * Gdx.graphics.getDeltaTime();

        //Gdx.app.log("TAG", Float.toString(Gdx.input.getAccelerometerY()));
        //Gdx.app.log("TAG", Float.toString(Gdx.input.getAccelerometerZ()));

        accelY = Gdx.input.getAccelerometerY();
        accelZ = Gdx.input.getAccelerometerZ();

        up = Gdx.input.isKeyPressed(Input.Keys.UP);
        down = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        left = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        right = Gdx.input.isKeyPressed(Input.Keys.RIGHT);


        // y = oikea vasen
        // z = eteen taakse
        if(accelY > leftThreshold || left) {
            getMyCorners(getX(playerXpos) + speed, getY(playerYpos));
            if(upRightCollision && upLeftCollision) {
                if(!checkPurpleGateCollision()) {
                    x += speed;
                } else {
                    x += (-1 * speed);
                }
            }
        }

        if(accelY < rightThreshhold || right) {
            getMyCorners(getX(playerXpos) - speed, getY(playerYpos));
            if(downLeftCollision && upLeftCollision) {

                x += (-1 * speed);
            }
        }

        if(accelZ > upThreshold || up) {
            getMyCorners(getX(playerXpos), getY(playerYpos) + speed);
            if(upLeftCollision && upRightCollision) {
                y += speed;
            }
        }

        if(accelZ < downThreshold || down) {
            getMyCorners(getX(playerXpos), getY(playerYpos) - speed);
            if(downLeftCollision && downRightCollision) {
                if(!checkRedGateCollision()) {
                    y += (-1 * speed);
                }
            }
        }

        playerRectangle.setPosition(x, y);
        Gdx.app.log("TAG", "x: " + Float.toString(x) + " y: " + Float.toString(y));

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

    private boolean checkRedGateCollision() {

        if(redColor) {
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

    private boolean checkPurpleGateCollision() {

        if(blueColor && redColor) {
            return false;
        }
        // Gets purple gate rectangle layer.
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

    public void getMyCorners(float pX, float pY) {

        float downYpos = pY;
        float upYpos = rolling.getKeyFrame(0).getRegionHeight() / 10 / 2 + downYpos;
        float leftXpos = pX;
        float rightXpos = rolling.getKeyFrame(0).getRegionWidth() / 10 / 2 + leftXpos;

        upLeftCollision = isFree(leftXpos, upYpos);
        downLeftCollision = isFree(leftXpos, downYpos);
        upRightCollision = isFree(rightXpos, upYpos);
        downRightCollision = isFree(rightXpos, downYpos);
    }

    private boolean isFree(float x, float y) {

        int indexXround = (int)(x / 32);
        int indexYround = (int)(y / 32);

        TiledMapTileLayer wallCells = (TiledMapTileLayer) tiledMap.getLayers().get("walls");

        if(wallCells.getCell(indexXround, indexYround) != null) {
            return false;

        } else {

            return true;
        }
    }

    public void setRed(boolean redColored) {
        redColor = redColored;
    }
    public void setBlue(boolean blueColored) {
        blueColor = blueColored;
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

