package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.components.door.Door;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.Event;
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent;
import nl.t64.game.rpg.screens.world.entity.events.OnActionEvent;
import nl.t64.game.rpg.screens.world.entity.events.StateEvent;


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
        if (event instanceof LoadEntityEvent loadEvent) {
            currentPosition = loadEvent.position;
            setBoundingBox();
        }
        if (event instanceof OnActionEvent onActionEvent) {
            if ((onActionEvent.playerDirection.equals(Direction.NORTH)
                 || onActionEvent.playerDirection.equals(Direction.SOUTH))
                && onActionEvent.checkRect.overlaps(boundingBox)) {
                isSelected = true;
            }
        }
    }

    @Override
    public void update(Entity entity, float dt) {
        if (isSelected) {
            isSelected = false;
            tryToOpenDoor(entity);
        }
    }

    @Override
    void setBoundingBox() {
        boundingBox.set(currentPosition.x, currentPosition.y, door.getWidth(), Constant.HALF_TILE_SIZE);
    }

    private void tryToOpenDoor(Entity entity) {
        stringBuilder.setLength(0);
        if (isFailingOnLock()) return;

        if (door.isClosed()) {
            openDoor(entity);
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
            Utils.getBrokerManager().componentObservers.notifyShowMessageDialog(stringBuilder.toString());
            return true;
        }
    }

    private void openDoor(Entity entity) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, door.getAudio());
        door.open();
        entity.send(new StateEvent(EntityState.OPENED));
        Utils.getBrokerManager().blockObservers.removeObserver(entity);
        Utils.getMapManager().setTiledGraph();
    }

    @Override
    public void debug(ShapeRenderer shapeRenderer) {
        // empty
    }

}
