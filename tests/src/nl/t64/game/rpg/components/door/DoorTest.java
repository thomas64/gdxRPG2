package nl.t64.game.rpg.components.door;

import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.audio.AudioEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class DoorTest extends GameTest {

    private DoorContainer doorContainer;

    @BeforeEach
    private void setup() {
        doorContainer = new DoorContainer();
    }

    @Test
    void whenDoorsAreCreated_ShouldHaveCorrectDataInside() {
        Door door = doorContainer.getDoor("door_simple_left");
        assertThat(door.getType()).isEqualTo(DoorType.SMALL);
        assertThat(door.getSpriteId()).isEqualTo("simple_left");
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(door::getKeyId);
        assertThat(door.getAudio()).isEqualTo(AudioEvent.SE_SMALL_DOOR);
        assertThat(door.getWidth()).isEqualTo(48f);
        assertThat(door.getHeight()).isEqualTo(96f);

        assertThat(door.isLocked()).isFalse();
        assertThat(door.isClosed()).isTrue();
        door.open();
        assertThat(door.isClosed()).isFalse();
        door.close();
        assertThat(door.isClosed()).isTrue();
    }

    @Test
    void whenDoorsWithLocksAreCreated_ShouldHaveCorrectDataInside() {
        Door door = doorContainer.getDoor("door_gate_mysterious_tunnel");
        assertThat(door.getType()).isEqualTo(DoorType.GATE);
        assertThat(door.getSpriteId()).isEqualTo("gate_with_lock");
        assertThat(door.getKeyId()).contains("key_mysterious_tunnel");
        assertThat(door.getAudio()).isEqualTo(AudioEvent.SE_GATE);
        assertThat(door.getWidth()).isEqualTo(48f);
        assertThat(door.getHeight()).isEqualTo(96f);

        assertThat(door.isLocked()).isTrue();
        assertThat(door.isClosed()).isTrue();
        door.unlock();
        assertThat(door.isLocked()).isFalse();
        assertThat(door.isClosed()).isTrue();
    }

    @Test
    void testForCoverage_UnusedConstructorsByJackson() {
        Door door1 = new Door("LARGE", "large_round");
        Door door2 = new Door("GATE", "gate_with_lock", "key_mysterious_tunnel");
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(door1::getKeyId);
        assertThat(door2.getKeyId()).contains("key_mysterious_tunnel");
    }

}
