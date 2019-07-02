package nl.t64.game.rpg.screens.inventory;

import nl.t64.game.rpg.components.party.CalcType;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryItem;

import static nl.t64.game.rpg.components.party.InventoryGroup.SHIELD;
import static nl.t64.game.rpg.components.party.InventoryGroup.WEAPON;


class CalcsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 190f;
    private static final float SECOND_COLUMN_WIDTH = 40f;
    private static final float THIRD_COLUMN_WIDTH = 30f;
    private static final float FOURTH_COLUMN_WIDTH = 30f;

    CalcsTable() {
        this.table.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.table.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.table.columnDefaults(2).width(THIRD_COLUMN_WIDTH);
        this.table.columnDefaults(3).width(FOURTH_COLUMN_WIDTH);
    }

    void render() {
        HeroItem selectedHero = InventoryUtils.selectedHero;
        InventoryItem hoveredItem = InventoryUtils.hoveredItem;
        table.clear();

        table.add("Weight");
        table.add("?");
        table.add("");
        table.add("").row();

        table.add("Movepoints");
        table.add("?");
        table.add("");
        table.add("").row();

        table.add(CalcType.BASE_HIT.getTitle());
        table.add(String.format("%s%%", selectedHero.getCalcValueOf(WEAPON, CalcType.BASE_HIT)));
        table.add("");
        createPreview(selectedHero.getPreviewCalcForVisualOf(CalcType.BASE_HIT, hoveredItem));

        table.add(CalcType.DAMAGE.getTitle());
        table.add(String.valueOf(selectedHero.getCalcValueOf(WEAPON, CalcType.DAMAGE)));
        table.add("");
        createPreview(selectedHero.getPreviewCalcForVisualOf(CalcType.BASE_HIT, hoveredItem));

        table.add(String.format("Total %s", CalcType.PROTECTION.getTitle()));
        table.add(String.valueOf(selectedHero.getTotalCalcOf(CalcType.PROTECTION)));
        table.add("");
        createPreview(selectedHero.getPreviewCalcForVisualOf(CalcType.PROTECTION, hoveredItem));

        table.add(String.format("Shield %s", CalcType.PROTECTION.getTitle()));
        table.add(String.valueOf(selectedHero.getCalcValueOf(SHIELD, CalcType.PROTECTION)));
        table.add("");
        createShieldPreview(CalcType.PROTECTION);

        table.add(CalcType.DEFENSE.getTitle());
        table.add(String.valueOf(selectedHero.getCalcValueOf(SHIELD, CalcType.DEFENSE)));
        table.add("");
        createPreview(selectedHero.getPreviewCalcForVisualOf(CalcType.DEFENSE, hoveredItem));

        table.add("Spell Battery");
        table.add("?");
        table.add("");
        table.add("").row();
    }

    private void createShieldPreview(CalcType calcType) {
        HeroItem selectedHero = InventoryUtils.selectedHero;
        InventoryItem hoveredItem = InventoryUtils.hoveredItem;

        if (hoveredItem == null) {
            table.add("").row();
        } else if (hoveredItem.getGroup().equals(SHIELD)) {
            createPreview(selectedHero.getPreviewCalcForVisualOf(calcType, hoveredItem));
        } else {
            table.add("").row();
        }
    }

}
