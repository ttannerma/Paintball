package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Map;
import java.util.logging.Level;

/**
 * Created by Teemu on 23.3.2018.
 */

public class MainMenuScreen extends ApplicationAdapter implements Screen {

    SpriteBatch batch;
    PaintBall host;
    BitmapFont logo;
    OrthographicCamera camera;

    boolean openedFirstTime = true;

    float width;
    float height;
    //ShapeRenderer shapeRenderer;

    Stage stage;
    Skin mySkin;

    public MainMenuScreen(final PaintBall host, boolean openedFirstTime) {
        this.openedFirstTime = openedFirstTime;
        batch = host.getBatch();
        this.host = host;
        //shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        logo = new BitmapFont(Gdx.files.internal("font.txt"));
        logo.getData().setScale(0.7f, 0.7f);

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        int row_height = Gdx.graphics.getWidth() / 12;
        int col_width = Gdx.graphics.getWidth() / 12;

        Button button2 = new TextButton("Pelaa",mySkin,"small");
        button2.setSize(col_width * 4, row_height);
        button2.setPosition(50,160);
        button2.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                LevelSelectionScreen levelSelectionScreen = new LevelSelectionScreen(host);
                host.setScreen(levelSelectionScreen);
                return true;
            }
        });

        Button button3 = new TextButton("Asetukset", mySkin, "small");
        button3.setSize(col_width * 4, row_height);
        button3.setPosition(50, 60);
        button3.addListener(new InputListener()  {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                host.setScreen(new MapFinished(host));
                return true;
            }
        });

        stage.addActor(button2);
        stage.addActor(button3);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(65/255f, 105/255f,225/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!openedFirstTime) {
            LanguageSelectionScreen langScreen = new LanguageSelectionScreen(host);
            host.setScreen(langScreen);

        }

        batch.begin();

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            LevelOne levelOne = new LevelOne(host);
            host.setScreen(levelOne);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            MainMenuScreen mainMenuScreen = new MainMenuScreen(host, false);
            host.setScreen(mainMenuScreen);
        }

        logo.draw(batch, "Paint Ball Game", width / 2 - 250f, height - 20f);
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
