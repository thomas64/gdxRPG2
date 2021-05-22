package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import nl.t64.game.rpg.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


record GameMapLayerLoader(TiledMap tiledMap) {

    private static final int BACKGROUND_REGION_MULTIPLIER = 2;
    private static final String PARALLAX_BACKGROUND = "parallax_background";
    private static final String LIGHTMAP_CAMERA_PROPERTY = "lightmap_camera";
    private static final String LIGHTMAP_MAP_PROPERTY = "lightmap_map";
    private static final String LIGHTMAP_PLAYER_PROPERTY = "lightmap_player";
    private static final String DEFAULT_LIGHTMAP = "default";


    <T> List<T> loadLayer(String layerName) {
        return loadLayer(layerName, rectObject -> (T) rectObject);
    }

    <T> List<T> loadLayer(String layerName, Function<RectangleMapObject, T> mapper) {
        return loadLayer(layerName, rectObject -> true, mapper);
    }

    <T> List<T> startsWith(String layerName, String match) {
        return loadLayer(layerName, rectObject -> rectObject.getName().startsWith(match), rectObject -> (T) rectObject);
    }

    <T> List<T> startsWith(String layerName, String match, Function<RectangleMapObject, T> mapper) {
        return loadLayer(layerName, rectObject -> rectObject.getName().startsWith(match), mapper);
    }

    <T> List<T> equalsIgnoreCase(String layerName, String match, Function<RectangleMapObject, T> mapper) {
        return loadLayer(layerName,
                         rectObject -> rectObject.getProperties().get("type", String.class).equalsIgnoreCase(match),
                         mapper);
    }

    <T> List<T> loadLayer(String layerName, Predicate<RectangleMapObject> filter, Function<RectangleMapObject, T> mapper) {
        return getMapLayer(layerName)
                .map(mapLayer -> createRectObjectsList(mapLayer, filter, mapper))
                .orElseGet(List::of);
    }

    <T> List<T> loadTextureLayer(String layerName, Function<TextureMapObject, T> mapper) {
        return getMapLayer(layerName)
                .map(mapLayer -> createTextureObjectsList(mapLayer, mapper))
                .orElseGet(List::of);
    }

    TextureRegion loadParallaxBackground() {
        String id = tiledMap.getProperties().get(PARALLAX_BACKGROUND, DEFAULT_LIGHTMAP, String.class);
        Texture texture = Utils.createLightmap(id);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        var region = new TextureRegion(texture);
        region.setRegionWidth(texture.getWidth() * BACKGROUND_REGION_MULTIPLIER);
        return region;
    }

    List<Texture> loadLightmapCamera() {
        String lightmapStrings = tiledMap.getProperties().get(LIGHTMAP_CAMERA_PROPERTY, DEFAULT_LIGHTMAP, String.class);
        return Arrays.stream(lightmapStrings.split("\\s*,\\s*"))
                     .map(Utils::createLightmap)
                     .toList();
    }

    Sprite loadLightmapMap() {
        String id = tiledMap.getProperties().get(LIGHTMAP_MAP_PROPERTY, DEFAULT_LIGHTMAP, String.class);
        Texture texture = Utils.createLightmap(id);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        var region = new TextureRegion(texture);
        region.setRegionWidth(texture.getWidth() * GameMap.LIGHTMAP_REGION_MULTIPLIER);
        region.setRegionHeight(texture.getHeight() * GameMap.LIGHTMAP_REGION_MULTIPLIER);
        return new Sprite(region);
    }

    Sprite loadLightmapPlayer() {
        return Optional.ofNullable(tiledMap.getProperties().get(LIGHTMAP_PLAYER_PROPERTY, String.class))
                       .map(Utils::createLightmap)
                       .map(Sprite::new)
                       .orElse(null);
    }

    private <T> List<T> createRectObjectsList(MapLayer mapLayer, Predicate<RectangleMapObject> filter, Function<RectangleMapObject, T> mapper) {
        return getMapObjects(mapLayer)
                .map(RectangleMapObject.class::cast)
                .filter(filter)
                .map(mapper)
                .toList();
    }

    private <T> List<T> createTextureObjectsList(MapLayer mapLayer, Function<TextureMapObject, T> mapper) {
        return getMapObjects(mapLayer)
                .map(TextureMapObject.class::cast)
                .map(mapper)
                .toList();
    }

    private Stream<MapObject> getMapObjects(MapLayer mapLayer) {
        return StreamSupport.stream(mapLayer.getObjects().spliterator(), false);
    }

    private Optional<MapLayer> getMapLayer(String layerName) {
        return Optional.ofNullable(tiledMap.getLayers().get(layerName));
    }

}
