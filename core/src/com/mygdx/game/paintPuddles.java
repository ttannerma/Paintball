package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


/**
 * Contains implementation of paint puddles that player can collect.
 * Created by Teemu on 28.2.2018.
 */
public class paintPuddles extends Sprite{

    private float blueX;
    private float blueY;
    private Texture bluePaint;
    public Rectangle blueRectangle;

    public paintPuddles() {

        blueX = 200f;
        blueY = 200f;

        bluePaint = new Texture(Gdx.files.internal("sketch_paint_blue.png"));
        blueRectangle = new Rectangle(blueX, blueY, bluePaint.getWidth() / 4, bluePaint.getHeight() / 4);
    }

    public void render(SpriteBatch batch) {

        batch.begin();
        batch.draw(bluePaint, blueX, blueY, bluePaint.getWidth() / 2, bluePaint.getHeight() / 2);

        batch.end();
    }

    public void dispose() {
        bluePaint.dispose();

    }

}
