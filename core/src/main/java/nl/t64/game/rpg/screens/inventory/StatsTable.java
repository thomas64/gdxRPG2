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
        setTopBorder(this.table);
    }

    void render() {
        selectedHero = InventoryUtils.selectedHero;
        hoveredItem = InventoryUtils.hoveredItem;
        table.clear();
        fillRows();
    }

    private void fillRows() {
        fillStats();
        fillExperience();
    }

    private void fillStats() {
        for (StatType statType : StatType.values()) {
            ownStat = selectedHero.getOwnStatOf(statType);
            totalBonus = selectedHero.getExtraStatForVisualOf(statType);
            difference = selectedHero.getPreviewStatForVisualOf(statType, hoveredItem);
            fillRow(statType);
        }
    }

    private void fillExperience() {
        table.add("").row();
        fillRow("XP Remaining", selectedHero.getXpRemaining());
        fillRow("Total XP", selectedHero.getTotalXp());
        fillRow("Next Level", selectedHero.getNeededXpForNextLevel()); // todo, is dit de juiste van de 2 methodes?
    }

    private void fillRow(StatType statType) {
        table.add(statType.getTitle());
        table.add(String.valueOf(ownStat));
        createBonusFromInventory(totalBonus);
        createPreview(difference);
    }

    private void fillRow(String key, int value) {
        table.add(key);
        table.add(String.valueOf(value));
        table.add("");
        table.add("").row();
    }

}
