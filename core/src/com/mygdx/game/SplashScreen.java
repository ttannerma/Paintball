package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * @author Teemu Tannerma
 * @version 1.6
 * @since 8.5.2018
 */

public class SplashScreen implements Screen {

    PaintBall host;
    Texture exerium;
    Texture tiko;
    Texture ihanPihalla;
    Texture tamk;
    Stage stage;
    private OrthographicCamera camera;
    float elapsedTime;
    float width;
    float height;
    private Image splashImageExerium;
    private Image splashImageTiko;
    private Image splashImageIhanPihalla;
    private Image splashImageTamk;
    private Texture backgroundImage;
    SpriteBatch batch;

    private int splashScreenTime;


    public SplashScreen(PaintBall host) {
        this.host = host;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1280,800);
        stage = new Stage(new StretchViewport(camera.viewportWidth,camera.viewportHeight));
        batch = new SpriteBatch();
        backgroundImage = new Texture(Gdx.files.internal("settings_menu.png"));

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        exerium = new Texture("exerium_logo.png");
        splashImageExerium = new Image(exerium);
        splashImageExerium.setPosition(camera.viewportWidth/4*3-exerium.getWidth()/2, camera.viewportHeight/4 *2 - exerium.getHeight()/2);
        splashImageExerium.setSize(exerium.getWidth(), exerium.getHeight());

        tiko = new Texture("tiko_musta_fin.png");
        splashImageTiko = new Image(tiko);
        splashImageTiko.setPosition(camera.viewportWidth/4*3-tiko.getWidth()*0.5f/2, camera.viewportHeight/4 *3 - tiko.getHeight()*0.5f/2);
        splashImageTiko.setSize(tiko.getWidth()*0.5f, tiko.getHeight()*0.5f);

        tamk = new Texture("tamk.png");
        splashImageTamk = new Image(tamk);
        splashImageTamk.setPosition(camera.viewportWidth/4*3-tamk.getWidth()*0.4f/2, camera.viewportHeight/4*1 - tamk.getHeight()*0.4f/2);
        splashImageTamk.setSize(tamk.getWidth()*0.4f, tamk.getHeight()*0.4f);

        ihanPihalla = new Texture("softcoregames_logo.png");
        splashImageIhanPihalla = new Image(ihanPihalla);
        splashImageIhanPihalla.setPosition(camera.viewportWidth/4 - ihanPihalla.getWidth()/2, camera.viewportHeight/2 - ihanPihalla.getHeight() /2);
        splashImageIhanPihalla.setSize(ihanPihalla.getWidth(),ihanPihalla.getHeight());

        splashImageExerium.addAction(Actions.sequence(Actions.alpha(0f),
                Actions.fadeIn(1.0f), Actions.delay(0.5f)));
        splashImageTiko.addAction(Actions.sequence(Actions.alpha(0f),
                Actions.fadeIn(1.0f), Actions.delay(0.5f)));
        splashImageIhanPihalla.addAction(Actions.sequence(Actions.alpha(0f),
                Actions.fadeIn(1.0f), Actions.delay(0.5f)));
        splashImageTamk.addAction(Actions.sequence(Actions.alpha(0f),
                Actions.fadeIn(1.0f), Actions.delay(0.5f)));

        elapsedTime = 0;
        splashScreenTime = 4;
        Gdx.app.log("SplashScreen", "Constructor");

        stage.addActor(splashImageExerium);
        stage.addActor(splashImageTiko);
        stage.addActor(splashImageIhanPihalla);
        stage.addActor(splashImageTamk);
    }

    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(100/255f, 100/255f, 100/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundImage, 0,0, width, height);
        batch.end();

        stage.act();
        stage.draw();

        if (timer(splashScreenTime) || Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            elapsedTime = 0;
            MainMenuScreen mainMenuScreen = new MainMenuScreen(host);
            host.setScreen(mainMenuScreen);
        }
    }

    public boolean timer(int timeToPass) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (elapsedTime >= timeToPass) {
            return true;
        }
        return false;
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
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }
}