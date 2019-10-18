package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.StatType;


class StatsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 190f;
    private static final float SECOND_COLUMN_WIDTH = 40f;
    private static final float THIRD_COLUMN_WIDTH = 35f;

    private final StatTooltip tooltip;

    private HeroItem selectedHero;
    private int statRank;
    private int totalBonus;

    StatsTable(StatTooltip tooltip) {
        this.tooltip = tooltip;
        this.table.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.table.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.table.columnDefaults(2).width(THIRD_COLUMN_WIDTH);
        setTopBorder(this.table);
    }

    void update() {
        selectedHero = InventoryUtils.selectedHero;
        table.clear();
        fillRows();
    }

    private void fillRows() {
        fillStats();
        fillExperience();
    }

    private void fillStats() {
        for (StatType statType : StatType.values()) {
            statRank = selectedHero.getStatRankOf(statType);
            totalBonus = selectedHero.getExtraStatForVisualOf(statType);
            fillRow(statType);
        }
    }

    private void fillExperience() {
        table.add("").row();
        fillRow("XP to Invest", selectedHero.getXpToInvest());
        fillRow("Total XP", selectedHero.getTotalXp());
        fillRow("Next Level", selectedHero.getXpNeededForNextLevel()); // todo, is dit de juiste van de 2 methodes?
    }

    private void fillRow(StatType statType) {
        var statTitle = new Label(statType.getTitle(), table.getSkin());
        statTitle.addListener(new StatTooltipListener(tooltip, statType));
        table.add(statTitle);
        table.add(String.valueOf(statRank));
        createBonusFromInventory(totalBonus);
    }

    private void fillRow(String key, int value) {
        table.add(key);
        table.add(String.valueOf(value));
        table.add("").row();
    }

}
