package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.SpellItem;


class SpellsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 48f;
    private static final float SECOND_COLUMN_WIDTH = 210;
    private static final float THIRD_COLUMN_WIDTH = 40f;
    private static final float FOURTH_COLUMN_WIDTH = 30f;
    private static final float CONTAINER_HEIGHT = 704f;
    private static final float ROW_HEIGHT = 48f;
    private static final float SECOND_COLUMN_PAD_LEFT = 15f;

    final Table container;
    final ScrollPane scrollPane;

    private final PersonalityTooltip tooltip;

    private HeroItem selectedHero;

    SpellsTable(PersonalityTooltip tooltip) {
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
        fillSchoolRow();
        fillSpellRows();
    }

    private void fillSchoolRow() {
        table.add(createImageOf(selectedHero.getSchool().name()));
        table.add(selectedHero.getSchool().getTitle() + " School").padLeft(SECOND_COLUMN_PAD_LEFT);
        table.add("");
        table.add("").row();
        table.add("").row();
    }

    private void fillSpellRows() {
        for (SpellItem spellItem : selectedHero.getAllSpells()) {
            fillSpellRow(spellItem);
        }
    }

    private void fillSpellRow(SpellItem spellItem) {
        table.add(createImageOf(spellItem.getId()));
        final var spellName = new Label(spellItem.getName(), table.getSkin());
        spellName.addListener(new PersonalityTooltipListener(tooltip, spellItem));
        table.add(spellName).padLeft(SECOND_COLUMN_PAD_LEFT);
        table.add(String.valueOf(spellItem.getRank()));
        table.add("").row();
    }

}
