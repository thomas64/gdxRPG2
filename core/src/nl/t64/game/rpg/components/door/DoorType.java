package nl.t64.game.rpg.components.door;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.constants.Constant;


@AllArgsConstructor
public enum DoorType {

    SMALL(Constant.TILE_SIZE, Constant.TILE_SIZE * 2f, AudioEvent.SE_SMALL_DOOR),
    LARGE(Constant.TILE_SIZE * 3f, Constant.TILE_SIZE * 2f, AudioEvent.SE_LARGE_DOOR),
    GATE(Constant.TILE_SIZE, Constant.TILE_SIZE * 2f, AudioEvent.SE_GATE);

    final float width;
    final float height;
    final AudioEvent audioEvent;

}
