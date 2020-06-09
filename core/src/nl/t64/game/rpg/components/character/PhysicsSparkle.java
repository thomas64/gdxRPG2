package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.LoadCharacterEvent;
import nl.t64.game.rpg.events.character.SelectEvent;


public class PhysicsSparkle extends PhysicsComponent {

    private final Loot sparkle;
    private boolean isSelected;

    public PhysicsSparkle(Loot sparkle) {
        this.sparkle = sparkle;
        this.isSelected = false;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadCharacterEvent loadEvent) {
            currentPosition = loadEvent.position;
        }
        if (event instanceof SelectEvent) {
            isSelected = true;
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Character character, float dt) {
        if (isSelected) {
            isSelected = false;
            notifyShowLootDialog(sparkle);
        }
    }

    @Override
    public void debug(ShapeRenderer shapeRenderer) {
        // empty
    }

}
