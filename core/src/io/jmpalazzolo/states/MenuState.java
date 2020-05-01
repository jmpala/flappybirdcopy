package io.jmpalazzolo.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.jmpalazzolo.MyGdxGame;

// Contains the main menu
public class MenuState extends State {

    // Creation of screen elements
    private Texture background;
    private Texture playBtn;

    public MenuState(GameSateManager gsm) {
        super(gsm);
        background = new Texture("bg.png");
        playBtn = new Texture("playbtn.png");
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()) {
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        sb.draw(playBtn,(MyGdxGame.WIDTH / 2) - (playBtn.getWidth() / 2), (MyGdxGame.HEIGHT / 2) - (playBtn.getHeight() / 2));
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
        System.out.println("MenuState disposed");
    }
}
