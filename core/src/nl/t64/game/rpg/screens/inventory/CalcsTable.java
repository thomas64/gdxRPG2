package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.CalcAttributeId;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.inventory.tooltip.PersonalityTooltip;


class CalcsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 190f;
    private static final float SECOND_COLUMN_WIDTH = 40f;
    private static final float THIRD_COLUMN_WIDTH = 35f;

    CalcsTable(PersonalityTooltip tooltip) {
        super(tooltip);
        this.table.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.table.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.table.columnDefaults(2).width(THIRD_COLUMN_WIDTH);

        this.container.add(this.table);
        this.container.setBackground(Utils.createTopBorder());
        this.container.addListener(new ListenerKeyVertical(this::updateIndex));
    }

    private void updateIndex(int deltaIndex) {
        selectedIndex += deltaIndex;
        if (selectedIndex < 0) {
            selectedIndex = table.getRows() - 1;
        } else {
            if (selectedIndex >= table.getRows()) {
                selectedIndex = 0;
            }
        }
        hasJustUpdated = true;
    }

    @Override
    protected void fillRows() {
        table.add(new Label("Weight", createLabelStyle()));
        table.add("?");
        table.add("").row();

        table.add(new Label("Movepoints", createLabelStyle()));
        table.add("?");
        table.add("").row();

        table.add(new Label(CalcAttributeId.BASE_HIT.getTitle(), createLabelStyle()));
        table.add(String.format("%s%%", selectedHero.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)));
        table.add("").row();

        table.add(new Label(CalcAttributeId.DAMAGE.getTitle(), createLabelStyle()));
        table.add(String.valueOf(selectedHero.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)));
        table.add("").row();

        table.add(new Label(String.format("Total %s", CalcAttributeId.PROTECTION.getTitle()), createLabelStyle()));
        table.add(String.valueOf(selectedHero.getTotalCalcOf(CalcAttributeId.PROTECTION)));
        table.add("").row();

        table.add(new Label(String.format("Shield %s", CalcAttributeId.PROTECTION.getTitle()), createLabelStyle()));
        table.add(String.valueOf(selectedHero.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.PROTECTION)));
        table.add("").row();

        table.add(new Label(CalcAttributeId.DEFENSE.getTitle(), createLabelStyle()));
        table.add(String.valueOf(selectedHero.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)));
        table.add("").row();

        table.add(new Label("Spell Battery", createLabelStyle()));
        table.add("?");
        table.add("").row();

        if (table.hasKeyboardFocus()) {
            setSelected();
        }
    }

    private void setSelected() {
        Label child = (Label) table.getChild(selectedIndex * 3);
        child.getStyle().fontColor = Constant.DARK_RED;
        if (hasJustUpdated) {
            hasJustUpdated = false;
            tooltip.setPosition(FIRST_COLUMN_WIDTH / 1.5f, this::getTooltipY);
            tooltip.refresh(child, x -> "ToDo");
        }
    }

    private Label.LabelStyle createLabelStyle() {
        return new Label.LabelStyle(font, Color.BLACK);
    }

    private float getTooltipY() {
        float rowHeight = table.getRowHeight(0);
        return container.getHeight() - (rowHeight * selectedIndex) - (rowHeight * 0.5f);
    }

}
