
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
    float lastXVelocity = 0;
    float lastYVelocity = 0;
    String collision;

    public Player (float x, float y, TiledMap tiledMap) {
        setTexture(new Texture(Gdx.files.internal("sketch_ball.png")));
        setupTextureRegion();
        setX(x);
        setY(y);
        this.tiledMap = tiledMap;
        redColor = false;
        blueColor = false;
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

    private float yValueLastTime = 0;

    public void render(SpriteBatch batch) {

        redColor = getRed(redColor);
        blueColor = getBlue(blueColor);

        float positiveThreshold = 1;
        float negativeThreshold = -1;
        float speed = 80 * Gdx.graphics.getDeltaTime();


        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y = y - speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y = y + speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x = x - speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x = x + speed;
        }


        //Gdx.app.log("TAG", Float.toString(Gdx.input.getAccelerometerY()));
        //Gdx.app.log("TAG", Float.toString(Gdx.input.getAccelerometerZ()));

        if(Gdx.input.getAccelerometerY() < 1 && Gdx.input.getAccelerometerZ() - 4 > 1) {

            getMyCorners(getX(playerXpos) - speed, getY(playerYpos));
            if (Gdx.input.getAccelerometerY() < negativeThreshold && downLeftCollision && upLeftCollision) {
                Gdx.app.log("TAG", "first");
                x += (-1 * speed);
            }

            getMyCorners(getX(playerXpos), getY(playerYpos) + speed);
            if (Gdx.input.getAccelerometerZ() - 4 > positiveThreshold && upLeftCollision && upRightCollision) {
                Gdx.app.log("TAG", "second");
                y += speed;
            }
        }

        if(Gdx.input.getAccelerometerY() > 1 && Gdx.input.getAccelerometerZ() - 4 > 1) {

            getMyCorners(getX(playerXpos) + speed, getY(playerYpos));
            if (Gdx.input.getAccelerometerY() > positiveThreshold && upRightCollision && downRightCollision) {
                Gdx.app.log("TAG", "third");
                if (!checkPurpleGateCollision()) {
                    x += speed;
                } else {
                    x += (-1 * speed);
                }
            }

            getMyCorners(getX(playerXpos), getY(playerYpos) + speed);
            if (Gdx.input.getAccelerometerZ() - 4 > positiveThreshold && upLeftCollision && upRightCollision) {
                Gdx.app.log("TAG", "fourth");
                y += speed;
            }
        }


        if(Gdx.input.getAccelerometerY() > 1 && Gdx.input.getAccelerometerZ() - 4 < 1) {

            getMyCorners(getX(playerXpos) + speed, getY(playerYpos));
            if (Gdx.input.getAccelerometerY() > positiveThreshold && downRightCollision && upRightCollision) {
                Gdx.app.log("TAG", "sixth");
                if(!checkPurpleGateCollision()) {
                    x += speed;
                } else {
                    x += (-1 * speed);
                }
            }

            getMyCorners(getX(playerXpos), getY(playerYpos) - speed);
            if (Gdx.input.getAccelerometerZ() - 4 < negativeThreshold && downLeftCollision && downRightCollision) {
                Gdx.app.log("TAG", "seventh");
                if(!checkRedGateCollision()) {
                    y += (-1 * speed);
                } else {
                    y += speed;
                }
            }
        }

        if(Gdx.input.getAccelerometerY() < 1 && Gdx.input.getAccelerometerZ() - 4 < 1) {

            getMyCorners(getX(playerXpos) - speed, getY(playerYpos));
            if (Gdx.input.getAccelerometerY() < negativeThreshold && downLeftCollision && upLeftCollision) {
                Gdx.app.log("TAG", "eight");
                x += (-1 * speed);
            }

            getMyCorners(getX(playerXpos), getY(playerYpos) - speed);
            if (Gdx.input.getAccelerometerZ() - 4 < negativeThreshold && downLeftCollision && downRightCollision) {
                Gdx.app.log("TAG", "ninth");
                if(!checkRedGateCollision()) {
                    y += (-1 * speed);
                } else {
                    y += speed;
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

