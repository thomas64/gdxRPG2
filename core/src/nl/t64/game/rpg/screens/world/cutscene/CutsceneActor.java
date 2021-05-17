package nl.t64.game.rpg.screens.world.cutscene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import lombok.Setter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.door.Door;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.*;
import nl.t64.game.rpg.screens.world.entity.events.DirectionEvent;
import nl.t64.game.rpg.screens.world.entity.events.StateEvent;


class CutsceneActor extends Image {

    private final Entity entity;
    @Setter
    private EntityState entityState;
    @Setter
    private Direction direction;
    private float stateTime;

    private CutsceneActor(Entity entity, EntityState entityState, Direction direction) {
        this.stateTime = Constant.NO_FRAMES;
        this.entity = entity;
        this.entityState = entityState;
        this.direction = direction;
        this.entity.send(new StateEvent(this.entityState));
        this.entity.send(new DirectionEvent(this.direction));

        TextureRegion keyFrame = this.entity.getAnimation().getKeyFrame(this.stateTime);
        super.setDrawable(new TextureRegionDrawable(keyFrame));
        super.setScaling(Scaling.stretch);
        super.setAlign(Align.center);
        super.setSize(super.getPrefWidth(), super.getPrefHeight());
        super.setVisible(false);
    }

    private CutsceneActor(Entity entity, EntityState entityState) {
        this.stateTime = Constant.NO_FRAMES;
        this.entity = entity;
        this.entityState = entityState;
        this.direction = null;

        TextureRegion keyFrame = this.entity.getAnimation().getKeyFrame(this.stateTime);
        super.setDrawable(new TextureRegionDrawable(keyFrame));
        super.setScaling(Scaling.stretch);
        super.setAlign(Align.center);
        super.setSize(super.getPrefWidth(), super.getPrefHeight());
        super.setVisible(true);
    }

    static CutsceneActor createCharacter(String characterId) {
        var entity = new Entity(characterId, new InputEmpty(), new PhysicsNpc(), new GraphicsNpc(characterId));
        return new CutsceneActor(entity, EntityState.IDLE, Direction.SOUTH);
    }

    static CutsceneActor createDoor(String doorId) {
        Door door = Utils.getGameData().getDoors().getDoor(doorId);
        var entity = new Entity(doorId, new InputEmpty(), new PhysicsDoor(door), new GraphicsDoor(door));
        return new CutsceneActor(entity, EntityState.IDLE);
    }

    @Override
    public void act(float dt) {
        entity.send(new StateEvent(entityState));
        entity.send(new DirectionEvent(direction));
        stateTime = getStateTime(dt);
        TextureRegion region = entity.getAnimation().getKeyFrame(stateTime);
        ((TextureRegionDrawable) getDrawable()).setRegion(region);
        super.act(dt);
    }

    private float getStateTime(float dt) {
        return switch (entityState) {
            case WALKING, RUNNING -> (stateTime + dt) % 12;
            case OPENED -> stateTime + dt;
            default -> Constant.NO_FRAMES;
        };
    }

}
