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

/**
 * Created by Teemu on 23.3.2018.
 */

public class LevelSelectionScreen extends ApplicationAdapter implements Screen {

    PaintBall host;
    OrthographicCamera camera;
    SpriteBatch batch;
    BitmapFont logo;
    Stage stage;
    Skin mySkin;


    float width;
    float height;

    public LevelSelectionScreen(final PaintBall host) {

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

        Button button1 = new TextButton("Level 1",mySkin,"small");
        button1.setSize(col_width * 4, row_height);
        button1.setPosition(50,160);
        button1.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                LevelOne levelone = new LevelOne(host);
                host.setScreen(levelone);
                return true;
            }
        });

        Button button2 = new TextButton("Level 2", mySkin, "small");
        button2.setSize(col_width * 4, row_height);
        button2.setPosition(50, 60);
        button2.addListener(new InputListener()  {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                LevelTwo leveltwo = new LevelTwo(host);
                host.setScreen(leveltwo);
                return true;
            }
        });

        Button button3 = new TextButton("Main Menu", mySkin, "small");
        button3.setSize(col_width * 4, row_height);
        button3.setPosition(50, 260);
        button3.addListener(new InputListener()  {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                MainMenuScreen mainMenuScreen = new MainMenuScreen(host, true);
                host.setScreen(mainMenuScreen);
                return true;
            }
        });

        stage.addActor(button1);
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

        logo.draw(batch, "Level Selection", width / 2 - 250f, height - 20f);
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