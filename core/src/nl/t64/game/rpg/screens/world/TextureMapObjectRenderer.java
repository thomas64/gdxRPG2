package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import nl.t64.game.rpg.Utils;


public class TextureMapObjectRenderer extends OrthogonalTiledMapRenderer {

    public TextureMapObjectRenderer() {
        super(null);
    }

    public void renderLowerTextures() {
        Utils.getMapManager().getLowerMapTextures()
             .stream()
             .filter(GameMapQuestTexture::isVisible)
             .map(GameMapQuestTexture::getTexture)
             .forEach(this::renderObject);
    }

    public void renderUpperTextures() {
        Utils.getMapManager().getUpperMapTextures()
             .stream()
             .filter(GameMapQuestTexture::isVisible)
             .map(GameMapQuestTexture::getTexture)
             .forEach(this::renderObject);
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
