package es.uca.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
 
public class MainMenuScreen implements Screen {
  Texture mainImage;
  Rectangle portada;
  final Drop game;
 
	OrthographicCamera camera;
 
	public MainMenuScreen(final Drop gam) {
		game = gam;	
		mainImage = new Texture(Gdx.files.internal("portadajuego.png"));
 
	
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
 
		
 
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		// create a Rectangle to logically represent the bucket
		portada = new Rectangle();
		portada.x = 800 / 2 - 256 / 2; // center the bucket horizontally
		portada.y = 480 / 2 - 256 / 2; // bottom left corner of the bucket is 20 pixels above
								// the bottom screen edge
		portada.width = 256;
		portada.height = 256;
 
	}
 
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.9f, 0.8f, 0.8f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
 
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
 
		game.batch.begin();
		game.batch.draw(mainImage, portada.x, portada.y);
		game.batch.end();
 
		if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
	}
 
	@Override
	public void resize(int width, int height) {
	}
 
	@Override
	public void show() {
	}
 
	@Override
	public void hide() {
	}
 
	@Override
	public void pause() {
	}
 
	@Override
	public void resume() {
	}
 
	@Override
	public void dispose() {
	}
}