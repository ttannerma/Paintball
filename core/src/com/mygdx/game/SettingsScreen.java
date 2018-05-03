package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.PaintBall;

import java.awt.Paint;

/**
 * Created by Teemu Tannerma on 2.5.2018.
 */
public class SettingsScreen implements Screen {

    PaintBall host;
    ShapeRenderer shapeRenderer;
    Stage stage;
    OrthographicCamera camera;
    public Slider sliderR;
    public Slider sliderL;
    public Slider sliderU;
    public Slider sliderD;
    public CheckBox sliderA;
    public SelectBox sliderRow;
    public SelectBox sliderAttribute;
    public SelectBox sliderRound;
    public SelectBox sliderStaringDifficulty;
    public CheckBox sliderUseDifficulty;
    public CheckBox sliderIncreasingDifficulty;
    public SelectBox waitingTime;
    public SelectBox faceShown;
    public TextButton calibrate;
    public TextButton freePlay;
    public TextButton general;
    public TextButton save;
    public TextButton back;
    public Slider sliderV;
    public CheckBox soundEffects;
    public SelectBox timerSides;
    public SelectBox timerUp;
    public SelectBox timerDown;
    public TextButton clearProfile1;
    public TextButton clearProfile2;
    public TextButton clearProfile3;
    public CheckBox horizontalAxis;

    float selectBoxSize;

    float row_height;
    float col_width;
    float height;
    float width;

    private Label calibrateText;
    private Label sameAttributesText;
    private Label rowLengthText;
    private Label roundsText;
    private Label startingDifficultyText;
    private Label selectionWaitingTimeText;
    private Label faceShownText;
    private Label volumeText;
    private Label savedText;
    private Label calibratedText;
    private Label timerSidesText;
    private Label timerUpText;
    private Label timerDownText;

    private Texture sensitivityGraphTexture;
    public Image sensitivityGraphImage;


    Skin mySkin;
    Settings settings;
    final float MEDIUM_TEXT_SCALE = 0.5f;


    public SettingsScreen(PaintBall host) {
        this.host = host;
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1280,800);
        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));

        row_height = camera.viewportHeight / 15;
        col_width = camera.viewportWidth / 12;
        width = camera.viewportWidth;
        height = camera.viewportHeight;

        selectBoxSize = camera.viewportWidth * 0.15f;


        mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        calibrateText = new Label("Sensitivity", mySkin, "big");
        calibrateText.setPosition(col_width*5 - calibrateText.getWidth()/2, row_height * 13);

        savedText = new Label("Saved!", mySkin, "big");
        savedText.setPosition(col_width - savedText.getWidth()/2, row_height*2);
        savedText.setAlignment(Align.center);
        savedText.setVisible(false);

        calibratedText = new Label("Calibrated!", mySkin, "big");
        calibratedText.setPosition(col_width*1.5f - calibratedText.getWidth()/2, row_height*2);
        calibratedText.setAlignment(Align.center);
        calibratedText.setVisible(false);

        stage.addActor(calibrateText);

        buttonSave();
        buttonBack();
        buttonCalibrate();
        sliderVolume();
        soundEffects();
        horizontalAxis();
        sliderRight();
        sliderLeft();
        sliderUp();
        sliderDown();
        stage.addActor(savedText);
        stage.addActor(calibratedText);
    }

    public void settingValues () {
        settings = Settings.getInstance();

        soundEffects.setChecked(settings.getBoolean("soundEffects", GameData.DEFAULT_SOUND_EFFECTS));
        sliderV.setValue(settings.getFloat("volume", GameData.DEFAULT_VOLUME));
        sliderR.setValue(settings.getFloat("sensitivityRight", GameData.DEFAULT_SENSITIVITY_RIGHT));
        sliderL.setValue(settings.getFloat("sensitivityLeft", GameData.DEFAULT_SENSITIVITY_LEFT));
        sliderU.setValue(settings.getFloat("sensitivityUp", GameData.DEFAULT_SENSITIVITY_UP));
        sliderD.setValue(settings.getFloat("sensitivityDown", GameData.DEFAULT_SENSITIVITY_DOWN));
        horizontalAxis.setChecked(settings.getBoolean("horizontalAxis", GameData.DEFAULT_HORIZONTAL_AXIS));
    }

    public void buttonBack() {
        back = new TextButton("Main Menu",mySkin,"small");
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
                    MainMenuScreen mainMenuScreen = new MainMenuScreen(host, false);
                    host.setScreen(mainMenuScreen);
                    settingValues();
            }

        });

        stage.addActor(back);
    }

    public void buttonSave() {
        save = new TextButton("SAVE",mySkin);
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
                //settings.setFloat("zeroPointX", Gdx.input.getAccelerometerY());
                //settings.setFloat("zeroPointY", Gdx.input.getAccelerometerZ());
                //Gdx.app.log("save", "accel Y: "+ settings.getFloat("zeroPointY"));
                settings.setFloat("sensitivityRight", sliderR.getValue());
                settings.setFloat("sensitivityLeft", sliderL.getValue());
                settings.setFloat("sensitivityUp", sliderU.getValue());
                settings.setFloat("sensitivityDown", sliderD.getValue());
                settings.setBoolean("soundEffects", soundEffects.isChecked());
                settings.setFloat("volume", sliderV.getValue());
                settings.setBoolean("horizontalAxis", horizontalAxis.isChecked());

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

    public void buttonCalibrate() {
        calibrate = new TextButton("Calibrate", mySkin);
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

    public void setZeroPoint() {
        settings = Settings.getInstance();

        settings.setFloat("zeroPointX", Gdx.input.getAccelerometerY());
        settings.setFloat("zeroPointY", Gdx.input.getAccelerometerZ());
        settings.setFloat("zeroPointZ", Gdx.input.getAccelerometerX());

        settings.saveSettings();
        host.updateControls();
        Gdx.app.log("SettingsScreen", "Zero point set");
        Gdx.app.log("SettingsScreen", "zeropointX" + settings.getFloat("zeroPointX", GameData.DEFAULT_ZERO_POINT_X));
        Gdx.app.log("SettingsScreen", "zeropointY" + settings.getFloat("zeroPointY", GameData.DEFAULT_ZERO_POINT_Y));
    }

    private void sliderRight() {
        sliderR = new Slider(0f,10f,1f,false, mySkin);
        sliderR.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderR.setWidth(selectBoxSize);
        sliderR.setPosition(col_width*5, row_height * 8 - sliderR.getHeight()/2);

        stage.addActor(sliderR);
    }

    private void sliderLeft() {
        sliderL = new Slider(-10f,0f,1f,false, mySkin);
        sliderL.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderL.setWidth(selectBoxSize);
        sliderL.setPosition(col_width*5 - sliderL.getWidth(), row_height * 8 - sliderR.getHeight()/2);
        stage.addActor(sliderL);
    }

    private void sliderUp() {
        sliderU = new Slider(0f,10f,1f,true, mySkin);
        sliderU.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderU.setHeight(selectBoxSize);
        sliderU.setPosition(col_width*5 - sliderU.getWidth()/2, row_height * 8);
        stage.addActor(sliderU);
    }

    private void sliderDown() {
        sliderD = new Slider(-10f,0f,1f,true, mySkin);
        sliderD.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderD.setHeight(selectBoxSize);
        sliderD.setPosition(col_width*5 - sliderD.getWidth()/2, row_height * 8 - sliderD.getHeight());
        stage.addActor(sliderD);
    }

    private void sliderVolume() {
        sliderV = new Slider(0f,100f,1f,false, mySkin);
        sliderV.setAnimateInterpolation(Interpolation.smooth);
        //slider.setAnimateDuration(0.1f);
        sliderV.setWidth(selectBoxSize*2);
        sliderV.setPosition(col_width*8, row_height * 13);
        volumeText = new Label("Volume", mySkin, "big");
        volumeText.setPosition(col_width *8 + (selectBoxSize*1.5f) - volumeText.getWidth()*1.5f/2, row_height *14);
        volumeText.setFontScale(MEDIUM_TEXT_SCALE);
        stage.addActor(sliderV);
        stage.addActor(volumeText);
    }

    public void soundEffects() {
        soundEffects = new CheckBox("Sound Effects", mySkin);
        soundEffects.getLabel().setFontScale(MEDIUM_TEXT_SCALE);
        soundEffects.setPosition(col_width*8 + selectBoxSize - soundEffects.getWidth()/2, row_height * 11);
        stage.addActor(soundEffects);
    }


    public void horizontalAxis() {
        horizontalAxis = new CheckBox("Horizontal Axis", mySkin);
        horizontalAxis.getLabel().setFontScale(MEDIUM_TEXT_SCALE);
        horizontalAxis.setPosition(col_width*8 + selectBoxSize - horizontalAxis.getWidth()/2, row_height * 0.5f);
        stage.addActor(horizontalAxis);
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
        stage.dispose();
        mySkin.dispose();
    }
}
