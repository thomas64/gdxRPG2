package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltipListener;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


public class EquipSlotsTable {

    private static final String SPRITE_SILHOUETTE = "sprites/silhouette.png";
    private static final float SLOT_SIZE = 64f;
    private static final float EQUIP_SPACING = 10f;

    public final Table container;
    private final HeroItem heroItem;
    private final DragAndDrop dragAndDrop;
    private final ItemSlotTooltip tooltip;

    private final ItemSlot helmetSlot;
    private final ItemSlot necklaceSlot;
    private final ItemSlot shouldersSlot;
    private final ItemSlot chestSlot;
    private final ItemSlot cloakSlot;
    private final ItemSlot bracersSlot;
    private final ItemSlot glovesSlot;
    private final ItemSlot weaponSlot;
    private final ItemSlot accessorySlot;
    private final ItemSlot ringSlot;
    private final ItemSlot shieldSlot;
    private final ItemSlot beltSlot;
    private final ItemSlot pantsSlot;
    private final ItemSlot bootsSlot;
    private final List<ItemSlot> equipSlotList;

    public EquipSlotsTable(HeroItem heroItem, DragAndDrop dragAndDrop, ItemSlotTooltip tooltip) {
        this.heroItem = heroItem;
        this.dragAndDrop = dragAndDrop;
        this.tooltip = tooltip;
        this.container = new Table();

        this.helmetSlot = createEquipSlot(InventoryGroup.HELMET);
        this.necklaceSlot = createEquipSlot(InventoryGroup.NECKLACE);
        this.shouldersSlot = createEquipSlot(InventoryGroup.SHOULDERS);

        this.chestSlot = createEquipSlot(InventoryGroup.CHEST);
        this.cloakSlot = createEquipSlot(InventoryGroup.CLOAK);

        this.bracersSlot = createEquipSlot(InventoryGroup.BRACERS);
        this.glovesSlot = createEquipSlot(InventoryGroup.GLOVES);
        this.weaponSlot = createEquipSlot(InventoryGroup.WEAPON);

        this.accessorySlot = createEquipSlot(InventoryGroup.ACCESSORY);
        this.ringSlot = createEquipSlot(InventoryGroup.RING);
        this.shieldSlot = createEquipSlot(InventoryGroup.SHIELD);

        this.beltSlot = createEquipSlot(InventoryGroup.BELT);
        this.pantsSlot = createEquipSlot(InventoryGroup.PANTS);
        this.bootsSlot = createEquipSlot(InventoryGroup.BOOTS);

        this.equipSlotList = List.of(helmetSlot, necklaceSlot, shouldersSlot, chestSlot, cloakSlot,
                                     bracersSlot, glovesSlot, weaponSlot, accessorySlot, ringSlot,
                                     shieldSlot, beltSlot, pantsSlot, bootsSlot);

        this.createTable();
    }

    private EquipSlot createEquipSlot(InventoryGroup inventoryGroup) {
        return new EquipSlot(inventoryGroup, heroItem);
    }

    Optional<ItemSlot> getPossibleSlotOfGroup(InventoryGroup inventoryGroup) {
        return equipSlotList.stream()
                            .filter(equipslot -> equipslot.filterGroup.equals(inventoryGroup))
                            .findAny();
    }

    private void createTable() {
        equipSlotList.forEach(equipSlot -> {
            equipSlot.addListener(new ItemSlotTooltipListener(tooltip));
            equipSlot.addListener(new ItemSlotClickListener(DoubleClickHandler::handleEquip));
            dragAndDrop.addTarget(new ItemSlotTarget(equipSlot));
            heroItem.getInventoryItem(equipSlot.filterGroup).ifPresent(addToSlot(equipSlot));
        });
        setDefaults();
        fillTable();
    }

    private void setDefaults() {
        container.defaults().space(EQUIP_SPACING).size(SLOT_SIZE, SLOT_SIZE);
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_SILHOUETTE);
        var sprite = new Sprite(texture);
        var silhouette = new SpriteDrawable(sprite);
        container.setBackground(silhouette);
    }

    private void fillTable() {
        container.add(helmetSlot).colspan(3);

        container.row();

        container.add();
        container.add(necklaceSlot);
        container.add(shouldersSlot).left();

        container.row();

        Table bodySlots = new Table();
        bodySlots.defaults().space(EQUIP_SPACING).size(SLOT_SIZE, SLOT_SIZE);
        bodySlots.add(chestSlot);
        bodySlots.add(cloakSlot);
        container.add(bodySlots).colspan(3);

        container.row();

        container.add(bracersSlot).expandX().left().padLeft(EQUIP_SPACING);
        container.add();
        container.add(accessorySlot).expandX().right().padRight(EQUIP_SPACING);

        container.row();

        container.add(glovesSlot).expandX().left().padLeft(EQUIP_SPACING);
        container.add(beltSlot);
        container.add(ringSlot).expandX().right().padRight(EQUIP_SPACING);

        container.row();

        container.add(weaponSlot).expandX().left().padLeft(EQUIP_SPACING);
        container.add(pantsSlot);
        container.add(shieldSlot).expandX().right().padRight(EQUIP_SPACING);

        container.row();

        container.add().colspan(3);
        container.row();
        container.add().colspan(3);
        container.row();

        container.add(bootsSlot).colspan(3);
    }

    private Consumer<InventoryItem> addToSlot(ItemSlot equipSlot) {
        return inventoryItem -> {
            var inventoryImage = new InventoryImage(inventoryItem);
            equipSlot.addToStack(inventoryImage);
            var itemSlotSource = new ItemSlotSource(inventoryImage, dragAndDrop);
            dragAndDrop.addSource(itemSlotSource);
        };
    }

}
