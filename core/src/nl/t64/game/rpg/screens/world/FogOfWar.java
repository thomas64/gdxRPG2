package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import lombok.NoArgsConstructor;
import nl.t64.game.rpg.constants.Constant;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


class FogOfWar {

    private static final float FOG_OF_WAR_RADIUS = (Constant.TILE_SIZE * 6) - 1;

    private final Map<String, Set<FogPoint>> container = new HashMap<>();

    void putIfAbsent(GameMap gameMap) {
        container.putIfAbsent(gameMap.mapTitle, fillFogOfWar(gameMap));
    }

    void update(Vector2 playerPosition, String mapTitle) {
        var sightRadius = new Circle(playerPosition, FOG_OF_WAR_RADIUS);
        container.get(mapTitle)
                 .stream()
                 .filter(sightRadius::contains)
                 .forEach(FogPoint::setExplored);
    }

    void draw(ShapeRenderer shapeRenderer, String mapTitle) {
        shapeRenderer.setColor(Color.BLACK);
        container.get(mapTitle)
                 .stream()
                 .filter(FogPoint::isUnexplored)
                 .forEach(point -> shapeRenderer.rect(point.x, point.y,
                                                      Constant.HALF_TILE_SIZE, Constant.HALF_TILE_SIZE));
    }

    private Set<FogPoint> fillFogOfWar(GameMap gameMap) {
        return IntStream.range(0, gameMap.getWidth())
                        .mapToObj(x -> getFogOfWarStream(x, gameMap))
                        .flatMap(Function.identity())
                        .collect(Collectors.toSet());
    }

    private Stream<FogPoint> getFogOfWarStream(int x, GameMap gameMap) {
        return IntStream.range(0, gameMap.getHeight())
                        .mapToObj(y -> new FogPoint(x, y));
    }

    @NoArgsConstructor
    private static class FogPoint extends Vector2 {

        private boolean isExplored;

        private FogPoint(float x, float y) {
            super(x * Constant.HALF_TILE_SIZE, y * Constant.HALF_TILE_SIZE);
            this.isExplored = false;
        }

        private void setExplored() {
            isExplored = true;
        }

        private boolean isUnexplored() {
            return !isExplored;
        }

    }

}
