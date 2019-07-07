package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.SpellType;


class SpellsTable extends BaseTable {

    private static final float FIRST_COLUMN_WIDTH = 64f;
    private static final float SECOND_COLUMN_WIDTH = 210;
    private static final float THIRD_COLUMN_WIDTH = 40f;
    private static final float FOURTH_COLUMN_WIDTH = 30f;
    private static final float FIFTH_COLUMN_WIDTH = 30f;
    private static final float CONTAINER_HEIGHT = 704f;
    private static final float ROW_HEIGHT = 64f;
    private static final float SECOND_COLUMN_PAD_LEFT = 20f;

    final Table container;
    final ScrollPane scrollPane;

    private HeroItem selectedHero;

    SpellsTable() {
        this.table.columnDefaults(0).width(FIRST_COLUMN_WIDTH);
        this.table.columnDefaults(1).width(SECOND_COLUMN_WIDTH);
        this.table.columnDefaults(2).width(THIRD_COLUMN_WIDTH);
        this.table.columnDefaults(3).width(FOURTH_COLUMN_WIDTH);
        this.table.columnDefaults(4).width(FIFTH_COLUMN_WIDTH);
        this.table.top();
        this.table.defaults().height(ROW_HEIGHT);

        this.scrollPane = new ScrollPane(this.table);
        this.container = new Table();
        this.container.add(this.scrollPane).height(CONTAINER_HEIGHT);
        setTopBorder(this.container);
    }

    @Override
    void render() {
        selectedHero = InventoryUtils.selectedHero;
        table.clear();
        fillSchoolRow();
        fillSpellRows();
    }

    private void fillSchoolRow() {
        table.add("");
        table.add(selectedHero.getSchool().getTitle() + " School").padLeft(SECOND_COLUMN_PAD_LEFT);
        table.add("");
        table.add("");
        table.add("").row();
    }

    private void fillSpellRows() {
        for (SpellType spellType : selectedHero.getAllSpells()) {
            fillSpellRow(spellType);
        }
    }

    private void fillSpellRow(SpellType spellType) {
        table.add("");
        table.add(spellType.getTitle()).padLeft(SECOND_COLUMN_PAD_LEFT);
        table.add(String.valueOf(selectedHero.getSpellRankOf(spellType)));
        table.add("");
        table.add("").row();
    }

}
