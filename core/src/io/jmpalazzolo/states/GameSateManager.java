package io.jmpalazzolo.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

// Manage through the different states of the app
public class GameSateManager {

    private Stack<State> states;

    public GameSateManager() {
        states = new Stack<State>();
    }

    public void push(State state) {
        states.push(state);
    }

    public void pop() {
        states.pop().dispose();
    }

    public void set(State state) {
        states.pop().dispose();
        states.push(state);
    }

    public void update(float dt) {
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb) {
        states.peek().render(sb);
    }
}
