package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.SkillItem;


class SkillsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 48f;
    private static final float SECOND_COLUMN_WIDTH = 136f;
    private static final float THIRD_COLUMN_WIDTH = 40f;
    private static final float FOURTH_COLUMN_WIDTH = 35f;
    private static final float CONTAINER_HEIGHT = 704f;
    private static final float ROW_HEIGHT = 48f;
    private static final float SECOND_COLUMN_PAD_LEFT = 15f;

    final Table container;
    final ScrollPane scrollPane;

    private final StatTooltip tooltip;

    private HeroItem selectedHero;

    SkillsTable(StatTooltip tooltip) {
        this.tooltip = tooltip;
        this.table.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.table.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.table.columnDefaults(2).width(THIRD_COLUMN_WIDTH);
        this.table.columnDefaults(3).width(FOURTH_COLUMN_WIDTH);
        this.table.top();
        this.table.defaults().height(ROW_HEIGHT);

        this.scrollPane = new ScrollPane(this.table);
        this.container = new Table();
        this.container.add(this.scrollPane).height(CONTAINER_HEIGHT);
        setTopBorder(this.container);
    }

    @Override
    void update() {
        selectedHero = InventoryUtils.selectedHero;
        table.clear();
        fillRows();
    }

    private void fillRows() {
        for (SkillItem skillItem : selectedHero.getAllSkillsAboveZero()) {
            fillRow(skillItem);
        }
    }

    private void fillRow(SkillItem skillItem) {
        table.add(createImageOf(skillItem.getId().name()));
        final var skillName = new Label(skillItem.getName(), table.getSkin());
        skillName.addListener(new PersonalityTooltipListener(tooltip, skillItem));
        table.add(skillName).padLeft(SECOND_COLUMN_PAD_LEFT);
        table.add(String.valueOf(skillItem.getRank()));
        final int totalExtra = selectedHero.getExtraSkillForVisualOf(skillItem);
        addToTable(totalExtra);
    }

}
