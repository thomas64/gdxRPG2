package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.components.door.Door;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.LoadCharacterEvent;
import nl.t64.game.rpg.events.character.SelectEvent;
import nl.t64.game.rpg.events.character.StateEvent;


public class PhysicsDoor extends PhysicsComponent {

    private final Door door;
    private final StringBuilder stringBuilder;
    private boolean isSelected;

    public PhysicsDoor(Door door) {
        this.door = door;
        this.stringBuilder = new StringBuilder();
        this.isSelected = false;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadCharacterEvent loadEvent) {
            currentPosition = loadEvent.position;
            setBoundingBox();
        }
        if (event instanceof SelectEvent) {
            isSelected = true;
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Character character, float dt) {
        if (isSelected) {
            isSelected = false;
            tryToOpenDoor(character);
        }
    }

    @Override
    void setBoundingBox() {
        boundingBox.set(currentPosition.x, currentPosition.y, door.getWidth(), Constant.TILE_SIZE / 2f);
    }

    private void tryToOpenDoor(Character character) {
        stringBuilder.setLength(0);
        if (isFailingOnLock()) return;

        if (door.isClosed()) {
            openDoor(character);
        }
    }

    private boolean isFailingOnLock() {
        if (door.isLocked()) {
            return door.getKeyId()
                       .map(this::isUnableToUnlockWithKey)
                       .orElseThrow();
        }
        return false;
    }

    private boolean isUnableToUnlockWithKey(String keyId) {
        InventoryContainer inventory = Utils.getGameData().getInventory();
        if (inventory.hasEnoughOfItem(keyId, 1)) {
            inventory.autoRemoveItem(keyId, 1);
            door.unlock();
            return false;
        } else {
            stringBuilder.append("This door is locked.");
            stringBuilder.append(System.lineSeparator());
            stringBuilder.append("You need a key to open the door.");
            notifyShowMessageDialog(stringBuilder.toString());
            return true;
        }
    }

    private void openDoor(Character character) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, door.getAudio());
        door.open();
        character.send(new StateEvent(CharacterState.OPENED));
        Utils.getMapManager().removeFromBlockers(boundingBox);
    }

    @Override
    public void debug(ShapeRenderer shapeRenderer) {
        // empty
    }

}
