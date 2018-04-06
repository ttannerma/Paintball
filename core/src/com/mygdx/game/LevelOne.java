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
 * Created by Teemu on 23.3.2018.
 */

public class LevelOne extends ApplicationAdapter implements Screen {

    PaintBall host;
    SpriteBatch batch;
    Rectangle rectangle;
    OrthographicCamera camera;
    String puddleCol;
    Player player;
    CollisionDetection collisionDetection;
    boolean blueColorChanged;
    boolean redColorChanged;
    boolean purpleColorChanged;
    boolean upLeftCollision;
    boolean downLeftCollision;
    boolean upRightCollision;
    boolean downRightCollision;
    boolean mapFinished;
    int timer = 100;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    public LevelOne(final PaintBall host) {

        batch = host.getBatch();
        this.host = host;
        camera = new OrthographicCamera();

        camera.setToOrtho(false, 400f, 200f);
        puddleCol = "white";
        tiledMap = new TmxMapLoader().load("tutorial_level.tmx");
        player = new Player(32 * 4,32 * 14, tiledMap);
        player.setOriginCenter();
        blueColorChanged = false;
        redColorChanged = false;
        purpleColorChanged = false;
        mapFinished = false;

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        collisionDetection = new CollisionDetection(tiledMapRenderer, tiledMap);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        player.rotate(180f);
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.begin();

        mapFinished = setMapFinished();

        if(mapFinished) {
            MapFinished mapFinished = new MapFinished(host);
            host.setScreen(mapFinished);
        }

        if(checkResetCollision()) {
            changeColor("null");
            player.setupTextureRegion();
        }

        redColorChanged = checkPaintCollision(redColorChanged, "red_puddle_object");
        blueColorChanged = checkPaintCollision(blueColorChanged, "blue_puddle_object");
        setColorOfPlayer(redColorChanged, blueColorChanged);


        checkWallCollision();

        batch.end();

        player.render(batch);

        if(player.getX(player.playerXpos) / 32 >= 7 && player.getX(player.playerXpos) / 32 <= 25) {
            camera.position.x = player.getX(player.playerXpos);
        }
        if(player.getY(player.playerYpos) / 32 >= 4 && player.getY(player.playerYpos) / 32 <= 14.5) {
            camera.position.y = player.getY(player.playerYpos);
        }
        camera.update();

        Gdx.app.log("X AND Y: ", "x: " + player.getX(player.playerXpos) / 32 + " y: " + player.getY(player.playerYpos) / 32);

        timer--;

    }

    public boolean setMapFinished() {
        mapFinished = collisionDetection.checkGoalCollision(player.playerRectangle);
        return mapFinished;
    }

    public void setPuddleCol(String puddleCol) {
        this.puddleCol = puddleCol;
    }

    public String getPuddleCol() {
        return puddleCol;
    }


    private void setColorOfPlayer(boolean red, boolean blue) {
        if(red && !blue) {
            setPuddleCol("red");
            clearGate("red_gate");
        }
        if(blue && !red) {
            setPuddleCol("blue");
            clearGate("blue_gate");
        }
        if(red && blue) {
            setPuddleCol("purple");
            clearGate("purple_gate");
            clearGate("blue_gate");
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
            cell.setCell(7, 2, null);
            cell.setCell(7, 3, null);

        } else if(path.equals("red_gate")) {
            cell.setCell(2, 9, null);
            cell.setCell(3, 9, null);
            boolean redColorSet = true;
            player.setRed(redColorSet);

        } else if(path.equals("blue_gate")) {
            cell.setCell(8, 2, null);
            cell.setCell(8, 3, null);
            boolean blueColorSet = true;
            player.setBlue(blueColorSet);
        }
    }

    public boolean checkPaintCollision(boolean color, String path) {
        if(player.isColorChanged() == false && color == true) {
            return color;
        }
        // Gets worlds wall rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get(path);

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
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

    public void checkWallCollision() {

        // Gets worlds wall rectangle layer.
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("map_walls_object");

        // All the objects of the layer.
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        //Collects all rectangles in an array.
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Loop through all rectangles.
        for(RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle rectangle = rectangleObject.getRectangle();

            if(player.playerRectangle.overlaps(rectangle)) {
                Gdx.app.log("TAG", "WALL HIT");
            }
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

    public void changeColor(String color) {
        if(color.equals("red")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_red.png")));
        }
        else if(color.equals("blue")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_blue.png")));
        }
        else if(color.equals("purple")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_purple.png")));
        }
        else if(color.equals("green")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball_green.png")));
        }
        else if(color.equals("null")) {
            player.setTexture(new Texture(Gdx.files.internal("sketch_ball.png")));
            redColorChanged = false;
            blueColorChanged = false;
        }
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

