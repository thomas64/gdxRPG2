package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.StatItem;
import nl.t64.game.rpg.screens.inventory.tooltip.PersonalityTooltip;
import nl.t64.game.rpg.screens.inventory.tooltip.PersonalityTooltipListener;


class StatsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 190f;
    private static final float SECOND_COLUMN_WIDTH = 40f;
    private static final float THIRD_COLUMN_WIDTH = 35f;

    private final PersonalityTooltip tooltip;

    private HeroItem selectedHero;

    StatsTable(PersonalityTooltip tooltip) {
        this.tooltip = tooltip;
        this.table.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.table.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.table.columnDefaults(2).width(THIRD_COLUMN_WIDTH);
        this.table.setBackground(Utils.createTopBorder());
    }

    @Override
    protected void update() {
        selectedHero = InventoryUtils.getSelectedHero();
        table.clear();
        fillRows();
    }

    private void fillRows() {
        fillStats();
        fillExperience();
    }

    private void fillStats() {
        for (StatItem statItem : selectedHero.getAllStats()) {
            fillRow(statItem);
        }
    }

    private void fillExperience() {
        table.add("").row();
        fillRow("XP to Invest", selectedHero.getXpToInvest());
        fillRow("Total XP", selectedHero.getTotalXp());
        fillRow("Next Level", selectedHero.getXpNeededForNextLevel()); // todo, is dit de juiste van de 2 methodes?
    }

    private void fillRow(StatItem statItem) {
        var statTitle = new Label(statItem.getName(), table.getSkin());
        statTitle.addListener(new PersonalityTooltipListener(tooltip, statItem));
        table.add(statTitle);
        table.add(String.valueOf(statItem.getRank()));
        final int totalExtra = selectedHero.getExtraStatForVisualOf(statItem);
        addToTable(totalExtra);
    }

    private void fillRow(String key, int value) {
        table.add(key);
        table.add(String.valueOf(value));
        table.add("").row();
    }

}
