package io.jmpalazzolo.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.jmpalazzolo.MyGdxGame;
import io.jmpalazzolo.sprites.Bird;
import io.jmpalazzolo.sprites.Tube;

// Contains the actual game state
public class PlayState extends State {

    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -50;

    // Creation of screen elements
    private Bird bird;
    private Texture bg;
    private Texture ground;
    private Vector2 groundPo1, groundPo2;

    private Array<Tube> tubes;

    protected PlayState(GameSateManager gsm) {
        super(gsm);
        bird = new Bird(50, 100);
        camera.setToOrtho(false, MyGdxGame.WIDTH / 2, MyGdxGame.HEIGHT / 2);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        groundPo1 = new Vector2(camera.position.x - camera.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPo2 = new Vector2((camera.position.x - camera.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);

        tubes = new Array<Tube>();
        for(int i=1; i<=TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched())
            bird.jump();
    }

    @Override
    public void update(float dt) {
        handleInput();
        bird.update(dt);
        camera.position.x = bird.getPosition().x + 80; // camera offset 80

        for(int i=0; i<tubes.size; i++) {
            if(camera.position.x - (camera.viewportWidth / 2) > tubes.get(i).getPosTopTube().x + tubes.get(i).getTopTube().getWidth()) {
                tubes.get(i).reposition(tubes.get(i).getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
            }
            if(tubes.get(i).collides(bird.getBounds())) {
                gsm.set(new PlayState(gsm));
            }
        }

        upgradeGround();

        if(bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET)
            gsm.set(new PlayState(gsm));

        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {

        // look to the EOF for good if method explanation
        sb.setProjectionMatrix(camera.combined);

        sb.begin();
        sb.draw(bg, camera.position.x - (camera.viewportWidth / 2), 0);
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        for (Tube tube: tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        sb.draw(ground, groundPo1.x, groundPo1.y);
        sb.draw(ground, groundPo2.x, groundPo2.y);
        sb.end();
    }

    // re-utilization of ground sprites
    private void upgradeGround() {
        if(camera.position.x - (camera.viewportWidth / 2) > groundPo1.x + ground.getWidth())
            groundPo1.add(ground.getWidth() * 2, 0);
        if(camera.position.x - (camera.viewportWidth / 2) > groundPo2.x + ground.getWidth())
            groundPo2.add(ground.getWidth() * 2, 0);
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        ground.dispose();
        for (Tube tube: tubes) {
            tube.dispose();
        }
        System.out.println("PlayState disposed");
    }
}


/*
Good explanation ;) url("https://stackoverflow.com/questions/33703663/understanding-the-libgdx-projection-matrix")

Consider taking a picture with a camera. E.g. using your smartphone camera taking a picture of a bench in the park.
When you do that, then you'll see the bench in the park on the screen of your smartphone. This might seem very obvious,
but let's look at what this involves.

The location of the bench on the picture is relative to where you were standing when taking the photo. In other words,
it is relative to the camera. In a typical game, you don't place object relative to the object. Instead you place them
in your game world. Translating between your game world and your camera, is done using a matrix (which is simply a
mathematical way to transform coordinates). E.g. when you move the camera to the right, then the bench moves to the left
on the photo. This is called the View matrix.

The exact location of the bench on the picture also depends on the distance between bench and the camera. At least,
it does in 3D (2D is very similar, so keep reading). When it is further away it is smaller, when it is close to the
camera it is bigger. This is called a perspective projection. You could also have an orthographic projection, in which
case the size of the object does not change according to the distance to the camera. Either way, the location and size
of the bench in the park is translated to the location and size in pixels on the screen. E.g. the bench is two meters
wide in the park, while it is 380 pixels on the photo. This is called the projection matrix.

camera.combined represents the combined view and projection matrix. In other words: it describes where things in your
game world should be rendered onto the screen.

Calling batch.setProjectionMatrix(cam.combined); instruct the batch to use that combined matrix. You should call that
whenever the value changes. This is typically when resize is called and also whenever you move or otherwise alter the
camera.

If you are uncertain then you can call that in the start of your render method.

*/