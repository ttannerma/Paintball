package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Contains implementation of the player
 * Created by sauli on 2/23/2018.
 */
public class Player extends Sprite {

    private Texture texture;
    public Rectangle playerRectangle;
    private TextureRegion[][] playerRegion;
    private TextureRegion currentFrame;
    private SpriteBatch batch;
    private Animation<TextureRegion> rolling;
    TiledMap tiledMap;

    int i = 0;

    float x;
    float y;
    float playerYpos;
    float playerXpos;
    private float speedMul = 20;
    private int animationFrame = 1;
    private float timer = 0;
    boolean upLeftCollision;
    boolean downLeftCollision;
    boolean upRightCollision;
    boolean downRightCollision;


    public Player (float x, float y) {
        texture = new Texture(Gdx.files.internal("sketch_ball.png"));
        playerRegion = TextureRegion.split(texture, texture.getWidth() / 4, texture.getHeight());
        TextureRegion[] rollingAnimation = convertTo1D(playerRegion);
        rolling = new Animation<TextureRegion>(1 / 60f, rollingAnimation);
        playerRectangle = new Rectangle(x, y, rolling.getKeyFrame(0).getRegionWidth() / 10, texture.getHeight() / 10);
        currentFrame = rolling.getKeyFrames()[1];
        setX(x);
        setY(y);
        tiledMap = new TmxMapLoader().load("paintball_map_new.tmx");
    }

    private TextureRegion[] convertTo1D(TextureRegion[][] region) {
        TextureRegion[] animation = new TextureRegion[region.length * region[0].length];
        int index = 0;
        for(int i = 0; i < region.length; i++) {
            for(int j = 0; j < region[i].length; j++) {
                animation[index++] = region[i][j];
            }
        }

        return animation;
    }

    private float yValueLastTime = 0;

    public void render(SpriteBatch batch) {
        float positiveThreshold = 2;
        float negativeThreshold = -2;
        float speed = 50 * Gdx.graphics.getDeltaTime();
        timer = timer - 5 * speed * Gdx.graphics.getDeltaTime();


        if(Gdx.input.getAccelerometerY() < 0 && Gdx.input.getAccelerometerZ() > 0) {

            getMyCorners(playerXpos + (rolling.getKeyFrame(0).getRegionWidth() / 10 / 2) - speed,playerYpos + rolling.getKeyFrame(0).getRegionHeight() / 10 / 2);
            if(Gdx.input.getAccelerometerY() < negativeThreshold && downLeftCollision && upLeftCollision) {
                x += (-1 * speed);
            }

            getMyCorners(playerXpos + (rolling.getKeyFrame(0).getRegionWidth() / 10 / 2),playerYpos + rolling.getKeyFrame(0).getRegionHeight() / 10  / 2 + speed);
            if(Gdx.input.getAccelerometerZ() > positiveThreshold && upLeftCollision && upRightCollision) {
                y += speed;
            }
        }

        if(Gdx.input.getAccelerometerY() > 0 && Gdx.input.getAccelerometerZ() > 0 ) {

            getMyCorners(playerXpos + (rolling.getKeyFrame(0).getRegionWidth() / 10 / 2) + speed,playerYpos + rolling.getKeyFrame(0).getRegionHeight() / 10 / 2);
            if(Gdx.input.getAccelerometerY() > positiveThreshold && upRightCollision && downRightCollision) {
                x += speed;
            }

            getMyCorners(playerXpos + (rolling.getKeyFrame(0).getRegionWidth() / 10 / 2), playerYpos + rolling.getKeyFrame(0).getRegionHeight() / 10 / 2+ speed);
            if(Gdx.input.getAccelerometerZ() > positiveThreshold && upLeftCollision && upRightCollision) {
                y += speed;
            }
        }


        if(Gdx.input.getAccelerometerY() > 0 && Gdx.input.getAccelerometerZ() < 0 ) {

            getMyCorners(playerXpos + (rolling.getKeyFrame(0).getRegionWidth() / 10 / 2) + speed,playerYpos + rolling.getKeyFrame(0).getRegionHeight() / 10 / 2);
            if(Gdx.input.getAccelerometerY() > positiveThreshold && downRightCollision && upRightCollision) {
                x += speed;
            }

            getMyCorners(playerXpos + (rolling.getKeyFrame(0).getRegionWidth() / 10 / 2), playerYpos + rolling.getKeyFrame(0).getRegionHeight() / 10  / 2 - speed);
            if(Gdx.input.getAccelerometerZ() < negativeThreshold && downLeftCollision && downRightCollision) {
                y += (-1 * speed);
            }
        }

        if(Gdx.input.getAccelerometerY() < 0 && Gdx.input.getAccelerometerZ() < 0) {

            getMyCorners(playerXpos + (rolling.getKeyFrame(0).getRegionWidth() / 10 / 2) - speed,playerYpos + rolling.getKeyFrame(0).getRegionHeight() / 10 / 2);
            if(Gdx.input.getAccelerometerY() < negativeThreshold && downLeftCollision && upLeftCollision) {
                x += (-1 * speed);
            }

            getMyCorners(playerXpos + (rolling.getKeyFrame(0).getRegionWidth() / 10 / 2), playerYpos + rolling.getKeyFrame(0).getRegionHeight() / 10 / 2 - speed);
            if(Gdx.input.getAccelerometerZ() < negativeThreshold && downLeftCollision && downRightCollision) {
                y += (-1 * speed);
            }
        }
        playerRectangle.setPosition(x, y);
        Gdx.app.log("TAG", "x: " + Float.toString(x) + " y: " + Float.toString(y));

        if(yValueLastTime > y && timer <= 0) {
            currentFrame = rolling.getKeyFrames()[animationFrame];
            if(animationFrame >= 3) {
                animationFrame = 0;
            } else {
                animationFrame++;
            }
            timer = 1;

        } else if (yValueLastTime < y && timer <= 0) {
            currentFrame = rolling.getKeyFrames()[animationFrame];
            animationFrame--;
            if(animationFrame <= 0) {
                animationFrame = 3;
            } else {
                animationFrame--;
            }
            timer = 1;
        } else {
            currentFrame = rolling.getKeyFrames()[1];
        }

        i++;
        Gdx.app.log("TAG", Integer.toString(i));
        batch.begin();
        batch.draw(currentFrame, playerRectangle.x, playerRectangle.y,
                playerRectangle.getHeight() /2,
                playerRectangle.getWidth() /2,
                playerRectangle.width, playerRectangle.height, 1, 1, -x);
        batch.end();
        playerXpos = playerRectangle.x;
        playerYpos = playerRectangle.y;

        yValueLastTime = y;
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

    public void dispose() {
        texture.dispose();
    }
}
