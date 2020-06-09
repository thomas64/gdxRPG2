package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.SkillItemId;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.LoadCharacterEvent;
import nl.t64.game.rpg.events.character.SelectEvent;
import nl.t64.game.rpg.events.character.StateEvent;


public class PhysicsChest extends PhysicsComponent {

    private final Loot chest;
    private final StringBuilder stringBuilder;
    private boolean isSelected;

    public PhysicsChest(Loot chest) {
        this.chest = chest;
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
            tryToOpenChest(character);
        }
    }

    @Override
    void setBoundingBox() {
        boundingBox.set(currentPosition.x, currentPosition.y, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

    private void tryToOpenChest(Character character) {
        stringBuilder.setLength(0);
        if (isFailingOnTrap()) return;
        if (isFailingOnLock()) return;

        character.send(new StateEvent(CharacterState.OPENED));
        notifyShowLootDialog(chest, finishStringBuilder());
    }

    private boolean isFailingOnTrap() {
        if (chest.isTrapped()) {
            return isTrapToDifficult();
        }
        return false;
    }

    private boolean isTrapToDifficult() {
        HeroItem bestMechanic = Utils.getGameData().getParty().getHeroWithHighestSkill(SkillItemId.MECHANIC);
        if (canHandleTrapWith(bestMechanic)) {
            doHandleTrapWith(bestMechanic);
            return false;
        } else {
            dontHandleTrap();
            return true;
        }
    }

    private boolean canHandleTrapWith(HeroItem bestMechanic) {
        return bestMechanic.getCalculatedTotalSkillOf(SkillItemId.MECHANIC) >= chest.getTrapLevel();
    }

    private void dontHandleTrap() {
        stringBuilder.append("There's a dangerous trap on this treasure chest.")
                     .append(System.lineSeparator())
                     .append(String.format("You need a level %s Mechanic to disarm the trap.", chest.getTrapLevel()));
        notifyShowMessageDialog(stringBuilder.toString());
    }

    private void doHandleTrapWith(HeroItem bestMechanic) {
        stringBuilder.append(String.format("%s disarmed the trap", bestMechanic.getName()));
        chest.disarmTrap();
    }

    private boolean isFailingOnLock() {
        if (chest.isLocked()) {
            return isLockToDifficult();
        }
        return false;
    }

    private boolean isLockToDifficult() {
        HeroItem bestThief = Utils.getGameData().getParty().getHeroWithHighestSkill(SkillItemId.THIEF);
        if (canHandleLockWith(bestThief)) {
            doHandleLockWith(bestThief);
            return false;
        } else {
            dontHandleLock();
            return true;
        }
    }

    private boolean canHandleLockWith(HeroItem bestThief) {
        return bestThief.getCalculatedTotalSkillOf(SkillItemId.THIEF) >= chest.getLockLevel();
    }

    private void dontHandleLock() {
        if (stringBuilder.toString().isBlank()) {
            stringBuilder.append("There's a lock on this treasure chest.")
                         .append(System.lineSeparator());
        } else {
            stringBuilder.append(",")
                         .append(System.lineSeparator())
                         .append("but it seems the treasure chest is also locked.")
                         .append(System.lineSeparator());
        }
        stringBuilder.append(String.format("You need a level %s Thief to pick the lock.", chest.getLockLevel()));
        notifyShowMessageDialog(stringBuilder.toString());
    }

    private void doHandleLockWith(HeroItem bestThief) {
        if (stringBuilder.toString().contains(bestThief.getName())) {
            stringBuilder.append(System.lineSeparator())
                         .append("and picked the lock");
        } else if (stringBuilder.toString().isBlank()) {
            stringBuilder.append(String.format("%s picked the lock", bestThief.getName()));
        } else {
            stringBuilder.append(System.lineSeparator())
                         .append(String.format(" and %s picked the lock", bestThief.getName()));
        }
        chest.pickLock();
    }

    private String finishStringBuilder() {
        if (!stringBuilder.toString().isBlank()) {
            stringBuilder.append('.');
        }
        return stringBuilder.toString();
    }

    @Override
    public void debug(ShapeRenderer shapeRenderer) {
        // empty
    }

}
