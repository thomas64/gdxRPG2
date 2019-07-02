package nl.t64.game.rpg.screens.inventory;

import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.components.party.StatType;


class StatsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 190f;
    private static final float SECOND_COLUMN_WIDTH = 40f;
    private static final float THIRD_COLUMN_WIDTH = 30f;
    private static final float FOURTH_COLUMN_WIDTH = 30f;

    private HeroItem selectedHero;
    private InventoryItem hoveredItem;
    private int ownStat;
    private int totalBonus;
    private int difference;

    StatsTable() {
        this.table.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.table.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.table.columnDefaults(2).width(THIRD_COLUMN_WIDTH);
        this.table.columnDefaults(3).width(FOURTH_COLUMN_WIDTH);
    }

    void render() {
        selectedHero = InventoryUtils.selectedHero;
        hoveredItem = InventoryUtils.hoveredItem;
        table.clear();
        fillRows();
    }

    private void fillRows() {
        for (StatType statType : StatType.values()) {
            ownStat = selectedHero.getOwnStatOf(statType);
            totalBonus = selectedHero.getExtraStatForVisualOf(statType);
            difference = selectedHero.getPreviewStatForVisualOf(statType, hoveredItem);
            fillRow(statType);
        }

        table.add("").row();
        table.add("XP Remaining");
        table.add(String.valueOf(selectedHero.getXpRemaining()));
        table.add("");
        table.add("").row();
        table.add("Total XP");
        table.add(String.valueOf(selectedHero.getTotalXp()));
        table.add("");
        table.add("").row();
        table.add("Next Level");
        table.add(String.valueOf(selectedHero.getNeededXpForNextLevel())); // todo, is dit de juiste van de 2 methodes?
        table.add("");
        table.add("");
    }

    private void fillRow(StatType statType) {
        table.add(statType.getTitle());
        table.add(String.valueOf(ownStat));
        createBonusFromInventory(totalBonus);
        createPreview(difference);
    }

}
