package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.mapobjects.GameMapLight;
import nl.t64.game.rpg.screens.world.mapobjects.GameMapQuestTexture;

import java.util.List;


public class TextureMapObjectRenderer extends OrthogonalTiledMapRenderer {

    private static final int[] UNDER_LAYERS = new int[]{0, 1, 2, 3, 4, 5};
    private static final int[] OVER_LAYERS = new int[]{6, 7, 8};
    private static final float SCROLL_SPEED = 10f;
    private static final String LIGHTMAP_ID = "light_object";

    private final FrameBuffer frameBuffer;
    private final Camera camera;
    private float scrollerX = 0f;
    private float scrollerY = 0f;

    public TextureMapObjectRenderer(Camera camera) {
        super(null);
        this.frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        this.camera = camera;
    }

    void renderMap() {
        renderWithoutPlayerLight(() -> {});
    }

    public void renderAll(Vector2 playerPosition, Runnable renderEntities) {
        Utils.getMapManager().getLightmapPlayer()
             .ifPresentOrElse(sprite -> renderWithPlayerLight(playerPosition, sprite, renderEntities),
                              () -> renderWithoutPlayerLight(renderEntities));
    }

    private void renderWithPlayerLight(Vector2 playerPosition, Sprite sprite, Runnable renderEntities) {
        frameBuffer.begin();
        ScreenUtils.clear(Color.BLACK);
        renderLightmapPlayer(playerPosition, sprite);
        frameBuffer.end();
        renderMapLayers(renderEntities);
        renderFrameBuffer();
    }

    private void renderWithoutPlayerLight(Runnable renderEntities) {
        ScreenUtils.clear(Color.BLACK);
        renderMapLayers(renderEntities);
    }

    public void updateCamera() {
        setView(camera);
    }

    private void renderFrameBuffer() {
        batch.setBlendFunction(GL20.GL_ZERO, GL20.GL_SRC_COLOR);
        batch.setProjectionMatrix(batch.getProjectionMatrix().idt());

        batch.begin();
        batch.draw(frameBuffer.getColorBufferTexture(), -1f, 1f, 2f, -2f);
        batch.end();
    }

    private void renderLightmapPlayer(Vector2 playerPosition, Sprite lightmapPlayer) {
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);

        batch.begin();
        float x = playerPosition.x + Constant.HALF_TILE_SIZE - (lightmapPlayer.getWidth() / 2f);
        float y = playerPosition.y + Constant.HALF_TILE_SIZE - (lightmapPlayer.getHeight() / 2f);
        lightmapPlayer.setPosition(x, y);
        lightmapPlayer.draw(batch);
        renderOtherMapLights();
        batch.end();
    }

    private void renderOtherMapLights() {
        Texture darkness = Utils.createLightmap(LIGHTMAP_ID);
        for (GameMapLight light : Utils.getMapManager().getGameMapLights()) {
            if (light.getId().equals("blue")) {
                batch.setColor(Color.ROYAL);
            }
            float x = light.getCenter().x - (darkness.getWidth() / 2f);
            float y = light.getCenter().y - (darkness.getHeight() / 2f);
            batch.draw(darkness, x, y);
            batch.setColor(Color.WHITE);
        }
    }

    private void renderMapLayers(Runnable renderEntities) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        renderBackground();
        batch.setProjectionMatrix(camera.combined);
        render(UNDER_LAYERS);
        batch.begin();
        renderTextures(Utils.getMapManager().getLowerMapTextures());
        renderEntities.run();
        renderTextures(Utils.getMapManager().getUpperMapTextures());
        batch.end();
        render(OVER_LAYERS);
        renderLightmap();
    }

    private void renderTextures(List<GameMapQuestTexture> gameMapQuestTextures) {
        gameMapQuestTextures.stream()
                            .filter(GameMapQuestTexture::isVisible)
                            .map(GameMapQuestTexture::getTexture)
                            .forEach(this::renderObject);
    }

    private void renderObject(TextureMapObject object) {
        batch.draw(object.getTextureRegion(),
                   object.getX(),
                   object.getY());
    }

    private void renderBackground() {
        batch.begin();
        batch.setProjectionMatrix(camera.projection);
        Utils.getMapManager().getParallaxBackground(camera).draw(batch);
        batch.end();
    }

    private void renderLightmap() {
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);

        batch.begin();
        renderLightmapCamera();
        renderLightmapMap();
        batch.end();
    }

    private void renderLightmapCamera() {
        batch.setProjectionMatrix(camera.projection);
        for (Sprite lightmap : Utils.getMapManager().getLightmapCamera(camera)) {
            lightmap.draw(batch);
        }
    }

    private void renderLightmapMap() {
        batch.setProjectionMatrix(camera.combined);
        Sprite lightmap = Utils.getMapManager().getLightmapMap();
        scrollerX += Gdx.graphics.getDeltaTime() * SCROLL_SPEED;
        scrollerY += Gdx.graphics.getDeltaTime() * SCROLL_SPEED;
        if (scrollerX > lightmap.getWidth() / GameMap.LIGHTMAP_REGION_MULTIPLIER) {
            scrollerX = 0f;
        }
        if (scrollerY > lightmap.getHeight() / GameMap.LIGHTMAP_REGION_MULTIPLIER) {
            scrollerY = 0f;
        }
        scrollDefinedLightmaps(lightmap);
        lightmap.draw(batch);
    }

    private void scrollDefinedLightmaps(Sprite lightmap) {
        String textureName = lightmap.getTexture().toString();
        if (textureName.contains("forest")) {
            lightmap.setX(-scrollerX);
            lightmap.setY(-scrollerY);
        } else if (textureName.contains("bubbles")) {
            lightmap.setX(-camera.getHorizontalSpaceBetweenCameraAndMapEdge());
            lightmap.setY(-scrollerY);
        }
    }

}
