package nl.t64.game.rpg.screens.inventory;

import nl.t64.game.rpg.components.party.CalcType;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryGroup;


class CalcsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 190f;
    private static final float SECOND_COLUMN_WIDTH = 40f;
    private static final float THIRD_COLUMN_WIDTH = 35f;

    CalcsTable() {
        this.table.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.table.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.table.columnDefaults(2).width(THIRD_COLUMN_WIDTH);
        setTopBorder(this.table);
    }

    void update() {
        HeroItem selectedHero = InventoryUtils.selectedHero;
        table.clear();

        table.add("Weight");
        table.add("?");
        table.add("").row();

        table.add("Movepoints");
        table.add("?");
        table.add("").row();

        table.add(CalcType.BASE_HIT.getTitle());
        table.add(String.format("%s%%", selectedHero.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)));
        table.add("").row();

        table.add(CalcType.DAMAGE.getTitle());
        table.add(String.valueOf(selectedHero.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)));
        table.add("").row();

        table.add(String.format("Total %s", CalcType.PROTECTION.getTitle()));
        table.add(String.valueOf(selectedHero.getTotalCalcOf(CalcType.PROTECTION)));
        table.add("").row();

        table.add(String.format("Shield %s", CalcType.PROTECTION.getTitle()));
        table.add(String.valueOf(selectedHero.getCalcValueOf(InventoryGroup.SHIELD, CalcType.PROTECTION)));
        table.add("").row();

        table.add(CalcType.DEFENSE.getTitle());
        table.add(String.valueOf(selectedHero.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)));
        table.add("").row();

        table.add("Spell Battery");
        table.add("?");
        table.add("").row();
    }

}
