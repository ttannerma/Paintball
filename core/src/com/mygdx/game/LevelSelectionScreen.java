package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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

/**
 * @author Teemu Tannerma
 * @version 1.6
 * @since 23.3.2018
 *
 * Contains buttons for levels.
 */
public class LevelSelectionScreen extends ApplicationAdapter implements Screen {

    PaintBall host;
    OrthographicCamera camera;
    SpriteBatch batch;
    BitmapFont logo;
    Stage stage;
    Skin mySkin;
    Texture backgroundImage;
    Settings settings;

    Music music;

    float width;
    float height;
    float musicVol;
    String levelOneButtonText;
    String levelTwoButtonText;
    String levelThreeButtonText;
    String mainMenuButtonText;

    /**
     * Constructor for LevelSelectionScreen.
     * @param host
     */
    public LevelSelectionScreen(final PaintBall host) {

        batch = host.getBatch();
        this.host = host;

        settings = Settings.getInstance();
        levelOneButtonText = settings.getString("levelOneButtonText", GameData.DEFAULT_LEVEL_ONE_EN);
        levelTwoButtonText = settings.getString("levelTwoButtonText", GameData.DEFAULT_LEVEL_TWO_EN);
        levelThreeButtonText = settings.getString("levelThreeButtonText", GameData.DEFAULT_LEVEL_THREE_EN);
        mainMenuButtonText = settings.getString("mainMenuButtonText", GameData.DEFAULT_MAIN_MENU_EN);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        logo = new BitmapFont(Gdx.files.internal("font.txt"));
        logo.getData().setScale(0.7f, 0.7f);
        backgroundImage = new Texture(Gdx.files.internal("settings_menu.png"));
        music = Gdx.audio.newMusic(Gdx.files.internal("mainmenu_music.wav"));
        musicVol = host.settings.getFloat("volume", 0.5f) / 100f;
        music.play();
        music.setVolume(musicVol);
        music.setLooping(true);


        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        int row_height = Gdx.graphics.getWidth() / 12;
        int col_width = Gdx.graphics.getWidth() / 12;

        Button button1 = new TextButton(levelOneButtonText,mySkin,"small");
        button1.setSize(col_width * 4, row_height);
        button1.setPosition(50,60);
        button1.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                LevelOne levelone = new LevelOne(host, musicVol, mainMenuButtonText);
                host.setScreen(levelone);
                return true;
            }
        });

        Button button2 = new TextButton(levelTwoButtonText, mySkin, "small");
        button2.setSize(col_width * 4, row_height);
        button2.setPosition(50, 160);
        button2.addListener(new InputListener()  {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                LevelTwo leveltwo = new LevelTwo(host, musicVol, mainMenuButtonText);
                host.setScreen(leveltwo);
                return true;
            }
        });

        Button button3 = new TextButton(mainMenuButtonText, mySkin, "small");
        button3.setSize(col_width * 4, row_height);
        button3.setPosition(width / 2, 60);
        button3.addListener(new InputListener()  {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                MainMenuScreen mainMenuScreen = new MainMenuScreen(host);
                host.setScreen(mainMenuScreen);
                return true;
            }
        });

        Button button4 = new TextButton(levelThreeButtonText, mySkin, "small");
        button4.setSize(col_width * 4, row_height);
        button4.setPosition(50, 260);
        button4.addListener(new InputListener()  {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                LevelThree levelThree = new LevelThree(host, musicVol, mainMenuButtonText);
                host.setScreen(levelThree);
                return true;
            }
        });

        stage.addActor(button1);
        stage.addActor(button2);
        stage.addActor(button3);
        stage.addActor(button4);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(65/255f, 105/255f,225/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(backgroundImage,0,0, width, height);
        //logo.draw(batch, "Level Selection", width / 2 - 250f, height - 20f);
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
        backgroundImage.dispose();
        stage.dispose();
    }
}
