package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


/**
 * Created by Teemu on 23.3.2018.
 */

public class MainMenuScreen extends ApplicationAdapter implements Screen {

    SpriteBatch batch;
    PaintBall host;
    OrthographicCamera camera;
    Texture backgroundImage;
    Music music;
    Settings settings;

    float width;
    float height;
    float row_height;
    float col_width;
    float musicVol;
    boolean language;
    String playButtonText;
    String settingButtonText;

    Stage stage;
    Skin mySkin;

    public MainMenuScreen(final PaintBall host) {

        settings = Settings.getInstance();
        playButtonText = settings.getString("playButtonText", GameData.DEFAULT_PLAY_EN);
        settingButtonText = settings.getString("settingButtonText", GameData.DEFAULT_SETTINGS_EN);

        batch = host.getBatch();
        this.host = host;
        settings = Settings.getInstance();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        backgroundImage = new Texture(Gdx.files.internal("main_menu_background.png"));
        music = Gdx.audio.newMusic(Gdx.files.internal("mainmenu_music.wav"));
        musicVol = settings.getFloat("volume", 0.5f) / 100f;
        music.play();
        music.setVolume(musicVol);
        music.setLooping(true);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        row_height = Gdx.graphics.getWidth() / 12;
        col_width = Gdx.graphics.getWidth() / 12;

        Button button2 = new TextButton(playButtonText,mySkin,"small");
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

        Button button3 = new TextButton(settingButtonText, mySkin, "small");
        button3.setSize(col_width * 4, row_height);
        button3.setPosition(50, 60);
        button3.addListener(new InputListener()  {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                host.setScreen(new SettingsScreen(host, musicVol));
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

        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(65/255f, 105/255f,225/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundImage, 0,0, camera.viewportWidth, camera.viewportHeight);
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

        music.dispose();
        stage.dispose();
    }
}
