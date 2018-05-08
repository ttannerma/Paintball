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
import com.badlogic.gdx.utils.viewport.StretchViewport;


/**
 * @author Teemu Tannerma
 * @version 1.6
 * @since 23.3.2018
 *
 * Main menu screen.
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

    /**
     * Constructor for MainMenuScreen.
     * @param host
     */
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

        stage = new Stage(new StretchViewport(camera.viewportWidth, camera.viewportHeight));
        Gdx.input.setInputProcessor(stage);
        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        width = camera.viewportWidth;
        height = camera.viewportHeight;

        col_width = width / 12;
        row_height = height / 12;

        Button button2 = new TextButton(playButtonText,mySkin, "small");
        button2.setSize(col_width * 3, row_height * 2);
        button2.setPosition(col_width, row_height * 4);
        button2.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                LevelSelectionScreen levelSelectionScreen = new LevelSelectionScreen(host);
                host.setScreen(levelSelectionScreen);
                return true;
            }
        });

        Button button3 = new TextButton(settingButtonText, mySkin, "small");
        button3.setSize(col_width * 3, row_height * 2);
        button3.setPosition(col_width, row_height);
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
