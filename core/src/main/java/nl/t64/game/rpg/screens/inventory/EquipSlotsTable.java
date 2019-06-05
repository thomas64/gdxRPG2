package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.inventory.InventoryItem;
import nl.t64.game.rpg.components.party.HeroItem;

import java.util.function.Consumer;

import static nl.t64.game.rpg.components.inventory.InventoryGroup.*;


class EquipSlotsTable {

    private static final int SLOT_SIZE = 64;
    private static final int EQUIP_SPACING = 10;
    private static final int PARTY_INDEX = 0;

    Table equipSlots;

    private DragAndDrop dragAndDrop;
    private InventorySlotTooltip tooltip;

    private InventorySlot helmetSlot;
    private InventorySlot necklaceSlot;
    private InventorySlot shouldersSlot;
    private InventorySlot chestSlot;
    private InventorySlot cloakSlot;
    private InventorySlot bracersSlot;
    private InventorySlot glovesSlot;
    private InventorySlot weaponSlot;
    private InventorySlot accessorySlot;
    private InventorySlot ringSlot;
    private InventorySlot shieldSlot;
    private InventorySlot beltSlot;
    private InventorySlot pantsSlot;
    private InventorySlot bootsSlot;

    EquipSlotsTable(DragAndDrop dragAndDrop, InventorySlotTooltip tooltip) {
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

    private void createTable() {
//        equipSlots.debugAll();
        equipSlots.defaults().space(EQUIP_SPACING).size(SLOT_SIZE, SLOT_SIZE);
        addListeners();
        addTargets();
        addToSlots();
        fillTable();
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
        HeroItem heroItem = Utils.getGameData().getParty().getHero(PARTY_INDEX);

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
//        bodySlots.debugAll();
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
