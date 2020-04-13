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
import nl.t64.game.rpg.components.tooltip.InventorySlotTooltip;
import nl.t64.game.rpg.components.tooltip.InventorySlotTooltipListener;

import java.util.Optional;
import java.util.function.Consumer;


public class EquipSlotsTable {

    private static final String SPRITE_SILHOUETTE = "sprites/silhouette.png";
    private static final float SLOT_SIZE = 64f;
    private static final float EQUIP_SPACING = 10f;

    public final Table equipSlots;
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

    public EquipSlotsTable(HeroItem heroItem, DragAndDrop dragAndDrop, InventorySlotTooltip tooltip) {
        this.heroItem = heroItem;
        this.dragAndDrop = dragAndDrop;
        this.tooltip = tooltip;
        this.equipSlots = new Table();

        this.helmetSlot = new InventorySlot(InventoryGroup.HELMET);
        this.necklaceSlot = new InventorySlot(InventoryGroup.NECKLACE);
        this.shouldersSlot = new InventorySlot(InventoryGroup.SHOULDERS);

        this.chestSlot = new InventorySlot(InventoryGroup.CHEST);
        this.cloakSlot = new InventorySlot(InventoryGroup.CLOAK);

        this.bracersSlot = new InventorySlot(InventoryGroup.BRACERS);
        this.glovesSlot = new InventorySlot(InventoryGroup.GLOVES);
        this.weaponSlot = new InventorySlot(InventoryGroup.WEAPON);

        this.accessorySlot = new InventorySlot(InventoryGroup.ACCESSORY);
        this.ringSlot = new InventorySlot(InventoryGroup.RING);
        this.shieldSlot = new InventorySlot(InventoryGroup.SHIELD);

        this.beltSlot = new InventorySlot(InventoryGroup.BELT);
        this.pantsSlot = new InventorySlot(InventoryGroup.PANTS);
        this.bootsSlot = new InventorySlot(InventoryGroup.BOOTS);

        this.createTable();
    }

    Optional<InventorySlot> getPossibleSlotOfGroup(InventoryGroup inventoryGroup) {
        for (Actor actor : equipSlots.getChildren()) {
            if (actor instanceof Table table) {
                for (Actor deepActor : table.getChildren()) {
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

        helmetSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
        necklaceSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
        shouldersSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
        chestSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
        cloakSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
        bracersSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
        glovesSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
        weaponSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
        accessorySlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
        ringSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
        shieldSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
        beltSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
        pantsSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
        bootsSlot.addListener(new InventorySlotClickListener(InventoryUtils::handleDoubleClickEquipOrShop));
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
        heroItem.getInventoryItem(InventoryGroup.HELMET).ifPresent(addToSlot(helmetSlot));
        heroItem.getInventoryItem(InventoryGroup.NECKLACE).ifPresent(addToSlot(necklaceSlot));
        heroItem.getInventoryItem(InventoryGroup.SHOULDERS).ifPresent(addToSlot(shouldersSlot));
        heroItem.getInventoryItem(InventoryGroup.CHEST).ifPresent(addToSlot(chestSlot));
        heroItem.getInventoryItem(InventoryGroup.CLOAK).ifPresent(addToSlot(cloakSlot));
        heroItem.getInventoryItem(InventoryGroup.BRACERS).ifPresent(addToSlot(bracersSlot));
        heroItem.getInventoryItem(InventoryGroup.GLOVES).ifPresent(addToSlot(glovesSlot));
        heroItem.getInventoryItem(InventoryGroup.WEAPON).ifPresent(addToSlot(weaponSlot));
        heroItem.getInventoryItem(InventoryGroup.ACCESSORY).ifPresent(addToSlot(accessorySlot));
        heroItem.getInventoryItem(InventoryGroup.RING).ifPresent(addToSlot(ringSlot));
        heroItem.getInventoryItem(InventoryGroup.SHIELD).ifPresent(addToSlot(shieldSlot));
        heroItem.getInventoryItem(InventoryGroup.BELT).ifPresent(addToSlot(beltSlot));
        heroItem.getInventoryItem(InventoryGroup.PANTS).ifPresent(addToSlot(pantsSlot));
        heroItem.getInventoryItem(InventoryGroup.BOOTS).ifPresent(addToSlot(bootsSlot));
    }

    private void fillTable() {
        equipSlots.add(helmetSlot).colspan(3);

        equipSlots.row();

        equipSlots.add();
        equipSlots.add(necklaceSlot);
        equipSlots.add(shouldersSlot).left();

        equipSlots.row();

        Table bodySlots = new Table();
        bodySlots.defaults().space(EQUIP_SPACING).size(SLOT_SIZE, SLOT_SIZE);
        bodySlots.add(chestSlot);
        bodySlots.add(cloakSlot);
        equipSlots.add(bodySlots).colspan(3);

        equipSlots.row();

        equipSlots.add(bracersSlot).expandX().left().padLeft(EQUIP_SPACING);
        equipSlots.add();
        equipSlots.add(accessorySlot).expandX().right().padRight(EQUIP_SPACING);

        equipSlots.row();

        equipSlots.add(glovesSlot).expandX().left().padLeft(EQUIP_SPACING);
        equipSlots.add(beltSlot);
        equipSlots.add(ringSlot).expandX().right().padRight(EQUIP_SPACING);

        equipSlots.row();

        equipSlots.add(weaponSlot).expandX().left().padLeft(EQUIP_SPACING);
        equipSlots.add(pantsSlot);
        equipSlots.add(shieldSlot).expandX().right().padRight(EQUIP_SPACING);

        equipSlots.row();

        equipSlots.add().colspan(3);
        equipSlots.row();
        equipSlots.add().colspan(3);
        equipSlots.row();

        equipSlots.add(bootsSlot).colspan(3);
    }

    private Consumer<InventoryItem> addToSlot(InventorySlot inventorySlot) {
        return inventoryItem -> {
            inventorySlot.amount = 1;
            inventorySlot.addToStack(new InventoryImage(inventoryItem));
            dragAndDrop.addSource(new InventorySlotSource(inventorySlot.getCertainInventoryImage(), dragAndDrop));
        };
    }

}
