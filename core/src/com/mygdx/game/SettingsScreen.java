package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;


/**
 * @author Teemu Tannerma
 * @version 1.6
 * @since 2.5.2018
 *
 * A screen that holds all settings for the game.
 */
public class SettingsScreen implements Screen {

    PaintBall host;
    ShapeRenderer shapeRenderer;
    SpriteBatch batch;
    Stage stage;
    OrthographicCamera camera;
    Texture backgroundImage;
    Music music;

    public Slider sliderR;
    public Slider sliderL;
    public Slider sliderU;
    public Slider sliderD;

    public TextButton calibrate;

    public TextButton save;
    public TextButton back;
    public Slider sliderV;
    public CheckBox language;

    public CheckBox usingChair;

    float selectBoxSize;

    float row_height;
    float col_width;
    float height;
    float width;

    private Label calibrateText;

    private Label volumeText;
    private Label savedText;
    private Label calibratedText;

    Skin mySkin;
    Settings settings;
    final float MEDIUM_TEXT_SCALE = 0.5f;

    boolean musicPlaying;
    boolean useEnglish;
    float musicVol;

    String mainMenuText;
    String saveButtonText;
    String savedHoverText;
    String calibrateButtonText;
    String calibratedHoverText;
    String volumeButtonText;
    String languageButtonText;
    String usingChairButtonText;
    String sensitivityButtonText;

    /**
     * Setting screen constructor.
     * @param host
     * @param musicVolume
     */
    public SettingsScreen(PaintBall host, float musicVolume) {
        batch = host.getBatch();
        this.host = host;
        settings = Settings.getInstance();

        savedHoverText = settings.getString("savedHoverText", GameData.DEFAULT_SAVED_EN);
        calibratedHoverText = settings.getString("calibratedHoverText", GameData.DEFAULT_CALIBRATED_EN);
        sensitivityButtonText = settings.getString("sensitivityButtonText", GameData.DEFAULT_SENSITIVITY_TEXT_EN);
        useEnglish = settings.getBoolean("language", GameData.DEFAULT_LANGUAGE);

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1280,800);
        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));
        backgroundImage = new Texture(Gdx.files.internal("settings_menu.png"));
        music = Gdx.audio.newMusic(Gdx.files.internal("mainmenu_music.wav"));
        musicVol = musicVolume;

        row_height = camera.viewportHeight / 15;
        col_width = camera.viewportWidth / 12;
        width = camera.viewportWidth;
        height = camera.viewportHeight;

        selectBoxSize = camera.viewportWidth * 0.15f;

        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        calibrateText = new Label(sensitivityButtonText, mySkin, "big");
        calibrateText.setPosition(col_width*5 - calibrateText.getWidth()/2, row_height * 13);

        savedText = new Label(savedHoverText, mySkin, "big");
        savedText.setPosition(col_width - savedText.getWidth()/2, row_height*2);
        savedText.setAlignment(Align.center);
        savedText.setVisible(false);

        calibratedText = new Label(calibratedHoverText, mySkin, "big");
        calibratedText.setPosition(col_width*1.5f - calibratedText.getWidth()/2, row_height*2);
        calibratedText.setAlignment(Align.center);
        calibratedText.setVisible(false);

        stage.addActor(calibrateText);

        buttonSave();
        buttonBack();
        buttonCalibrate();
        sliderVolume();
        language();
        usingChair();
        sliderRight();
        sliderLeft();
        sliderUp();
        sliderDown();
        stage.addActor(savedText);
        stage.addActor(calibratedText);
    }

    /**
     * Sets the values for the setting values.
     */
    public void settingValues () {
        settings = Settings.getInstance();

        language.setChecked(settings.getBoolean("language", GameData.DEFAULT_LANGUAGE));
        sliderV.setValue(settings.getFloat("volume", GameData.DEFAULT_VOLUME));
        sliderR.setValue(settings.getFloat("sensitivityRight", GameData.DEFAULT_SENSITIVITY_RIGHT));
        sliderL.setValue(settings.getFloat("sensitivityLeft", GameData.DEFAULT_SENSITIVITY_LEFT));
        sliderU.setValue(settings.getFloat("sensitivityUp", GameData.DEFAULT_SENSITIVITY_UP));
        sliderD.setValue(settings.getFloat("sensitivityDown", GameData.DEFAULT_SENSITIVITY_DOWN));
        usingChair.setChecked(settings.getBoolean("gameChair", GameData.DEFAULT_USE_CHAIR));
    }

    /**
     * Creates a button for returning to main menu.
     */
    public void buttonBack() {
        mainMenuText = settings.getString("mainMenuButtonText", GameData.DEFAULT_MAIN_MENU_EN);
        back = new TextButton(mainMenuText ,mySkin,"small");
        back.setSize(col_width*2,row_height*2);
        back.setPosition(0,height - back.getHeight());
        back.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("TAG", "back");
                    music.dispose();
                    MainMenuScreen mainMenuScreen = new MainMenuScreen(host);
                    host.setScreen(mainMenuScreen);
                    settingValues();
            }

        });

        stage.addActor(back);
    }

    /**
     * Creates a save button.
     */
    public void buttonSave() {
        saveButtonText = settings.getString("saveButtonText", GameData.DEFAULT_SAVE_EN);
        save = new TextButton(saveButtonText ,mySkin);
        save.setSize(col_width*2,row_height*2);
        save.setPosition(0,0);
        save.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("TAG", "save");
                settings.setFloat("zeroPointX", Gdx.input.getAccelerometerY());
                settings.setFloat("zeroPointY", Gdx.input.getAccelerometerZ());
                //Gdx.app.log("save", "accel Y: "+ settings.getFloat("zeroPointY"));
                settings.setFloat("sensitivityRight", sliderR.getValue() * 0.7f);
                settings.setFloat("sensitivityLeft", sliderL.getValue() * 0.7f);
                settings.setFloat("sensitivityUp", sliderU.getValue() * 0.7f);
                settings.setFloat("sensitivityDown", sliderD.getValue() * 0.7f);
                settings.setBoolean("language", language.isChecked());
                settings.setFloat("volume", sliderV.getValue());
                settings.setBoolean("gameChair", usingChair.isChecked());

                settings.saveSettings();
                host.updateSettings();
                host.updateControls();

                if(settings.getBoolean("language", language.isChecked())) {
                    setEnglish();
                } else {
                    setFinnish();
                }
                settings.saveSettings();
                host.updateSettings();
                host.updateControls();
                savedText.setVisible(true);
                savedText.toFront();
                savedText.addAction(Actions.sequence(Actions.alpha(1f),
                        Actions.fadeOut(3.0f), Actions.delay(3f)));
            }

        });

        stage.addActor(save);
    }

    /**
     * Creates calibration button.
     */
    public void buttonCalibrate() {
        calibrateButtonText = settings.getString("calibrateButtonText", GameData.DEFAULT_CALIBRATE_EN);
        calibrate = new TextButton(calibrateButtonText, mySkin);
        calibrate.setSize(selectBoxSize*2, row_height*2);
        calibrate.setPosition(col_width*5 - calibrate.getWidth()/2, row_height * 2);
        calibrate.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setZeroPoint();
                calibratedText.setVisible(true);
                calibratedText.toFront();
                calibratedText.addAction(Actions.sequence(Actions.alpha(1f),
                        Actions.fadeOut(3.0f), Actions.delay(3f)));
            }

        });

        stage.addActor(calibrate);
    }

    /**
     * Sets accelerometer values
     */
    public void setZeroPoint() {

        settings.setFloat("zeroPointX", Gdx.input.getAccelerometerY());
        settings.setFloat("zeroPointY", Gdx.input.getAccelerometerZ());
        settings.setFloat("zeroPointZ", Gdx.input.getAccelerometerX());

        settings.saveSettings();
        host.updateControls();
        Gdx.app.log("SettingsScreen", "Zero point set");
        Gdx.app.log("SettingsScreen", "zeropointX" + settings.getFloat("zeroPointX", GameData.DEFAULT_ZERO_POINT_X));
        Gdx.app.log("SettingsScreen", "zeropointY" + settings.getFloat("zeroPointY", GameData.DEFAULT_ZERO_POINT_Y));
    }

    /**
     * Creates a slider for right side sensitivity settings.
     */
    private void sliderRight() {
        sliderR = new Slider(0f,10f,1f,false, mySkin);
        sliderR.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderR.setWidth(selectBoxSize);
        sliderR.setPosition(col_width*5, row_height * 8 - sliderR.getHeight()/2);

        stage.addActor(sliderR);
    }

    /**
     * Creates a slider for left side sensitivity settings.
     */
    private void sliderLeft() {
        sliderL = new Slider(-10f,0f,1f,false, mySkin);
        sliderL.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderL.setWidth(selectBoxSize);
        sliderL.setPosition(col_width*5 - sliderL.getWidth(), row_height * 8 - sliderR.getHeight()/2);
        stage.addActor(sliderL);
    }

    /**
     * Creates a slider for up sensitivity settings.
     */
    private void sliderUp() {
        sliderU = new Slider(0f,10f,1f,true, mySkin);
        sliderU.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderU.setHeight(selectBoxSize);
        sliderU.setPosition(col_width*5 - sliderU.getWidth()/2, row_height * 8);
        stage.addActor(sliderU);
    }

    /**
     * Creates a slider for down sensitivity settings.
     */
    private void sliderDown() {
        sliderD = new Slider(-10f,0f,1f,true, mySkin);
        sliderD.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderD.setHeight(selectBoxSize);
        sliderD.setPosition(col_width*5 - sliderD.getWidth()/2, row_height * 8 - sliderD.getHeight());
        stage.addActor(sliderD);
    }

    /**
     * Creates a slider for volume control.
     */
    private void sliderVolume() {
        volumeButtonText = settings.getString("volumeButtonText", GameData.DEFAULT_VOLUME_TEXT_EN);

        sliderV = new Slider(0f,100f,1f,false, mySkin);
        sliderV.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderV.setWidth(selectBoxSize*2);
        sliderV.setPosition(col_width*8, row_height * 13);
        volumeText = new Label(volumeButtonText, mySkin, "big");
        volumeText.setPosition(col_width *8 + (selectBoxSize*1.5f) - volumeText.getWidth()*1.5f/2, row_height *14);
        volumeText.setFontScale(MEDIUM_TEXT_SCALE);
        stage.addActor(sliderV);
        stage.addActor(volumeText);
    }

    /**
     * Creates a button for language.
     */
    public void language() {
        languageButtonText = settings.getString("languageButtonText", GameData.DEFAULT_LANGUAGE_BUTTON_EN);

        language = new CheckBox(languageButtonText, mySkin);
        language.getImageCell().height(100);
        language.getImageCell().width(100);
        language.getLabel().setFontScale(MEDIUM_TEXT_SCALE * 2);
        language.setPosition(col_width*8 + selectBoxSize - language.getWidth()/2, row_height * 11);
        stage.addActor(language);
    }

    /**
     * Creates a button for using chair.
     */
    public void usingChair() {
        usingChairButtonText = settings.getString("usingChairButtonText", GameData.DEFAULT_USING_CHAIR_TEXT_EN);

        usingChair = new CheckBox(usingChairButtonText, mySkin);
        usingChair.getImageCell().height(100);
        usingChair.getImageCell().width(100);
        usingChair.getLabel().setFontScale(MEDIUM_TEXT_SCALE * 2);
        usingChair.setPosition(col_width*8 + selectBoxSize - usingChair.getWidth()/2, row_height * 0.5f);
        stage.addActor(usingChair);
    }

    /**
     * Sets String values to English.
     */
    public void setEnglish() {
        settings.setString("mainMenuButtonText", GameData.DEFAULT_MAIN_MENU_EN);
        settings.setString("settingButtonText", GameData.DEFAULT_SETTINGS_EN);
        settings.setString("levelCompletedText", GameData.DEFAULT_LEVEL_COMPLETED_EN);
        settings.setString("playButtonText", GameData.DEFAULT_PLAY_EN);
        settings.setString("saveButtonText", GameData.DEFAULT_SAVE_EN);
        settings.setString("calibrateButtonText", GameData.DEFAULT_CALIBRATE_EN);
        settings.setString("volumeButtonText", GameData.DEFAULT_VOLUME_TEXT_EN);
        settings.setString("languageButtonText", GameData.DEFAULT_LANGUAGE_BUTTON_EN);
        settings.setString("sensitivityButtonText", GameData.DEFAULT_SENSITIVITY_TEXT_EN);
        settings.setString("usingChairButtonText", GameData.DEFAULT_USING_CHAIR_TEXT_EN);
        settings.setString("levelOneButtonText", GameData.DEFAULT_LEVEL_ONE_EN);
        settings.setString("levelTwoButtonText", GameData.DEFAULT_LEVEL_TWO_EN);
        settings.setString("levelThreeButtonText", GameData.DEFAULT_LEVEL_THREE_EN);
        settings.setString("calibratedHoverText", GameData.DEFAULT_CALIBRATED_EN);
        settings.setString("savedHoverText", GameData.DEFAULT_SAVED_EN);
        settings.setString("levelCompletedText", GameData.DEFAULT_LEVEL_COMPLETED_EN);
    }

    /**
     * Sets String values to Finnish.
     */
    public void setFinnish() {
        settings.setString("mainMenuButtonText", "Valikko");
        settings.setString("settingButtonText", "Asetukset");
        settings.setString("levelCompletedText", "Taso suoritettu");
        settings.setString("playButtonText", "Pelaa");
        settings.setString("saveButtonText", "Tallenna");
        settings.setString("calibrateButtonText", "Kalibroi");
        settings.setString("volumeButtonText", "Volyymi");
        settings.setString("languageButtonText", "Englannin kieli");
        settings.setString("sensitivityButtonText", "Herkkyys");
        settings.setString("usingChairButtonText", "Aseta asetukset Exeriumin GameXR tuolille");
        settings.setString("levelOneButtonText", "Harjoitus taso");
        settings.setString("levelTwoButtonText", "Taso 2");
        settings.setString("levelThreeButtonText", "Taso 3");
        settings.setString("calibratedHoverText", "Kalibroitu");
        settings.setString("savedHoverText", "Tallennettu");
    }

    @Override
    public void show() {
        settingValues();
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(13/255f,54/255f,70/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        musicPlaying = music.isPlaying();

        if(!musicPlaying) {
            music.play();
            music.setVolume(musicVol);
            music.setLooping(true);
        }

        batch.begin();
        batch.draw(backgroundImage, 0,0, width, height);
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
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void dispose() {

        backgroundImage.dispose();
        batch.dispose();
        stage.dispose();
        mySkin.dispose();
    }
}
