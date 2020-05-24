package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.character.Character;
import nl.t64.game.rpg.components.character.GraphicsSparkle;
import nl.t64.game.rpg.components.character.InputSparkle;
import nl.t64.game.rpg.components.character.PhysicsSparkle;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.events.character.LoadCharacterEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


class SparklesLoader {

    private final GameMap currentMap;
    private final List<Character> sparkles;
    private Loot sparkle;

    SparklesLoader(GameMap currentMap) {
        this.currentMap = currentMap;
        this.sparkles = new ArrayList<>();
    }

    List<Character> createSparkles() {
        loadSparkles();
        return Collections.unmodifiableList(sparkles);
    }

    private void loadSparkles() {
        for (RectangleMapObject gameMapSparkle : currentMap.sparkles) {
            sparkle = Utils.getGameData().getSparkles().getSparkle(gameMapSparkle.getName());
            if (!sparkle.isTaken()) {
                loadSparkle(gameMapSparkle);
            }
        }
    }

    private void loadSparkle(RectangleMapObject gameMapSparkle) {
        var character = new Character(new InputSparkle(), new PhysicsSparkle(sparkle), new GraphicsSparkle());
        sparkles.add(character);
        var position = new Vector2(gameMapSparkle.getRectangle().x, gameMapSparkle.getRectangle().y);
        character.send(new LoadCharacterEvent(position));
    }

}
