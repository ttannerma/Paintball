package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by sauli on 3/26/2018.
 */

public class LanguageSelectionScreen implements Screen {

    PaintBall host;
    OrthographicCamera camera;
    SpriteBatch batch;
    BitmapFont logo;
    Stage stage;
    Skin mySkin;

    boolean english;

    float width;
    float height;

    public LanguageSelectionScreen(final PaintBall host) {
        batch = host.getBatch();
        this.host = host;
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

        Button button2 = new TextButton("English",mySkin,"small");
        button2.setSize(col_width * 4, row_height);
        button2.setPosition(50,160);
        button2.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                MainMenuScreen mainMenuScreen = new MainMenuScreen(host, false);
                host.setScreen(mainMenuScreen);
                host.setLanguage("English");
                return true;
            }
        });

        Button button3 = new TextButton("Finnish", mySkin, "small");
        button3.setSize(col_width * 4, row_height);
        button3.setPosition(50, 60);
        button3.addListener(new InputListener()  {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                MainMenuScreen mainMenuScreen = new MainMenuScreen(host, false);
                host.setScreen(mainMenuScreen);
                host.setLanguage("Finnish");
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

    }

    @Override
    public void dispose() {

    }
}
