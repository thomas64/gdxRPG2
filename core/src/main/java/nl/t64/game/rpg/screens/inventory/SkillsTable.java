package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.SkillItemId;


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
    private int skillRank;
    private int totalExtra;

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

    void update() {
        selectedHero = InventoryUtils.selectedHero;
        table.clear();
        fillRows();
    }

    private void fillRows() {
        for (SkillItemId skillItemId : selectedHero.getAllSkillsAboveZero()) {
            skillRank = selectedHero.getSkillRankOf(skillItemId);
            totalExtra = selectedHero.getExtraSkillForVisualOf(skillItemId);
            fillRow(skillItemId);
        }
    }

    private void fillRow(SkillItemId skillItemId) {
        table.add(createImageOf(skillItemId.name()));
        var skillTitle = new Label(skillItemId.getTitle(), table.getSkin());
        skillTitle.addListener(new StatTooltipListener(tooltip, skillItemId));
        table.add(skillTitle).padLeft(SECOND_COLUMN_PAD_LEFT);
        table.add(String.valueOf(skillRank));
        addToTable(totalExtra);
    }

}
