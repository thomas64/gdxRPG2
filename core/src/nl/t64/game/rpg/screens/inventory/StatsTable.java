package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.StatItem;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.inventory.tooltip.PersonalityTooltip;

import java.util.List;
import java.util.stream.IntStream;


class StatsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 190f;
    private static final float SECOND_COLUMN_WIDTH = 40f;
    private static final float THIRD_COLUMN_WIDTH = 35f;

    StatsTable(PersonalityTooltip tooltip) {
        super(tooltip);
        this.table.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.table.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.table.columnDefaults(2).width(THIRD_COLUMN_WIDTH);

        this.container.add(this.table);
        this.container.setBackground(Utils.createTopBorder());
        this.container.addListener(new ListenerKeyVertical(this::updateIndex));
    }

    @Override
    protected void fillRows() {
        fillStats();
        fillExperience();
    }

    private void updateIndex(int deltaIndex) {
        selectedIndex += deltaIndex;
        if (selectedIndex < 0) {
            selectedIndex = selectedHero.getAllStats().size() - 1;
        } else {
            if (selectedIndex >= selectedHero.getAllStats().size()) {
                selectedIndex = 0;
            }
        }
        hasJustUpdated = true;
    }

    private void fillStats() {
        List<StatItem> statItemList = selectedHero.getAllStats();
        IntStream.range(0, statItemList.size())
                 .forEach(i -> fillRow(statItemList.get(i), i));
    }

    private void fillExperience() {
        table.add("").row();
        fillRow("XP to Invest", selectedHero.getXpToInvest());
        fillRow("Total XP", selectedHero.getTotalXp());
        fillRow("Next Level", selectedHero.getXpNeededForNextLevel());
    }

    private void fillRow(StatItem statItem, int index) {
        var statTitle = new Label(statItem.getName(), new Label.LabelStyle(font, Color.BLACK));
        table.add(statTitle);
        if (table.hasKeyboardFocus() && index == selectedIndex) {
            setSelected(statTitle, statItem);
        }
        table.add(String.valueOf(statItem.getRank()));
        final int totalExtra = selectedHero.getExtraStatForVisualOf(statItem);
        addToTable(totalExtra);
    }

    private void setSelected(Label statTitle, StatItem statItem) {
        statTitle.getStyle().fontColor = Constant.DARK_RED;
        if (hasJustUpdated) {
            hasJustUpdated = false;
            tooltip.setPosition(FIRST_COLUMN_WIDTH / 1.5f, this::getTooltipY);
            tooltip.refresh(statTitle, statItem);
        }
    }

    private void fillRow(String key, int value) {
        table.add(key);
        table.add(String.valueOf(value));
        table.add("").row();
    }

    private float getTooltipY() {
        float rowHeight = table.getRowHeight(0);
        return container.getHeight() - (rowHeight * selectedIndex) - (rowHeight * 0.5f);
    }

}
