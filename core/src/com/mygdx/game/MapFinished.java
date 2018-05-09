package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * @author Teemu Tannerma
 * @version 1.6
 * @since 23.3.2018
 *
 * A screen that displays "map finished" text and a button for returning to main menu.
 */

public class MapFinished extends ApplicationAdapter implements Screen {

    SpriteBatch batch;
    PaintBall host;
    BitmapFont message;
    OrthographicCamera camera;
    Texture backgroundImage;
    Settings settings;

    float width;
    float height;
    float row_height;
    float col_width;

    String mapFinishedText;
    String mainMenuButtonText;

    Stage stage;
    Skin mySkin;

    /**
     * Map finished screen constructor
     * @param host
     */
    public MapFinished(final PaintBall host) {
        batch = host.getBatch();
        this.host = host;
        settings = Settings.getInstance();

        mapFinishedText = settings.getString("levelCompletedText", GameData.DEFAULT_LEVEL_COMPLETED_EN);
        mainMenuButtonText = settings.getString("mainMenuButtonText", GameData.DEFAULT_MAIN_MENU_EN);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        message = new BitmapFont(Gdx.files.internal("font.txt"));
        message.getData().setScale(0.7f, 0.7f);
        backgroundImage = new Texture(Gdx.files.internal("settings_menu.png"));

        width = camera.viewportWidth;
        height = camera.viewportHeight;

        row_height = height / 12;
        col_width = width / 12;

        stage = new Stage(new StretchViewport(width, height));
        Gdx.input.setInputProcessor(stage);
        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        Button button2 = new TextButton(mainMenuButtonText, mySkin, "small");
        button2.setSize(col_width *3, row_height*3);
        button2.setPosition(0,0);
        button2.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                MainMenuScreen mainMenuScreen = new MainMenuScreen(host);
                host.setScreen(mainMenuScreen);
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

        Gdx.gl.glClearColor(65/255f, 105/255f,225/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(backgroundImage, 0,0, width, height);
        message.draw(batch, mapFinishedText, col_width, row_height * 10);
        batch.end();

        stage.act();
        stage.draw();

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
        dispose();
    }

    @Override
    public void dispose() {

        stage.dispose();
    }
}
