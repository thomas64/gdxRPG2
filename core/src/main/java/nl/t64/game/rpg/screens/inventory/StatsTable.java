package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.StatItemId;


class StatsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 190f;
    private static final float SECOND_COLUMN_WIDTH = 40f;
    private static final float THIRD_COLUMN_WIDTH = 35f;

    private final StatTooltip tooltip;

    private HeroItem selectedHero;
    private int statRank;
    private int totalExtra;

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
        for (StatItemId statItemId : StatItemId.values()) {
            statRank = selectedHero.getStatRankOf(statItemId);
            totalExtra = selectedHero.getExtraStatForVisualOf(statItemId);
            fillRow(statItemId);
        }
    }

    private void fillExperience() {
        table.add("").row();
        fillRow("XP to Invest", selectedHero.getXpToInvest());
        fillRow("Total XP", selectedHero.getTotalXp());
        fillRow("Next Level", selectedHero.getXpNeededForNextLevel()); // todo, is dit de juiste van de 2 methodes?
    }

    private void fillRow(StatItemId statItemId) {
        var statTitle = new Label(statItemId.getTitle(), table.getSkin());
        statTitle.addListener(new StatTooltipListener(tooltip, statItemId));
        table.add(statTitle);
        table.add(String.valueOf(statRank));
        addToTable(totalExtra);
    }

    private void fillRow(String key, int value) {
        table.add(key);
        table.add(String.valueOf(value));
        table.add("").row();
    }

}
