package nl.t64.game.rpg.screens.inventory;

import nl.t64.game.rpg.components.party.CalcAttributeId;
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

    @Override
    protected void update() {
        HeroItem selectedHero = InventoryUtils.getSelectedHero();
        table.clear();

        table.add("Weight");
        table.add("?");
        table.add("").row();

        table.add("Movepoints");
        table.add("?");
        table.add("").row();

        table.add(CalcAttributeId.BASE_HIT.getTitle());
        table.add(String.format("%s%%", selectedHero.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)));
        table.add("").row();

        table.add(CalcAttributeId.DAMAGE.getTitle());
        table.add(String.valueOf(selectedHero.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)));
        table.add("").row();

        table.add(String.format("Total %s", CalcAttributeId.PROTECTION.getTitle()));
        table.add(String.valueOf(selectedHero.getTotalCalcOf(CalcAttributeId.PROTECTION)));
        table.add("").row();

        table.add(String.format("Shield %s", CalcAttributeId.PROTECTION.getTitle()));
        table.add(String.valueOf(selectedHero.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.PROTECTION)));
        table.add("").row();

        table.add(CalcAttributeId.DEFENSE.getTitle());
        table.add(String.valueOf(selectedHero.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)));
        table.add("").row();

        table.add("Spell Battery");
        table.add("?");
        table.add("").row();
    }

}
