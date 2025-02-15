package org.bsutherla1.jade;

public abstract class Scene {

    protected Camera camera;    // Quick and dirty update to test Camera

    public Scene() {

    }

    public void init() {

    }

    public abstract void update(float dt);
}
