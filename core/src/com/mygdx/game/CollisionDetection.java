package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;

import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Teemu on 25.3.2018.
 */

public class CollisionDetection {

    TiledMap tiledMap;
    TiledMapRenderer renderer;

    public CollisionDetection(TiledMapRenderer renderer, TiledMap tiledMap) {

        this.tiledMap = tiledMap;
        this.renderer = renderer;
    }

    public boolean checkGoalCollision(Rectangle playersRectangle) {

        // Gets the goals rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("goal_object");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            com.badlogic.gdx.math.Rectangle rectangle = rectangleObject.getRectangle();

            if(playersRectangle.overlaps(rectangle)) {
                Gdx.app.log("Goal hit", "HIT");
                return true;
            }
        }

        return false;
    }

}
