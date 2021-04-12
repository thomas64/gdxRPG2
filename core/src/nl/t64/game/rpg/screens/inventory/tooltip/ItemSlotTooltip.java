package nl.t64.game.rpg.screens.inventory.tooltip;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.*;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.inventory.InventoryUtils;
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;

import java.util.List;
import java.util.Optional;


public class ItemSlotTooltip extends BaseTooltip {

    private static final float SLOT_SIZE = 64f;
    private static final float THREE_QUARTERS = SLOT_SIZE * 0.75f;
    private static final String RIGHT_BORDER = "sprites/tooltip_right.png";
    private static final float COLUMN_SPACING = 20f;
    private static final float HALF_SPACING = 10f;
    private static final String EMPTY_ROW = "";
    private static final String LEFT_TITLE = EMPTY_ROW;
    private static final String RIGHT_TITLE = "Currently Equipped";
    private static final Color ORANGE = new Color(0xFF9000FF);
    private static final float DELAY = 0.5f;

    @Override
    public void toggle(ItemSlot itemSlot) {
        if (itemSlot.hasItem()) {
            boolean isEnabled = Utils.getGameData().isTooltipEnabled();
            Utils.getGameData().setTooltipEnabled(!isEnabled);
            setupTooltip(itemSlot);
            window.setVisible(!isEnabled);
        }
    }

    @Override
    public void toggleCompare(ItemSlot itemSlot) {
        if (itemSlot.hasItem() && !window.hasActions()) {
            boolean isEnabled = Utils.getGameData().isComparingEnabled();
            Utils.getGameData().setComparingEnabled(!isEnabled);
            updateDescription(itemSlot);
        }
    }

    public void refresh(ItemSlot itemSlot) {
        hide();
        if (Utils.getGameData().isTooltipEnabled() && itemSlot.hasItem()) {
            setupTooltip(itemSlot);
            window.addAction(Actions.sequence(Actions.delay(DELAY),
                                              Actions.show()));
        }
    }

    private void setupTooltip(ItemSlot itemSlot) {
        var localCoords = new Vector2(itemSlot.getOriginX(), itemSlot.getOriginY());
        itemSlot.localToStageCoordinates(localCoords);
        updateDescription(itemSlot);
        window.setPosition(localCoords.x + THREE_QUARTERS, localCoords.y + THREE_QUARTERS);
        window.toFront();
    }

    void updateDescription(ItemSlot itemSlot) {
        window.clear();
        if (itemSlot.hasItem()) {
            final InventoryImage hoveredImage = itemSlot.getCertainInventoryImage();
            final InventoryItem hoveredItem = hoveredImage.inventoryItem;
            final InventoryGroup inventoryGroup = hoveredItem.getGroup();
            final HeroItem selectedHero = InventoryUtils.getSelectedHero();
            final Optional<String> errorMessage = selectedHero.createMessageIfHeroHasNotEnoughFor(hoveredItem);
            final Optional<InventoryItem> equippedItem = selectedHero.getInventoryItem(inventoryGroup);

            if (itemSlot.isOnHero()) {
                createSingleTooltip(hoveredImage);
            } else if (inventoryGroup.equals(InventoryGroup.RESOURCE)
                       || inventoryGroup.equals(InventoryGroup.POTION)
                       || inventoryGroup.equals(InventoryGroup.ITEM)) {
                createResourceTooltip(hoveredImage);
            } else if (errorMessage.isEmpty()
                       && equippedItem.isPresent()
                       && Utils.getGameData().isComparingEnabled()) {
                createDualTooltip(hoveredImage, new InventoryImage(equippedItem.get()));
            } else {
                createSingleTooltip(hoveredImage);
            }
        }
        window.pack();
    }

    void createResourceTooltip(InventoryImage inventoryImage) {
        Table hoveredTable = createDefaultTooltip(inventoryImage);
        window.add(hoveredTable);

        window.add().row();
        window.add(createLabel(EMPTY_ROW, Color.WHITE)).row();
        String description = String.join(System.lineSeparator(), inventoryImage.inventoryItem.getDescription());
        window.add(createLabel(description, Color.WHITE));
    }

    void createSingleTooltip(InventoryImage inventoryImage) {
        Table hoveredTable = createDefaultTooltip(inventoryImage);
        addPossibleDescription(inventoryImage, hoveredTable);
        window.add(hoveredTable);
    }

    private Table createDefaultTooltip(InventoryImage inventoryImage) {
        final var hoveredTable = new Table();
        hoveredTable.defaults().align(Align.left);

        final int totalMerchant = Utils.getGameData().getParty().getSumOfSkill(SkillItemId.MERCHANT);
        List<InventoryDescription> descriptionList = inventoryImage.getSingleDescription(totalMerchant);
        removeLeftUnnecessaryAttributes(descriptionList);
        descriptionList.forEach(attribute -> addToTable(hoveredTable, attribute, createSingleLabelStyle(attribute)));
        return hoveredTable;
    }

    private void createDualTooltip(InventoryImage hoveredImage, InventoryImage equippedImage) {
        final Table hoveredTable = createLeftTooltip(hoveredImage, equippedImage);
        final Table equippedTable = createRightTooltip(equippedImage, hoveredImage);
        window.add(hoveredTable);
        window.add().spaceRight(HALF_SPACING);
        window.add(equippedTable);
    }

    private Table createLeftTooltip(InventoryImage hoveredImage, InventoryImage equippedImage) {
        final var hoveredTable = new Table(window.getSkin());
        setRightBorder(hoveredTable);
        hoveredTable.padRight(HALF_SPACING);
        hoveredTable.defaults().align(Align.left);
        hoveredTable.add(createLabel(LEFT_TITLE, Color.WHITE)).row();

        final int totalMerchant = Utils.getGameData().getParty().getSumOfSkill(SkillItemId.MERCHANT);
        List<InventoryDescription> descriptionList = hoveredImage.getDualDescription(equippedImage, totalMerchant);
        removeLeftUnnecessaryAttributes(descriptionList);
        descriptionList.forEach(attribute -> addToTable(hoveredTable, attribute, createLeftLabelStyle(attribute)));
        addPossibleDescription(hoveredImage, hoveredTable);
        return hoveredTable;
    }

    private Table createRightTooltip(InventoryImage equippedImage, InventoryImage hoveredImage) {
        final var equippedTable = new Table();
        equippedTable.defaults().align(Align.left);
        equippedTable.add(createLabel(RIGHT_TITLE, Color.LIGHT_GRAY)).row();

        final int totalMerchant = Utils.getGameData().getParty().getSumOfSkill(SkillItemId.MERCHANT);
        List<InventoryDescription> descriptionList = equippedImage.getDualDescription(hoveredImage, totalMerchant);
        removeRightUnnecessaryAttributes(descriptionList);
        descriptionList.forEach(attribute -> addToTable(equippedTable, attribute, createRightLabelStyle(attribute)));
        addPossibleDescription(equippedImage, equippedTable);
        return equippedTable;
    }

    void addPossibleDescription(InventoryImage inventoryImage, Table table) {
        if (inventoryImage.inventoryItem.getDescription() != null) {
            String description = String.join(System.lineSeparator(), inventoryImage.inventoryItem.getDescription());
            table.add(createLabel(description, Color.VIOLET)).colspan(2);
        }
    }

    void addToTable(Table hoveredTable, InventoryDescription attribute, Label.LabelStyle labelStyle) {
        hoveredTable.add(new Label(getKey(attribute), labelStyle)).spaceRight(COLUMN_SPACING);
        hoveredTable.add(new Label(getValue(attribute), labelStyle)).row();
    }

    void removeLeftUnnecessaryAttributes(List<InventoryDescription> descriptionList) {
        removeBuy(descriptionList);
        removeSell(descriptionList);
    }

    void removeRightUnnecessaryAttributes(List<InventoryDescription> descriptionList) {
        removeBuy(descriptionList);
        removeSell(descriptionList);
    }

    void removeBuy(List<InventoryDescription> descriptionList) {
        descriptionList.removeIf(attribute -> attribute.getKey().equals(Constant.DESCRIPTION_KEY_BUY));
        descriptionList.removeIf(attribute -> attribute.getKey().equals(Constant.DESCRIPTION_KEY_BUY_PIECE));
        descriptionList.removeIf(attribute -> attribute.getKey().equals(Constant.DESCRIPTION_KEY_BUY_TOTAL));
    }

    void removeSell(List<InventoryDescription> descriptionList) {
        descriptionList.removeIf(attribute -> attribute.getKey().equals(Constant.DESCRIPTION_KEY_SELL));
        descriptionList.removeIf(attribute -> attribute.getKey().equals(Constant.DESCRIPTION_KEY_SELL_PIECE));
        descriptionList.removeIf(attribute -> attribute.getKey().equals(Constant.DESCRIPTION_KEY_SELL_TOTAL));
    }

    Label.LabelStyle createSingleLabelStyle(InventoryDescription attribute) {
        if (isBuyOrSellValue(attribute)) {
            return createLabelStyle(Color.GOLD);
        }
        return switch (attribute.getCompare()) {
            case SAME, MORE -> createLabelStyle(Color.WHITE);
            case LESS -> createLabelStyle(Color.RED);
        };
    }

    private Label.LabelStyle createLeftLabelStyle(InventoryDescription attribute) {
        if (isBuyOrSellValue(attribute)) {
            return createLabelStyle(Color.GOLD);
        }
        return switch (attribute.getCompare()) {
            case SAME -> createLabelStyle(Color.WHITE);
            case LESS -> createLabelStyle(ORANGE);
            case MORE -> createLabelStyle(Color.LIME);
        };
    }

    private Label.LabelStyle createRightLabelStyle(InventoryDescription attribute) {
        if (isBuyOrSellValue(attribute)) {
            return createLabelStyle(Color.GOLD);
        }
        return createLabelStyle(Color.WHITE);
    }

    private String getKey(InventoryDescription attribute) {
        if (attribute.getKey() instanceof SuperEnum attributeKey) {
            return attributeKey.getTitle();
        } else {
            return String.valueOf(attribute.getKey());
        }
    }

    private String getValue(InventoryDescription attribute) {
        if (attribute.getValue() instanceof SkillItemId attributeValue) {
            return attributeValue.getTitle();
        } else if (attribute.getKey().equals(CalcAttributeId.BASE_HIT)) {
            return String.format("%s%%", attribute.getValue());
        } else {
            return String.valueOf(attribute.getValue());
        }
    }

    private void setRightBorder(Table table) {
        final var texture = Utils.getResourceManager().getTextureAsset(RIGHT_BORDER);
        final var ninepatch = new NinePatch(texture, 0, 1, 0, 0);
        final var drawable = new NinePatchDrawable(ninepatch);
        table.setBackground(drawable);
    }

    private Label createLabel(String text, Color color) {
        return new Label(text, createLabelStyle(color));
    }

    private Label.LabelStyle createLabelStyle(Color color) {
        return new Label.LabelStyle(new BitmapFont(), color);
    }

    private boolean isBuyOrSellValue(InventoryDescription attribute) {
        return attribute.getKey().equals(Constant.DESCRIPTION_KEY_BUY_TOTAL)
               || attribute.getKey().equals(Constant.DESCRIPTION_KEY_SELL_TOTAL)
               || attribute.getKey().equals(Constant.DESCRIPTION_KEY_BUY_PIECE)
               || attribute.getKey().equals(Constant.DESCRIPTION_KEY_SELL_PIECE)
               || attribute.getKey().equals(Constant.DESCRIPTION_KEY_BUY)
               || attribute.getKey().equals(Constant.DESCRIPTION_KEY_SELL);
    }

}
