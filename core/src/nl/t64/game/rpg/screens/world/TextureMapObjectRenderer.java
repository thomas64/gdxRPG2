package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;


class TextureMapObjectRenderer extends OrthogonalTiledMapRenderer {

    private static final int[] UNDER_LAYERS = new int[]{0, 1, 2, 3, 4, 5};
    private static final int[] OVER_LAYERS = new int[]{6, 7, 8};
    private static final float SCROLL_SPEED = 10f;
    private static final String LIGHTMAP_ID = "darkness";

    private final FrameBuffer frameBuffer;
    private final Camera camera;
    private float scroller = 0f;

    TextureMapObjectRenderer(Camera camera) {
        super(null);
        this.frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888,
                                           Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        this.camera = camera;
    }

    void renderAll(Vector2 playerPosition, Runnable renderCharacters) {
        Utils.getMapManager().getLightmapPlayer()
             .ifPresentOrElse(sprite -> {
                                  frameBuffer.begin();
                                  clear();
                                  renderLightmapPlayer(playerPosition, sprite);
                                  frameBuffer.end();
                                  renderMapLayers(renderCharacters);
                                  renderFrameBuffer();
                              }, () -> {
                                  clear();
                                  renderMapLayers(renderCharacters);
                              }
             );
    }

    void updateCamera() {
        setView(camera);
    }

    private void clear() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        float x = playerPosition.x + (Constant.TILE_SIZE / 2f) - (lightmapPlayer.getWidth() / 2f);
        float y = playerPosition.y + (Constant.TILE_SIZE / 2f) - (lightmapPlayer.getHeight() / 2f);
        lightmapPlayer.setPosition(x, y);
        lightmapPlayer.draw(batch);
        renderOtherMapLights();
        batch.end();
    }

    private void renderOtherMapLights() {
        for (Vector2 point : Utils.getMapManager().currentMap.lights) {
            Texture darkness = Utils.createLightmap(LIGHTMAP_ID);
            float x = point.x - (darkness.getWidth() / 2f);
            float y = point.y - (darkness.getHeight() / 2f);
            batch.draw(darkness, x, y);
        }
    }

    private void renderMapLayers(Runnable renderCharacters) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        render(UNDER_LAYERS);
        batch.begin();
        renderLowerTextures();
        renderCharacters.run();
        renderUpperTextures();
        batch.end();
        render(OVER_LAYERS);
        renderLightmap();
    }

    private void renderLowerTextures() {
        Utils.getMapManager().getLowerMapTextures()
             .stream()
             .filter(GameMapQuestTexture::isVisible)
             .map(GameMapQuestTexture::getTexture)
             .forEach(this::renderObject);
    }

    private void renderUpperTextures() {
        Utils.getMapManager().getUpperMapTextures()
             .stream()
             .filter(GameMapQuestTexture::isVisible)
             .map(GameMapQuestTexture::getTexture)
             .forEach(this::renderObject);
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
        scroller += Gdx.graphics.getDeltaTime() * SCROLL_SPEED;
        if (scroller > lightmap.getHeight() / GameMap.LIGHTMAP_REGION_MULTIPLIER) {
            scroller = 0f;
        }
        lightmap.setY(-scroller);
        lightmap.draw(batch);
    }

    @Override
    public void renderObject(MapObject object) {
        if (object instanceof TextureMapObject textureObject) {
            batch.draw(textureObject.getTextureRegion(),
                       textureObject.getX(),
                       textureObject.getY());
        }
    }

}
