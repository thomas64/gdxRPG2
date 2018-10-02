package nl.t64.game.rpg.components;

import com.badlogic.gdx.graphics.g2d.Batch;


public abstract class GraphicsComponent implements Component {

    public abstract void update();

    public abstract void render(Batch batch);

}
