package nl.t64.game.rpg.screens.inventory.equipslot;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;

import java.util.List;
import java.util.Optional;


public class EquipSlotsTable {

    private static final String SPRITE_SILHOUETTE = "sprites/silhouette.png";
    private static final float SLOT_SIZE = 64f;
    private static final float EQUIP_SPACING = 10f;

    final Table container;
    private final HeroItem heroItem;
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

    private final EquipSlotSelector selector;
    private final EquipSlotTaker taker;

    EquipSlotsTable(HeroItem heroItem, ItemSlotTooltip tooltip) {
        this.heroItem = heroItem;
        this.tooltip = tooltip;
        this.container = new Table();

        this.helmetSlot = createEquipSlot(0, InventoryGroup.HELMET);
        this.necklaceSlot = createEquipSlot(1, InventoryGroup.NECKLACE);
        this.shouldersSlot = createEquipSlot(21, InventoryGroup.SHOULDERS);

        this.chestSlot = createEquipSlot(2, InventoryGroup.CHEST);
        this.cloakSlot = createEquipSlot(22, InventoryGroup.CLOAK);

        this.bracersSlot = createEquipSlot(3, InventoryGroup.BRACERS);
        this.glovesSlot = createEquipSlot(4, InventoryGroup.GLOVES);
        this.weaponSlot = createEquipSlot(5, InventoryGroup.WEAPON);

        this.accessorySlot = createEquipSlot(23, InventoryGroup.ACCESSORY);
        this.ringSlot = createEquipSlot(24, InventoryGroup.RING);
        this.shieldSlot = createEquipSlot(25, InventoryGroup.SHIELD);

        this.beltSlot = createEquipSlot(14, InventoryGroup.BELT);
        this.pantsSlot = createEquipSlot(15, InventoryGroup.PANTS);
        this.bootsSlot = createEquipSlot(16, InventoryGroup.BOOTS);

        this.equipSlotList = List.of(helmetSlot, necklaceSlot, shouldersSlot, chestSlot, cloakSlot,
                                     bracersSlot, glovesSlot, weaponSlot, accessorySlot, ringSlot,
                                     shieldSlot, beltSlot, pantsSlot, bootsSlot);

        this.createTable();

        this.selector = new EquipSlotSelector(this.equipSlotList);
        this.taker = new EquipSlotTaker(this.selector);
        this.container.addListener(new EquipSlotsTableListener(this::dequipItem, selector::trySelectNewSlot));
    }

    private EquipSlot createEquipSlot(int index, InventoryGroup inventoryGroup) {
        return new EquipSlot(index, inventoryGroup, tooltip, heroItem);
    }

    private void dequipItem() {
        taker.dequip(selector.getCurrentSlot());
    }

    int getIndexOfCurrentSlot() {
        return selector.getIndex();
    }

    ItemSlot getCurrentSlot() {
        return selector.getCurrentSlot();
    }

    void deselectCurrentSlot() {
        selector.deselectCurrentSlot();
    }

    void selectCurrentSlot() {
        selector.selectCurrentSlot();
    }

    void setCurrentByIndex(int index) {
        selector.setNewCurrentByIndex(index);
    }

    public Optional<ItemSlot> getPossibleSlotOfGroup(InventoryGroup inventoryGroup) {
        return equipSlotList.stream()
                            .filter(equipSlot -> equipSlot.filterGroup.equals(inventoryGroup))
                            .findAny();
    }

    private void createTable() {
        equipSlotList.forEach(this::addPossibleEquippedItemToEquipSlot);
        setDefaults();
        fillTable();
    }

    private void addPossibleEquippedItemToEquipSlot(ItemSlot equipSlot) {
        heroItem.getInventoryItem(equipSlot.filterGroup)
                .ifPresent(inventoryItem -> equipSlot.addToStack(new InventoryImage(inventoryItem)));
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

}
