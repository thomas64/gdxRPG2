package nl.t64.game.rpg.screens.world.cutscene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import lombok.Setter;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.screens.world.entity.Entity;
import nl.t64.game.rpg.screens.world.entity.EntityState;
import nl.t64.game.rpg.screens.world.entity.events.DirectionEvent;
import nl.t64.game.rpg.screens.world.entity.events.StateEvent;


class CutsceneActor extends Image {

    private final Entity entity;
    @Setter
    private EntityState entityState;
    @Setter
    private Direction direction;
    private float frameTime;

    CutsceneActor(Entity entity, EntityState entityState, Direction direction) {
        this.frameTime = Constant.NO_FRAMES;
        this.entity = entity;
        this.entityState = entityState;
        this.direction = direction;
        this.entity.send(new StateEvent(this.entityState));
        this.entity.send(new DirectionEvent(this.direction));

        TextureRegion keyFrame = this.entity.getAnimation().getKeyFrame(this.frameTime);
        super.setDrawable(new TextureRegionDrawable(keyFrame));
        super.setScaling(Scaling.stretch);
        super.setAlign(Align.center);
        super.setSize(super.getPrefWidth(), super.getPrefHeight());
        super.setVisible(false);
    }

    @Override
    public void act(float dt) {
        entity.send(new StateEvent(entityState));
        entity.send(new DirectionEvent(direction));
        frameTime = entityState.equals(EntityState.WALKING) ? (frameTime + dt) % 12 : Constant.NO_FRAMES;
        TextureRegion region = entity.getAnimation().getKeyFrame(frameTime);
        ((TextureRegionDrawable) getDrawable()).setRegion(region);
        super.act(dt);
    }

}
