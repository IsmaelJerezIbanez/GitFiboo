package es.uca.drop;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
 
public class GameScreen implements Screen {
  final Drop game;
 
	Texture huchaImage;
	Texture monedaImage;
	Texture limonImage;
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	Rectangle hucha;
	Array<Rectangle> raindrops;
	long lastDropTime;
	int dropsGathered;
 
	public GameScreen(final Drop gam) {
		this.game = gam;
 
		// load the images for the droplet and the bucket, 64x64 pixels each
		monedaImage = new Texture(Gdx.files.internal("moneda.png"));
		huchaImage = new Texture(Gdx.files.internal("hucha.png"));
		limonImage = new Texture(Gdx.files.internal("hucha.png"));
 
		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		rainMusic.setLooping(true);
 
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
 
		// create a Rectangle to logically represent the bucket
		hucha = new Rectangle();
		hucha.x = 800 / 2 - 64 / 2; // center the bucket horizontally
		hucha.y = 20; // bottom left corner of the bucket is 20 pixels above
						// the bottom screen edge
		hucha.width = 64;
		hucha.height = 64;
 
		// create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();
 
	}
 
	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800 - 64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}
 
	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0.9f, 0.8f, 0.8f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
 
		// tell the camera to update its matrices.
		camera.update();
 
		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);
 
		// begin a new batch and draw the bucket and
		// all drops
		game.batch.begin();
		game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
		game.batch.draw(huchaImage, hucha.x, hucha.y);
		for (Rectangle raindrop : raindrops) {
			game.batch.draw(monedaImage, raindrop.x, raindrop.y);
		}
		game.batch.end();
 
		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			hucha.x = touchPos.x - 64 / 2;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			hucha.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			hucha.x += 200 * Gdx.graphics.getDeltaTime();
 
		// make sure the bucket stays within the screen bounds
		if (hucha.x < 0)
			hucha.x = 0;
		if (hucha.x > 800 - 64)
			hucha.x = 800 - 64;
 
		// check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();
 
		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we play back
		// a sound effect as well.
		Iterator<Rectangle> iter = raindrops.iterator();
		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 64 < 0)
				iter.remove();
			if (raindrop.overlaps(hucha)) {
				dropsGathered++;
				dropSound.play();
				iter.remove();
			}
		}
	}
 
	@Override
	public void resize(int width, int height) {
	}
 
	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		rainMusic.play();
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
		monedaImage.dispose();
		huchaImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
	}
 
}