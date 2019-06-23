package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.party.InventoryItem;

import java.util.Optional;
import java.util.function.Consumer;

import static nl.t64.game.rpg.components.party.InventoryGroup.*;


class EquipSlotsTable {

    private static final String SPRITE_SILHOUETTE = "sprites/silhouette.png";
    private static final float SLOT_SIZE = 64f;
    private static final float EQUIP_SPACING = 10f;

    final Table equipSlots;
    private final HeroItem heroItem;
    private final DragAndDrop dragAndDrop;
    private final InventorySlotTooltip tooltip;

    private final InventorySlot helmetSlot;
    private final InventorySlot necklaceSlot;
    private final InventorySlot shouldersSlot;
    private final InventorySlot chestSlot;
    private final InventorySlot cloakSlot;
    private final InventorySlot bracersSlot;
    private final InventorySlot glovesSlot;
    private final InventorySlot weaponSlot;
    private final InventorySlot accessorySlot;
    private final InventorySlot ringSlot;
    private final InventorySlot shieldSlot;
    private final InventorySlot beltSlot;
    private final InventorySlot pantsSlot;
    private final InventorySlot bootsSlot;

    EquipSlotsTable(HeroItem heroItem, DragAndDrop dragAndDrop, InventorySlotTooltip tooltip) {
        this.heroItem = heroItem;
        this.dragAndDrop = dragAndDrop;
        this.tooltip = tooltip;
        this.equipSlots = new Table();

        this.helmetSlot = new InventorySlot(HELMET);
        this.necklaceSlot = new InventorySlot(NECKLACE);
        this.shouldersSlot = new InventorySlot(SHOULDERS);

        this.chestSlot = new InventorySlot(CHEST);
        this.cloakSlot = new InventorySlot(CLOAK);

        this.bracersSlot = new InventorySlot(BRACERS);
        this.glovesSlot = new InventorySlot(GLOVES);
        this.weaponSlot = new InventorySlot(WEAPON);

        this.accessorySlot = new InventorySlot(ACCESSORY);
        this.ringSlot = new InventorySlot(RING);
        this.shieldSlot = new InventorySlot(SHIELD);

        this.beltSlot = new InventorySlot(BELT);
        this.pantsSlot = new InventorySlot(PANTS);
        this.bootsSlot = new InventorySlot(BOOTS);

        this.createTable();
    }

    Optional<InventorySlot> getPossibleSlotOfGroup(InventoryGroup inventoryGroup) {
        for (Actor actor : equipSlots.getChildren()) {
            if (actor instanceof Table) {
                for (Actor deepActor : ((Table) actor).getChildren()) {
                    InventorySlot slot = (InventorySlot) deepActor;
                    if (slot.filterGroup.equals(inventoryGroup)) {
                        return Optional.of(slot);
                    }
                }
            } else {
                InventorySlot slot = (InventorySlot) actor;
                if (slot.filterGroup.equals(inventoryGroup)) {
                    return Optional.of(slot);
                }
            }
        }
        return Optional.empty();
    }

    private void createTable() {
        setDefaults();
        addListeners();
        addTargets();
        addToSlots();
        fillTable();
    }

    private void setDefaults() {
        equipSlots.defaults().space(EQUIP_SPACING).size(SLOT_SIZE, SLOT_SIZE);
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_SILHOUETTE);
        var sprite = new Sprite(texture);
        var silhouette = new SpriteDrawable(sprite);
        equipSlots.setBackground(silhouette);
    }

    private void addListeners() {
        helmetSlot.addListener(new InventorySlotTooltipListener(tooltip));
        necklaceSlot.addListener(new InventorySlotTooltipListener(tooltip));
        shouldersSlot.addListener(new InventorySlotTooltipListener(tooltip));
        chestSlot.addListener(new InventorySlotTooltipListener(tooltip));
        cloakSlot.addListener(new InventorySlotTooltipListener(tooltip));
        bracersSlot.addListener(new InventorySlotTooltipListener(tooltip));
        glovesSlot.addListener(new InventorySlotTooltipListener(tooltip));
        weaponSlot.addListener(new InventorySlotTooltipListener(tooltip));
        accessorySlot.addListener(new InventorySlotTooltipListener(tooltip));
        ringSlot.addListener(new InventorySlotTooltipListener(tooltip));
        shieldSlot.addListener(new InventorySlotTooltipListener(tooltip));
        beltSlot.addListener(new InventorySlotTooltipListener(tooltip));
        pantsSlot.addListener(new InventorySlotTooltipListener(tooltip));
        bootsSlot.addListener(new InventorySlotTooltipListener(tooltip));

        helmetSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquip));
        necklaceSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquip));
        shouldersSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquip));
        chestSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquip));
        cloakSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquip));
        bracersSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquip));
        glovesSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquip));
        weaponSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquip));
        accessorySlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquip));
        ringSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquip));
        shieldSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquip));
        beltSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquip));
        pantsSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquip));
        bootsSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquip));
    }

    private void addTargets() {
        dragAndDrop.addTarget(new InventorySlotTarget(helmetSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(necklaceSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(shouldersSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(chestSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(cloakSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(bracersSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(glovesSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(weaponSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(accessorySlot));
        dragAndDrop.addTarget(new InventorySlotTarget(ringSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(shieldSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(beltSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(pantsSlot));
        dragAndDrop.addTarget(new InventorySlotTarget(bootsSlot));
    }

    private void addToSlots() {
        heroItem.getInventoryItem(HELMET).ifPresent(addToSlot(helmetSlot));
        heroItem.getInventoryItem(NECKLACE).ifPresent(addToSlot(necklaceSlot));
        heroItem.getInventoryItem(SHOULDERS).ifPresent(addToSlot(shouldersSlot));
        heroItem.getInventoryItem(CHEST).ifPresent(addToSlot(chestSlot));
        heroItem.getInventoryItem(CLOAK).ifPresent(addToSlot(cloakSlot));
        heroItem.getInventoryItem(BRACERS).ifPresent(addToSlot(bracersSlot));
        heroItem.getInventoryItem(GLOVES).ifPresent(addToSlot(glovesSlot));
        heroItem.getInventoryItem(WEAPON).ifPresent(addToSlot(weaponSlot));
        heroItem.getInventoryItem(ACCESSORY).ifPresent(addToSlot(accessorySlot));
        heroItem.getInventoryItem(RING).ifPresent(addToSlot(ringSlot));
        heroItem.getInventoryItem(SHIELD).ifPresent(addToSlot(shieldSlot));
        heroItem.getInventoryItem(BELT).ifPresent(addToSlot(beltSlot));
        heroItem.getInventoryItem(PANTS).ifPresent(addToSlot(pantsSlot));
        heroItem.getInventoryItem(BOOTS).ifPresent(addToSlot(bootsSlot));
    }

    private void fillTable() {
        equipSlots.add(helmetSlot).colspan(5);

        equipSlots.row();

        equipSlots.add();
        equipSlots.add();
        equipSlots.add(necklaceSlot);
        equipSlots.add(shouldersSlot);
        equipSlots.add();

        equipSlots.row();

        Table bodySlots = new Table();
        bodySlots.defaults().space(EQUIP_SPACING).size(SLOT_SIZE, SLOT_SIZE);
        bodySlots.add(chestSlot);
        bodySlots.add(cloakSlot);
        equipSlots.add(bodySlots).colspan(5);

        equipSlots.row();

        equipSlots.add(bracersSlot).colspan(2);
        equipSlots.add();
        equipSlots.add(accessorySlot).colspan(2);

        equipSlots.row();

        equipSlots.add(glovesSlot).colspan(2);
        equipSlots.add(beltSlot);
        equipSlots.add(ringSlot).colspan(2);

        equipSlots.row();

        equipSlots.add(weaponSlot).colspan(2);
        equipSlots.add(pantsSlot);
        equipSlots.add(shieldSlot).colspan(2);

        equipSlots.row();

        equipSlots.add().colspan(5);
        equipSlots.row();
        equipSlots.add().colspan(5);
        equipSlots.row();

        equipSlots.add(bootsSlot).colspan(5);
    }

    private Consumer<InventoryItem> addToSlot(InventorySlot inventorySlot) {
        return inventoryItem -> {
            inventorySlot.amount = 1;
            inventorySlot.addToStack(new InventoryImage(inventoryItem));
            dragAndDrop.addSource(new InventorySlotSource(inventorySlot.getCertainInventoryImage(), dragAndDrop));
        };
    }

}
