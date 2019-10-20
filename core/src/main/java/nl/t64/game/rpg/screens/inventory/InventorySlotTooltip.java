package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.*;

import java.util.Optional;


class InventorySlotTooltip extends BaseToolTip {

    private static final String RIGHT_BORDER = "sprites/tooltip_right.png";
    private static final float COLUMN_SPACING = 20f;
    private static final float HALF_SPACING = 10f;
    private static final String EMPTY_ROW = "";
    private static final String LEFT_TITLE = EMPTY_ROW;
    private static final String RIGHT_TITLE = "Currently Equipped";

    void setVisible(InventorySlot inventorySlot, boolean visible) {
        window.setVisible(visible);
        if (!inventorySlot.hasItem()) {
            window.setVisible(false);
        }
    }

    void updateDescription(InventorySlot inventorySlot) {
        window.clear();
        if (inventorySlot.hasItem()) {
            final InventoryImage hoveredImage = inventorySlot.getCertainInventoryImage();
            final InventoryItem hoveredItem = hoveredImage.inventoryItem;
            final InventoryGroup inventoryGroup = hoveredItem.getGroup();
            final HeroItem selectedHero = InventoryUtils.selectedHero;
            final Optional<String> isAbleToEquip = selectedHero.isAbleToEquip(hoveredItem);
            final Optional<InventoryItem> equippedItem = selectedHero.getInventoryItem(inventoryGroup);

            if (inventorySlot.isOnHero()) {
                createSingleTooltip(hoveredImage);
            } else if (inventoryGroup.equals(InventoryGroup.RESOURCE)) {
                createResourceTooltip(hoveredImage);
            } else if (isAbleToEquip.isEmpty() && equippedItem.isPresent()) {
                createDualTooltip(hoveredImage, new InventoryImage(equippedItem.get()));
            } else {
                createSingleTooltip(hoveredImage);
            }
        }
        window.pack();
    }

    private void createResourceTooltip(InventoryImage inventoryImage) {
        createSingleTooltip(inventoryImage);
        window.add().row();
        window.add(new Label(EMPTY_ROW, new Label.LabelStyle(font, Color.WHITE))).row();
        final String description = inventoryImage.inventoryItem.getDescription();
        final var labelStyle = new Label.LabelStyle(font, Color.WHITE);
        final var label = new Label(description, labelStyle);
        window.add(label);
    }

    private void createSingleTooltip(InventoryImage inventoryImage) {
        final var hoveredTable = new Table();
        hoveredTable.defaults().align(Align.left);
        addAttributesForDescription(inventoryImage, hoveredTable);
        window.add(hoveredTable);
    }

    private void createDualTooltip(InventoryImage hoveredImage, InventoryImage equippedImage) {
        final Table hoveredTable = createLeftTooltip(hoveredImage, equippedImage);
        final Table equippedTable = createRightTooltip(equippedImage);
        window.add(hoveredTable);
        window.add().spaceRight(HALF_SPACING);
        window.add(equippedTable);
    }

    private Table createLeftTooltip(InventoryImage hoveredImage, InventoryImage equippedImage) {
        final var hoveredTable = new Table(window.getSkin());
        setRightBorder(hoveredTable);
        hoveredTable.padRight(HALF_SPACING);
        hoveredTable.defaults().align(Align.left);
        hoveredTable.add(new Label(LEFT_TITLE, new Label.LabelStyle(font, Color.WHITE))).row();
        addAttributesForDualDescription(hoveredImage, equippedImage, hoveredTable);
        return hoveredTable;
    }

    private Table createRightTooltip(InventoryImage equippedImage) {
        final var equippedTable = new Table();
        equippedTable.defaults().align(Align.left);
        equippedTable.add(new Label(RIGHT_TITLE, new Label.LabelStyle(font, Color.LIGHT_GRAY))).row();
        addAttributesForDescription(equippedImage, equippedTable);
        return equippedTable;
    }

    private void addAttributesForDescription(InventoryImage inventoryImage, Table hoveredTable) {
        for (InventoryDescription attribute : inventoryImage.getDescription()) {
            final var labelStyle = createLabelStyle(attribute);
            hoveredTable.add(new Label(getKey(attribute), labelStyle)).spaceRight(COLUMN_SPACING);
            hoveredTable.add(new Label(getValue(attribute), labelStyle)).row();
        }
    }

    private void addAttributesForDualDescription(InventoryImage item1, InventoryImage item2, Table hoveredTable) {
        for (InventoryDescription attribute : item1.getDualDescription(item2)) {
            final var labelStyle = createDualLabelStyle(attribute);
            hoveredTable.add(new Label(getKey(attribute), labelStyle)).spaceRight(COLUMN_SPACING);
            hoveredTable.add(new Label(getValue(attribute), labelStyle)).row();
        }
    }

    private Label.LabelStyle createLabelStyle(InventoryDescription attribute) {
        switch (attribute.getCompare()) {
            case SAME:
            case MORE:
                return new Label.LabelStyle(font, Color.WHITE);
            case LESS:
                return new Label.LabelStyle(font, Color.RED);
            default:
                throw new IllegalArgumentException(String.format("Attribute '%s' not usable.", attribute));
        }
    }

    private Label.LabelStyle createDualLabelStyle(InventoryDescription attribute) {
        switch (attribute.getCompare()) {
            case SAME:
                return new Label.LabelStyle(font, Color.WHITE);
            case LESS:
                return new Label.LabelStyle(font, Color.RED);
            case MORE:
                return new Label.LabelStyle(font, Color.GREEN);
            default:
                throw new IllegalArgumentException(String.format("Attribute '%s' not usable.", attribute));
        }
    }

    private String getKey(InventoryDescription attribute) {
        return ((SuperEnum) attribute.getKey()).getTitle();
    }

    private String getValue(InventoryDescription attribute) {
        if (attribute.getValue() instanceof SkillType) {
            return ((SkillType) attribute.getValue()).getTitle();
        } else if (attribute.getKey().equals(CalcType.BASE_HIT)) {
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

}
