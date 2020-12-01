package nl.t64.game.rpg.components.door;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.t64.game.rpg.audio.AudioEvent;

import java.util.Optional;


@Getter
@NoArgsConstructor
public class Door {

    DoorType type;
    String spriteId;
    String keyId;
    AudioEvent audio;
    float width;
    float height;
    boolean isLocked;
    boolean isClosed;

    Door(String type, String spriteId) {
        this(type, spriteId, null);
    }

    Door(String type, String spriteId, String keyId) {
        this.type = DoorType.valueOf(type);
        this.spriteId = spriteId;
        this.keyId = keyId;
    }

    public Optional<String> getKeyId() {
        return Optional.of(keyId);
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void unlock() {
        isLocked = false;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void open() {
        isClosed = false;
    }

    public void close() {
        isClosed = true;
    }

}
