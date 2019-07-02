package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.components.party.SkillType;


class SkillsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 32f;
    private static final float SECOND_COLUMN_WIDTH = 190f;
    private static final float THIRD_COLUMN_WIDTH = 40f;
    private static final float FOURTH_COLUMN_WIDTH = 30f;
    private static final float FIFTH_COLUMN_WIDTH = 30f;
    private static final float TABLE_HEIGHT = 705f;

    private HeroItem selectedHero;
    private InventoryItem hoveredItem;
    private int ownSkill;
    private int totalBonus;
    private int difference;

    SkillsTable() {
        this.table.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.table.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.table.columnDefaults(2).width(THIRD_COLUMN_WIDTH);
        this.table.columnDefaults(3).width(FOURTH_COLUMN_WIDTH);
        this.table.columnDefaults(4).width(FIFTH_COLUMN_WIDTH);
        this.table.getBackground().setMinHeight(TABLE_HEIGHT);
        this.table.align(Align.left).top();
    }

    void render() {
        selectedHero = InventoryUtils.selectedHero;
        hoveredItem = InventoryUtils.hoveredItem;
        table.clear();
        fillRows();
    }

    private void fillRows() {
        for (SkillType skillType : selectedHero.getAllSkillsAboveZero()) {
            ownSkill = selectedHero.getOwnSkillOf(skillType);
            totalBonus = selectedHero.getExtraSkillForVisualOf(skillType);
            difference = selectedHero.getPreviewSkillForVisualOf(skillType, hoveredItem);
            fillRow(skillType);
        }
    }

    private void fillRow(SkillType skillType) {
        table.add("");
        table.add(skillType.getTitle());
        table.add(String.valueOf(ownSkill));
        createBonusFromInventory(totalBonus);
        createPreview(difference);
    }

}
